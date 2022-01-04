package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.exceptions.InvalidActionException;
import simulator.exceptions.InvalidParameterException;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
	private RoadMap mapaCarreteras;
	private List<Event> events;
	private int time;
	
	private List<TrafficSimObserver> obs;
	
	public TrafficSimulator () {
		mapaCarreteras = new RoadMap();
		events = new SortedArrayList<Event>();
		time = 0;
		
		this.obs = new ArrayList<TrafficSimObserver>();
	}
	 
	public void addEvent(Event e) {
		events.add(e);
		
		for (TrafficSimObserver ob: obs) ob.onEventAdded(this.mapaCarreteras, this.events, e, this.time);
	}
	
	public void advance() throws InvalidActionException, InvalidParameterException {
		time++;
		for (TrafficSimObserver ob: obs) ob.onAdvanceStart(mapaCarreteras, events, time);
		
		try {
		
			while(!events.isEmpty() && events.get(0).getTime() == this.time) { 
				events.get(0).execute(mapaCarreteras);
				events.remove(0);
			}
			List<Junction> aux = mapaCarreteras.getJunctions();
			for(Junction j : aux) {
				mapaCarreteras.getJunction(j.getId()).advance(time);
			}
			List<Road> auxC = mapaCarreteras.getRoads();
			for(Road r : auxC) {
				mapaCarreteras.getRoad(r.getId()).advance(time);
			}
			
			for (TrafficSimObserver ob: obs) ob.onAdvanceEnd(mapaCarreteras, events, time);
			
		}catch (Exception e) {
			for (TrafficSimObserver ob: obs) ob.onError(e.getMessage());
			
			throw e;
			
		}
		
	}
	
	public void reset() {
		mapaCarreteras.reset();
		events.clear();
		time = 0;
		
		for (TrafficSimObserver ob: obs) ob.onReset(mapaCarreteras, events, time);
	}
	

	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time",this.time);
		jo.put("state", mapaCarreteras.report());
		
		return jo;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		if (!this.obs.contains(o)) {
			obs.add(o);
		
			for (TrafficSimObserver ob: obs) ob.onRegister(mapaCarreteras, events, time);
		}
		
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		if (this.obs.contains(o)) this.obs.remove(o);
		
	}
		
	public void removeEvent(Event e) {
		this.events.remove(e);
	}

	
}
