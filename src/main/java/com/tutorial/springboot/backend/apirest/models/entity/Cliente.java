package com.tutorial.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Entity
@Table(name="clientes")
public class Cliente implements Serializable {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false)
@NotEmpty(message = " este campo no puede estar vacio!")
@Size(min=4, max=12, message=" este campo debe tener entre 4 a 12 caracteres")
private String nombre;
@NotEmpty(message = " este campo no puede estar vacio!")
@Size(min=4, max=12, message=" este campo debe tener entre 4 a 12 caracteres")
private String apellido;
@NotEmpty(message = " este campo no puede estar vacio!")
@Email(message = " debe escribir un direccion de email correcta")
@Column(nullable = false, unique = false)
private String email;
@NotNull (message = " este campo no puede estar vacio!")
@Column(name = "create_at")
@Temporal(TemporalType.DATE)
private Date createAt;

public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getApellido() {
	return apellido;
}
public void setApellido(String apellido) {
	this.apellido = apellido;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public Date getCreateAt() {
	return createAt;
}
public void setCreateAt(Date createAt) {
	this.createAt = createAt;
}

private static final long serialVersionUID = 1L;
}
