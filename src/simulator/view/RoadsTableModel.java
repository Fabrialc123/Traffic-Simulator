package simulator.view;

import java.util.List;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;

public class RoadsTableModel extends TableModel<Road>{
	private static final long serialVersionUID = 1L;
	
	private static String[] colNamesRoad = {"Id", "Lenght", "Weather", "Max. Speed","Speed","Total CO2", "CO2 Limit"};

	public RoadsTableModel(Controller ctrl) {
		super (ctrl, colNamesRoad);
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		Road r = this.data.get(rowIndex);
		switch (columnIndex) {
			case 0:
				s = r.getId();
				break;
			case 1:
				s = r.getLenght();
				break;
			case 2:
				s = r.getWeather().name();
				break;
			case 3:
				s = r.getMaxSpeed();
				break;
			case 4:
				s = r.getCurrentSpeed();
				break;
			case 5:
				s = r.getTotalCO2();
				break;
			case 6:
				s = r.getCO2Limit();
				break;
		}
		return s;
	}


	@Override
	protected void update(RoadMap map, List<Event> events) {
		this.data = map.getRoads();
		this.fireTableDataChanged();
	}

}
