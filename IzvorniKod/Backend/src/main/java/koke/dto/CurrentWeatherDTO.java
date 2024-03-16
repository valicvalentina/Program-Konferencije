package koke.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CurrentWeatherDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String description;
	private BigDecimal temperature;
	private BigDecimal feelsLike;
	private BigDecimal windSpeed;

	public CurrentWeatherDTO(String description, BigDecimal temperature, BigDecimal feelsLike, BigDecimal windSpeed) {
		super();
		this.description = description;
		this.temperature = temperature;
		this.feelsLike = feelsLike;
		this.windSpeed = windSpeed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getTemperature() {
		return temperature;
	}

	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}

	public BigDecimal getFeelsLike() {
		return feelsLike;
	}

	public void setFeelsLike(BigDecimal feelsLike) {
		this.feelsLike = feelsLike;
	}

	public BigDecimal getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(BigDecimal windSpeed) {
		this.windSpeed = windSpeed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((feelsLike == null) ? 0 : feelsLike.hashCode());
		result = prime * result + ((temperature == null) ? 0 : temperature.hashCode());
		result = prime * result + ((windSpeed == null) ? 0 : windSpeed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrentWeatherDTO other = (CurrentWeatherDTO) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (feelsLike == null) {
			if (other.feelsLike != null)
				return false;
		} else if (!feelsLike.equals(other.feelsLike))
			return false;
		if (temperature == null) {
			if (other.temperature != null)
				return false;
		} else if (!temperature.equals(other.temperature))
			return false;
		if (windSpeed == null) {
			if (other.windSpeed != null)
				return false;
		} else if (!windSpeed.equals(other.windSpeed))
			return false;
		return true;
	}

}
