package com.jameseng.dscatalog2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jameseng.dscatalog2.entities.Category;
import com.jameseng.dscatalog2.repositories.CategoryRepository;

//tem também: @Component e @Repository para outras
@Service // registrar a classe como um componente do sistema de injeção de dep. do Spring
public class CategoryService {

	@Autowired // instancia de injeção de dependencia do Repository
	private CategoryRepository repository;

	@Transactional(readOnly=true)
	public List<Category> findAll() {
		return repository.findAll();
	}

}