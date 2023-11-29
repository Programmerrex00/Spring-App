package com.example.ram.model;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "HistorialPagos")
public class HistorialPagos {
	
	@Id
	private String id;
	
	private User usuario;
	
	private List<Servicios> servicios = new ArrayList<>();
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaActual;
	
	private double totalPagado;


	public HistorialPagos(String id, User usuario, List<Servicios> servicios, LocalDate fechaActual,
			double totalPagado) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.servicios = servicios;
		this.fechaActual = fechaActual;
		this.totalPagado = totalPagado;
	}



	public HistorialPagos() {
		super();
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public User getUsuario() {
		return usuario;
	}



	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}



	public List<Servicios> getServicios() {
		return servicios;
	}



	public void setServicios(List<Servicios> servicios) {
		this.servicios = servicios;
	}



	public LocalDate getFechaActual() {
		return fechaActual;
	}



	public void setFechaActual(LocalDate fechaActual) {
		this.fechaActual = fechaActual;
	}



	public double getTotalPagado() {
		return totalPagado;
	}



	public void setTotalPagado(double totalPagado) {
		this.totalPagado = totalPagado;
	}



	
	
	
}
