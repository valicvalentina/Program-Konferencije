package koke.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import koke.domain.*;
import koke.exception.*;
import koke.service.*;
//import koke.service.VerificationToken;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import koke.dto.LoginDTO;
import koke.dto.UserAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import koke.dao.UserAccountRepository;

@Service
public class UserAccountServiceJpa implements UserAccountService {

	private static final String EMAIL_FORMAT = "^(.+)@(\\S+)$";

	private static final String PHONE_NUMBER_FORMAT = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

	@Autowired
	private UserAccountRepository accountRepo;

	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private VerificationTokenService verificationTokenService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public List<UserAccount> listAll() {
		return accountRepo.findAll();
	}

//    @Override
//    public List<DataGroup> listDataGroups(UserAccount account) {
//        List<DataGroup> dg = dataGroupService.listAll();
//        List<DataGroup> datag = new ArrayList<>();
//        for (DataGroup datagroup : dg) {
//            if (account.getConference() == datagroup.getConference()) {
//                datag.add(datagroup);
//            }
//        }
//        return datag;
//    }

	@Override
	public UserAccount findByUsername(String username) {
		return accountRepo.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Username " + username + " not found!"));
	}

	@Override
	public UserAccount createParticipantAccount(String firstAndLastName, String phoneNumber, String email,
			String companyName, String country, String address, String username, String password, Conference conference,
			String detailsOfParticipation) {
		if (firstAndLastName == null || phoneNumber == null || email == null || username == null || password == null) {
			throw new RequestDeniedException("Must enter name, phone, email, username and password!");
		}
		UserAccount userAccount = new UserAccount(firstAndLastName, phoneNumber, email, country, companyName, address,
				username, bcryptEncoder.encode(password), true, false, false, false, conference,
				detailsOfParticipation);
		validateUsernameAndPassword(username, password);
		validetePhoneNumber(phoneNumber);
		validateEmail(email);
		UserAccount userAccount1 = accountRepo.save(userAccount);
		sendMainWithVetificationToken(userAccount1);

		return userAccount1;
	}

	@Override
	public UserAccount createOperativeAccount(String firstAndLastName, String phoneNumber, String email,
			String companyName, String country, String address, String username, String password, Conference conference,
			String detailsOfParticipation) {
		if (firstAndLastName == null || phoneNumber == null || email == null || username == null || password == null) {
			throw new RequestDeniedException("Must enter name, phone, email, username and password!");
		}
		UserAccount userAccount = new UserAccount(firstAndLastName, phoneNumber, email, country, companyName, address,
				username, bcryptEncoder.encode(password), false, true, false, false, conference,
				detailsOfParticipation);
		userAccount.setEnabled(true);
		validateUsernameAndPassword(username, password);
		validateEmail(userAccount.getEmail());
		validetePhoneNumber(phoneNumber);
		UserAccount userAccount1 = accountRepo.save(userAccount);

		return userAccount1;
	}

	@Override
	public UserAccount createMainUserAccount(String firstAndLastName, String phoneNumber, String email,
			String companyName, String country, String address, String username, String password, Conference conference,
			String detailsOfParticipation) {
		if (firstAndLastName == null || phoneNumber == null || email == null || username == null || password == null) {
			throw new RequestDeniedException("Must enter name, phone, email, username and password!");
		}
		UserAccount userAccount = new UserAccount(firstAndLastName, phoneNumber, email, country, companyName, address,
				username, bcryptEncoder.encode(password), false, false, true, false, conference,
				detailsOfParticipation);
		userAccount.setEnabled(true);
		validateUsernameAndPassword(username, password);
		validateEmail(email);
		validetePhoneNumber(phoneNumber);
		if (!conferenceService.checkIfMainAdminExists(conference)) {
			UserAccount userAccount1 = accountRepo.save(userAccount);
			return userAccount1;
		} else
			throw new RequestDeniedException("Main admin already exists");
		// return accountRepo.save(userAccount);
	}

