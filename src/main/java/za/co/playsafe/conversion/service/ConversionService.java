package za.co.playsafe.conversion.service;

public interface ConversionService {
	
	public Double getkelvinFromCelsius(String celciusInput);
	
	public Double getCelsiusFromkelvin(String kelvinInput);
	
	public Double getkilometersFromMiles(String milesInput);
	
	public Double getMilesFromkilometers(String kilometresInput);

}
