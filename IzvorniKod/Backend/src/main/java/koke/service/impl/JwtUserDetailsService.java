package koke.service.impl;

import koke.domain.UserAccount;
import koke.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountService accountService;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAccount userAccount = accountService.findByUsername(username);
        if (userAccount == null) {
            throw new UsernameNotFoundException("Invalid username.");
        }else {
            String fetchedPass = userAccount.getPassword(); //ovdje ne mmogu raditi auth pass jer ga nemam
            return new User(username, fetchedPass, authorities(username));//authorities(emp)
        }
}

//            if ("systemowner".equals(username)) {
//                return new User("systemowner", "$2a$12$wXO5u8WRWKMUA1y9XdHpbe5i4f0YDi1CLlIcBjqcTaugxt0RI0G5e", new ArrayList<>());
//            } else {
//                throw new UsernameNotFoundException("User not found with username: " + username);
//            }

    private List<GrantedAuthority> authorities(String username) {
        if ("systemowner".equals(username))
            return commaSeparatedStringToAuthorityList("ROLE_SYSTEM_OWNER");
        UserAccount userAccount = accountService.findByUsername(username);
        if (userAccount.isMainAdmin())
            return commaSeparatedStringToAuthorityList("ROLE_MAIN_ADMIN");
        else if (userAccount.isOperativeAdmin())
            return commaSeparatedStringToAuthorityList("ROLE_OPERATIVE_ADMIN");
        else
            return commaSeparatedStringToAuthorityList("ROLE_PARTICIPANT");

    }
}


