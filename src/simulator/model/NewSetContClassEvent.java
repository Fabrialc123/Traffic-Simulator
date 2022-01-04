package simulator.model;

import java.util.List;

import simulator.exceptions.InvalidActionException;
import simulator.exceptions.InvalidParameterException;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {
	private List<Pair<String, Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String, Integer>> cs) throws InvalidParameterException {
		super(time);
		if (cs != null) {
			this.cs = cs;
		}
		else 
			throw new InvalidParameterException("Error: Invalid list of contamination class for SetContClass");
	}

	@Override
	void execute(RoadMap map) throws InvalidActionException, InvalidParameterException {
		for (Pair<String, Integer> c : cs) {
			if (map.getVehicle(c.getFirst()) != null) {
					map.getVehicle(c.getFirst()).setContaminationClass(c.getSecond());
			}
			else {
				throw new InvalidActionException("Error: Unavailable to execute SetContClass, the vehicle "+ c.getFirst() + " dont exists. ");
			}
		}

	}
	
	public String toString() {
		String desc = "";
		for (Pair<String, Integer> p: cs) desc += "[" + p.getFirst() + ":" + p.getSecond() + "] ";
		return "Set Contamination Class " + desc;
	}

}
