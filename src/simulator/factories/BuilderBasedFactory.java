package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.exceptions.InvalidJSONInputException;
import simulator.exceptions.InvalidParameterException;

public class BuilderBasedFactory<T> implements Factory<T> {

	private List<Builder<T>> _builders;

	public BuilderBasedFactory(List<Builder<T>> builders) {
		_builders = new ArrayList<>(builders);
	}

	@Override
	public T createInstance(JSONObject info) throws JSONException, InvalidJSONInputException, InvalidParameterException {
		if (info != null) {
			for (Builder<T> bb : _builders) {
				T o = bb.createInstance(info);
				if (o != null)
					return o;
			}
		}

		throw new IllegalArgumentException("Invalid value for createInstance: " + info);
	}
}
