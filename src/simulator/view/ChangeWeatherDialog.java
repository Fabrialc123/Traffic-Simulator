package simulator.view;

import java.awt.Frame;

public class ChangeWeatherDialog extends ChangeDefaultDialog {
	private static final long serialVersionUID = 1L;
	
	private static String title = "Change Weather";
	private static String description = "Schedule for change the road weather";
	
	
	public ChangeWeatherDialog(Frame frameParent, String[] roads, String[] weathers) {
		super(frameParent, title, description,
								"Roads :",
								roads,
								"Weathers :",
								weathers);
		
	}

	

	
	
	public String  getRoadSelected() {
		return (String) this.firstOption.getSelectedItem();
	}
	
	public String getWeatherSelected() {
		return (String) this.secondOption.getSelectedItem();
	}

}
