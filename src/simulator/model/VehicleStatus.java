package simulator.model;

public enum VehicleStatus {
	PENDING, TRAVELING, WAITING, ARRIVED;
	
	
	
	
	public static VehicleStatus parse(String cadenaEntrada) {
		for (VehicleStatus status : VehicleStatus.values())
			if (status.name().equalsIgnoreCase(cadenaEntrada))
				return status;
		return PENDING;
	}
}
