package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import simulator.exceptions.InvalidActionException;
import simulator.exceptions.InvalidParameterException;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle>{
	

	private List<Junction> itinerary;
	private int indice;
	private int maximumSpeed;
	private int currentSpeed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contaminationClass;
	private int totalContamination;
	private int totalTravelledDistance;
	
	Vehicle(String id,int maxSpeed,int contClass, List<Junction> lista) throws InvalidParameterException {
		super(id);
		if(maxSpeed < 0 ) {
			throw new InvalidParameterException("Error: Max Speed of vehicle " + this + " must be positive. ");
		}
		this.maximumSpeed = maxSpeed;
		if (contClass < 0 || contClass > 10) {
			throw new InvalidParameterException("Error: Contamination Class of vehicle " + this + " must be 0 - 10. ");
		}
		setContaminationClass(contClass);
		if (lista.size() < 2) {
			throw new InvalidParameterException("Error: Itinerary list of vehicle " + this + " must have 2 or more. ");
		}
		this.itinerary = Collections.unmodifiableList(new ArrayList<>(lista));
		this.totalContamination = 0;
		this.status = VehicleStatus.PENDING;
		this.currentSpeed = 0;
		this.indice = 0;
		this.totalTravelledDistance = 0;
	}
	
	protected void setSpeed(int s) throws InvalidParameterException {
		if (this.status == VehicleStatus.TRAVELING ) {						// Es necesario ya que en el advance de las Roads, cambia su velocidad independientemente de su status
			if(s < 0) {
				throw new InvalidParameterException("Error: Velocity of vehicle " + this + " is not valid. ");
			}else if(s > maximumSpeed) {
				this.currentSpeed = maximumSpeed;
			}else this.currentSpeed = s;
		}
	}
	
	void setContaminationClass(int c) throws InvalidParameterException {
		if(c < 0 || c > 10) {
			throw new InvalidParameterException("Error: Contamination class of vehicle " + this + " is not valid. ");
		}else this.contaminationClass = c;
	}
	
	public int getContaminationClass() {
		return this.contaminationClass;
	}
	
	protected void advance(int time) {
		if(this.status == VehicleStatus.TRAVELING ) {
			int oldLocation = this.location;
			int contamination = 0;
			if(location + currentSpeed > road.getLenght()) {		// Actualiza la posición del vehículo
				this.location = road.getLenght();
			}else {
				this.location += currentSpeed;
			}
			this.totalTravelledDistance += this.location - oldLocation;					// Actualiza la distancia total recorrida
			contamination = (this.location - oldLocation)*this.contaminationClass;		// Calcula la contaminación causada
			this.totalContamination += contamination;
			try {
				this.road.addContamination(contamination);
			}catch (InvalidParameterException e) {
				System.out.println(e.getMessage());
			}
			
			if(this.location >= road.getLenght()) {				// Indica que ya ha recorrida la carretera
				this.status = VehicleStatus.WAITING;
				this.currentSpeed = 0;
				this.road.getDes().enter(this);
			}	
		}	
	}
	
	protected void moveToNextRoad() throws InvalidActionException {
		if(this.status == VehicleStatus.PENDING || this.status == VehicleStatus.WAITING ) {
			if (this.status == VehicleStatus.WAITING)	this.road.exit(this); 				// Sale de la carretera
			this.location = 0;
			this.currentSpeed = 0;
			if (itinerary.size() == indice + 1) {											//¿Ha llegado a su destino?
				this.status = VehicleStatus.ARRIVED;
				this.currentSpeed = 0;
			}
			else {
				Road a = itinerary.get(indice).roadTo(itinerary.get(indice + 1));
				a.enter(this);																// Entra en la siguiente carretera
				this.road = a;
				this.status = VehicleStatus.TRAVELING;
				this.indice++;
			}
		}
		else {
			 throw new InvalidActionException("Error: Vehicle(" + this + ") is not available to move to next road. ");
		}
		
	}
	
	public Road getRoad() {
		return this.road;
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id",_id);
		jo.put("speed",currentSpeed);
		jo.put("distance", totalTravelledDistance);
		jo.put("co2", totalContamination);
		jo.put("class", contaminationClass);
		jo.put("status", status);
		
		if(this.status == VehicleStatus.TRAVELING || status == VehicleStatus.WAITING) {
			jo.put("road",road);
			jo.put("location",location);
		}
	
		return jo;
	}
	
	public int getLocation() {
		return location;
	}

	public List<Junction> getItinerary() {
		return itinerary;
	}

	public int getCurrentSpeed() {
		return currentSpeed;
	}

	public VehicleStatus getStatus() {
		return status;
	}

	@Override
	public int compareTo(Vehicle o) {
		if(this.getLocation() > o.getLocation()) {
			return -1;
		}else if (this.getLocation() < o.getLocation()) {
			return 1;
		}else return 0;
	}
	
	public int getMaxSpeed() {
		return this.maximumSpeed;
	}
	
	public int getTotalCO2() {
		return this.totalContamination;
	}
	
	public int getTotalTravel() {
		return this.totalTravelledDistance;
	}
}

