# Spring-Security-Demo
## Demo with Basic/JWT authentication in Spring Boot application

Spring security helps secure web applications with minimum configurations.
Spring security provides lots of interfaces/annotations/classes that help in protecting API endpoints,
as well as protecting against CSRF attacks (CsrfTokenRequestAttributeHandler class to generate CSRF tokens), CORS allowed origins etc.
It also helps with method level security (method invocation based on roles). 
Spring security supports various standards of security like basic, JWT, OAuth, OpenId etc.
It also supports certain properties that can be given in the application.properties of springboot application.

Spring security also supports OAuth2. For eg. enum class called CommonOAuth2Provider, 
provides oath2 server information like token url, issuer url, auth uri etc., 
for common OAuth2 servers in the market like Github, Google, facebook etc.

Q. How does Spring Security works?
Ans. Spring Security dependency in pom of spring boot when auto-configured, creates a delegate filter. 
This delegate filter forwards request to other filters as per the security config classes configured in code 
First authentication filters are invoked and then authorization filters.
You can also write custom filters.

Once a request for AuthN is successful using spring security, a cookie named JSESSIONID is et in browser,
which is used for subsequent requests. Your app should ensure to delete this cookie on logout to protct from 
cookie stealing attacks. Using JWT tokens is better, as all user information is stored in token itself,
and you dont have to store the state of logged in user in JSESSIONID cookie. 
The signature of JWT ensured, that the token that backend generated and sent to client app,
has not been tampered with in any way.

This spring boot based demo application demonstrates Basic/JWT authenticating with different set of URLs.

## Steps to create an app with spring security:

1. Go to https://start.spring.io/, create a project with added dependencies of Web and **spring-boot-starter-security**. 
   JWT(JsonWebToken) is not available at spring initializer, hence is added to pom separately.
2. Load the project into your IDE and add JWT dependency.
3. Create the necessary config and security files, and bring the application up. 
   
This project uses JDK 1.8. If upgrading JDK version, upgrade spring as well.

## Running the app
Import the postman collection included at the root of the project.

Bring the application up, and using postman, hit the apis as per their order:

1. Get with JWT token missing :
   
 Hit url http://localhost:8080/rest/hello, and get JWT missing error
	
Notice 401 ‘Unauthorized’ is returned in response.

2. POST call to get JWT token (in actual scenario, this token would be created by an oauth server):
		
http://localhost:8080/token

Notice the userdetails in the body of the request.

In the response you will get the JWT authorization token. 

3. Copy the JWT token received above and paste it in header against key **Authorization**.
   Call rest/hello again, and see the response coming fine this time.
4. Time to test Basic auth now. Hit this url, and see the authentication fail 401 response:
	http://localhost:8080/basic/hello
	
5. Now hit the api with Basic auth credentials, as configured in BasicSecurityConfig.java, 
and see 200 ok response from the api. 

Also note the authorization token that got added in the header with encoded Basic token. 
ie. **Authorization = Basic <encoded token>**

6 and 7. demonstrate use of Spring provided UserDetails class.

Hit an endpoint (/user/hello) protected by basic auth, using username and password of a user named 'user'.

Then hit user/getLoggedInUser which returns the details of the logged in user.

8.Now hit the admin url using admin's credentials :admin/adminpass

## Some files to notice

1. **WebSecurityConfigurerAdapter**:

Note how 3 security config files have been created in this project for different set of urls using Basic and JWT authentications.
Also note the use of @Order between similar configuration files.
Also note in config files, WebSecurityConfigurerAdapter class being extended, and the annotations used are from spring-security dependency.

WebSecurityConfigurerAdapter provides configure method which can be overridden in two ways:
```
class Myconfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(AuthenticationManagerBuilder builder) // For Authentication

	OR

	@Override
	protected void configure(HttpSecurity http) // For Authorization
}
```

Or, you can also use inteface GrantedAuthority, and its one of the implementing classes - SimpleGrantedAuthority for AuthZ putposes.
It provides methods has fetchAuthorities/hasAuthority, etc., the implementation of which can be connected to 
roles information stored in application's database.

2. Take note of how role are assigned to basic auth users in BasicSecurityConfig.java

3. Ideally the original passwords (youtube/adminpass/password) should not be kept in source code, but have been shown in the code in this poc for understanding purposes only. 

