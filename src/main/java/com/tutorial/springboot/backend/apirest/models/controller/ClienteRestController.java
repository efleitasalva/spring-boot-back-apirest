package com.tutorial.springboot.backend.apirest.models.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import com.tutorial.springboot.backend.apirest.models.entity.Cliente;
import com.tutorial.springboot.backend.apirest.models.services.IClienteService;

import net.bytebuddy.implementation.bind.annotation.BindingPriority;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	@Autowired
	private IClienteService clienteService;

	@GetMapping("/clientes")
	public List<Cliente> index() {// lista de clientes en array
		return clienteService.findAll();// return el service con el metodo que se declaro
	}
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page) {// index de elementos y crea parametro page que seria numero de pagina
		Pageable pageable = PageRequest.of(page, 4); // crea instancia que almacena la lista de pagina que paso por parametro y numero de elementos por lista
		return clienteService.findAll(pageable);// return el service con el metodo declarado
	}

	@GetMapping("clientes/{id}")
	public ResponseEntity <?> show(@PathVariable Long id) {// show cliente por id, importante usar el @PathVariable para asociar elid al url
		
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>(); // array que guarda el mensaje y objeto en este caso cliente
		try {// captar errores de servidor
			cliente = clienteService.findById(id); 
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));// guarda el mensaje de error a gran escala y concatena con el mensaje especifico de error
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		if(cliente == null) {//capta mensaje de error para desplegar en el backend
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);// encontro respuesta sin mensaje solo ok
	}

	@PostMapping("/clientes") // post para save
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {// save para guardar el cliente donde debe ir asociado e cliente en el body requestBody porque trae un json / valid para traer validaciones de dto
		Cliente clienteNew = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors() // array de errores y getfielderrors para traer los campos con error
					.stream()//modifica a sting
					.map(err -> "El campo'"+ err.getField()+"'"+ err.getDefaultMessage())// transforma a sting y trae el campo con el mensaje de error
					.collect(Collectors.toList());// colecta los errores y los cambia a lista
			
			response.put("errors", errors);//almacena la lista en el mapa de response
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
		clienteNew = clienteService.save(cliente); 	//realiza accion
		}catch (DataAccessException e){// capta error de servidor
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);// mapea mensaje de error
		}
		
		response.put("mensaje","Cliente creado con exito!");
		response.put("cliente", clienteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);//mapea respuesta de elemento creado
	}

	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {// update de datos donde le pasamos y// asociamos la entidad y su id
		
		Cliente clienteActual = clienteService.findById(id);
		
		Cliente clienteUpdated = null;
		
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			System.out.println("Aca estoyyyyyy");
			List<String> errors = result.getFieldErrors() // array de errores y getfielderrors para traer los campos con error
					.stream()//modifica a sting
					.map(err -> "El campo'"+ err.getField()+"'"+ err.getDefaultMessage())// transforma a sting y trae el campo con el mensaje de error
					.collect(Collectors.toList());// colecta los errores y los cambia a lista
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		if(clienteActual == null) {//capta mensaje de error para desplegar en el backend
			response.put("mensaje", "Error: No se puede editar el cliente ID: ".concat(id.toString().concat(" porque no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		
		try {//captura error servidor
			clienteActual.setNombre(cliente.getNombre());// esta setteando el nuevo dato mediante el get
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setCreateAt(cliente.getCreateAt());
			
			clienteUpdated = clienteService.save(clienteActual);
		}catch (DataAccessException e){// capta error de servidor
			response.put("mensaje", "Error al realizar el update en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);// mapea mensaje de error
		}		
		response.put("mensaje", "El cliente se actualizo con exito en la base de datos");
		response.put("cliente", clienteUpdated);// realiza el update
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);// mapea mensaje y objeto
	}

	@DeleteMapping("/clientes/{id}")
	
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try{
			clienteService.delete(id);//elimina de acuerdo a ID 
		}catch (DataAccessException e){// capta error de servidor
			response.put("mensaje", "Error al realizar el delete en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);// mapea mensaje de error
		}	
		response.put("mensaje", "El cliente se ah eliminado con exito de la base de datos");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);// mapea mensaje y objeto
			
	}
}
