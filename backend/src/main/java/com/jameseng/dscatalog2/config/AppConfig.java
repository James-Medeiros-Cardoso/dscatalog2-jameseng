package com.jameseng.dscatalog2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class AppConfig {

	// @Bean = o método abaixo será um componente gerenciado pelo spring
	@Bean // componente do spring (como o service)
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// acessar tokens JWT (ler, codificar, decodificar)
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter(); // intanciar
		tokenConverter.setSigningKey("MY-JWT-SECRET"); // registrar a chave do token
		return tokenConverter;
	}

	// acessar tokens JWT (ler, codificar, decodificar)
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

}