	private void sendMainWithVetificationToken(UserAccount userAccount) {
		validateEmail(userAccount.getEmail());
		String token = verificationTokenService.createVerificationTokenDTOFromUserId(userAccount.getIdUserAccount());
		sendVerificationMail(userAccount, token);
	}

	private void sendVerificationMail(UserAccount userAccount, String token) {
		SimpleMailMessage msg = new SimpleMailMessage();
		// TODO kad deployamo stavi kokeferencije.onrender bla bla
		String url = "http://kokeferencija.onrender.com/api/users/confirmRegistration?token=" + token;
		msg.setFrom("kokeferencija7@gmail.com");
		msg.setSubject("Email address confirmation");
		msg.setText("Hi " + userAccount.getFirstAndLastName()
				+ ", \nCan You please confirm your email address by clicking on the following URL: \n" + url);
		msg.setTo(userAccount.getEmail());
		javaMailSender.send(msg);
	}

	@Override
	public void sendMailToMainAdmin(UserAccount mainAdmin, String type) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("kokeferencija7@gmail.com");
		msg.setSubject("Special event overbooked!");
		msg.setText("Hi " + mainAdmin.getFirstAndLastName()
				+ ", \nThere are people in line for special event " + type + " from your conference! Please increase capacity for event if possible!");
		msg.setTo(mainAdmin.getEmail());
		javaMailSender.send(msg);
	}

	@Override
	public UserAccount getUserAccountByIdUserAccount(Long idUserAccount) {
		return accountRepo.findById(idUserAccount)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + idUserAccount + " not found"));
	}

	@Override
	public UserAccountDTO login(LoginDTO loginDTO) {
		UserAccount userAccount = new UserAccount();
		userAccount = accountRepo.findByUsername(loginDTO.getUsername()).orElseThrow(() -> new InvalidLogInException());

		if (!bcryptEncoder.matches(loginDTO.getPassword(), userAccount.getPassword())) {
			throw new InvalidLogInException("Wrong username or password");
		}

		if (!userAccount.isEnabled())
			throw new InvalidLogInException("Email is not confirmed");

		return UserAccountDTO.toDTO(userAccount);
	}

	/**
	 * Šalje mail prvih increase
	 */

	@Override
	public void sendNotificationMail(SpecialEvent specialEvent, Integer increase) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("kokeferencija7@gmail.com");
		msg.setSubject("Great news!");
		msg.setText("You can now attend special event " + specialEvent.getType());

		List<UserAccount> pending = specialEvent.getPending().stream().toList();
		for (int i = 0; i < increase && i < pending.size(); i++) {
			msg.setTo(String.valueOf(pending.get(i).getEmail()));
			javaMailSender.send(msg);
		}
	}

	private VerificationToken getVerificationToken(String token) {
		return verificationTokenService.getVerificationToken(token);
	}

	@Override
	public void confirmRegistration(String token) {
		VerificationToken verificationToken = getVerificationToken(token);
		UserAccount userAccount = accountRepo.getById(verificationToken.getIdUserAccount());
		userAccount.setEnabled(true);
		accountRepo.save(userAccount);
	}

	private void validateEmail(String email) {
		if (email == null || email.isBlank() || !email.matches(EMAIL_FORMAT)) {
			throw new RequestDeniedException("Email " + email + "is not valid");
		}
	}

	private void validateUsernameAndPassword(String username, String password) {
		if (username == null || username.length() < 5)
			throw new RequestDeniedException("Username too short, should be at least 5 characters!");
		if (password == null || password.length() < 8)
			throw new RequestDeniedException("Password too short, should be at least 8 characters!");
		if (!accountRepo.findByUsername(username).isEmpty())
			throw new RequestDeniedException("Username already exists!");
	}

	private void validetePhoneNumber(String phoneNumber) {
		if (!phoneNumber.matches(PHONE_NUMBER_FORMAT))
			throw new RequestDeniedException("Invalid phone number format for " + phoneNumber + "!");
	}

	@Override
	public UserAccount addAppliedSpecialEvent(Long idUserAccount, SpecialEvent specialEvent) {
		UserAccount user = getUserAccountByIdUserAccount(idUserAccount);
		user.addSpecialEventToApplied(specialEvent);
		return accountRepo.save(user);
	}

	@Override
	public UserAccount addAttendedSpecialEvent(Long idUserAccount, SpecialEvent specialEvent) {
		UserAccount user = getUserAccountByIdUserAccount(idUserAccount);
		user.addSpecialEventToAttends(specialEvent);
		return accountRepo.save(user);
	}

