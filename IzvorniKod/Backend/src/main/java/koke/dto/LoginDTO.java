package koke.dto;

import koke.domain.UserAccount;

public class LoginDTO {

	private String username;
	private String password;

	public LoginDTO() {
	}

	public LoginDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public static LoginDTO toDTO(UserAccount account) {
		return new LoginDTO(account.getUsername(), account.getPassword());
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
