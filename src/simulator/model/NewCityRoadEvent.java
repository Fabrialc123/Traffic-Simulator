package simulator.model;

import simulator.exceptions.InvalidActionException;

public class NewCityRoadEvent extends Event {
	String id;
	String junSrc;
	String junDest;
	int length;
	int co2;
	int maxSpeed;
	Weather weather;
	
	

	public NewCityRoadEvent(int time, String id, String junSrc, String junDest,int maxSpeed,int length,
			int co2, Weather weather) {
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
			CityRoad cr = new CityRoad(id, junSrc1, junDst1, maxSpeed, co2, length, weather);
			map.addRoad(cr);
			junSrc1.addOutGoingRoad(cr);
			junDst1.addIncommingRoad(cr);
		}catch (Exception e){
			throw new InvalidActionException("Unavailable to execute New City Road. " + e.toString() + " .");
		}
	}
	
	public String toString() {
		return "New City Road '" + this.id + "'";
	}

}
