package koke.rest;

import java.util.*;
import java.util.stream.Collectors;

import koke.dto.*;
import koke.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import koke.service.ConferenceService;

@RestController
@RequestMapping("/")
public class ConferenceController {
	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private UserAccountService accountService;

	// GET METODE

	/*
	 * Dohvati sve aktivne konferencije.
	 */
	@GetMapping("/active")
	public List<ConferenceDTO> listActiveConferences() {
		Calendar calendar = Calendar.getInstance();
		//calendar.add(Calendar.DATE, -1);
		Date active = calendar.getTime();
		return conferenceService.getAll().stream().filter(conference -> conference.getDateStart().before(active) && active.compareTo(conference.getDateEnd()) <= 0)
				.map(c -> ConferenceDTO.toDTO(c)).collect(Collectors.toList());
	}

	/*
	 * Dohvaća sve konferencije bez main admina.
	 */

	@GetMapping("/noMainAdmin")
	public ResponseEntity<List<ConferenceDTO>> listConferencesWithoutMainAdmin(@AuthenticationPrincipal User user) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))) {
			return new ResponseEntity<>(
					conferenceService.getAllConferencesWithoutMainAdmin().stream()
							.map(conference -> ConferenceDTO.toDTO(conference)).collect(Collectors.toList()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvati konferenciju za korisnika.
	 * 
	 * @param idUserAccount
	 */

	@GetMapping("/myConference/{idUserAccount}")
	public ResponseEntity<ConferenceDTO> getMyConference(@AuthenticationPrincipal User user,
			@PathVariable Long idUserAccount) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(
					ConferenceDTO.toDTO(conferenceService.getConferenceByUserAccountId(idUserAccount)), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvati konferenciju za korisnika - LISTA.
	 * 
	 * @param idUserAccount
	 */

	@GetMapping("/myConferenceList/{idUserAccount}")
	public ResponseEntity<List<ConferenceDTO>> getMyConferenceList(@AuthenticationPrincipal User user,
			@PathVariable Long idUserAccount) {
//		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))){
//			return new ResponseEntity<>(conferenceService.getAll().stream().map(conference -> ConferenceDTO.toDTO(conference)).collect(Collectors.toList()), HttpStatus.OK);
//		}
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(
					List.of(ConferenceDTO.toDTO(conferenceService.getConferenceByUserAccountId(idUserAccount))),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvati sve sudionike na nekoj konferenciji.
	 * 
	 * @param idConference
	 */
	@GetMapping("/myConference/{idConference}/participants")
	public ResponseEntity<Set<UserAccountDTO>> getParticipants(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {
			if (idConference == accountService.getUserAccountByUsername(user.getUsername()).getConference()
					.getIdConference()) {
				return new ResponseEntity<>(
						conferenceService.getParticipants(idConference).stream()
								.map(userAccount -> UserAccountDTO.toDTO(userAccount)).collect(Collectors.toSet()),
						HttpStatus.OK);
			} else
				return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvati sve operativne admine na nekoj konferenciji.
	 *
	 * @param idConference
	 */
	@GetMapping("/myConference/{idConference}/operative")
	public ResponseEntity<Set<UserAccountDTO>> getOperative(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {
			if (idConference == accountService.getUserAccountByUsername(user.getUsername()).getConference()
					.getIdConference()) {
				return new ResponseEntity<>(
						conferenceService.getOperativeAdmins(idConference).stream()
								.map(userAccount -> UserAccountDTO.toDTO(userAccount)).collect(Collectors.toSet()),
						HttpStatus.OK);
			} else
				return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvati sve grupe podataka neke konferencije.
	 * 
	 * @param idConference
	 */

	@GetMapping("/dataGroup/{idConference}")
	public ResponseEntity<List<DataGroupDTO>> getDataGroupsByConferenceId(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(conferenceService.getDataGroupsByConferenceId(idConference).stream()
					.map(dataGroup -> DataGroupDTO.toDTO(dataGroup)).toList(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvaća obavezne grupe podataka neke konferencije.
	 * 
	 * @param idConference
	 */

	@GetMapping("/mandatoryDataGroups/{idConference}")
	public ResponseEntity<List<DataGroupDTO>> getMandatoryDataGroupsByConferenceId(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {
			return new ResponseEntity<>(conferenceService.getMandatoryDataGroupsByConferenceId(idConference).stream()
					.map(mdg -> DataGroupDTO.toDTO(mdg)).toList(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvaca sve specijalne evente za odredenu konferenciju.
	 * 
	 * @param idConference
	 */

	@GetMapping("/get/{idConference}")
	public ResponseEntity<List<SpecialEventDTO>> getSpecialEventsByConferenceId(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {
			return new ResponseEntity<>(conferenceService.getSpecialEventsByConferenceId(idConference).stream()
					.map(specialEvent -> SpecialEventDTO.toDTO(specialEvent)).toList(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Vraća jesu li postavljene sve obavezne grupe podataka.
	 * 
	 * @param idConference
	 * @return true if all mandatory data is not null
	 */

	@GetMapping("/mandatorySet/{idConference}")
	public ResponseEntity<Boolean> areMandatoryDataGroupsSet(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(conferenceService.checkIfAllMandatoryDataGroupsDataIsNotNull(idConference),
					HttpStatus.OK);
		}
		// response entity
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

//	/**
//	 * Izrađuje file o konferenciji i pohranjuje ga u odabrani direktorij main
//	 * admina.
//	 *
//	 * @param idConference
//	 * @param dir
//	 * @return true if file is added, false if any exception occured
//	 */
//
//	@PostMapping("/getConfInfo/{idConference}")
//	public ResponseEntity<Boolean> getConferenceInfoInPdf(@AuthenticationPrincipal User user, @PathVariable Long idConference, @RequestBody PathDTO dir) {
//		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {
//		File d = new File(dir.getPath());
//		return new ResponseEntity<>(conferenceService.createFile(d, idConference), HttpStatus.OK);
//	    }
//		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
//
//	}

	// POST METODE

	/**
	 * Kreiraj konferenciju.
	 * 
	 * @param conference
	 */

	@PostMapping("")
	public ResponseEntity<ConferenceDTO> createConference(@AuthenticationPrincipal User user,
			@RequestBody ConferenceDTO conference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))) {
			if (conference.getDateStart().after(conference.getDateEnd()))
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(ConferenceDTO.toDTO(conferenceService.createConference(conference.getName(),
					conference.getCity(), conference.getCountry(), conference.getDateStart(), conference.getDateEnd(),
					conference.getDescription(), conference.getTopics())), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
	}

}
