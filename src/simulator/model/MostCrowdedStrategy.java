package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot){
		this.timeSlot = timeSlot;
	}
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		int nextGreen = 0;
		int mayorTrafico = 0;
		if (roads.size() == 0) nextGreen = -1;
		else if (currGreen == -1) {
			for (int i = 0; i < roads.size(); i++) {				// Recorre desde 0 para ver quien tiene la cola de espera mas larga
				if (qs.get(i).size() > mayorTrafico ) {
					mayorTrafico = qs.get(i).size();
					nextGreen = i;
				}
			}
		}
		else if ((currTime - lastSwitchingTime) < this.timeSlot) nextGreen = currGreen;
		else {
			int i = (currGreen + 1) % roads.size();
			nextGreen = currGreen;
			while (i != currGreen) {								// Recorre circularmente
				if (qs.get(i).size() > mayorTrafico ) {
					mayorTrafico = qs.get(i).size();
					nextGreen = i;
				}
				i = (i + 1) % roads.size();
			}
		}
		return nextGreen;
	}
	
}
