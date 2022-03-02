package com.jameseng.dscatalog2.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@GetMapping // será um endpoint do resource categories
	public ResponseEntity<List<CategoryDTO>> findAll() {

		// busca uma lista de todas as categorias no service
		List<CategoryDTO> list = service.findAll();

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

}
