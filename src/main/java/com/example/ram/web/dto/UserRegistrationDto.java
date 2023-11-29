package com.example.ram.web.dto;

public class UserRegistrationDto {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Long telefono;
	private String direccion;
	private byte[] imagen;
	
	public UserRegistrationDto(){
		
	}
	

	




	public UserRegistrationDto(String firstName, String lastName, String email, String password, Long telefono,
			String direccion, byte[] imagen) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.telefono = telefono;
		this.direccion = direccion;
		this.imagen = imagen;
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







	public byte[] getImagen() {
		return imagen;
	}







	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}










}