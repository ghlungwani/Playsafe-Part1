package za.co.playsafe.conversion.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConversionServiceImpl implements ConversionService {

	@Value("${Celcius.kelvin.Constant}")
	private Double celkelConst;

	@Value("${kilometre.Conversion.factor}")
	private Double kilConversionfactor;

	@Value("${mile.Conversion.factor}")
	private Double mileConversionFactor;

	public static final String DEFAULT_NULL_ERROR_MESSAGE = "Null Value.";

	@Override
	public Double getkelvinFromCelsius(String celciusInput) {

		checkkInputValidity(celciusInput);
		return (new Double(celciusInput) + celkelConst);
	}

	@Override
	public Double getCelsiusFromkelvin(String kelvinInput) {
		checkkInputValidity(kelvinInput);
		return (new Double(kelvinInput) - celkelConst);
	}

	@Override
	public Double getkilometersFromMiles(String milesInput) {
		checkkInputValidity(milesInput);
		return (new Double(milesInput) * kilConversionfactor);
	}

	@Override
	public Double getMilesFromkilometers(String kilometresInput) {
		checkkInputValidity(kilometresInput);
		return (new Double(kilometresInput) * mileConversionFactor);
	}

	// Check validity of input
	private void checkkInputValidity(String input) {
		if (StringUtils.isBlank(input) || !StringUtils.isNumeric(input)) {
			throw new RuntimeException(DEFAULT_NULL_ERROR_MESSAGE);
		}
	}

}
