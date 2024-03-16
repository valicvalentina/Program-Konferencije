package koke.dto;

import koke.domain.UserAccount;
import koke.rest.UserAccountRole;

public class UserAccountDTO {
	private int idUserAccount;
	private String firstAndLastName;
	private String phoneNumber;
	private String email;
	private String country;
	private String companyName;
	private String address;
	private String username;
	private String password;
	private String detailsOfParticipation;
	private UserAccountRole userAccountRole;
	private Long conferenceId;

	public UserAccountDTO() {
	}

	public UserAccountDTO(int idUserAccount, String firstAndLastName, String phoneNumber, String email, String country,
			String companyName, String address, String username, String password, String detailsOfParticipation,
			UserAccountRole accountRole, Long conferenceId) {
		super();
		this.idUserAccount = idUserAccount;
		this.firstAndLastName = firstAndLastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.country = country;
		this.companyName = companyName;
		this.address = address;
		this.username = username;
		this.password = password;
		this.detailsOfParticipation = detailsOfParticipation;
		this.userAccountRole = accountRole;
		this.conferenceId = conferenceId;
	}

	public static UserAccountDTO toDTO(UserAccount a) {
		UserAccountRole userAccountRole;
		userAccountRole = UserAccountRole.PARTICIPANT;
		if (a.isSystemOwner())
			userAccountRole = UserAccountRole.SYSTEMOWNER;
		if (a.isMainAdmin())
			userAccountRole = UserAccountRole.MAINADMIN;
		if (a.isOperativeAdmin())
			userAccountRole = UserAccountRole.OPERATIVEADMIN;
		return new UserAccountDTO(a.getIdUserAccount().intValue(), a.getFirstAndLastName(), a.getPhoneNumber(),
				a.getEmail(), a.getCountry(), a.getCompanyName(), a.getAddress(), a.getUsername(), a.getPassword(),
				a.getDetailsOfParticipation(), userAccountRole,
				a.getConference() == null ? null : a.getConference().getIdConference());
	}

	public int getIdUserAccount() {
		return idUserAccount;
	}

	public void setIdUserAccount(int idUserAccount) {
		this.idUserAccount = idUserAccount;
	}

	public String getFirstAndLastName() {
		return firstAndLastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public String getCountry() {
		return country;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getAddress() {
		return address;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setFirstAndLastName(String firstAndLastName) {
		this.firstAndLastName = firstAndLastName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDetailsOfParticipation() {
		return detailsOfParticipation;
	}

	public void setDetailsOfParticipation(String detailsOfParticipation) {
		this.detailsOfParticipation = detailsOfParticipation;
	}

	public UserAccountRole getUserAccountRole() {
		return userAccountRole;
	}

	public void setUserAccountRole(UserAccountRole accountRole) {
		this.userAccountRole = accountRole;
	}

	public Long getConferenceId() {
		return conferenceId;
	}

	public void setConferenceId(Long conferenceId) {
		this.conferenceId = conferenceId;
	}

}