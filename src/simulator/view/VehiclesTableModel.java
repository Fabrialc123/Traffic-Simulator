package simulator.view;

import java.util.List;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends TableModel<Vehicle> {
	private static final long serialVersionUID = 1L;
	
	private static String[] colNamesVehicle = {"ID","Location","Itinerary","CO2 Class","Max. Speed","Speed","Total CO2", "Distance"};

	public VehiclesTableModel(Controller ctrl) {
		super(ctrl, colNamesVehicle);
	}


	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		VehicleStatus st;
		Vehicle v =  this.data.get(rowIndex);
		switch (columnIndex) {
			case 0:
				s = v.getId();
				break;
				
			case 1:
				st = v.getStatus();
				if (st == VehicleStatus.WAITING) s = new String ("Waiting:" + v.getRoad().getDes().getId());
				else if (st == VehicleStatus.TRAVELING) s = new String (v.getRoad().getId() + ":" + v.getLocation());	
				else s = new String (st.name());
				break;
				
			case 2:
				s = v.getItinerary().toString();
				break;
				
			case 3:
				s = v.getContaminationClass();
				break;
			case 4:
				s = v.getMaxSpeed();
				break;
			case 5:
				s = v.getCurrentSpeed();
				break;
			case 6:
				s = v.getTotalCO2();
				break;
			case 7:
				s = v.getTotalTravel();
				break;			
		}
		
		return s;
	}

	protected void update(RoadMap map, List<Event> events) {
		this.data = map.getVehicles();
		this.fireTableDataChanged();
	}

}
