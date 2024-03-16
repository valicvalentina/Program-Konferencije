package koke.rest;

import java.util.List;

import koke.dto.IncreaseDTO;
import koke.dto.SpecialEventDTO;
import koke.dto.UserAccountDTO;
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

import koke.domain.Conference;
import koke.domain.SpecialEvent;
import koke.domain.UserAccount;
import koke.service.ConferenceService;
import koke.service.SpecialEventService;
import koke.service.UserAccountService;

@RestController
@RequestMapping("/specialEvents")
public class SpecialEventController {
	@Autowired
	private SpecialEventService specialEventService;
	@Autowired
	private ConferenceService conferenceService;
	@Autowired
	private UserAccountService accountService;

	// GET METODE

	/**
	 * Dohvaca sve specijalne evente neke konferencije.
	 *
	 * @param idConference
	 */
	@GetMapping("/{idConference}")
	public ResponseEntity<List<SpecialEventDTO>> listEventsByConference(@AuthenticationPrincipal User user, @PathVariable Long idConference) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(specialEventService.listAll().stream().filter(a -> a.getConference().getIdConference() == idConference)
					.map(specialEvent -> SpecialEventDTO.toDTO(specialEvent)).toList(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvaca usere koji su se prijavili na special event.
	 */
	//ne koristimo ali koristi frontend, koji role?
	@GetMapping("/pending/{idSpecialEvent}")
	public ResponseEntity<List<UserAccountDTO>> getPending(@AuthenticationPrincipal User user, @PathVariable Long idSpecialEvent) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(specialEventService.getPending(idSpecialEvent).stream().map(userAccount -> UserAccountDTO.toDTO(userAccount)).toList(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvaca usere koji pohadaju special event.
	 */
	@GetMapping("/attending/{idSpecialEvent}")
	public ResponseEntity<List<UserAccountDTO>> getAttendees(@AuthenticationPrincipal User user, @PathVariable Long idSpecialEvent) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {
			return new ResponseEntity<>(specialEventService.getAttendees(idSpecialEvent).stream().map(userAccount -> UserAccountDTO.toDTO(userAccount))
					.toList(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Vraća ima li mjesta u nekom special eventu.
	 *
	 * @param idSpecialEvent
	 * @return true if current special event has free capacity
	 */
	@GetMapping("/free/{idSpecialEvent}")
	public ResponseEntity<Boolean> hasFreeCapacity(@AuthenticationPrincipal User user, @PathVariable Long idSpecialEvent) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(specialEventService.getBySpecialEventId(idSpecialEvent).getCapacity()
					- specialEventService.getAttendees(idSpecialEvent).size() > 0, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Vraća broj pending na nekom special eventu.
	 *
	 * @param idSpecialEvent
	 * @return size of pending
	 */
	@GetMapping("/pendingSize/{idSpecialEvent}")
	public ResponseEntity<Integer> numberOfPending(@AuthenticationPrincipal User user, @PathVariable Long idSpecialEvent) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {
			return new ResponseEntity<>(specialEventService.getNumberOfPending(idSpecialEvent), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	// POST METODE

	/**
	 * Kreira specijalni event za neku konferenciju.
	 *
	 * @param specialEvent
	 */
	@PostMapping("/add/{idConference}")
	public ResponseEntity<SpecialEventDTO> createSpecialEvent(@AuthenticationPrincipal User user, @RequestBody SpecialEventDTO specialEvent,
															  @PathVariable Long idConference) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {

			if (conferenceService.getCurrentNumberOfDataGroupsByConferenceId(idConference) >= 15)
				return new ResponseEntity<>(specialEvent, HttpStatus.METHOD_NOT_ALLOWED);

			SpecialEvent specialEvent2 = specialEventService.createSpecialEvent(specialEvent.getCapacity(),
					specialEvent.getType(), specialEvent.getMessage());
			Conference conference = conferenceService.getByConferenceId(idConference);
			specialEvent2.setConference(conference);
			conference.addSpecialEvent(specialEvent2);
			conferenceService.updateConference(idConference, conference);
			return new ResponseEntity<>(SpecialEventDTO.toDTO(specialEventService.createSpecialEvent(specialEvent2)),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * User se prijavljuje na special event.
	 *
	 * @param idSpecialEvent
	 * @param idUser
	 */
	@PostMapping("/apply/{idSpecialEvent}/{idUser}")
	public ResponseEntity<SpecialEventDTO> applyToSpecialEvent(@AuthenticationPrincipal User user, @PathVariable Long idSpecialEvent, @PathVariable Long idUser) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			UserAccount userAccount = accountService.getUserAccountByIdUserAccount(idUser);
			return new ResponseEntity<>(SpecialEventDTO.toDTO(specialEventService.applyToSpecialEvent(userAccount, idSpecialEvent)), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * User pohada event.
	 *
	 * @param idSpecialEvent
	 * @param idUser
	 */
	@PostMapping("/attend/{idSpecialEvent}/{idUser}")
	public ResponseEntity<SpecialEventDTO> attendToSpecialEvent(@AuthenticationPrincipal User user, @PathVariable Long idSpecialEvent,
																@PathVariable Long idUser) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {

			if (specialEventService.getAttendees(idSpecialEvent).size() >= specialEventService
					.getBySpecialEventId(idSpecialEvent).getCapacity()) {
				return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
			}
			UserAccount userAccount = accountService.getUserAccountByIdUserAccount(idUser);
			return new ResponseEntity<>(
					SpecialEventDTO.toDTO(specialEventService.attendToSpecialEvent(userAccount, idSpecialEvent)), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	@GetMapping("/isParticipantAttending/{idSpecialEvent}/{idUser}")
	public ResponseEntity<Boolean> isParticipantAttending(@AuthenticationPrincipal User user, @PathVariable Long idSpecialEvent,
														  @PathVariable Long idUser){
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			if(specialEventService.getAttendees(idSpecialEvent).contains(accountService.getUserAccountByIdUserAccount(idUser)))
				return new ResponseEntity<>(true, HttpStatus.OK);
			return new ResponseEntity<>(false, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	@GetMapping("/isParticipantPending/{idSpecialEvent}/{idUser}")
	public ResponseEntity<Boolean> isParticipantPending(@AuthenticationPrincipal User user, @PathVariable Long idSpecialEvent,
														  @PathVariable Long idUser){
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			if(specialEventService.getPending(idSpecialEvent).contains(accountService.getUserAccountByIdUserAccount(idUser)))
				return new ResponseEntity<>(true, HttpStatus.OK);
			return new ResponseEntity<>(false, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	@PostMapping("/increasingCapacity/{idSpecialEvent}")
	public ResponseEntity<SpecialEventDTO> increaseCapacity(@AuthenticationPrincipal User user, @PathVariable Long idSpecialEvent, @RequestBody IncreaseDTO increaseDTO){
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {

			if (specialEventService.getNumberOfPending(idSpecialEvent) <= 0) {
				return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
			}
			SpecialEvent specialEvent = specialEventService.increaseCapacity(idSpecialEvent, increaseDTO.getIncrease());
			return new ResponseEntity<>(SpecialEventDTO.toDTO(specialEvent), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

	}
}
