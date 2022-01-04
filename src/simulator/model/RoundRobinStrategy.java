package simulator.model;
//!
import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy{
	private int timeSlot;
	public RoundRobinStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		int nextGreen = 0;
		if(roads.size() == 0) {
			nextGreen = -1;
		}else if(currGreen == -1) {
			nextGreen = 0;
		}else if(currTime - lastSwitchingTime < timeSlot) {
			nextGreen = currGreen;
		}else {
			nextGreen = (currGreen + 1) % roads.size(); 
		}
		
		return nextGreen;
	}
}
