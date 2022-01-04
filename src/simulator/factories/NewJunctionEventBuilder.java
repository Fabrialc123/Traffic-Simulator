package simulator.factories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.exceptions.InvalidJSONInputException;
import simulator.exceptions.InvalidParameterException;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{

	Factory<LightSwitchingStrategy> lssFactory;
	Factory<DequeuingStrategy> dqsFactory;
	
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory,Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) throws InvalidJSONInputException, JSONException, InvalidParameterException {
			if (data.has("time") && data.has("id") && data.has("coor") && data.has("ls_strategy") && data.has("dq_strategy")) {
				int time = data.getInt("time");
				String id = data.getString("id");
				JSONArray coor = data.getJSONArray("coor");
				int coorX = coor.getInt(0);
				int coorY = coor.getInt(1);
				LightSwitchingStrategy strat = lssFactory.createInstance(data.getJSONObject("ls_strategy"));
				DequeuingStrategy deq = dqsFactory.createInstance(data.getJSONObject("dq_strategy")); 
		
				return new NewJunctionEvent(time,id,coorX,coorY,strat,deq);
			}
			else {
				throw new InvalidJSONInputException("Error: Missing data in JSON of Junction");
			}
		}
}


