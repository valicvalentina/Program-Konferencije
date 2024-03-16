package koke.service.impl;

import koke.dao.VerificationTokenRepository;
import koke.domain.VerificationToken;
import koke.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class VerificationTokenServiceJpa implements VerificationTokenService {

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Override
	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	@Override
	public String createVerificationTokenDTOFromUserId(Long idUserAccount) {
		String token = generateVerificationToken();
		verificationTokenRepository.save(new VerificationToken(token, idUserAccount));
		return token;
	}

	@Override
	public VerificationToken findByToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	private String generateVerificationToken() {
		StringBuilder token = new StringBuilder();
		long currentTimeInMilisecond = Instant.now().toEpochMilli();
		return token.append(currentTimeInMilisecond).append("-").append(UUID.randomUUID().toString()).toString();
	}
}
