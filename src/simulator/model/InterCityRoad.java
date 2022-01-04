package simulator.model;

import simulator.exceptions.InvalidParameterException;

public class InterCityRoad extends Road{

	InterCityRoad(String id, Junction srcJun, Junction desJunc, int maxSpeed, int contLimit, int lenght,
			Weather weather) throws InvalidParameterException {
		super(id, srcJun, desJunc, maxSpeed, contLimit, lenght, weather);
	}

	@Override
	void reduceTotalContamination() {
		//this.totalContamination = (int)Math.ceil((((100.0 - this.weatherConditions.getReduceContamination())/100)*this.totalContamination));		// Con Redondeo
		this.totalContamination = (int)(((100.0 - this.weatherConditions.getReduceContamination())/100)*this.totalContamination);					// Sin redondeo
		if (this.totalContamination < 0) {
			this.totalContamination = 0;
		}
		
	}

	@Override
	void updateSpeedLimit() {
		if(this.totalContamination > this.contaminationAlarmLimit) {
			//this.currentSpeed = (int)(this.maximunSpeed*0.5);				//Sin redondeo
			this.currentSpeed = (int)Math.ceil(this.maximunSpeed*0.5);		// Con redondeo
		}else this.currentSpeed = this.maximunSpeed;
		
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int vel = 0;
		if(this.weatherConditions.isStorm()) {		
			//vel = (int)(this.currentSpeed*0.8);						//Sin redondeo
			vel = (int)Math.ceil((this.currentSpeed*0.8));				// Con redondeo
		}else vel = this.currentSpeed;
		return vel;
	}

}
