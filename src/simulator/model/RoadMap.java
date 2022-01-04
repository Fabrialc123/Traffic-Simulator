package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.InvalidActionException;

public class RoadMap {
	List<Junction> listaCruces;
	List<Road> listaCarreteras;
	List<Vehicle> listaVehiculos;
	Map<String,Junction> mapaCruces;
	Map<String,Road> mapaCarreteras;
	Map<String,Vehicle> mapaVehiculos;
	
	RoadMap(){
		listaCruces = new ArrayList<>();
		listaCarreteras = new ArrayList<>();
		listaVehiculos = new ArrayList<>();
		mapaCruces = new HashMap<>();
		mapaCarreteras = new HashMap<>();
		mapaVehiculos = new HashMap<>();
	}
	
	void addJunction(Junction j) throws InvalidActionException {
		if(!mapaCruces.containsKey(j.getId())) {
			listaCruces.add(j);
			mapaCruces.put(j.getId(), j);
		}
		else {
			throw new InvalidActionException("Error: Unavailable to add Junction " + j + " to the RoadMap, junction already exists. ");
		}
	}
	
	void addRoad(Road r) throws InvalidActionException {
		if(!mapaCarreteras.containsKey(r.getId())) {
			if(mapaCruces.containsKey(r.getDes().getId())) {
				if(mapaCruces.containsKey(r.getSrc().getId())) {
					listaCarreteras.add(r);
					mapaCarreteras.put(r.getId(),r);
				}
				else {
					throw new InvalidActionException("Error: Unavailable to add Road " + r + " to the RoadMap, the destiny of Road dont exists. ");
				}
			}
			else {
				throw new InvalidActionException("Error: Unavailable to add Road " + r + " to the RoadMap, the source of Road dont exists. ");
			}
		}
		else {
			throw new InvalidActionException("Error: Unavailable to add Road " + r + " to the RoadMap, the Road already exists. ");
		}
	}
	
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(this.listaCruces);				// Solo lectura
	}
	
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(this.listaVehiculos);				// Solo lectura
	}
	
	public List<Road> getRoads(){
		return Collections.unmodifiableList(this.listaCarreteras);			// Solo lectura
	}
	
	void addVehicle(Vehicle v) throws InvalidActionException {
		if (!mapaVehiculos.containsKey(v.getId())) {
			for (int i = 0; i < v.getItinerary().size() - 1; i++) {
				if (v.getItinerary().get(i).roadTo(v.getItinerary().get(i + 1)) == null) throw new InvalidActionException("Error: Unavailable to add the vehicle " + v + " to the RoadMap, dont exists a road connection between " + v.getItinerary().get(i) + " and " + v.getItinerary().get(i + 1) + " . ");
			}
			listaVehiculos.add(v);
			mapaVehiculos.put(v.getId(), v);
		}
		else throw new InvalidActionException("Error: Unavailable to add the vehicle " + v + " to the RoadMap, the vehicle already exists. ");
	}
	
	public Junction getJunction(String id) {
		Junction a = mapaCruces.get(id);
		return a;
	}
	
	public Road getRoad(String id) {
		Road r = mapaCarreteras.get(id);
		return r;
	}
	
	public Vehicle getVehicle(String id) {
		Vehicle v = mapaVehiculos.get(id);
		return v;
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray jun = new JSONArray();
		JSONArray road = new JSONArray();
		JSONArray vehi = new JSONArray();
		for(Road ro : listaCarreteras) {
			road.put(ro.report());
		}
		jo.put("roads", road);
		for(Junction ju : listaCruces) {
			jun.put(ju.report());
		}
		jo.put("junctions", jun);
		
		
		for(Vehicle ve : listaVehiculos) {
			vehi.put(ve.report());
		}
		jo.put("vehicles", vehi);
				
		return jo;
	}
	
	void reset() {
		listaCruces.clear();
		listaCarreteras.clear();
		listaVehiculos.clear();
		mapaCruces.clear();
		mapaCarreteras.clear();
		mapaVehiculos.clear();
	}
	

 }
