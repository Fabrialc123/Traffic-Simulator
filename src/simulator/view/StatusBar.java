package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {
	private static final long serialVersionUID = 1L;
	
	private JLabel timeText;
	private JLabel lastEventText;

	public StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new FlowLayout());
		timePanel.setPreferredSize(new Dimension (125,30));
		timePanel.setMaximumSize(new Dimension (125,30));
		this.add(timePanel);
		
		timePanel.add(new JLabel("Time : "));
		timeText = new JLabel ("0");
		timePanel.add(timeText);
		
		this.add(Box.createHorizontalGlue());
		
		lastEventText = new JLabel("Welcome!");
		this.add(lastEventText);
		
		this.add(Box.createHorizontalGlue());
		
		this.setVisible(true);
		
	}
	
	private void update (String time, String lastEvent) {
		this.timeText.setText(time);
		this.lastEventText.setText(lastEvent);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update("" + time, "");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update("" + time, "Event added(" + e.toString() + ")");
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update("" + time, "");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
		this.lastEventText.setText(err);
	}
	
}
