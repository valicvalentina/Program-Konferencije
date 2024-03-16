package koke.service.impl;

import java.util.List;

import koke.domain.Conference;
import koke.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import koke.dao.DataGroupRepository;
import koke.domain.DataGroup;
import koke.service.DataGroupService;

@Service
public class DataGroupServiceJpa implements DataGroupService {
	@Autowired
	private DataGroupRepository dataGroupRepo;

	@Override
	public List<DataGroup> listAll() {
		return dataGroupRepo.findAll();
	}

	@Override
	public DataGroup createDataGroup(String groupName, String data) {
		return dataGroupRepo.save(new DataGroup(groupName, data));
	}

	@Override
	public void createMandatoryDataGroups(Conference conference) {
		DataGroup dg1 = createDataGroup("Raspored predavanja", null);
		dg1.setConference(conference);
		conference.addDataGroup(dg1);
		dataGroupRepo.save(dg1);

		DataGroup dg2 = createDataGroup("Zbornik radova", null);
		dg2.setConference(conference);
		conference.addDataGroup(dg2);
		dataGroupRepo.save(dg2);

		DataGroup dg3 = createDataGroup("Prezentacije predavanja", null);
		dg3.setConference(conference);
		conference.addDataGroup(dg3);
		dataGroupRepo.save(dg3);

		DataGroup dg4 = createDataGroup("Mjesto događanja", null);
		dg4.setConference(conference);
		conference.addDataGroup(dg4);
		dataGroupRepo.save(dg4);

	}

	@Override
	public DataGroup createDataGroup(DataGroup dataGroup) {
		return dataGroupRepo.save(dataGroup);
	}

	@Override
	public DataGroup updateMandatoryDataGroup(Long idDataGroup, String data) {
		DataGroup group = dataGroupRepo.findById(idDataGroup).orElseThrow(() -> new ResourceNotFoundException("No data group with id " + idDataGroup));
		group.setData(data);
		return dataGroupRepo.save(group);
	}

	@Override
	public void createOptionalMandatoryDataGroup(DataGroup dataGroup) {
		Conference conference = dataGroup.getConference();
		conference.addDataGroup(dataGroup);
		dataGroupRepo.save(dataGroup);
	}

}
