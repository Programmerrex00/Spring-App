package com.example.ram.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.example.ram.model.Role;
import com.example.ram.model.User;
import com.example.ram.repository.UserRepository;
import com.example.ram.web.dto.UserRegistrationDto;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoderUser;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public User save(UserRegistrationDto registrationDto) {
		User user = new User(registrationDto.getFirstName(), 
				registrationDto.getLastName(), registrationDto.getEmail(),
				passwordEncoderUser.encode(registrationDto.getPassword()), Arrays.asList(new Role("USER")),
				registrationDto.getTelefono(), registrationDto.getDireccion(), registrationDto.getImagen());
		
		return userRepository.save(user);
	}
	
	public User saveAdmin(UserRegistrationDto registrationDto) {
	    User user = new User(registrationDto.getFirstName(), 
	            registrationDto.getLastName(), registrationDto.getEmail(),
	            passwordEncoderUser.encode(registrationDto.getPassword()), Arrays.asList(new Role("ADMIN")),
	            registrationDto.getTelefono(), registrationDto.getDireccion(), registrationDto.getImagen());
	    
	    return userRepository.save(user);
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
	
	
	
	// Envio por email
	
	public void updatePassword(String email, String newPassword) {
	    User user = userRepository.findByEmail(email);
	    if (user != null) {
	        user.setPassword(passwordEncoderUser.encode(newPassword));
	        userRepository.save(user);
	    }
	}
	
	
	public void SavaUserPassword(String newPassword, User user) {
	    if (newPassword != null && !newPassword.isEmpty()) {
	        user.setPassword(passwordEncoderUser.encode(newPassword));
	    }
	    userRepository.save(user);
	}


		
	      
	
	
	
    public void sendPasswordResetEmail(String to, String newPassword) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("maicolcubidespe@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject("Recuperación de contraseña");
        mimeMessageHelper.setText("Tu nueva contraseña temporal es: " + newPassword);

        javaMailSender.send(mimeMessage);
    }
	
}
