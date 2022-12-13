package com.tutorial.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tutorial.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteService {
public List<Cliente> findAll();
public Page<Cliente> findAll(Pageable pageable);// busca elemento de una lista y los trae por pagina
public Cliente save(Cliente cliente);
public void delete(Long id);
public Cliente findById(Long id);

}
