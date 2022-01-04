package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {
	private static final long serialVersionUID = 1L;

	private Controller ctrl;
	
	private RoadMap map;
	
	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _ROAD_COLOR = Color.BLACK;
	private static final Color _ROAD_LABEL_COLOR = Color.BLACK;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	
	private Image car;
	
	
	public MapByRoadComponent(Controller ctrl) {
		this.ctrl = ctrl;
		this.ctrl.addObserver(this);
		this.setPreferredSize(new Dimension(300,200));
		initGUI();
	}

	private void initGUI() {
		this.car = loadImage("car.png");
		
	}
	
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (map == null || map.getRoads().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			//updatePrefferedSize();
			drawMapByRoad(g);
		}
	}
	
	private void drawMapByRoad(Graphics g) {
		int i = 0;
		for (Road r: this.map.getRoads()) {
			int xSrc = 50;
			int ySrc = (i + 1) * 50;
			int xDest = this.getWidth() - 100;
			int yDest = ySrc;
			
			//DRAW THE ROAD
			g.setColor(_ROAD_COLOR);
			g.drawLine(xSrc, ySrc, xDest, yDest);
			g.setColor(_ROAD_LABEL_COLOR);
			g.drawString(r.getId(), 15, yDest + 2);
			
			//DRAW THE JUNCTIONS
			drawJunction(g, xSrc,ySrc, _JUNCTION_COLOR, r.getSrc().getId());		// SOURCE JUNCTION
			Color juncDesColor = _RED_LIGHT_COLOR;
			if (r.getId().equals(r.getDes().getCurrentGreen())) juncDesColor = _GREEN_LIGHT_COLOR;
			drawJunction(g, xDest,yDest, juncDesColor, r.getDes().getId());			// DESTINY JUNCTION
			
			//DRAW VEHICLES
			for (Vehicle v: r.getVehicles()) {
				drawVehicle(g, xSrc, xDest, ySrc, v, r.getLenght());
			}
			
			//DRAW WEATHER
			drawWeather(g,xDest + 12, ySrc - 18, r.getWeather().name() );
			
			//DRAW CONTAMINATION
			drawContamination(g, xDest + 50, yDest - 18, r.getTotalCO2(), r.getCO2Limit());
			
			i++;
		}
	}
	
	private void drawContamination(Graphics g, int x, int y, int totalCO2, int co2Limit) {
		int c = (int) Math.floor(Math.min((double) totalCO2/(1.0 + (double) co2Limit),1.0) / 0.19);
		Image contImg = loadImage("cont_" + c + ".png");
		g.drawImage(contImg, x, y , 32,32,this);
	}

	private void drawWeather(Graphics g, int x, int y, String weatherName) {
		Image weatherImg = loadImage(weatherName + ".png");
		g.drawImage(weatherImg, x, y , 32,32,this);
	}
	
	private void drawVehicle(Graphics g,int x1, int x2, int y, Vehicle v, int roadLenght) {
		int xPos =  x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) roadLenght));
		int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContaminationClass()));
		
		g.drawImage(car, xPos, y - 10, 16, 16, this);
		g.setColor(new Color(0, vLabelColor, 0));
		g.drawString(v.getId(), xPos, y - 11);		
	}

	private void drawJunction(Graphics g,int x, int y, Color junctionColor, String id) {
		g.setColor(junctionColor);
		g.fillOval(x - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
		
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(id, x, y - 8);
	}

	private void update(RoadMap map) {
		this.map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}


}
