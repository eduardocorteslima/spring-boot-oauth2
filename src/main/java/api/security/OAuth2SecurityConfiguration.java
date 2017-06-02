package api.security;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configuration.ClientDetailsServiceConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;

@Configuration
@Order(1)
@Import({ ClientDetailsServiceConfiguration.class, AuthorizationServerEndpointsConfiguration.class })
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.authenticationProvider(authenticationProvider);
	}

	@Autowired
	private List<AuthorizationServerConfigurer> configurers = Collections.emptyList();

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private AuthorizationServerEndpointsConfiguration endpoints;

	@Autowired
	public void configure(ClientDetailsServiceConfigurer clientDetails) throws Exception {
		for (AuthorizationServerConfigurer configurer : configurers) {
			configurer.configure(clientDetails);
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		AuthorizationServerSecurityConfigurer configurer = new AuthorizationServerSecurityConfigurer();
		FrameworkEndpointHandlerMapping handlerMapping = endpoints.oauth2EndpointHandlerMapping();
		http.setSharedObject(FrameworkEndpointHandlerMapping.class, handlerMapping);
		configure(configurer);
		http.apply(configurer);
		String tokenEndpointPath = handlerMapping.getServletPath("/oauth/token");
		String tokenKeyPath = handlerMapping.getServletPath("/oauth/token_key");
		String checkTokenPath = handlerMapping.getServletPath("/oauth/check_token");
		if (!endpoints.getEndpointsConfigurer().isUserDetailsServiceOverride()) {
			UserDetailsService userDetailsService = http.getSharedObject(UserDetailsService.class);
			endpoints.getEndpointsConfigurer().userDetailsService(userDetailsService);
		}
		// @formatter:off
		http.authorizeRequests()
				.antMatchers(tokenEndpointPath).fullyAuthenticated()
				.antMatchers(tokenKeyPath).access(configurer.getTokenKeyAccess())
				.antMatchers(checkTokenPath).access(configurer.getCheckTokenAccess())
				.antMatchers("/", "/oauth/**", "/webjars/**", "/swagger-ui.html**", "/swagger-resources/**",
						"/v2/api-docs/**").fullyAuthenticated()
			.and().requestMatchers()
				.antMatchers("/", "/oauth/**", "/webjars/**", "/swagger-ui.html**", "/swagger-resources/**",
						"/v2/api-docs/**", tokenEndpointPath, tokenKeyPath, checkTokenPath)
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
		// @formatter:on
		http.setSharedObject(ClientDetailsService.class, clientDetailsService);
	}

	protected void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		for (AuthorizationServerConfigurer configurer : configurers) {
			configurer.configure(oauthServer);
		}
	}

}
