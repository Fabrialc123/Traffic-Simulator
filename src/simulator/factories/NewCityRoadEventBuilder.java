package simulator.factories;

import org.json.JSONObject;

import simulator.exceptions.InvalidJSONInputException;
import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends Builder<Event>{

	public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) throws InvalidJSONInputException {
		if (data.has("time")&& data.has("id") && data.has("src") && data.has("dest") && data.has("length") && data.has("co2limit") && data.has("maxspeed") && data.has("weather")) {
			int time = data.getInt("time");
			String id = data.getString("id");
			String junSrc = data.getString("src");
			String junDest = data.getString("dest");
			int lenght = data.getInt("length");
			int co2 = data.getInt("co2limit");
			int maxSpeed = data.getInt("maxspeed");
			String wt = data.getString("weather");
			Weather weather = Weather.valueOf(wt.toUpperCase());
			
			return new NewCityRoadEvent(time,id,junSrc,junDest,maxSpeed,lenght,co2,weather);
		}
		else {
			throw new InvalidJSONInputException("Error: Missing data in JSON of CityRoad");
		}
	}

}
