package com.jameseng.dscatalog2.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jameseng.dscatalog2.dto.UserDTO;
import com.jameseng.dscatalog2.dto.UserInsertDTO;
import com.jameseng.dscatalog2.services.UserService;

@RestController 
@RequestMapping(value = "/users")
public class UserResource {

	@Autowired
	private UserService service;

	@GetMapping 
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {

		Page<UserDTO> page = service.findAllPaged(pageable);

		// .ok() = resposta 200 = sucesso
		// .body(page) = list no corpo da resposta
		return ResponseEntity.ok().body(page);
	}

	@GetMapping(value = "/{id}") // será um endpoint do resource categories
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
		// @PathVariable = Spring entender que deve vincular o Long id ao
		// @GetMapping(value = "/{id}")

		// busca um UserDTO por id no service
		UserDTO dto = service.findById(id);

		// .ok = resposta 200 = sucesso
		// .body(dto) = entidade DTO no corpo da resposta
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping // inserir novo recurso
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {

		// @RequestBody = reconher o objeto enviado na requisição e vincular ao dto

		UserDTO newDto = service.insert(dto);

		// insersão de novo recurso
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();

		// created() = retorna o código 201
		return ResponseEntity.created(uri).body(newDto); // tem que retornar código 201 = recurso criado
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);

		// noContent().build() = reposta código 204 de corpo vazio
		return ResponseEntity.noContent().build();
	}

}
