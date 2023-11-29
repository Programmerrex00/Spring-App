package com.example.ram.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ram.model.Servicios;

public interface ServiciosRepository extends MongoRepository<Servicios, String>{

}
