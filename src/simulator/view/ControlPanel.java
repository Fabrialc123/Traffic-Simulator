package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;


import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	private static final long serialVersionUID = 1L;
	
	private Controller ctrl;
	private List<Vehicle> vehicles;
	private List<Road> roads;
	private List<Weather> weather;
	
	private int time;
	private boolean stop;
	private String initFile;
	
	private JToolBar toolBar;
	private JButton loadButton;
	private JButton setContButton;
	private JButton setWeatButton;
	private JButton runButton;
	private JButton	stopButton;
	private JButton	resetButton;
	private JButton exitButton;
	
	private JFileChooser fc;
	private JSpinner ticks;
	
	
	private Frame frameParent;
	
	public ControlPanel(Frame parent, Controller ctrl, String initFile) {
		this.frameParent = parent;
		this.time = 0;
		this.stop = true;
		this.ctrl = ctrl;
		this.ctrl.addObserver(this);
		
		this.vehicles = new ArrayList<>();
		this.roads = new ArrayList<>();
		this.weather = Weather.getWeathers();
		
		this.initFile = initFile;
		
		initGUI();
	}
	
	

	private void initGUI() {
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	
		toolBar = new JToolBar();
		this.add(toolBar);			
		toolBar.setFloatable(false);
		
			// LOAD BUTTON
			fc = new JFileChooser();
			fc.setCurrentDirectory(new File("resources/examples"));
			loadButton = new JButton();
			loadButton.setIcon(new ImageIcon("resources/icons/open.png"));
			loadButton.setToolTipText("Load a file");
			loadButton.addActionListener(new LoadButtonActionListener());
			toolBar.add(loadButton);
			
		toolBar.addSeparator();
			
			// SET CONTAMINATION BUTTON
			setContButton = new JButton();
			setContButton.setIcon(new ImageIcon("resources/icons/co2class.png"));
			setContButton.setToolTipText("Change the contamination class of a vehicle");
			setContButton.addActionListener(new SetContButtonListener());
			toolBar.add(setContButton);
			
			// SET WEATHER BUTTON
			setWeatButton = new JButton();
			setWeatButton.setIcon(new ImageIcon("resources/icons/weather.png"));
			setWeatButton.setToolTipText("Change the weather of a road");
			setWeatButton.addActionListener(new SetWeatButtonListener());
			toolBar.add(setWeatButton);
			
		toolBar.addSeparator();
			
			//	COMMAND BUTTONS
			runButton = new JButton();
			runButton.setIcon(new ImageIcon("resources/icons/run.png"));
			runButton.setToolTipText("Run the simulation");
			runButton.addActionListener(new RunButtonListener());
			toolBar.add(runButton);
			
			stopButton = new JButton();
			stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
			stopButton.setToolTipText("Stop the simulation");
			stopButton.addActionListener(new StopButtonListener());
			toolBar.add(stopButton);
			
			toolBar.add(new JLabel("Ticks:"));
			ticks = new JSpinner(new SpinnerNumberModel(1,1,1000,1));
			ticks.setToolTipText("Run the simulation 1-1000");
			ticks.setMaximumSize(new Dimension (50,50));
			toolBar.add(ticks);
			
			resetButton = new JButton();
			resetButton.setIcon(new ImageIcon("resources/icons/reset.png"));
			resetButton.setToolTipText("Reset the simulation");
			resetButton.addActionListener(new ResetButtonListener());
			toolBar.add(resetButton);
			
			
		toolBar.addSeparator();
			
			// EXIT BUTTON
			toolBar.add(Box.createHorizontalGlue());
			exitButton = new JButton();
			exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
			exitButton.setToolTipText("Quit");
			exitButton.addActionListener(new ExitButtonListener());
			toolBar.add(exitButton);
			
		
	}
	
	
	private void update (int time, RoadMap map, List<Event> events) {
		this.time = time;
		this.vehicles = map.getVehicles();
		this.roads = map.getRoads();
		
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(time, map, events);
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(time, map, events);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(time, map, events);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(time, map, events);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(time, map, events);

	}

	@Override
	public void onError(String err) {
		this.exitButton.setEnabled(true);					// Por si ocurre un error se pueda salir de la aplicacion
	}
	
	
	class LoadButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int v = fc.showOpenDialog(null);
			if (v == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					ctrl.reset();
					ctrl.loadEvents(new FileInputStream(file));
				} catch (Exception ex) {
					showError(ex.getMessage());
				} 
			}
		}
	}
	
	class SetContButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				List<String> vs = new ArrayList <String>();
				for (Vehicle v: vehicles) vs.add(v.getId());
				
				ChangeC02ClassDialog dialog = new ChangeC02ClassDialog(frameParent,vs.toArray(new String[0]));
				
				int status = dialog.getStatus();
				
				if(status == 1) {
					List<Pair<String,Integer>> cs = new ArrayList<>();
					cs.add(new Pair<String,Integer>(dialog.getVehicleSelected(), dialog.getContClassSelected()));
					ctrl.addEvent(new NewSetContClassEvent(time + dialog.getTicks(), cs));
				}
			}catch (Exception ex) {
				showError(ex.getMessage());
			}
		}
	}
	
	class SetWeatButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				List<String> rds = new ArrayList<String>();
				for (Road r: roads) rds.add(r.getId());
				List<String> wts = new ArrayList<String>();
				for (Weather w: weather) wts.add(w.name());
				
				ChangeWeatherDialog dialog = new ChangeWeatherDialog(frameParent,rds.toArray(new String[0]),wts.toArray(new String[0]) );
				
				int status = dialog.getStatus();
				
				if(status == 1) {
					List<Pair<String,Weather>> we = new ArrayList<>();
					we.add(new Pair<String,Weather>(dialog.getRoadSelected(), Weather.parse(dialog.getWeatherSelected())));
					ctrl.addEvent(new SetWeatherEvent(time + dialog.getTicks(), we));
				}
			}catch (Exception ex) {
				showError(ex.getMessage());
			}
			
		}
	}
	
	class RunButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ControlPanel.this.setEnabledButtons(false);			// Desactiva los botones y el menu
			ControlPanel.this.stop = false;
			ControlPanel.this.run_sim((int) ticks.getValue());
		}
	}
	
	class StopButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ControlPanel.this.stop();
		}
		
	}
	
	class ResetButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (fc.getSelectedFile() != null) {
				File file = fc.getSelectedFile();
				try {
					ctrl.reset();
					ctrl.loadEvents(new FileInputStream(file));
				} catch (Exception ex) {
					showError(ex.getMessage());
				} 
			}
			else if (initFile != null)  {
				try {
					ctrl.reset();
					ctrl.loadEvents(new FileInputStream(initFile));
				} catch (Exception ex) {
					showError(ex.getMessage());
					ex.printStackTrace();
				} 
				
			}
			else showError("There is no a loaded file");
		}
		
	}
	
	class ExitButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Object[] options = {"Exit", "Cancel"};
			int n = JOptionPane.showOptionDialog(null, "Are you sure?", "Exit", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,new ImageIcon("resources/icons/exit.png"),options, options[0] );
			if (n == JOptionPane.OK_OPTION) System.exit(0);
		}
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(null,message,"Error",JOptionPane.ERROR_MESSAGE);
	}

	private void run_sim(int n) {
		if (n > 0 && !stop) {
			try {
				ctrl.run(1);
				Thread.sleep(500);
			} catch (Exception e) {
				showError(e.getMessage());
				stop = true;
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					run_sim(n - 1);
				}
			});
			} 
		else {
			setEnabledButtons(true);
			stop = true;
		}
		
	}

	private void stop() {
		this.stop = true;
	}

	
	private void setEnabledButtons(boolean mode) {
		loadButton.setEnabled(mode);
		setContButton.setEnabled(mode);
		setWeatButton.setEnabled(mode);
		runButton.setEnabled(mode);
		resetButton.setEnabled(mode);
		exitButton.setEnabled(mode);							
	}
	

	

	
}
