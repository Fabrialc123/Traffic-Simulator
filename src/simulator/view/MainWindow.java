package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


import simulator.control.Controller;

public class MainWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	
	private ControlPanel control;		// Para poder coger su menuBar
	private String initFile	;			// Para que el boton Reset pueda funcionar desde el principio en caso de que no sea null
	
	
	public MainWindow(Controller ctrl, String initFile) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		this.initFile = initFile;
		initGUI();
	}
	
	private void initGUI() {
		JPanel mainPanel = new JPanel (new BorderLayout());
		this.setContentPane(mainPanel);
		
		this.control = new ControlPanel(this, _ctrl, initFile);
		mainPanel.add(control, BorderLayout.PAGE_START);
		
		mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);
		
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel,BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);
		
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel,BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
		
		
		// 										TABLAS									
		
		// EVENTOS
		JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		eventsView.setPreferredSize(new Dimension(500,400));
		tablesPanel.add(eventsView);
		
		//	VEHICULOS
		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		vehiclesView.setPreferredSize(new Dimension(500,400));
		tablesPanel.add(vehiclesView);
		
		//	CARRETERAS
		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		roadsView.setPreferredSize(new Dimension(500,400));
		tablesPanel.add(roadsView);
		
		//	CRUCES
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		junctionsView.setPreferredSize(new Dimension(500,400));
		tablesPanel.add(junctionsView);
		
		
		//										MAPAS									
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500,400));
		mapsPanel.add(mapView);
		
		JPanel mapByRoadView = createViewPanel(new MapByRoadComponent(_ctrl), "MapByRoad");
		mapByRoadView.setPreferredSize(new Dimension(500,400));
		mapsPanel.add(mapByRoadView);
		
		
		
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,2), title));
		p.add(new JScrollPane(c));
		return p;
	}
}
