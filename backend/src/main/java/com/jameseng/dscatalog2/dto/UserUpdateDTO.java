package com.jameseng.dscatalog2.dto;

import com.jameseng.dscatalog2.services.validation.UserUpdateValid;

@UserUpdateValid // vai processar e ver se o email inserido ja existe ou não no banco, no update
public class UserUpdateDTO extends UserDTO {
	private static final long serialVersionUID = 1L;

}
