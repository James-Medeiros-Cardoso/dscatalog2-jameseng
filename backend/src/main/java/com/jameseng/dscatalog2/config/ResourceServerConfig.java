package com.jameseng.dscatalog2.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer // essa classe implementa p ResourceServer do oauth2
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private JwtTokenStore tokenStore;

	@Autowired
	private Environment env;

	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" }; // essa rota tem que ser publico para
																				// fazer o login

	// rotas liberadas para operador e admin:
	private static final String[] OPERATOR_OR_ADMIN = { "/products/**", "/categories/**" };

	// rotas somente para admin:
	private static final String[] ADMIN = { "/users/**" };

	// o resource server será capaz de analisar o token recebido
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		// liberar o H2:
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}

		http.authorizeRequests().antMatchers(PUBLIC).permitAll() // liberado de autorizações / permitAll() = permite
																	// todos
				// HttpMethod.GET = libera o GET no vetor OPERATOR_OR_ADMIN
				// e .permitAll() = libera para todos
				.antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll()

				// quem for "OPERATOR" ou "ADMIN" acessa as rotas do OPERATOR_OR_ADMIN
				.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")

				// quem "ADMIN" acessa as rotas do ADMIN
				.antMatchers(ADMIN).hasAnyRole("ADMIN")

				// quem acessar qualquer outra rota, tem que estar logado.
				.anyRequest().authenticated();
	}

}
