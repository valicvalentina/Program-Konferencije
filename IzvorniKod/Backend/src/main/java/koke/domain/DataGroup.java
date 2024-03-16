package koke.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class DataGroup {
	@Id
	@GeneratedValue
	private Long idDataGroup;

	@ManyToOne
	private Conference conference;

	@NotNull
	private String GroupName;

	private String data;

	public DataGroup() {
	}

	public DataGroup(@NotNull String GroupName, String data) {
		super();
		this.GroupName = GroupName;
		this.data = data;
	}

	public Long getId() {
		return this.idDataGroup;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference Conference) {
		this.conference = Conference;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String GroupName) {
		this.GroupName = GroupName;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
