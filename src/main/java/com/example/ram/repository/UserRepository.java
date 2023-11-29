package com.example.ram.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.ram.model.User;

public interface UserRepository extends MongoRepository<User, String>{
	User findByEmail(String email);
}
