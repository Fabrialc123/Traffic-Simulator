package simulator.model;

import java.util.List;

import simulator.exceptions.InvalidActionException;
import simulator.exceptions.InvalidParameterException;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	private List<Pair<String,Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) throws InvalidParameterException {
		super(time);
		if (ws != null) {
			this.ws = ws;
		}
		else throw new InvalidParameterException("Error: Invalid list of climatics conditions for SetWeatherEvent. ");
	}

	@Override
	void execute(RoadMap map) throws InvalidActionException, InvalidParameterException {
		for (Pair<String, Weather> p: ws) {
			if (map.getRoad(p.getFirst()) != null) {
					map.getRoad(p.getFirst()).setWeather(p.getSecond());		
			}
			else {
				throw new InvalidActionException("Error: Unavailable to execute SetWeather, the Road "+ p.getFirst() + " dont exists. ");
			}
		}
		
	}
	
	public String toString() {
		String desc = "";
		for (Pair<String,Weather> p: ws) desc += "[" + p.getFirst() + ":" + p.getSecond().name() + "] " ;
		return "Set Weather " + desc;
	}

}
