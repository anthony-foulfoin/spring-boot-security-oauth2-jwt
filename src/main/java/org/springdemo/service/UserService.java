package org.springdemo.service;

import org.springdemo.domain.User;
import org.springdemo.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class UserService implements UserDetailsService {

  @Inject
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.userRepository.findByEmail(username);

    if (user == null) throw new UsernameNotFoundException(String.format("No user found with username '%s'", username));
    return user;
  }

  public Boolean hasProtectedAccess() {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
  }

}
