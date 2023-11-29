 package com.example.ram.model;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "user")
public class User {
	
	@Id
	private String id;

	private String firstName;

	private String lastName;

	private String email;

	private String password;

	private Collection<Role> roles;
	
	private Long telefono;
	
	private String direccion;
	
	private List<Servicios> servicios = new ArrayList<>();
	
	private byte[] imagen;
	
	

	public User() {
		super();
	}
	
	
	public User(String email, Collection<Role> roles) {
		super();
		this.email = email;
		this.roles = roles;
	}






	public User(String firstName, String lastName, String email, String password, Collection<Role> roles, Long telefono,
			String direccion, byte[] imagen) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.telefono = telefono;
		this.direccion = direccion;
		this.imagen = imagen;
	}




	public User(String id, String firstName, String lastName, String email, String password, Collection<Role> roles,
			Long telefono, String direccion, List<Servicios> servicios, byte[] imagen) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.telefono = telefono;
		this.direccion = direccion;
		this.servicios = servicios;
		this.imagen = imagen;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Collection<Role> getRoles() {
		return roles;
	}


	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}


	public Long getTelefono() {
		return telefono;
	}


	public void setTelefono(Long telefono) {
		this.telefono = telefono;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public List<Servicios> getServicios() {
		return servicios;
	}


	public void setServicios(List<Servicios> servicios) {
		this.servicios = servicios;
	}


	public byte[] getImagen() {
		return imagen;
	}


	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}


	

	

}
	