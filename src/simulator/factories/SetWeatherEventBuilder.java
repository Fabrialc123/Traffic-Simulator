package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.InvalidJSONInputException;
import simulator.exceptions.InvalidParameterException;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) throws InvalidJSONInputException, InvalidParameterException {
		if (data.has("time") && data.has("info")) {
			int time = data.getInt("time");
			JSONArray wsArray = data.getJSONArray("info");
			List<Pair<String,Weather>> ws = new ArrayList<>();
			String road;
			String weather;
			for (int i = 0; i < wsArray.length(); i++) {
				road = wsArray.getJSONObject(i).getString("road");
				weather = wsArray.getJSONObject(i).getString("weather");
				ws.add(new Pair<String,Weather>(road, Weather.valueOf(weather.toUpperCase())));
			}

			try {
				return new SetWeatherEvent(time,ws);
			} catch (InvalidParameterException e) {
				throw e;
			}
		}
		else {
			throw new InvalidJSONInputException("Error: Missing data in JSON of SetWeather");
		}
	}

}
