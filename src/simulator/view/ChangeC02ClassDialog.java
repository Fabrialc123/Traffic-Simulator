package simulator.view;

import java.awt.Frame;

public class ChangeC02ClassDialog extends ChangeDefaultDialog {
	private static final long serialVersionUID = 1L;
	
	private static String title = "Change C02 Class";
	private static String description = "Schedule for change C02 Class";
	private static String[] contClassValues = {"0","1","2","3","4","5","6","7","8","9","10"};

	
	public ChangeC02ClassDialog(Frame frameParent,String[] vehicles) {
		super (frameParent, title, description,
							"Vehicles :",
							vehicles,
							"Cont Class :",
							contClassValues);
	}


	
	public String getVehicleSelected() {
		return  (String) this.firstOption.getSelectedItem();
	}

	public int getContClassSelected() {
		return  Integer.parseInt((String) this.secondOption.getSelectedItem());
	}

}
