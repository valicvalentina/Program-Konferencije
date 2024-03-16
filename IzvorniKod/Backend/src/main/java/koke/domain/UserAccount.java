package koke.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
public class UserAccount implements Serializable {
	@Id
	@GeneratedValue
	private Long idUserAccount;

	@OneToOne
	private Conference conference;

	@Column(unique = true)
	@NotNull(message = "username shouldn't be null")
	private String username;

	@NotNull(message = "password shouldn't be null")
	@Size(min = 8)
	private String password;
	@NotNull(message = "name shouldn't be null")
	private String firstAndLastName;
	//	 @Pattern(regexp =
//	 "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$", message = "invalid phone number")
	private String phoneNumber;
	//	 @Pattern(regexp =
//	 "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)(\\.[A-Za-z]{2,})$", message = "invalid email")
	private String email;

	private boolean enabled;
	private String country;
	private String companyName;
	private String address;
	private boolean isSystemOwner;
	private boolean isMainAdmin;
	private boolean isOperativeAdmin;
	private boolean isParticipant;
	private String detailsOfParticipation;

	@ManyToMany
	private Set<SpecialEvent> attendsSpecialEvents;

	@ManyToMany
	private Set<SpecialEvent> appliedSpecialEvents;

	public UserAccount() {
	}

	public UserAccount(String firstAndLastName, String phoneNumber, String email, String country, String companyName,
					   String address, @NotNull String username, @NotNull @Size(min = 8) String password, boolean isParticipant,
					   boolean isOperativeAdmin, boolean isMainAdmin, boolean isSystemOwner, Conference conference,
					   String detailsOfParticipation) {
		super();
		this.isParticipant = isParticipant;
		this.isOperativeAdmin = isOperativeAdmin;
		this.isMainAdmin = isMainAdmin;
		this.isSystemOwner = isSystemOwner;
		this.firstAndLastName = firstAndLastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.country = country;
		this.companyName = companyName;
		this.address = address;
		this.username = username;
		this.password = password;
		this.conference = conference;
		this.detailsOfParticipation = detailsOfParticipation;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConferenceId(Conference conference) {
		this.conference = conference;
	}

	public String getFirstAndLastName() {
		return firstAndLastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public String getCountry() {
		return country;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getAddress() {
		return address;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getIdUserAccount() {
		return idUserAccount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSystemOwner() {
		return isSystemOwner;
	}

	public void setSystemOwner(boolean isSystemOwner) {
		this.isSystemOwner = isSystemOwner;
	}

	public boolean isMainAdmin() {
		return isMainAdmin;
	}

	public void setMainAdmin(boolean isMainAdmin) {
		this.isMainAdmin = isMainAdmin;
	}

	public boolean isOperativeAdmin() {
		return isOperativeAdmin;
	}

	public void setOperativeAdmin(boolean isOperativeAdmin) {
		this.isOperativeAdmin = isOperativeAdmin;
	}

	public boolean isParticipant() {
		return isParticipant;
	}

	public void setParticipant(boolean isParticipant) {
		this.isParticipant = isParticipant;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public void setFirstAndLastName(String firstAndLastName) {
		this.firstAndLastName = firstAndLastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDetailsOfParticipation() {
		return detailsOfParticipation;
	}

	public void setDetailsOfParticipation(String detailsOfParticipation) {
		this.detailsOfParticipation = detailsOfParticipation;
	}

	public Set<SpecialEvent> getAttendsSpecialEvents() {
		return attendsSpecialEvents;
	}

	public void setAttendsSpecialEvents(Set<SpecialEvent> attendsSpecialEvents) {
		this.attendsSpecialEvents = attendsSpecialEvents;
	}

	public Set<SpecialEvent> getAppliedSpecialEvents() {
		return appliedSpecialEvents;
	}

	public void setAppliedSpecialEvents(Set<SpecialEvent> appliedSpecialEvents) {
		this.appliedSpecialEvents = appliedSpecialEvents;
	}

	public void addSpecialEventToAttends(SpecialEvent specialEvent) {
		if (this.attendsSpecialEvents == null)
			this.attendsSpecialEvents = new HashSet<>();
		this.attendsSpecialEvents.add(specialEvent);
	}

	public void addSpecialEventToApplied(SpecialEvent specialEvent) {
		if (this.appliedSpecialEvents == null)
			this.appliedSpecialEvents = new HashSet<>();
		this.appliedSpecialEvents.add(specialEvent);
	}

}