//    @Override
//    public String getInformationAboutParticipant(Long idUserAccount) {
//        UserAccount participant = getUserAccountByIdUserAccount(idUserAccount);
//        Conference conference = participant.getConference();
//
//        // String participation = "Certificate of participation \n\n\n";
//
//        String participation = "This certifies that \n";
//        participation += participant.getFirstAndLastName() + "\n";
//        participation += participant.getCompanyName() + ", " + participant.getCountry() + "\n\n";
//
//        participation += "has attended the conference " + conference.getName() + "\n";
//        participation += conference.getCity() + ", " + conference.getDescription() + ",\n";
//        participation += "which ended on " + conference.getDateEnd() + ",\n";
//
//        participation += "as " + participant.getDetailsOfParticipation() + ".\n";
//
//        return participation;
//    }
//
//    @Override
//    public boolean getCertificate(Long idUserAccount, File d) {
//        Font basic = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
//        Font title = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
//        if (!d.isDirectory())
//            return false;
//        File certificate = null;
//        if (d.toString().contains("\\"))
//            certificate = new File(d.toString() + "\\certificate.pdf");
//        else if (d.toString().contains("/"))
//            certificate = new File(d.toString() + "/certificate.pdf");
//        else
//            return false;
//        int count = 1;
//        while (certificate.exists()) {
//            String s = "";
//            if (certificate.toString().endsWith(").pdf"))
//                s = certificate.toString().substring(0, certificate.toString().length() - 7);
//            else
//                s = certificate.toString().substring(0, certificate.toString().length() - 4);
//            certificate = new File(s += "(" + count++ + ").pdf");
//        }
//
//        Document document = new Document();
//        try {
//            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(certificate));
//            document.open();
//            document.add(new Chunk(""));
//            Paragraph preface = new Paragraph();
//
//            preface.add(new Paragraph(" "));
//
//            // Lets write a big header
//            document.addTitle("Participation certificate");
//            Paragraph titleP = new Paragraph("Participation certificate", title);
//            titleP.setAlignment(Element.ALIGN_CENTER);
//            Paragraph basicInfo = new Paragraph(getInformationAboutParticipant(idUserAccount), basic);
//            document.add(titleP);
//            document.add(preface);
//            document.add(basicInfo);
//            document.close();
//            writer.close();
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }

	@Override
	public UserAccount getUserAccountByUsername(String username) {
		UserAccount userAccount;
		try {
			userAccount = accountRepo.findByUsername(username).orElseThrow();
		} catch (NoSuchElementException e) {
			throw new ResourceNotAvailableException("No user account with username: " + username);
		}
		return userAccount;
	}

	@Override
	public void createSystemOwner() {
		try {
			findByUsername("systemowner");
		} catch (Exception e) {
			UserAccount systemowner = new UserAccount("Jose Mouhrino", "0987654321", "owner@fer.hr", "Hrvatska", "FER",
					"Unska 3", "systemowner", bcryptEncoder.encode("password"), false, false, false, true, null,
					"system owner");
			systemowner.setEnabled(true);
			accountRepo.save(systemowner);
		}
	}

}