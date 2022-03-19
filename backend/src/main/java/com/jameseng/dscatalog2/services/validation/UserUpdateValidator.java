package com.jameseng.dscatalog2.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.jameseng.dscatalog2.dto.UserUpdateDTO;
import com.jameseng.dscatalog2.entities.User;
import com.jameseng.dscatalog2.repositories.UserRepository;
import com.jameseng.dscatalog2.resources.exceptions.FieldMessage;

// UserInsertDTO = classe que vai receber o anotation UserInsertValid
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

	@Autowired
	private UserRepository repository;

	@Autowired
	private HttpServletRequest request; // HttpServletRequest = tem informações da requisição

	@Override
	public void initialize(UserUpdateValid ann) {
	}

	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		// coletar o id da requisição
		@SuppressWarnings({ "unchecked" })
		var requestId = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		long userId = Long.parseLong(requestId.get("id"));

		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à
		// lista

		User user = repository.findByEmail(dto.getEmail());
		if (user != null && userId != user.getId()) {
			list.add(new FieldMessage("email", "O email " + user.getEmail() + " já foi cadastrado."));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
