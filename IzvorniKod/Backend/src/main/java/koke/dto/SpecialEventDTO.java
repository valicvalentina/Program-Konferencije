package koke.dto;

import koke.domain.SpecialEvent;

public class SpecialEventDTO {

	private int idSpecialEvent;
	private int capacity;
	private String type;
	private String message;

	public SpecialEventDTO() {
	}

	public SpecialEventDTO(int idSpecialEvent, int capacity, String type, String message) {
		super();
		this.idSpecialEvent = idSpecialEvent;
		this.capacity = capacity;
		this.type = type;
		this.message = message;
	}

	public static SpecialEventDTO toDTO(SpecialEvent specialEvent) {
		return new SpecialEventDTO(specialEvent.getIdSpecialEvent().intValue(), specialEvent.getCapacity(),
				specialEvent.getType(), specialEvent.getMessage());
	}

	public int getIdSpecialEvent() {
		return idSpecialEvent;
	}

	public void setIdSpecialEvent(int idSpecialEvent) {
		this.idSpecialEvent = idSpecialEvent;
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

}
