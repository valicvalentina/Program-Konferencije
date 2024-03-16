package koke.service.impl;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import koke.exception.RequestDeniedException;
import koke.exception.ResourceNotFoundException;
import koke.service.DataGroupService;
import koke.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import koke.dao.ConferenceRepository;
import koke.domain.Conference;
import koke.domain.DataGroup;
import koke.domain.SpecialEvent;
import koke.domain.UserAccount;
import koke.service.ConferenceService;

@Service
public class ConferenceServiceJpa implements ConferenceService {

	@Autowired
	private ConferenceRepository conferenceRepo;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private DataGroupService dataGroupService;

	@Override
	public Set<UserAccount> getParticipants(Long idConference) {
		Conference conference = this.getByConferenceId(idConference);
		return (Set<UserAccount>) conferenceRepo.findById(conference.getIdConference())
				.orElseThrow(() -> new ResourceNotFoundException("Conference with id " + idConference + " not found"))
				.getUsers();
	}

//	// Ne koristi se?
//	@Override
//	public Optional<Conference> getConferenceByAccount(UserAccount account) {
//		return Optional.ofNullable(conferenceRepo.findById(account.getConference().getIdConference())
//				.orElseThrow(() -> new ResourceNotFoundException(
//						"Conference with id " + account.getIdUserAccount() + " not found")));
//	}

	@Override
	public Conference createConference(String name, String city,String country,Date dateStart, Date dateTimeEnd, String description, String topics) {
		if(name == null || city == null || dateTimeEnd == null || dateStart == null){
			throw new RequestDeniedException("Must enter name, city and date!");
		}
		Conference conference = conferenceRepo.save(new Conference(name, city,country,dateStart, dateTimeEnd, description, topics,
				userAccountService.getUserAccountByUsername("systemowner")));
		dataGroupService.createMandatoryDataGroups(conference);
		return conferenceRepo.save(conference);
	}

	@Override
	public Conference getConferenceByUserAccountId(Long idUserAccount) {
		UserAccount userAccount = userAccountService.getUserAccountByIdUserAccount(idUserAccount);
		return conferenceRepo.findById(userAccount.getConference().getIdConference())
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + idUserAccount + " not found"));
	}

	@Override
	public Conference getByConferenceId(Long conferenceId) {
		// TODO hvatat exception
		return conferenceRepo.findById(conferenceId)
				.orElseThrow(() -> new ResourceNotFoundException("Concerence with id " + conferenceId + " not found"));
	}

	@Override
	public Conference updateConference(Long idConference, Conference conference) {
		Conference conference2 = conferenceRepo.findById(conference.getIdConference()).get();
		conference2.setCity(conference.getCity());
		conference2.setConferenceInfo(conference.getConferenceInfo());
		conference2.setDateStart(conference.getDateStart());
		conference2.setDateEnd(conference.getDateEnd());
		conference2.setDescription(conference.getDescription());
		conference2.setMainAdmin(conference.getMainAdmin());
		conference2.setName(conference.getName());
		conference2.setOperationalAdmins(conference.getOperationalAdmins());
		conference2.setTopics(conference.getTopics());
		conference2.setUsers(conference.getUsers());
		return conferenceRepo.save(conference2);
	}

	@Override
	public Set<DataGroup> getDataGroupsByConferenceId(Long idConference) {
		return conferenceRepo.findById(idConference)
				.orElseThrow(() -> new ResourceNotFoundException("Conference with id " + idConference + " not found"))
				.getConferenceInfo();
	}

	@Override
	public Set<SpecialEvent> getSpecialEventsByConferenceId(Long idConference) {
		return conferenceRepo.findById(idConference)
				.orElseThrow(() -> new ResourceNotFoundException("Cinference with id " + idConference + " not found"))
				.getSpecialEvents();
	}

	@Override
	public List<Conference> getAll() {
		return conferenceRepo.findAll();
	}

	@Override
	public List<Conference> getAllConferencesWithoutMainAdmin() {
		return getAll().stream().filter(c -> !checkIfMainAdminExists(c)).toList();
	}

	@Override
	public boolean checkIfMainAdminExists(Conference conference) {
		if (conference.getMainAdmin() != null)
			return true;
		return false;
	}

	@Override
	public int getCurrentNumberOfDataGroupsByConferenceId(Long idConference) {
		return getDataGroupsByConferenceId(idConference).size();
	}

	@Override
	public boolean checkIfAllMandatoryDataGroupsDataIsNotNull(Long idConference) {
		for (DataGroup group : getDataGroupsByConferenceId(idConference)) {
			if (group.getGroupName().equals("Raspored predavanja") || group.getGroupName().equals("Zbornik radova")
					|| group.getGroupName().equals("Prezentacije predavanja")
					|| group.getGroupName().equals("Mjesto događanja")) {
				if (group.getData() == null) {
					return false;
				}
			}
		}
		return true;

	}

	@Override
	public Set<DataGroup> getMandatoryDataGroupsByConferenceId(Long idConference) {
		return getDataGroupsByConferenceId(idConference).stream()
				.filter(dg -> dg.getGroupName().equals("Raspored predavanja")
						|| dg.getGroupName().equals("Zbornik radova")
						|| dg.getGroupName().equals("Prezentacije predavanja")
						|| dg.getGroupName().equals("Mjesto događanja"))
				.collect(Collectors.toSet());
	}

