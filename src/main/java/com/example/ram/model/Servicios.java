package com.example.ram.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "service")
public class Servicios {
	
	@Id
	private String id;
	
	private String nombre;
	
	private String descripcion;
	
	private double costo;
	
	private String caracteristicas;
	
	private String proveedor;
	
	private String categoria;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaLanzamiento;
	
	private List<String> tecnologias;
	
	private byte[] imagen;
	
	private String requerido;
	
	private List<User> usuarios;

	public Servicios() {
		super();
	}

	


	public Servicios(String id, String nombre, String descripcion, double costo, String caracteristicas,
			String proveedor, String categoria, Date fechaLanzamiento, List<String> tecnologias, byte[] imagen,
			String requerido, List<User> usuarios) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.costo = costo;
		this.caracteristicas = caracteristicas;
		this.proveedor = proveedor;
		this.categoria = categoria;
		this.fechaLanzamiento = fechaLanzamiento;
		this.tecnologias = tecnologias;
		this.imagen = imagen;
		this.requerido = requerido;
		this.usuarios = usuarios;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public double getCosto() {
		return costo;
	}



	public void setCosto(double costo) {
		this.costo = costo;
	}



	public String getCaracteristicas() {
		return caracteristicas;
	}



	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}



	public String getProveedor() {
		return proveedor;
	}



	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}



	public String getCategoria() {
		return categoria;
	}



	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}



	public Date getFechaLanzamiento() {
		return fechaLanzamiento;
	}



	public void setFechaLanzamiento(Date fechaLanzamiento) {
		this.fechaLanzamiento = fechaLanzamiento;
	}



	public List<String> getTecnologias() {
		return tecnologias;
	}



	public void setTecnologias(List<String> tecnologias) {
		this.tecnologias = tecnologias;
	}



	public byte[] getImagen() {
		return imagen;
	}



	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}



	public List<User> getUsuarios() {
		return usuarios;
	}



	public void setUsuarios(List<User> usuarios) {
		this.usuarios = usuarios;
	}


	public String getRequerido() {
		return requerido;
	}


	public void setRequerido(String requerido) {
		this.requerido = requerido;
	}



	
}
