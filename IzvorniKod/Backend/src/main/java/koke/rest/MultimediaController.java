package koke.rest;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import koke.dto.DateDTO;
import koke.dto.MultimediaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import koke.service.ConferenceService;
import koke.service.MultimediaService;

@RestController
@RequestMapping("/multimedia")
public class MultimediaController {

	@Autowired
	MultimediaService multimediaService;

	@Autowired
	ConferenceService conferenceService;

	// GET METODE

	/**
	 * Dohvaca sve slike.
	 *
	 */
	@GetMapping("/getImages/{idConference}")
	public ResponseEntity<List<MultimediaDTO>> getImages(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(multimediaService.getAllImages(idConference).stream()
					.map(a -> MultimediaDTO.toDTO(a)).collect(Collectors.toList()), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvaca sve videe.
	 *
	 */
	@GetMapping("/getVideos/{idConference}")
	public ResponseEntity<List<MultimediaDTO>> getVideo(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(multimediaService.getAllVideo(idConference).stream()
					.map(a -> MultimediaDTO.toDTO(a)).collect(Collectors.toList()), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvaca multimediju po id-ju.
	 *
	 * @param idMultimedia
	 */
	@GetMapping("/get/{idMultimedia}")
	public ResponseEntity<MultimediaDTO> getImageById(@AuthenticationPrincipal User user,
			@PathVariable Long idMultimedia) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(MultimediaDTO.toDTO(multimediaService.getMultimediaById(idMultimedia)),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

//	/**
//	 * Preuzmi sliku.
//	 *
//	 * @param idMultimedia
//	 */
//	@GetMapping("/downloadImage/{idMultimedia}")
//	public ResponseEntity<Boolean> downloadImage(@AuthenticationPrincipal User user, @PathVariable Long idMultimedia, @RequestBody PathDTO dir) {
//		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
//				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
//				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
//				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
//			return new ResponseEntity<>(multimediaService.downloadImage(idMultimedia, dir), HttpStatus.OK);
//		}
//		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
//	}

	/**
	 * Dohvaca slike po danima.
	 *
	 * @param idConference
	 */
	@GetMapping("/getByDay/{idConference}")
	public ResponseEntity<Map<Date, List<MultimediaDTO>>> getMultimediaByDay(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(
					multimediaService.getAllImages(idConference).stream().map(a -> MultimediaDTO.toDTO(a))
							.filter(a -> a.getDate() != null).collect(Collectors.groupingBy(MultimediaDTO::getDate)),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	@GetMapping("/getDatesWithMultimedia/{idConference}")
	public ResponseEntity<List<Date>> getDatesWithMultimedia(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(multimediaService.getAllImages(idConference).stream()
					.map(multimedia -> multimedia.getDate()).distinct().toList(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	@GetMapping("/numberOfDays/{idConference}")
	public ResponseEntity<Integer> getNumberOfDays(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(multimediaService.getAllImages(idConference).stream().map(a -> a.getDate())
					.distinct().toList().size(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	// POST METODE

	/**
	 * Dodaje multimediju u bazu.
	 *
	 * @param idConference
	 * @param multimedia
	 */
	@PostMapping("/add/{idConference}")
	public ResponseEntity<MultimediaDTO> addMultimedia(@AuthenticationPrincipal User user,
			@PathVariable Long idConference, @RequestBody MultimediaDTO multimedia) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {
			if (conferenceService.getByConferenceId(idConference).getDateStart().after(multimedia.getDate())
					|| conferenceService.getByConferenceId(idConference).getDateEnd().before(multimedia.getDate()))
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(
					MultimediaDTO.toDTO(
							multimediaService.addMultimedia(multimedia.getUrl(), multimedia.getDate(), idConference)),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	@PostMapping("getAllImagesForDate/{idConference}")
	public ResponseEntity<List<MultimediaDTO>> getAllImagesForDate(@AuthenticationPrincipal User user,
			@RequestBody DateDTO dateDTO, @PathVariable Long idConference) {
		System.out.println(dateDTO.getDate());
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			System.out.println("Doslo do tu");

			return new ResponseEntity<>(multimediaService.getAllImagesForDate(dateDTO.getDate(), idConference),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}
}