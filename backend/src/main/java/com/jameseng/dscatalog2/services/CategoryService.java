package com.jameseng.dscatalog2.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jameseng.dscatalog2.dto.CategoryDTO;
import com.jameseng.dscatalog2.entities.Category;
import com.jameseng.dscatalog2.repositories.CategoryRepository;

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

}