//	@Override
//	public String getBasicInformationAboutConference(Long idConference) {
//		Conference conference = conferenceRepo.findById(idConference).get();
//		String text = "Conference: " + conference.getName() + ".\n";
//		text += "City: " + conference.getCity() + ".\n";
//		text += "Description: " + conference.getDescription() + ".\n";
//		text += "Ends: " + conference.getDateEnd() + ".\n";
//		text += "Topics: " + conference.getTopics() + ".\n";
//		text += "Conference owner: " + conference.getSystemOwner().getFirstAndLastName() + ".\n";
//		text += "Main admin of the conference: " + conference.getMainAdmin().getFirstAndLastName() + ".\n";
//		text += "City: " + conference.getCity() + ".\n";
//
//		return text;
//	}

//	@Override
//	public boolean createFile(File dir, Long idConference) {
//		Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
//		Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
//		if (!dir.isDirectory())
//			return false;
//		File conferenceInfo = null;
//		if (dir.toString().contains("\\"))
//			conferenceInfo = new File(dir.toString() + "\\conferenceInfo.pdf");
//		else if (dir.toString().contains("/"))
//			conferenceInfo = new File(dir.toString() + "/conferenceInfo.pdf");
//		else
//			return false;
//		int j = 1;
//		while (conferenceInfo.exists()) {
//			String s = "";
//			if (conferenceInfo.toString().endsWith(").pdf"))
//				s = conferenceInfo.toString().substring(0, conferenceInfo.toString().length() - 7);
//			else
//				s = conferenceInfo.toString().substring(0, conferenceInfo.toString().length() - 4);
//			conferenceInfo = new File(s += "(" + j++ + ").pdf");
//		}
//		Document document = new Document();
//		try {
//			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(conferenceInfo));
//			document.open();
//			document.add(new Chunk(""));
//			Paragraph preface = new Paragraph();
//			// We add one empty line
//			preface.add(new Paragraph(" "));
//			document.addTitle(conferenceRepo.findById(idConference).get().getName());
//			// Lets write a big header
//			Paragraph title = new Paragraph(conferenceRepo.findById(idConference).get().getName(), catFont);
//			title.setAlignment(Element.ALIGN_CENTER);
//
//			Paragraph basicInfo = new Paragraph(getBasicInformationAboutConference(idConference)
//					+ getInformationAboutConferenceOperativeAdmins(idConference)
//					+ getInformationAboutConferenceParticipants(idConference)
//					+ getInformationAboutSpecialEventsByConference(idConference)
//					+ getInformationAboutDataGroups(idConference), redFont);
//			document.add(title);
//			document.add(preface);
//			document.add(basicInfo);
//			document.close();
//			writer.close();
//		} catch (Exception e) {
//			return false;
//		}
//		return true;
//
//	}
//
//	@Override
//	public String getInformationAboutSpecialEventsByConference(Long idConference) {
//		String specialEvents = "Special events: \n";
//		for (SpecialEvent se : getSpecialEventsByConferenceId(idConference)) {
//			specialEvents += "   " + se.getType() + " (capacity: " + se.getCapacity() + ")\n";
//			Set<UserAccount> attendees = se.getAttendees();
//			if (!attendees.isEmpty()) {
//				specialEvents += "      Attendees:\n";
//				for (UserAccount ua : attendees) {
//					specialEvents += "         " + ua.getFirstAndLastName() + "\n";
//				}
//			}
//		}
//		return specialEvents + "\n";
//
//	}
//
//	@Override
//	public String getInformationAboutConferenceParticipants(Long idConference) {
//		String participants = "\nParticipants: \n";
//		for (UserAccount ua : getParticipants(idConference)) {
//			participants += "   " + ua.getFirstAndLastName() + "\n";
//			/*
//			 * participant += "    " + "    " + "email: " + ua.getEmail() + "\n";
//			 * participant += "    " + "    " + "country: " + ua.getCountry() + "\n";
//			 * participant += "    " + "    " + "adress: " + ua.getAddress() + "\n";
//			 * participant += "    " + "    " + "company: " + ua.getCompanyName() + "\n";
//			 * participant += "    " + "    " + "role: " + ua.getDetailsOfParticipation() +
//			 * "\n";
//			 */
//		}
//		participants += "\n";
//		return participants;
//	}
//
//	@Override
//	public String getInformationAboutConferenceOperativeAdmins(Long idConference) {
//		String operativeAdmins = "\nOperative admins: \n";
//		for (UserAccount oa : getOperativeAdmins(idConference)) {
//			operativeAdmins += "   " + oa.getFirstAndLastName() + "\n";
//		}
//		return operativeAdmins;
//	}
//
//	@Override
//	public String getInformationAboutDataGroups(Long idConference) {
//		String dataGroupNames = "Data groups: \n";
//		int i = 1;
//		for (DataGroup dg : getDataGroupsByConferenceId(idConference)) {
//			dataGroupNames += "        " + i + ". " + dg.getGroupName() + "\n";
//			i++;
//		}
//		return dataGroupNames;
//	}

	@Override
	public Set<UserAccount> getOperativeAdmins(Long idConference) {
		return getByConferenceId(idConference).getOperationalAdmins();
	}
}
