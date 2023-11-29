package com.example.ram.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ram.model.HistorialPagos;
import com.example.ram.model.User;

public interface HistorialRepository extends MongoRepository<HistorialPagos, String>{
	 List<HistorialPagos> findByUsuarioEmail(String email);
}
