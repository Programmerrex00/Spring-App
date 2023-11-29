package com.example.ram.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "role")
public class Role {
	
	@Id
	private Long id;
	private String name;

	
	public Role() {
		
	}


	public Role( String name) {
		super();
		
		this.name = name;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	
	

}
