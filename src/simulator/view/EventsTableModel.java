package simulator.view;

import java.util.List;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;

public class EventsTableModel extends TableModel<Event> {
	private static final long serialVersionUID = 1L;
	
	private static String[] colNamesEvent = {"Time","Description"};

	public EventsTableModel(Controller ctrl) {
		super(ctrl, colNamesEvent);
	}
	
	

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = this.data.get(rowIndex).getTime();
			break;
		case 1:
			s = this.data.get(rowIndex).toString();
			break;
		}
		return s;
	}


	protected void update(RoadMap map, List<Event> events) {
		this.data = events;
		this.fireTableDataChanged();
	}


	

}
