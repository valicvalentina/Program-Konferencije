package koke.rest;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

import java.util.stream.Collectors;

import koke.domain.VerificationToken;
import koke.dto.LoginDTO;
import koke.dto.PathDTO;
import koke.dto.UserAccountDTO;
import koke.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import koke.domain.Conference;
import koke.domain.UserAccount;
import koke.service.ConferenceService;
import koke.service.UserAccountService;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/users")
public class UserAccountController {
	@Autowired
	private UserAccountService accountService;
	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private VerificationTokenService verificationTokenService;
	/**
	 * Dohvaca usera po id-ju.
	 * 
	 * @param idUserAccount
	 */
	@GetMapping("/get/{idUser}")
	public ResponseEntity<UserAccountDTO> getUserById(@AuthenticationPrincipal User user, @PathVariable Long idUserAccount) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))){
			UserAccount userAccount = accountService.getUserAccountByIdUserAccount(idUserAccount);
			return new ResponseEntity<>(UserAccountDTO.toDTO(userAccount), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvaca sve usere.
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<UserAccountDTO>> getAll(@AuthenticationPrincipal User user) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))) {
			return new ResponseEntity<>(accountService.listAll().stream().map(userAccount -> UserAccountDTO.toDTO(userAccount)).toList(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Dohvati sve korisnike prema državi.
	 * 
	 * @return
	 */
	@GetMapping("/getByCountry/{idConference}")
	public ResponseEntity<Map<String, List<UserAccountDTO>>> getUserByCountry(@AuthenticationPrincipal User user,
			@PathVariable Long idConference) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {
			List<UserAccount> allUsers = accountService.listAll().stream()
					.filter(a -> (a.isParticipant() == true || a.isOperativeAdmin() == true)
							&& a.getConference().getIdConference() == idConference)
					.collect(Collectors.toList());
			return new ResponseEntity<>(allUsers.stream().map(a -> UserAccountDTO.toDTO(a))
					.collect(Collectors.groupingBy(UserAccountDTO::getCountry)), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	@GetMapping("/confirmRegistration")
	public ResponseEntity confirmRegistration(WebRequest request, @RequestParam("token") String token) {
		VerificationToken verificationToken = verificationTokenService.findByToken(token);
		if(verificationToken == null) return ResponseEntity.badRequest().body("Token doesn't exist!");
		if(verificationToken.getExpiryDate().before(new Timestamp(System.currentTimeMillis())))
			return ResponseEntity.badRequest().body("Token has expired!");
		accountService.confirmRegistration(token);
		return ResponseEntity.ok().body("You have successfully confirmed your Conference app user account!");
	}

//	/**
//	 * Generira pdf certifikat u dani direktorij.
//	 * @param idUserAccount
//	 * @param dir
//	 */
//	@PostMapping("/certificate/{idUserAccount}")
//	public ResponseEntity<Boolean> getCertificate(@AuthenticationPrincipal User user, @PathVariable Long idUserAccount, @RequestBody PathDTO dir) {
//		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
//			File d = new File(dir.getPath());
//			return new ResponseEntity<>(accountService.getCertificate(idUserAccount, d), HttpStatus.OK);
//		}
//		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
//	}

	// POST METODE

	/**
	 * Kreira sudionika na nekoj konferenciji.
	 * 
	 * @param account
	 * @param idConference
	 */
	@PostMapping("/createParticipant/{idConference}")
	public ResponseEntity<UserAccountDTO> createParticipantUserAccount(@AuthenticationPrincipal User user, @RequestBody UserAccountDTO account,
			@PathVariable Long idConference) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))) {
			Conference conference = conferenceService.getByConferenceId(idConference);
			UserAccount participant = accountService.createParticipantAccount(account.getFirstAndLastName(),
					account.getPhoneNumber(), account.getEmail(), account.getCompanyName(), account.getCountry(),
					account.getAddress(), account.getUsername(), account.getPassword(), conference,
					account.getDetailsOfParticipation());
			conference.addUser(participant);
			conferenceService.updateConference(conference.getIdConference(), conference);
			return new ResponseEntity<>(UserAccountDTO.toDTO(participant), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Kreira operativnog admina neke konferencije.
	 * 
	 * @param account
	 * @param idConference
	 */
	@PostMapping("/createOperativeAdmin/{idConference}")
	public ResponseEntity<UserAccountDTO> createOperativeUserAccount(@AuthenticationPrincipal User user, @RequestBody UserAccountDTO account,
			@PathVariable Long idConference) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))) {
			Conference conference = conferenceService.getByConferenceId(idConference);
			UserAccount operationalAdmin = accountService.createOperativeAccount(account.getFirstAndLastName(),
					account.getPhoneNumber(), account.getEmail(), account.getCompanyName(), account.getCountry(),
					account.getAddress(), account.getUsername(), account.getPassword(), conference,
					account.getDetailsOfParticipation());
			conference.addOperativeAdmin(operationalAdmin);
			conferenceService.updateConference(conference.getIdConference(), conference);
			return new ResponseEntity<>(UserAccountDTO.toDTO(operationalAdmin), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Kreira main admina neke konferencije.
	 * 
	 * @param account
	 * @param idConference
	 */
	@PostMapping("/createMainAdmin/{idConference}")
	public ResponseEntity<UserAccountDTO> createMainUserAccount(@AuthenticationPrincipal User user, @RequestBody UserAccountDTO account, @PathVariable Long idConference) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))) {
			Conference conference = conferenceService.getByConferenceId(idConference);
			UserAccount mainAdmin = accountService.createMainUserAccount(account.getFirstAndLastName(),
					account.getPhoneNumber(), account.getEmail(), account.getCompanyName(), account.getCountry(),
					account.getAddress(), account.getUsername(), account.getPassword(), conference,
					account.getDetailsOfParticipation());
			conference.setMainAdmin(mainAdmin);
			conferenceService.updateConference(conference.getIdConference(), conference);
			return new ResponseEntity<>(UserAccountDTO.toDTO(mainAdmin), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

	}

	/**
	 * Login. Ako nije proslo 30 dana od kraja konferencije, user se moze ulogirati.
	 * 
	 * @param loginDTO
	 * @return 403 Forbidden ako se korisnik pokusava ulogirati nakon sto je proslo
	 *         30 dana od konferencije (40 dana za main admina)
	 * @return 200 OK, UserAccountDTO ako je login uspjesan
	 */
	@PostMapping("/login")
	public ResponseEntity<UserAccountDTO> login(@RequestBody LoginDTO loginDTO) {
		Date current = new Date();
		Calendar conferenceEnded = Calendar.getInstance();
		UserAccountDTO user = accountService.login(loginDTO);
		if(user == null) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
 		if (user.getUserAccountRole() == UserAccountRole.SYSTEMOWNER) {
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		conferenceEnded.setTime(conferenceService.getByConferenceId(user.getConferenceId()).getDateEnd());
		conferenceEnded.add(Calendar.DAY_OF_MONTH, 31);
		if (user.getUserAccountRole() != UserAccountRole.MAINADMIN && current.after(conferenceEnded.getTime()))
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		conferenceEnded.add(Calendar.DAY_OF_MONTH, 10);
		if (user.getUserAccountRole() == UserAccountRole.MAINADMIN && current.after(conferenceEnded.getTime()))
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@EventListener
	public void  appReady(ApplicationReadyEvent event) {
		accountService.createSystemOwner();
	}

}
