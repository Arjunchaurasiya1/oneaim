package com.company.neurolink.services;
import com.company.neurolink.model.User;
import com.company.neurolink.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	

	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
			this.userRepository = userRepository;
			this.passwordEncoder = passwordEncoder;
		
	}

	public void registerCustomer(User user) {
		if (userExists(user.getEmail())) {
			throw new IllegalArgumentException("Email is already in use.");
		}
	
		user.setRole(User.Role.ROLE_CUSTOMER);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEmailVerified(false);
		user.setCreatedAt(new Date());
		user.setUpdatedAt(new Date());
		
		String verificationToken = UUID.randomUUID().toString();
		user.setVerificationToken(verificationToken); // ✅ Ensure the token is saved
		
		userRepository.save(user);
		
		
	}
	
	public void registerAdmin(User user) {
		if (userExists(user.getEmail())) {
			throw new IllegalArgumentException("Email is already in use.");
		}
	
		user.setRole(User.Role.ROLE_ADMIN);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEmailVerified(false);
		user.setCreatedAt(new Date());
		user.setUpdatedAt(new Date());
	
		String verificationToken = UUID.randomUUID().toString();
		user.setVerificationToken(verificationToken); // ✅ Ensure the token is saved
	
		userRepository.save(user);
	
		
	}
	

	public User login(String email, String password) {
	    User user = userRepository.findByEmail(email).orElse(null);
	    
	    if (user != null && passwordEncoder.matches(password, user.getPassword())) {
	        return user;
	    }
	    return null;
	}


	
	public boolean userExists(String email) {
	    Optional<User> user = userRepository.findByEmail(email);
	    System.out.println("Checking if user exists: " + email + " -> " + user.isPresent());
	    return user.isPresent();
	}


	public boolean deleteUser(Long id) {
		if (!userRepository.existsById(id)) {
			throw new EntityNotFoundException("User with ID " + id + " does not exist.");
		}
		userRepository.deleteById(id);
		return true;
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}

	public Optional<User> findByUserId(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

		System.out.println("Roles assigned: " + user.getRole());
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				AuthorityUtils.createAuthorityList(user.getRole().name()));
	}


	
	public void updateUser(User user) {
		userRepository.save(user); // Save or update the user in the database
	}


}

