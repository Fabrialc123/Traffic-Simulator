package simulator.model;

import java.util.ArrayList;
import java.util.List;

public enum Weather {
	SUNNY(2,2), 
	CLOUDY(3,2),
	RAINY(10,2),
	WINDY(15,10),
	STORM(20,10);
	
	private int reduceContamination;
	private int reduceContaminationUrban;
	
	private Weather(
			int reduceContamination,
			int reduceContaminationUrban
			)
	{
		this.reduceContamination = reduceContamination;
		this.reduceContaminationUrban = reduceContaminationUrban;
	}
	
	public int getReduceContamination() {
		return reduceContamination;
	}

	public int getReduceContaminationCity() {
		return reduceContaminationUrban;
	}
	
	public boolean isStorm() {
		return (this == Weather.STORM);
	}

	public static Weather parse(String cadenaEntrada) {
		for (Weather weather : Weather.values())
			if (weather.name().equalsIgnoreCase(cadenaEntrada))
				return weather;
		return SUNNY;
	}

	public static List<Weather> getWeathers() {
		List<Weather> weathers = new ArrayList<Weather>();
		for (Weather w: Weather.values())	weathers.add(w);
		return weathers;
	}
	
	
}
