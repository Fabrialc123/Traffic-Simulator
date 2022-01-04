package simulator.model;

import simulator.exceptions.InvalidParameterException;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJun, Junction desJunc, int maxSpeed, int contLimit, int lenght, Weather weather)
			throws InvalidParameterException {
		super(id, srcJun, desJunc, maxSpeed, contLimit, lenght, weather);
	}

	@Override
	void reduceTotalContamination() {
		this.totalContamination -= this.weatherConditions.getReduceContaminationCity();
		if (this.totalContamination < 0) {
			this.totalContamination = 0;
		}
		
	}

	@Override
	void updateSpeedLimit() {
		this.currentSpeed = this.maximunSpeed;			// Siempre es la velocidad maxima
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		//return (int)(((11.0 - v.getContaminationClass())/11.0)*this.currentSpeed);				//Sin redondeo
		return (int)Math.ceil((((11.0 - v.getContaminationClass())/11.0)*this.currentSpeed));		// Con redondeo
	}

}
