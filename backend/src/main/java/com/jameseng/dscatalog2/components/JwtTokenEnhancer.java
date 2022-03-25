package com.jameseng.dscatalog2.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.jameseng.dscatalog2.entities.User;
import com.jameseng.dscatalog2.repositories.UserRepository;

@Component // será um componente do spring
public class JwtTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserRepository userRepository;

	// acrecestar objetos (dados) ao Token:
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		User user = userRepository.findByEmail(authentication.getName());

		Map<String, Object> map = new HashMap<>();
		map.put("userFirstName", user.getFirstName()); // inserir o firstName no Token
		map.put("userId", user.getId()); // inserir o nameId no Token

		// accessToken não tem método para adicionar informações
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		token.setAdditionalInformation(map); // insere dados adicionais no token

		return accessToken;
	}

}
