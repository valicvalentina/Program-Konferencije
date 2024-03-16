package koke.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SpecialEvent {

	@Id
	@GeneratedValue
	private Long idSpecialEvent;

	@ManyToOne
	private Conference conference;

	@ManyToMany
	private Set<UserAccount> attendees = new HashSet<>();
	@ManyToMany
	private Set<UserAccount> pending = new LinkedHashSet<>();

	@NotNull(message = "capacity shouldn't be null")
	private int capacity;

	@NotNull(message = "type shouldn't be null")
	private String type;

	private String message;

	public SpecialEvent() {
	}

	public SpecialEvent(int capacity2, String type2, String message2) {
		this.capacity = capacity2;
		this.type = type2;
		this.message = message2;
	}

	public Long getIdSpecialEvent() {
		return idSpecialEvent;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Conference getConference() {
		return conference;
	}

	public Set<UserAccount> getAttendees() {
		return attendees;
	}

	public Set<UserAccount> getPending() {
		return pending;
	}

	public void setIdSpecialEvent(Long idSpecialEvent) {
		this.idSpecialEvent = idSpecialEvent;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public void setAttendees(HashSet<UserAccount> attendees) {
		this.attendees = attendees;
	}

	public void setPending(LinkedHashSet<UserAccount> pending) {
		this.pending = pending;
	}

	public void addPending(UserAccount user) {
		if (this.pending == null)
			this.pending = new LinkedHashSet<>();
		this.pending.add(user);
	}

	public void addAttendee(UserAccount user) {
		if (this.attendees == null)
			this.attendees = new HashSet<>();
		this.attendees.add(user);
	}
}
