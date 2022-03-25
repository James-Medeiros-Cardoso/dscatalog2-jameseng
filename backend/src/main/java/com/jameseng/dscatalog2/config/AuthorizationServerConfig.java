package com.jameseng.dscatalog2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer // essa classe representa o AuthorizationServer do oauth
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;

	@Autowired
	private JwtTokenStore tokenStore;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// permitAll() = nome do método
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// como será a autenticação e como serão os dados do cliente
		clients.inMemory() // processo em memória
				.withClient("dscatalog2") // withClient("dscatalog2") = nome da aplicação
				.secret(passwordEncoder.encode("dscatalog2123")) // senha da aplicação
				.scopes("read", "write") // acesso de leitura e escrita
				.authorizedGrantTypes("password") // password = padrão do oauth
				.accessTokenValiditySeconds(86400); // tempo de expiração do token (segundos) / 86400 = 24 horas
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// quem vai autorizar e como será o formato do token
		endpoints.authenticationManager(authenticationManager) // autenticação
				.tokenStore(tokenStore) // vai processar o token
				.accessTokenConverter(accessTokenConverter); //
	}

}
