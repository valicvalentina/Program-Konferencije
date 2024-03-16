package koke.rest;

import java.util.List;

import koke.dto.DataGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import koke.domain.Conference;
import koke.domain.DataGroup;
import koke.service.ConferenceService;
import koke.service.DataGroupService;

@RestController
@RequestMapping("/dataGroup")
public class DataGroupController {
	@Autowired
	private DataGroupService dataGroupService;
	@Autowired
	private ConferenceService conferenceService;

	// GET METODE

	/**
	 * Dohvati sve grupe podataka. NEKORSITI SE??
	 */
	@GetMapping("/getAll")
	public List<DataGroupDTO> listAll() {
		return dataGroupService.listAll().stream().map(dataGroup -> DataGroupDTO.toDTO(dataGroup)).toList();
	}

	/**
	 * Dohvati sve grupe podataka za odredenu konferenciju.
	 * 
	 * @param idConference
	 */

	@GetMapping("/getAll/{idConference}")
	public ResponseEntity<List<DataGroupDTO>> listAllByConference(@AuthenticationPrincipal User user, @PathVariable Long idConference) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_OWNER"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATIVE_ADMIN"))
				|| user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
			return new ResponseEntity<>(dataGroupService.listAll().stream().filter(a -> a.getConference().getIdConference() == idConference)
					.map(dataGroup -> DataGroupDTO.toDTO(dataGroup)).toList(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	// POST METODE

	/**
	 * Kreirak grupu podataka za neku konferenciju.
	 * 
	 * @param dataGroup
	 * @param idConference id konferencije u kojoj ce grupa biti kreirana.
	 * @return DataGroupDTO ako je dodavanje bilo uspješno
	 * @return 405 Method Not Allowed ako konferencija vec ima 15 grupa podataka
	 * @return 422 unprocessable entity ako je group Name null
	 */
	@PostMapping("/add/{idConference}")
	public ResponseEntity<DataGroupDTO> createDataGroup(@AuthenticationPrincipal User user, @RequestBody DataGroupDTO dataGroup,
			@PathVariable Long idConference) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))){
			Assert.notNull(dataGroup, "DataGroup object must be given");
			int numberOfCurrentDataGroups = conferenceService.getCurrentNumberOfDataGroupsByConferenceId(idConference);
			if (numberOfCurrentDataGroups >= 15)
				return new ResponseEntity<>(dataGroup, HttpStatus.METHOD_NOT_ALLOWED);
			if (dataGroup.getGroupName() == null) {
				return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			DataGroup group = dataGroupService.createDataGroup(dataGroup.getGroupName(), dataGroup.getData());
			Conference conference = conferenceService.getByConferenceId(idConference);
			group.setConference(conference);
			conference.addDataGroup(group);
			conferenceService.updateConference(conference.getIdConference(), conference);
			return new ResponseEntity<>(DataGroupDTO.toDTO(dataGroupService.createDataGroup(group)), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	/**
	 * Postavi obaveznu grupu podataka.
	 * 
	 * @param dataGroup
	 */
	@PostMapping("/update")
	public ResponseEntity<DataGroupDTO> updateMandatoryDataGroup(@AuthenticationPrincipal User user, @RequestBody DataGroupDTO dataGroup) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MAIN_ADMIN"))){
			return new ResponseEntity<>(DataGroupDTO.toDTO(dataGroupService.updateMandatoryDataGroup(Long.valueOf(dataGroup.getIdDataGroup()),
					dataGroup.getData())), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}
}
