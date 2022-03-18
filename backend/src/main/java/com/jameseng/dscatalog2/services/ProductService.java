package com.jameseng.dscatalog2.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jameseng.dscatalog2.dto.CategoryDTO;
import com.jameseng.dscatalog2.dto.ProductDTO;
import com.jameseng.dscatalog2.entities.Category;
import com.jameseng.dscatalog2.entities.Product;
import com.jameseng.dscatalog2.repositories.CategoryRepository;
import com.jameseng.dscatalog2.repositories.ProductRepository;
import com.jameseng.dscatalog2.services.exceptions.DatabaseException;
import com.jameseng.dscatalog2.services.exceptions.ResourceNotFoundException;

//tem também: @Component e @Repository para outras
@Service // registrar a classe como um componente do sistema de injeção de dep. do Spring
public class ProductService {

	@Autowired // instancia de injeção de dependencia do Repository
	private ProductRepository repository;

	@Autowired // instancia de injeção de dependencia do Repository
	private CategoryRepository categoryRepository;

	// (readOnly=true) = evitar looking no banco de dados, melhor performance
	/*
	 * @Transactional(readOnly = true) // Transactional = garantir integridade nas
	 * transações (do pacote spring) public List<ProductDTO> findAll() {
	 * List<Product> list = repository.findAll();
	 * 
	 * //List<ProductDTO> listDto=list.stream().map(x -> new
	 * //ProductDTO(x)).collect(Collectors.toList()); return listDto;
	 * 
	 * return list.stream().map(x -> new
	 * ProductDTO(x)).collect(Collectors.toList()); }
	 */

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {

		Page<Product> list = repository.findAll(pageRequest);

		// converte a lista de Product em ProductDTO e retorna ao controller
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		// busca o objeto Product por id
		// Optional = nunca será um objeto nulo (pode ter ou não a categoria)
		Optional<Product> obj = repository.findById(id);

		// Obtem o objeto e larga na variável entity
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found on id = " + id));

		// Converte a entity para ProductDTO e retorna
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {

		Product entity = new Product();

		copyDtoToEntity(dto, entity);

		entity = repository.save(entity); // salva no banco

		return new ProductDTO(entity); // retorna ao endpoint
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id); // getOne vai instanciar uma nova entidade sem tocar no banco, com
													// id
			copyDtoToEntity(dto, entity);

			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id " + id + " not found.");
		}
	}

	@Transactional
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id " + id + " not found.");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}

	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());

		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {
			// getOne() = intanciar uma entidade sem tocar no banco de dados.
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}

	}

}
