package com.jameseng.dscatalog2.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jameseng.dscatalog2.dto.CategoryDTO;
import com.jameseng.dscatalog2.services.CategoryService;

@RestController // informar que este arquivo será o um controlador REST
@RequestMapping(value = "/categories") // rota do recurso
public class CategoryResource {

	@Autowired // injetar auto. a dependencia
	private CategoryService service;

	// 1° endpoint:
	// ResponseEntity<tipo do dado a ser retornado> = vai encapsular uma resposta
	// http
	/*
	 * @GetMapping // será um endpoint do resource categories public
	 * ResponseEntity<List<CategoryDTO>> findAll() {
	 * 
	 * // busca uma lista de todas as categorias no service List<CategoryDTO> list =
	 * service.findAll();
	 * 
	 * // .ok() = resposta 200 = sucesso // .body(list) = list no corpo da resposta
	 * return ResponseEntity.ok().body(list); }
	 */

	@GetMapping // será um endpoint do resource categories
	public ResponseEntity<Page<CategoryDTO>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy) {

		PageRequest pageRequest=PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Page<CategoryDTO> list=service.findAllPaged(pageRequest);

		// .ok() = resposta 200 = sucesso
		// .body(list) = list no corpo da resposta
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}") // será um endpoint do resource categories
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
		// @PathVariable = Spring entender que deve vincular o Long id ao
		// @GetMapping(value = "/{id}")

		// busca um CategoryDTO por id no service
		CategoryDTO dto = service.findById(id);

		// .ok = resposta 200 = sucesso
		// .body(dto) = entidade DTO no corpo da resposta
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping // inserir novo recurso
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto) {

		// @RequestBody = reconher o objeto enviado na requisição e vincular ao dto

		dto = service.insert(dto);

		// insersão de novo recurso
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();

		// created() = retorna o código 201
		return ResponseEntity.created(uri).body(dto); // tem que retornar código 201 = recurso criado
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
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
