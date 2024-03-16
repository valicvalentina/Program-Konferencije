package koke.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Multimedia {

	@Id
	@GeneratedValue
	private Long idMultimedia;
	@ManyToOne
	private Conference conference;
	private String url;
	private Date date;

	public Multimedia() {
	}

	public Multimedia(Conference conference, String url, Date date) {
		this.conference = conference;
		this.url = url;
		this.date = date;
	}

	public Long getIdMultimedia() {
		return idMultimedia;
	}

	public void setIdMultimedia(Long idMultimedia) {
		this.idMultimedia = idMultimedia;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
