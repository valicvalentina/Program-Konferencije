package koke.rest;

import koke.dto.CurrentWeatherDTO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import koke.service.impl.LiveWeatherService;
import koke.service.impl.StubWeatherService;

@RestController
@RequestMapping("/weather")
public class CurrentWeatherController {

	@Autowired
	private final StubWeatherService stubWeatherService;
	@Autowired
	private final LiveWeatherService liveWeatherService;

	public CurrentWeatherController(StubWeatherService stubWeatherService, LiveWeatherService liveWeatherService) {
		this.stubWeatherService = stubWeatherService;
		this.liveWeatherService = liveWeatherService;
	}

	/**
	 * Dohvaća prognozu za neku konferenciju.
	 * 
	 * @param idConference
	 */
	@GetMapping("/{idConference}")
	public ResponseEntity<CurrentWeatherDTO> getCurrentWeather(@AuthenticationPrincipal User user, @PathVariable Long idConference) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			try {
				return new ResponseEntity<>(liveWeatherService.getCurrentWeather(idConference), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(stubWeatherService.getCurrentWeather(), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}
}
