package com.alienlab.catpower.security;

import com.alienlab.catpower.domain.User;
import com.alienlab.catpower.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<User> userFromDatabase=null;
        if(login.indexOf(",")>0){
            String [] loginarr=login.split(",");
            userFromDatabase = userRepository.findOneWithAuthoritiesByOpenid(loginarr[1]);
            return userFromDatabase.map(user -> {
                if (!user.getActivated()) {
                    throw new UserNotActivatedException("用户 " + lowercaseLogin + " 没有激活");
                }
                List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
                return new org.springframework.security.core.userdetails.User(loginarr[0],
                    new BCryptPasswordEncoder().encode(loginarr[1]),
                    grantedAuthorities);
            }).orElseThrow(() -> new UsernameNotFoundException("用户" + lowercaseLogin + " 在数据库中没有找到"));
        }else{
            userFromDatabase = userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin);
            return userFromDatabase.map(user -> {
                if (!user.getActivated()) {
                    throw new UserNotActivatedException("用户 " + lowercaseLogin + " 没有激活");
                }
                List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
                return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                    user.getPassword(),
                    grantedAuthorities);
            }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
                "database"));
        }
    }
}
