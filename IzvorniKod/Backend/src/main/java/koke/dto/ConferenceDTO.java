package koke.dto;

import java.util.Date;

import koke.domain.Conference;

public class ConferenceDTO {
	private int idConference;
	private String name;
	private String city;
	private String country;
	private Date dateStart;
	private Date dateEnd;
	private String description;
	private String topics;

	public ConferenceDTO() {
	}

	public ConferenceDTO(int idConference, String name, String city, String country,Date dateStart, Date dateTimeEnd,
			String description, String topics) {
		super();
		this.idConference = idConference;
		this.name = name;
		this.city = city;
		this.country = country;
		this.dateStart = dateStart;
		this.dateEnd = dateTimeEnd;
		this.description = description;
		this.topics = topics;
	}

	public static ConferenceDTO toDTO(Conference conference) {
		return new ConferenceDTO(conference.getIdConference().intValue(), conference.getName(), conference.getCity(),
				conference.getCountry(),conference.getDateStart(), conference.getDateEnd(), conference.getDescription(),
				conference.getTopics());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getIdConference() {
		return idConference;
	}

	public void setIdConference(int idConference) {
		this.idConference = idConference;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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
