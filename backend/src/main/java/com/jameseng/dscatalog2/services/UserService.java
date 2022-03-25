package com.jameseng.dscatalog2.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jameseng.dscatalog2.dto.RoleDTO;
import com.jameseng.dscatalog2.dto.UserDTO;
import com.jameseng.dscatalog2.dto.UserInsertDTO;
import com.jameseng.dscatalog2.dto.UserUpdateDTO;
import com.jameseng.dscatalog2.entities.Role;
import com.jameseng.dscatalog2.entities.User;
import com.jameseng.dscatalog2.repositories.RoleRepository;
import com.jameseng.dscatalog2.repositories.UserRepository;
import com.jameseng.dscatalog2.services.exceptions.DatabaseException;
import com.jameseng.dscatalog2.services.exceptions.ResourceNotFoundException;

//tem também: @Component e @Repository para outras
@Service // registrar a classe como um componente do sistema de injeção de dep. do Spring
public class UserService implements UserDetailsService {

	// instaniando um objeto Logger exibir menssagens no console
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired // instancia de injeção de dependencia do Repository
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {

		Page<User> list = repository.findAll(pageable);

		// converte a lista de User em UserDTO e retorna ao controller
		return list.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		// busca o objeto User por id
		// Optional = nunca será um objeto nulo (pode ter ou não a categoria)
		Optional<User> obj = repository.findById(id);

		// Obtem o objeto e larga na variável entity
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found on id = " + id));

		// Converte a entity para UserDTO e retorna
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {

		User entity = new User();

		copyDtoToEntity(dto, entity);

		// passwordEncoder.encode() = vai codificar o password
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));

		entity = repository.save(entity); // salva no banco

		return new UserDTO(entity); // retorna ao endpoint
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getOne(id); // getOne vai instanciar uma nova entidade sem tocar no banco, com
													// id
			copyDtoToEntity(dto, entity);

			entity = repository.save(entity);
			return new UserDTO(entity);
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

	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());

		entity.getRoles().clear();
		for (RoleDTO roleDto : dto.getRoles()) {
			// getOne() = intanciar uma entidade sem tocar no banco de dados.
			Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role);
		}

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = repository.findByEmail(username);
		if (user == null) {
			logger.error("Email " + username + " not found at database."); // aparece no console
			throw new UsernameNotFoundException("Email " + username + " not found at database.");
		}
		logger.info("User " + username + " found at database."); // aparece no console
		return user;
	}

}
