package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.exceptions.InvalidActionException;
import simulator.exceptions.InvalidParameterException;

public class NewVehicleEvent extends Event {

	String id;
	int maxSpeed;
	int contClass;
	List<String> itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = itinerary;
	}

	@Override
	void execute(RoadMap map) throws InvalidActionException {
		List<Junction> lj = new ArrayList<>();
		for(String s : itinerary) {
			lj.add(map.getJunction(s));
		}
		try {
			Vehicle v = new Vehicle(id, maxSpeed, contClass, lj);
			map.addVehicle(v);
			v.moveToNextRoad();		
		} catch (InvalidParameterException e) {
			throw new InvalidActionException("Error: Unavailable to execute New Vehicle. " + e.toString() + " .");
		}
	}
	
	public String toString() {
		return "New Vehicle '" + this.id + "'";
	}

}
