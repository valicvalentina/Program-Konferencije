package koke.service;

import java.util.List;
import java.util.Set;

import koke.domain.SpecialEvent;
import koke.domain.UserAccount;

public interface SpecialEventService {
	public List<SpecialEvent> listAll();

	public SpecialEvent createSpecialEvent(SpecialEvent event);

	public SpecialEvent createSpecialEvent(int capacity, String type, String message);

	SpecialEvent applyToSpecialEvent(UserAccount user, Long idSpecialEvent);

	public Set<UserAccount> getPending(Long idSpecialEvent);

	public SpecialEvent getBySpecialEventId(Long idSpecialEvent);

	public SpecialEvent attendToSpecialEvent(UserAccount user, Long idSpecialEvent);

	public Set<UserAccount> getAttendees(Long idSpecialEvent);

	public SpecialEvent increaseCapacity(Long idSpecialEvent, Integer increase);

	public int getNumberOfPending(Long idSpecialEvent);
}
