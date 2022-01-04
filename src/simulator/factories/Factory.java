package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.exceptions.InvalidJSONInputException;
import simulator.exceptions.InvalidParameterException;

public interface Factory<T> {
	public T createInstance(JSONObject info) throws JSONException, InvalidJSONInputException, InvalidParameterException;
}
