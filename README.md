# Spring Boot Security OAuth2 JWT example

This project provides an example of how to use a JWT token to secure a Spring Boot application.
There are a lot of examples available on github for doing this, but most of them weren't fully satisfying for several reasons:

 - **They don't use the Spring JWT implementation**. Spring Security now provides its own JWT project (spring-security-jwt) that is fully integrated with Spring, preventing you from writing a lot of boilerplate code.
 - **They don't use the Spring Boot autoconfiguration** and redefine a lot of things they don't need to. You just need to provide your RSA key and your client id, that's all.
 - **They use 2 or 3 different applications** for handling the OAuth authorization, serving the resources, and the client. If you want to have everything in just one app, the configuration is simpler.

For this reasons I share my own example that tries to re-use the already defined Spring Security code.

# Configuring Spring Oauth2 with JWT
## Step 1: Generate a RSA key pair

Here are the steps to create a RSA key pairs with the Java keytool command.

```sh
$ keytool -genkey -alias mytestkey -keyalg RSA -keystore keystore.jks -storepass mystorepass -dname "CN=Web Server,OU=Unit,O=Organization,L=City,S=State,C=US"
```

Put this file in your `src/main/resources`

This file contains your public and your private RSA keys. Keep this file **private**, **do not** commit it on a public git repo. You can export and publish the public key.

## Step 2: Configure Spring Oauth to use the keypair
Update the file `application.yml` to set your keystore password and your key alias

```yml
key-pair:
  alias: mytestkey
  store-password: mystorepass
```

## Step 3: Customize the Oauth client informations

Update the file `application.yml` to set your own client id and secret

```yml
client:
  clientId: myClient
  clientSecret: myClientSecret
```
This is your client 'API key' you will have to use each time you make a request to the OAuth2 authorization server.
You just need to set it once, and provide it in each OAuth requests thanks to a HTTP Basic header (https://en.wikipedia.org/wiki/Basic_access_authentication).

## Step 4 (Optionnal) : Override the default Spring Oauth2 config

Once you have completed the step 3, you are all done ! Spring Boot will handle all the boilerplate config for you and use the config provided in the `application.yml`.

See http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-security.html for more informations about the way Spring Boot autoconfigures the security layer.

### OAuth2 Authorization config

You can override the Spring Boot Oauth2 authorization config by making `OAuth2AuthorizationConfig` extending `AuthorizationServerConfigurerAdapter`

### Web security config

By default, all the endpoints need full authentication, excepting the ones under /oauth/, /css/, /js/, /images/. You can change this behavior by making `WebSecurityConfiguration` extending `ResourceServerConfigurerAdapter` and override the configure method. You can also use this method to enable or disable **csrf**

```java
public class WebSecurityConfiguration extends ResourceServerConfigurerAdapter {
  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
      .csrf()
        .disable()
      .authorizeRequests()
        .antMatchers("/my-public-api/**").permitAll()
        .anyRequest().authenticated();
  }
}
```

### Retrieving users

Users a retrieved thanks to the `UserService`. This class extends `UserDetailsService` that is a Spring Security class, and implements the `loadUserByUsername()` method. Spring will automatically uses this method to retrieve users from database.

### Passwords hashing

The passwords stored in the database are hashed thanks to a BCrypt algorithm using a cost of 10. The `OAuth2AuthorizationConfig.passwordEncoder()`method configures spring to use this algorithm automatically to compare the password received by the `/oauth` endpoints with the password in database.

# Testing the application

The users defined in the `import.sql` file are automatically inserted in the database when Spring Boot starts. We will use the user 'user' with a password 'password' (encrypted in the database with BCrypt).

## Step 1 : Get a JWT access token

Use the `/oauth/token` endpoint. Note that the provided user password is not encrypted. Spring will automatically hash the password with a BCrypt algorithm and compare it with the entry in the database.
```sh
$ curl myClient:myClientSecret@localhost:8080/oauth/token -d grant_type=password -d username=user -d password=password
```

The **myClient:myClientSecret** is your OAuth2 client API key, provided as Basic HTTP headers.

Result :
```json
{"access_token":"abcdefg","token_type":"bearer","refresh_token":"abcdefg","expires_in":43199,"scope":"openid","jti":"88821280-ac82-4066-af0c-e7a602f1bce6"}                                                                                          
```

The access_token is your jwt token. It contains your user informations.

## Step 2 : Use the JWT token to access secured endpoints

Use this access token in an **Authorization** header to query a secured endpoint. Note that you do not need to provide the client OAuth2 API key anymore (**myClient:myClientSecret**).
```sh
$ curl -H "Authorization: Bearer abcdef" localhost:8080/
```

Result :
```
Hello World
```

That's all !
