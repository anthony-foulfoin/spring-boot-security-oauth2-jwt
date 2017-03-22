package org.springdemo.repository;

import org.springdemo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.inject.Named;

@Named
public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);

}
