package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.exceptions.InvalidActionException;
import simulator.exceptions.InvalidJSONInputException;
import simulator.exceptions.InvalidParameterException;
import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class Controller {
	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		this.sim = sim;
		this.eventsFactory = eventsFactory;
	}
	
	public void loadEvents(InputStream in) throws InvalidJSONInputException, JSONException, InvalidParameterException {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		if (!jo.has("events")) throw new InvalidJSONInputException("Error: Missing data in JSON input");;							
		JSONArray re = jo.getJSONArray("events");
		int i = 0;
		while(i < re.length()) {
			sim.addEvent(eventsFactory.createInstance( re.getJSONObject(i)));
			i++;
		}
	}
		
	public void run (int n, OutputStream out) throws Exception {
		PrintStream p = null;
		if(out != null) {
			p = new PrintStream(out);
		}
		JSONObject reporte = new JSONObject();
		JSONArray estado = new JSONArray();
		reporte.put("states", estado);
		
		for(int i = 0; i < n; i++) {
			try {
				sim.advance();
				reporte.accumulate("states", sim.report());
			}
			catch(Exception e) {
				throw new Exception (e.getMessage() + " Ocurred at time " + i + " . ");
			}
		}
		
		if (out != null)	p.println(reporte.toString(3));
		else System.out.print(reporte.toString(3));
	}
	
	public void run (int n) throws InvalidActionException, InvalidParameterException {
		for (int i = 0; i < n; i++) sim.advance();
	}

	
	public void reset() {
		sim.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {this.sim.addObserver(o);}
	
	public void removeObserver(TrafficSimObserver o) { this.sim.removeObserver(o);}
	
	public void addEvent (Event e) {this.sim.addEvent(e);}
	
	public void removeEvent (Event e) {this.sim.removeEvent(e);}

	

}
