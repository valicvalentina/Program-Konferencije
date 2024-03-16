package koke.service;

import java.util.List;

import koke.domain.Conference;
import koke.domain.DataGroup;

public interface DataGroupService {
	public List<DataGroup> listAll();

	public DataGroup createDataGroup(DataGroup dataGroup);

	DataGroup createDataGroup(String groupName, String data);

	void createMandatoryDataGroups(Conference conference);

	public DataGroup updateMandatoryDataGroup(Long idDataGroup, String data);

	void createOptionalMandatoryDataGroup(DataGroup dataGroup);
}
