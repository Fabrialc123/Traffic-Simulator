package simulator.model;

import simulator.exceptions.InvalidActionException;

public class NewInterCityRoadEvent extends Event {
	String id;
	String junSrc;
	String junDest;
	int length;
	int co2;
	int maxSpeed;
	Weather weather;
	
	public NewInterCityRoadEvent(int time, String id, String junSrc, String junDest,int maxSpeed, int co2, int length, Weather weather) {
		super(time);
		this.id = id;
		this.junSrc = junSrc;
		this.junDest = junDest;
		this.length = length;
		this.co2 = co2;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}

	@Override
	void execute(RoadMap map) throws InvalidActionException {
		Junction junSrc1 = map.getJunction(junSrc);
		Junction junDst1 = map.getJunction(junDest);
		try {
			InterCityRoad ir = new InterCityRoad(id, junSrc1, junDst1, maxSpeed, co2, length, weather);
			map.addRoad(ir);
			junSrc1.addOutGoingRoad(ir);
			junDst1.addIncommingRoad(ir);	
		}catch (Exception e){
			throw new InvalidActionException("Unavailable to execute New Intercity Road. " + e.toString() + " .");
		}

	}
	
	public String toString() {
		return "New InterCity Road '"+ this.id + "'";
	}

}
