package app.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

	Logger logger = LoggerFactory.getLogger(AuthenticationProvider.class);

	@Value(value = "${auth.url}")
	String url;

	@Value(value = "${auth.codigo}")
	String codigo;

	@Value(value = "${auth.token}")
	String token;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {

		String user = auth.getName();
		String password = auth.getCredentials().toString();

		if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(password)) {

			byte[] passwordBytes = password.getBytes();
			byte[] base64PasswordBytes = Base64.encodeBase64(passwordBytes);
			String base64Password = new String(base64PasswordBytes);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.add("user.password", base64Password);
			headers.add("Content-type", "application/json");

			try {
				
				ResponseEntity<String> response;
				
				// Get info in api
				//response = getInfoUser(user, headers);

				// Mock
				if (user.equals("user") && password.equals("123")) {
					response = new ResponseEntity<>(
							"{\"records\": [{\"nome\":\"user\",\"systemAccessGroup\": \"admin\" }]}", HttpStatus.OK);
				} else {
					response = new ResponseEntity<>("{\"records\": []}", HttpStatus.UNAUTHORIZED);
				} // fim do Mock

				ObjectMapper mapper = new ObjectMapper();
				JsonNode root;

				root = mapper.readTree(response.getBody());
				JsonNode fields = mapper.readTree(root.path("records").toString());

				if (fields.has(0)) {

					JsonNode records = mapper.readTree(fields.path(0).toString());
					JsonNode role = records.path("systemAccessGroup");

					List<GrantedAuthority> grantedAuths = new ArrayList<>();

					grantedAuths.add(new SimpleGrantedAuthority(role.toString()));
					auth = new UsernamePasswordAuthenticationToken(user, password, grantedAuths);
				}

			} catch (IOException | RestClientException e) {
				logger.error(e.getMessage());
				logger.info("Usuário não autorizado:" + user);
			}
		}

		return auth;
	}

	
	@SuppressWarnings("unused")
	private ResponseEntity<String> getInfoUser(String user, HttpHeaders headers) {
		HttpEntity<String> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();

		return restTemplate.exchange(String.format(url, user, codigo), HttpMethod.GET, request, String.class);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
