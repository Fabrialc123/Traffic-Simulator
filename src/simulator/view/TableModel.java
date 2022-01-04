package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public abstract class TableModel<T> extends AbstractTableModel	implements TrafficSimObserver{
	private static final long serialVersionUID = 1L;
	
	protected String[] colNames;
	protected List<T> data;
	
	public TableModel(Controller ctrl, String[] colNames) {
		ctrl.addObserver(this);
		this.colNames = colNames;
	}
	
	protected abstract void update (RoadMap map, List<Event> events);

	@Override
	public int getRowCount() {
		return this.data == null ? 0 : this.data.size();
	}

	@Override
	public int getColumnCount() {
		return this.colNames.length;
	}
	
	public String getColumnName(int col) {
		return this.colNames[col];
	}

	@Override
	public abstract Object getValueAt(int rowIndex, int columnIndex);			// Es abstracta porque cada tabla coge un dato de un modo distinto

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map, events);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map, events);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map, events);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map, events);
	}

	@Override
	public void onError(String err) {
	}
	
	public void onEventRemoved(RoadMap map, List<Event> events, int time) {
		update(map,events);
	}

}
