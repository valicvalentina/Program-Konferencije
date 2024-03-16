package koke.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import koke.domain.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	int countByUsername(String username);

	Optional<UserAccount> findByUsername(String username);

	UserAccount getById(Long id);
}
