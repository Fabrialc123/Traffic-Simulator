package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.InvalidJSONInputException;
import simulator.exceptions.InvalidParameterException;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) throws InvalidJSONInputException, InvalidParameterException {
		if (data.has("time") && data.has("info")) {
			int time = data.getInt("time");
			JSONArray csArray = data.getJSONArray("info");
			List<Pair<String,Integer>> cs = new ArrayList<>();
			String vehicle;
			int  cont;
			for (int i = 0; i < csArray.length(); i++) {
				vehicle = csArray.getJSONObject(i).getString("vehicle");
				cont = csArray.getJSONObject(i).getInt("class");
				cs.add(new Pair<String,Integer>(vehicle, cont));
			}
			try {
				return new NewSetContClassEvent(time, cs);
			} catch (InvalidParameterException e) {
				throw e;
			}
		}
		else {
			throw new InvalidJSONInputException("Error: Missing data in JSON of SetContClass");
		}
	}
}
