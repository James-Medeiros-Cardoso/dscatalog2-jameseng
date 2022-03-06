package com.jameseng.dscatalog2.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jameseng.dscatalog2.dto.CategoryDTO;
import com.jameseng.dscatalog2.entities.Category;
import com.jameseng.dscatalog2.repositories.CategoryRepository;
import com.jameseng.dscatalog2.services.exceptions.DatabaseException;
import com.jameseng.dscatalog2.services.exceptions.ResourceNotFoundException;

//tem também: @Component e @Repository para outras
@Service // registrar a classe como um componente do sistema de injeção de dep. do Spring
public class CategoryService {

	@Autowired // instancia de injeção de dependencia do Repository
	private CategoryRepository repository;

	// (readOnly=true) = evitar looking no banco de dados, melhor performance
	@Transactional(readOnly = true) // Transactional = garantir integridade nas transações (do pacote spring)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();
		/*
		 * List<CategoryDTO> listDto=list.stream().map(x -> new
		 * CategoryDTO(x)).collect(Collectors.toList()); return listDto;
		 */
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		// busca o objeto Category por id
		// Optional = nunca será um objeto nulo (pode ter ou não a categoria)
		Optional<Category> obj = repository.findById(id);

		// Obtem o objeto e larga na variável entity
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found on id = " + id));

		// Converte a entity para CategoryDTO e retorna
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {

		Category entity = new Category();
		entity.setName(dto.getName());

		entity = repository.save(entity); // salva no banco

		return new CategoryDTO(entity); // retorna ao endpoint
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getOne(id); // getOne vai instanciar uma nova entidade sem tocar no banco, com
														// id
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id " + id + " not found.");
		}
	}

	@Transactional
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id " + id + " not found.");
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
		
	}

}
