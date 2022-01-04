package simulator.model;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.InvalidActionException;
import simulator.exceptions.InvalidParameterException;
import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject {
	protected Junction source;
	protected Junction destination;
	protected int lenght;
	protected int maximunSpeed;
	protected int currentSpeed;
	protected int contaminationAlarmLimit;
	protected Weather weatherConditions;
	protected int totalContamination;
	protected List<Vehicle> vehicles;
	
	Road(String id,Junction srcJun,Junction desJunc, int maxSpeed,
			int contLimit,int lenght,Weather weather) throws InvalidParameterException {
		
		super(id);
		this.source = srcJun;
		this.destination = desJunc;
		this.maximunSpeed = maxSpeed;
		this.contaminationAlarmLimit = contLimit;
		this.lenght = lenght;
		this.vehicles = new SortedArrayList<>();
		setWeather(weather);
		updateSpeedLimit();
		this.totalContamination = 0;
	}

	
	void setWeather(Weather w) throws InvalidParameterException {
		if(w == null) {
			throw new InvalidParameterException("Error: Invalid weather for Road " + this + " . ");
		}else this.weatherConditions = w;
	}
	public void addContamination(int c) throws InvalidParameterException{
		if(c < 0) {
			throw new InvalidParameterException("Error: Contamination is invalid in Road "+ this + " . ");
		}else this.totalContamination += c;
	}
	
	public int getLenght() {
		return this.lenght;
	}
	
	public Junction getSrc() {
		return this.source;
	}
	
	public Junction getDes() {
		return this.destination;
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	@Override
	
	
	protected void advance(int time) throws InvalidParameterException {
		reduceTotalContamination();
		updateSpeedLimit();
		for(Vehicle v : vehicles) {
				v.setSpeed(calculateVehicleSpeed(v));
				v.advance(time);
		}
		Collections.sort(vehicles, Vehicle::compareTo);						// Ordena los vehiculos segun su localizacion
		
	}
	
	void enter (Vehicle v) throws InvalidActionException {
		if (v.getCurrentSpeed() == 0 && v.getLocation() == 0)	vehicles.add(v);
		else {
			throw new InvalidActionException("Error: Vehicle (" + v + ") is not available to enter in road " + this + " . ");
		}
		
	}
	
	void exit (Vehicle v) {
		vehicles.remove(v);
	}
	
	public int getCurrentSpeed() {
		return this.currentSpeed;
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speedlimit", currentSpeed);
		jo.put("weather",weatherConditions);
		jo.put("co2",totalContamination);
	
		JSONArray ve = new JSONArray();
		for(Vehicle v :  Collections.unmodifiableList(vehicles)) {				// Hace gets de una lista de solo lectura
			 ve.put(v.getId());
		}
		jo.put("vehicles",ve);
	

		return jo;
	}


	public Weather getWeather() {
		return this.weatherConditions;
	}


	public int getMaxSpeed() {
		return this.maximunSpeed;
	}


	public int getTotalCO2() {
		return this.totalContamination;
	}


	public int getCO2Limit() {
		return this.contaminationAlarmLimit;
	}
	
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(this.vehicles);
	}
	
}
