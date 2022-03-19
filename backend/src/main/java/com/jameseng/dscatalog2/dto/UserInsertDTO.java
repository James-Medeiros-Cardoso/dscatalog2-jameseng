package com.jameseng.dscatalog2.dto;

import com.jameseng.dscatalog2.services.validation.UserInsertValid;

@UserInsertValid // vai processar e ver se o email inserido ja existe ou não no banco
public class UserInsertDTO extends UserDTO {
	private static final long serialVersionUID = 1L;

	private String password;

	public UserInsertDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
