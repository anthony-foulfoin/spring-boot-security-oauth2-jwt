package org.springdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig  {

  @Value("${security.oauth2.resource.jwt.key-store}")
  private String keyStorePath;

  @Value("${security.oauth2.resource.jwt.key-store-password}")
  private String keyStorePass;

  @Value("${security.oauth2.resource.jwt.key-alias}")
  private String keyPairAlias;

  /**
   * Spring will use this encoder to check the user password when performing an authorization
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource(keyStorePath), keyStorePass.toCharArray()).getKeyPair(keyPairAlias);
    converter.setKeyPair(keyPair);
    return converter;
  }
}
