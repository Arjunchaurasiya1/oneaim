package com.company.neurolink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.neurolink.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //User findByEmail(String email);
    Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
	Optional<User> findByVerificationToken(String token); 

	
	
	
}
