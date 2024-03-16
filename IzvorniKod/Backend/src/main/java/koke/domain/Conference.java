package koke.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.*;

@Entity
public class Conference {

	@Id
	@GeneratedValue
	private Long idConference;

	@NotNull(message = "name shouldn't be null")
	private String name;

	@NotNull(message = "city shouldn't be null")
	private String city;

	@NotNull(message = "country shouldn't be null")
	private String country;

	private String description;

	@NotNull(message = "date end shouldn't be null")
	private Date dateStart;

	@NotNull(message = "date end shouldn't be null")
	private Date dateEnd;

	/**
	 * Grupe podataka o konferenciji, mora imati obavezne i ne više on 15
	 */
	@OneToMany
	private Set<DataGroup> dataGroups = new HashSet<>(15);

	/**
	 * Teme koje ce se obradivati
	 */
	private String topics;

	@OneToOne
	private UserAccount systemOwner;

	@OneToOne
	private UserAccount mainAdmin;

	@OneToMany
	private Set<UserAccount> operationalAdmins;

	@OneToMany
	private Set<UserAccount> users;

	@OneToMany
	private Set<SpecialEvent> specialEvents;

	public Conference() {
	}

	public Conference(String name, String city, String country, Date dateStart, Date dateTimeEnd, String description,
			String topics, UserAccount systemOwner) {
		this.name = name;
		this.city = city;
		this.country = country;
		this.dateStart = dateStart;
		this.dateEnd = dateTimeEnd;
		this.description = description;
		this.topics = topics;
		this.systemOwner = systemOwner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getIdConference() {
		return idConference;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTopics() {
		return topics;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}

	public Set<DataGroup> getConferenceInfo() {
		return dataGroups;
	}

	public void setConferenceInfo(Set<DataGroup> conferenceInfo) {
		this.dataGroups = conferenceInfo;
	}

	public UserAccount getSystemOwner() {
		return systemOwner;
	}

	public void setSystemOwner(UserAccount systemOwner) {
		this.systemOwner = systemOwner;
	}

	public UserAccount getMainAdmin() {
		return mainAdmin;
	}

	public void setMainAdmin(UserAccount mainAdmin) {
		this.mainAdmin = mainAdmin;
	}

	public Set<UserAccount> getOperationalAdmins() {
		return operationalAdmins;
	}

	public void setOperationalAdmins(Set<UserAccount> operationalAdmins) {
		this.operationalAdmins = operationalAdmins;
	}

	public Set<UserAccount> getUsers() {
		return users;
	}

	public void setUsers(Set<UserAccount> users) {
		this.users = users;
	}

	public void setIdConference(Long idConference) {
		this.idConference = idConference;
	}

	public Set<SpecialEvent> getSpecialEvents() {
		return specialEvents;
	}

	public void setSpecialEvents(Set<SpecialEvent> specialEvents) {
		this.specialEvents = specialEvents;
	}

	public void addUser(UserAccount user) {
		if (this.users == null)
			this.users = new HashSet<UserAccount>();
		this.users.add(user);
	}

	public void addOperativeAdmin(UserAccount operationalAdmin) {
		if (this.operationalAdmins == null)
			this.operationalAdmins = new HashSet<UserAccount>();
		this.operationalAdmins.add(operationalAdmin);
	}

	public void addDataGroup(DataGroup dataGroup) {
		if (this.dataGroups == null)
			this.dataGroups = new HashSet<>();
		this.dataGroups.add(dataGroup);
	}

	public void addSpecialEvent(SpecialEvent specialEvent) {
		if (this.specialEvents == null)
			this.specialEvents = new HashSet<>();
		this.specialEvents.add(specialEvent);
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Set<DataGroup> getDataGroups() {
		return dataGroups;
	}

	public void setDataGroups(Set<DataGroup> dataGroups) {
		this.dataGroups = dataGroups;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

}