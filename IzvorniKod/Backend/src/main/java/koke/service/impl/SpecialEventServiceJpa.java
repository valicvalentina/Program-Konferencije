package koke.service.impl;

import java.util.List;
import java.util.Set;

import koke.domain.DataGroup;
import koke.domain.Conference;
import koke.exception.RequestDeniedException;
import koke.exception.ResourceNotFoundException;
import koke.service.ConferenceService;
import koke.service.DataGroupService;
import koke.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import koke.dao.SpecialEventRepository;
import koke.domain.SpecialEvent;
import koke.domain.UserAccount;
import koke.service.SpecialEventService;

@Service
public class SpecialEventServiceJpa implements SpecialEventService {

	@Autowired
	public SpecialEventRepository specialEventRepo;

	@Autowired
	public DataGroupService dataGroupService;

	@Autowired
	public UserAccountService userAccountService;


	@Override
	public List<SpecialEvent> listAll() {
		return specialEventRepo.findAll();
	}

	@Override
	public SpecialEvent createSpecialEvent(SpecialEvent event) {
		if (event == null)
			throw new RequestDeniedException("SpecialEvent object must be given");
		Conference conference = event.getConference();
		DataGroup dataGroup = new DataGroup("Obavijest: " + event.getType(), event.getMessage());
		dataGroup.setConference(conference);
		dataGroupService.createOptionalMandatoryDataGroup(dataGroup);
		return specialEventRepo.save(event);
	}

	@Override
	public SpecialEvent createSpecialEvent(int capacity, String type, String message) {
		if (capacity < 0 || type == null || message == null) {
			throw new RequestDeniedException("Must enter capacity, type, message");
		}
		return specialEventRepo.save(new SpecialEvent(capacity, type, message));
	}

	@Override
	public SpecialEvent applyToSpecialEvent(UserAccount user, Long idSpecialEvent) {
		SpecialEvent specialEvent = getBySpecialEventId(idSpecialEvent);
		specialEvent.addPending(user);
		userAccountService.addAppliedSpecialEvent(user.getIdUserAccount(), specialEvent);
		UserAccount mainAdmin = specialEvent.getConference().getMainAdmin();
		userAccountService.sendMailToMainAdmin(mainAdmin, specialEvent.getType());
		return specialEventRepo.save(specialEvent);
	}

	@Override
	public Set<UserAccount> getPending(Long idSpecialEvent) {
		return getBySpecialEventId(idSpecialEvent).getPending();
	}

	@Override
	public SpecialEvent getBySpecialEventId(Long idSpecialEvent) {
		return specialEventRepo.findById(idSpecialEvent)
				.orElseThrow(() -> new ResourceNotFoundException("No special event with id " + idSpecialEvent));
	}

	@Override
	public SpecialEvent attendToSpecialEvent(UserAccount user, Long idSpecialEvent) {
		SpecialEvent specialEvent = getBySpecialEventId(idSpecialEvent);
		specialEvent.addAttendee(user);
		userAccountService.addAttendedSpecialEvent(user.getIdUserAccount(), specialEvent);
		return specialEventRepo.save(specialEvent);
	}

	@Override
	public Set<UserAccount> getAttendees(Long idSpecialEvent) {
		return getBySpecialEventId(idSpecialEvent).getAttendees();
	}

	@Override
	public SpecialEvent increaseCapacity(Long idSpecialEvent, Integer increase) {
		SpecialEvent specialEvent = getBySpecialEventId(idSpecialEvent);
		specialEvent.setCapacity(specialEvent.getCapacity() + increase);
		userAccountService.sendNotificationMail(specialEvent, increase);
		moveFromPendingToAttendees(specialEvent, increase);
		return specialEventRepo.save(specialEvent);
	}

	private void moveFromPendingToAttendees(SpecialEvent specialEvent, Integer increase) {
		List<UserAccount> pending = specialEvent.getPending().stream().toList();

		for (int i = 0; i < increase && i < pending.size(); i++) {
			UserAccount moved = pending.get(i);
			specialEvent.getPending().remove(moved);
			specialEvent.getAttendees().add(moved);
		}

		specialEventRepo.save(specialEvent);
	}

	@Override
	public int getNumberOfPending(Long idSpecialEvent) {
		return getPending(idSpecialEvent).size();
	}
}
