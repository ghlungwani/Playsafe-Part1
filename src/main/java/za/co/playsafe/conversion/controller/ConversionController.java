package za.co.playsafe.conversion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import za.co.playsafe.conversion.service.ConversionService;

@Controller
@RequestMapping("/api/conversion")
public class ConversionController {

	
	@Autowired
	private ConversionService conversionService;
	
	@GetMapping(value="/kelvin/to/celcius/{kelvin}")
	public @ResponseBody ResponseEntity<Double> getkelvin(@PathVariable("kelvin") String kelvin) {
		
		Double celcius = conversionService.getCelsiusFromkelvin(kelvin);
		
		return new ResponseEntity<>(celcius, HttpStatus.OK);
	}
	
	@GetMapping(value="/celcius/to/kelvin/{celcius}")
	public @ResponseBody ResponseEntity<Double> getCelcius(@PathVariable("celcius") String celcius) {
		
		Double kelvin = conversionService.getkelvinFromCelsius(celcius);
		
		return new ResponseEntity<>(kelvin, HttpStatus.OK);
	}
	
	@GetMapping(value="/kilometres/to/miles/{kilometres}")
	public @ResponseBody ResponseEntity<Double> getMiles(@PathVariable("kilometres") String kilometres) {
		
		Double miles = conversionService.getMilesFromkilometers(kilometres);
		
		return new ResponseEntity<>(miles, HttpStatus.OK);
	}
	
	@GetMapping(value="/miles/to/kilometres/{miles}")
	public @ResponseBody ResponseEntity<Double> getkilometres(@PathVariable("miles") String miles) {
		
		Double kilometres = conversionService.getkilometersFromMiles(miles);
		
		return new ResponseEntity<>(kilometres, HttpStatus.OK);
	}
}
