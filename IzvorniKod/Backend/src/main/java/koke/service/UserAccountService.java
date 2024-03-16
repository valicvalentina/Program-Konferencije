package koke.service;

import java.util.List;

import koke.domain.*;
import koke.dto.LoginDTO;
import koke.dto.UserAccountDTO;

public interface UserAccountService {
	public List<UserAccount> listAll();

//	List<DataGroup> listDataGroups(UserAccount account);
//
	UserAccount findByUsername(String username);

	UserAccount createParticipantAccount(String firstAndLastName, String phoneNumber, String email, String companyName,
			String country, String address, String username, String password, Conference conference,
			String detailsOfParticipation);

	UserAccount createOperativeAccount(String firstAndLastName, String phoneNumber, String email, String companyName,
			String country, String address, String username, String password, Conference conference,
			String detailsOfParticipation);

	UserAccount createMainUserAccount(String firstAndLastName, String phoneNumber, String email, String companyName,
			String country, String address, String username, String password, Conference conference,
			String detailsOfParticipation);

	UserAccount getUserAccountByIdUserAccount(Long idUserAccount);

	UserAccountDTO login(LoginDTO loginDTO);

	void sendNotificationMail(SpecialEvent specialEvent, Integer increase);

	void confirmRegistration(String token);

	UserAccount addAppliedSpecialEvent(Long idUserAccount, SpecialEvent specialEvent);

	UserAccount addAttendedSpecialEvent(Long idUserAccount, SpecialEvent specialEvent);

//	String getInformationAboutParticipant(Long idUserAccount);
//
//	public boolean getCertificate(Long idUserAccount, File d);
//
	UserAccount getUserAccountByUsername(String username);

	void createSystemOwner();

	void sendMailToMainAdmin(UserAccount mainAdmin, String type);
}
