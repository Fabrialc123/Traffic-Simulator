package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.InvalidActionException;
import simulator.exceptions.InvalidParameterException;

public class Junction extends SimulatedObject {
	private List<Road> listaCarreterasEntrantes;
	private Map<Junction,Road> mapaCarreterasSalientes;
	private List<List<Vehicle>> queues;
	private int indSemaforoVerde;
	private int ultimoPaso;
	private LightSwitchingStrategy estrategiaSemaforo;
	private DequeuingStrategy estrategiaCola;
	private int x;
	private int y;
	
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy,
			int xCoor,int yCoor) throws InvalidParameterException {
		super(id);
		listaCarreterasEntrantes = new ArrayList<>();
		mapaCarreterasSalientes = new HashMap<>();
		indSemaforoVerde = 0;
		ultimoPaso = 1;
		queues = new ArrayList<>();
		if (lsStrategy != null)	this.estrategiaSemaforo = lsStrategy;
		else throw new InvalidParameterException("Error: lsStrategy in Junction " +this+ " is null." );
		if (dqStrategy != null )this.estrategiaCola = dqStrategy;
		else throw new InvalidParameterException("Error: dqStrategy in Junction " +this+ " is null." );
		if (xCoor >= 0 && yCoor >= 0) {
			this.x = xCoor;
			this.y = yCoor;
		}
		else throw new InvalidParameterException("Error: coords in Junction " +this+ " are negative." );
	}
	
	void addIncommingRoad(Road r) throws InvalidActionException {
		if(r.getDes().getId() == this._id) {
			listaCarreterasEntrantes.add(r);
			List<Vehicle> a = new LinkedList<>();
			queues.add(a);
		}
		else {
			throw new InvalidActionException("Error: Road " + r + " dont have Junction " + this + " as destiny.");
		}
	}
	
	void addOutGoingRoad(Road r) throws InvalidActionException {
		if(r.getSrc().getId() == this._id) {
			if(roadTo(r.getDes()) == null) {
				mapaCarreterasSalientes.put(r.getDes(), r);
			}
			else {
				throw new InvalidActionException("Error: Unvailable to connect Road " + r + " with Junction " + this + " , the Road has already other Junction connected.");
			}
		}
		else {
			throw new InvalidActionException("Error: Road "+ r + " dont have Junction " + this + " as source.");
		}
	}
	
	void enter(Vehicle v) {
		int i = listaCarreterasEntrantes.indexOf(v.getRoad());
		queues.get(i).add(v);
	}
	
	Road roadTo(Junction j) {
		Road a = mapaCarreterasSalientes.get(j);
		return a;
	}
	@Override
	void advance(int time) throws InvalidActionException {
		if((indSemaforoVerde < queues.size()) && indSemaforoVerde != -1 && (queues.get(indSemaforoVerde).size() != 0) ) {
			List<Vehicle> aux1 = estrategiaCola.dequeue(this.queues.get(indSemaforoVerde));
			queues.get(indSemaforoVerde).removeAll(aux1);
			for (Vehicle v: aux1) {
					v.moveToNextRoad();
			}
		}
		
		int indiceSemaforo = estrategiaSemaforo.chooseNextGreen(listaCarreterasEntrantes, queues, indSemaforoVerde, ultimoPaso, time);
		if (indiceSemaforo != this.indSemaforoVerde) {
			this.indSemaforoVerde = indiceSemaforo;
			this.ultimoPaso = time;
		}
		
	}
	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		if(indSemaforoVerde >= 0) {
			jo.put("green",listaCarreterasEntrantes.get(indSemaforoVerde).getId());
		}
		else jo.put("green","none");
		JSONArray re = new JSONArray();
		int indice = 0;
		for (List<Vehicle> lv: queues) {
			JSONObject obj = new JSONObject();
			JSONArray re2 = new JSONArray();
			for (Vehicle v: lv) {
				re2.put(v.getId());
			}
			obj.put("road", listaCarreterasEntrantes.get(indice).getId());
			obj.put("vehicles", re2);
			indice++;
			re.put(obj);
		}
		jo.put("queues",re);
		return jo;
	}
	
	public String getCurrentGreen() {
		return this.indSemaforoVerde >= 0 ? this.listaCarreterasEntrantes.get(indSemaforoVerde).getId() : "NONE";
	}

	public List<List<Vehicle>> getQueue() {
		return this.queues;
	}

	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

	public int getGreenLightIndex() {
		return this.indSemaforoVerde;
	}

	public List<Road> getInRoads() {
		return this.listaCarreterasEntrantes;
	}
	
	
}
