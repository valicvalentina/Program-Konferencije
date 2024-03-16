package koke.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import koke.dto.CurrentWeatherDTO;

@Service
public class StubWeatherService {
 
    public CurrentWeatherDTO getCurrentWeather() {
        return new CurrentWeatherDTO("Clear", BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.TEN);
    }
}
