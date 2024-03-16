package koke.service;

import java.util.*;

import koke.domain.Conference;
import koke.domain.DataGroup;
import koke.domain.SpecialEvent;
import koke.domain.UserAccount;

public interface ConferenceService {

	// Optional<List<Conference>> findAllActive();

	Set<UserAccount> getParticipants(Long conferenceId);

//	Optional<Conference> getConferenceByAccount(UserAccount account);

	Conference createConference(String name, String city,String country,Date dateStart, Date dateTimeEnd, String description, String topics);

	Conference getConferenceByUserAccountId(Long idUserAccount);

	Conference getByConferenceId(Long conferenceId);

	Conference updateConference(Long idConference, Conference conference);

	Set<DataGroup> getDataGroupsByConferenceId(Long idConference);

	Set<SpecialEvent> getSpecialEventsByConferenceId(Long idConference);

	int getCurrentNumberOfDataGroupsByConferenceId(Long idConference);

	List<Conference> getAll();

	boolean checkIfMainAdminExists(Conference conference);

	boolean checkIfAllMandatoryDataGroupsDataIsNotNull(Long idConference);

	Set<DataGroup> getMandatoryDataGroupsByConferenceId(Long idConference);

	List<Conference> getAllConferencesWithoutMainAdmin();

//	String getBasicInformationAboutConference(Long idConference);
//
//	boolean createFile(File dir, Long idConference);
//
//	String getInformationAboutSpecialEventsByConference(Long idConference);
//
//	String getInformationAboutConferenceParticipants(Long idConference);
//
//	String getInformationAboutDataGroups(Long idConference);
//
//	String getInformationAboutConferenceOperativeAdmins(Long idConference);

	Set<UserAccount> getOperativeAdmins(Long idConference);

}
