package com.example.ram.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.ram.model.User;
import com.example.ram.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
}
