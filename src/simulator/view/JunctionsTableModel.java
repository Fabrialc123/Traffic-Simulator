package simulator.view;

import java.util.List;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class JunctionsTableModel extends TableModel<Junction> {
	private static final long serialVersionUID = 1L;
	
	private static String[] colNamesJunction = {"Id", "Green", "Queues"};

	public JunctionsTableModel(Controller ctrl) {
		super(ctrl, colNamesJunction);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		Junction j = this.data.get(rowIndex);
		switch (columnIndex) {
			case 0:
				s = j.getId();
				break;
			case 1:
				s = j.getCurrentGreen();
				break;
			case 2: 
				String queues = " ";
				int i = 0;
				for (List<Vehicle> r: j.getQueue()) {
					queues += j.getInRoads().get(i).getId() + ":" + r.toString() + " ";
					i++;
				}
				s = queues;
				break;
		}
		return s;
	}

	@Override
	protected void update(RoadMap map, List<Event> events) {
		this.data = map.getJunctions();
		this.fireTableDataChanged();
	}


}
