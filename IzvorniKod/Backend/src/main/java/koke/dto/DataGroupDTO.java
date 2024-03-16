package koke.dto;

import koke.domain.DataGroup;

public class DataGroupDTO {
	private int idDataGroup;
	private String GroupName;
	private String data;

	public DataGroupDTO() {
	}

	public DataGroupDTO(int id, String groupName, String data) {
		this.idDataGroup = id;
		this.GroupName = groupName;
		this.data = data;
	}

	public static DataGroupDTO toDTO(DataGroup dataGroup) {
		return new DataGroupDTO(dataGroup.getId().intValue(), dataGroup.getGroupName(), dataGroup.getData());
	}

	public int getIdDataGroup() {
		return idDataGroup;
	}

	public void setIdDataGroup(int idDataGroup) {
		this.idDataGroup = idDataGroup;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
