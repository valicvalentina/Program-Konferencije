package koke.service;

import koke.domain.VerificationToken;

public interface VerificationTokenService {
	VerificationToken getVerificationToken(String token);

	String createVerificationTokenDTOFromUserId(Long idUserAccount);

	VerificationToken findByToken(String token);
}
