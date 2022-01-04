package simulator.model;

import simulator.exceptions.InvalidActionException;
import simulator.exceptions.InvalidParameterException;

public class NewJunctionEvent extends Event {

	String id;
	int x;
	int y;
	LightSwitchingStrategy lss;
	DequeuingStrategy dqs;
	
	
	public NewJunctionEvent(int time, String id, int coorX, int coorY, LightSwitchingStrategy strat,
			DequeuingStrategy deq) {
		super(time);
		this.id = id;
		this.x = coorX;
		this.y = coorY;
		this.lss = strat;
		this.dqs = deq;
	}

	@Override
	void execute(RoadMap map) throws InvalidActionException {
		try {
			Junction j = new Junction(id, lss, dqs, x, y);
			map.addJunction(j);
		}
		catch (InvalidParameterException e) {
			throw new InvalidActionException("Unavailable to execute New Junction. " + e.toString() + " ."); 
		}
	}
	
	public String toString() {
		return "New Junction '" + this.id + "'";
	}

}
