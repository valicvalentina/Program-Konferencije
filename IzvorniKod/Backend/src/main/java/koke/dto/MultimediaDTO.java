package koke.dto;

import java.util.Date;

import koke.domain.Multimedia;

public class MultimediaDTO {
	private int idMultimedia;
	private String url;
	private Date date;

	public MultimediaDTO() {
	}

	public MultimediaDTO(int idMultimedia, String url, Date date) {
		super();
		this.idMultimedia = idMultimedia;
		this.url = url;
		this.date = date;
	}

	public static MultimediaDTO toDTO(Multimedia multimedia) {
		return new MultimediaDTO(multimedia.getIdMultimedia().intValue(), multimedia.getUrl(), multimedia.getDate());
	}

	public int getIdMultimedia() {
		return idMultimedia;
	}

	public void setIdMultimedia(int idMultimedia) {
		this.idMultimedia = idMultimedia;
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