4. **UserDetailsService** interface:

   In memory user details as used in BasicSecurityconfig are good for demo purposes only. 
   Better way is to use an implementation of **UserDetailsService** interface as provided by Spring security. 
   This interface has one method - loadUserByName(). 
   So your implementation would have code to fetch the user by name from a database, or ldap etc., 
   and return the user as an object of **UserDetail** class (again provided by Spring Security). 
   See BasicUserDetailsConfig for this setup and /user/hello endpoint that returns user from UserDetails object.

  You can fetch the logged in user details in subsequent operations using Spring security provided **@AuthenticationPrincipal**, and the User class. 
  See BasicHelloController -> getLoggedInUser(). 
  Postman collection endpoints 6 and 7 mentioned above demonstrate this point.

5. **Method level security**: Spring provides @Secured annotation to tell what roles are required to call a specific method:
```
	@Secured({"ADMIN", "SUPER_USER"}) 
	public void someMethod() {
		
	}
```
To use @Secured, you will need to give @EnableGlobalMethodSecurity(securedEnabled=true) at top of Security config class, and also need to add spring-aop dependency in the pom.

Spring security also provides pre-authorizers using @Preauthorize annotation which can be put at Rest controller mapping functions.

Read more about these at: https://www.baeldung.com/spring-security-method-security

6. **Session management**: You would want to limit the max. no. of sessions of the logged in user can have open concurrently. This is imp. when yours is a paid website, and you do not want the user to share his credentials with others. To achieve this, you can set the maximum no. of active  sessions for a given user in Spring security config. See BasicUserDetailsConfig -> configure() method as an example.

7. Storing passwords: When storing passwords in db etc, never store them as plain text. Use encoding, encryption, or hasing.
   Encoding is least secure, as it can easily be recovered. eg of encoding is base64 encoding. 
   Encrypted password can be recovered, but only with the key used for encryption.
   Hashed passwords can never be recovered, hence hashing is the best solution for converting passwords.
   
   Spring security provides many **PasswordEncoders**, which help hash the password values. 
   See the usage of BCryptPasswordEncoder in BasicSecurityConfig.java.
   


## Misc. topics

### What is Basic Authentication?

Basic authentication is a standard HTTP header with the user and password encoded in base64 : Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==.The userName and password is encoded in the format username:password. This is one of the simplest technique to protect the REST resources because it does not require cookies, session identifiers or any login pages.

### What is JWT?
JSON Web Token (JWT) is a means of representing claims to be transferred between two parties. The claims in a JWT are encoded as a JSON object that is digitally signed using JSON Web Signature (JWS) and/or encrypted using JSON Web Encryption (JWE). JWT token consists of 3 parts separated by a period: header, body and signature.

### How JWT works?
During login the user sends a user/password to the server. The server checks the password, and sends a token to the user. This token states: "this is user <user_id> and his role are <role1, role2>". It is signed by the server, so the server can check later that its content is not altered. For the next requests, the client will only send his token, not his username or password. The server will check the token's validity, and can assume that the information it contains is valid because it signed the token itself!

JWT along with SSL (HTTPS) provides very good security for the application.

### JWT vs Basic Auth:
JWT also help protect you against CSRF attacks.

Basic auth is based on shared username and password which have high risk of being hacked.

In case of basic authentication, the username and password is only encoded with Base64, but not encrypted or hashed in any way. Hence, it can be compromised by any man in the middle. Hence, it is always recommended to authenticate rest API calls by this header over a ssl connection.

### CSRF
When a malicious site makes you click on a link etc., and performs a state changing operation on a website, using your logged in credentials on that website. Solution is to generate a csrf token when the user logs in, and store that token  on the client side (as hidden input element of a form, so that it is sent to backend everytime form is submitted, or better in HTML5 Web Storage), and send it on every state changing operation (POST/PUT/DELETE/PATCH) to the backend. Backend would then verify the token and allow the operation. Backend will also return a new CSRF token in the response, the frontend can then save this token for the next state changing operation.

### Learn more about Security:
http://www.devglan.com/spring-security/spring-boot-security-rest-basic-authentication

https://jwt.io/introduction/

https://codeburst.io/jwt-to-authenticate-servers-apis-c6e179aa8c4e

https://medium.com/@rahulgolwalkar/pros-and-cons-in-using-jwt-json-web-tokens-196ac6d41fb4

http://www.baeldung.com/spring-security-multiple-entry-points

Online tool for bCrypt password generator:

http://www.devglan.com/online-tools/bcrypt-hash-generator

