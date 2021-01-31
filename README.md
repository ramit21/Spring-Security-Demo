# Spring-Security-Demo
## Demo with Basic/JWT authentication in Spring Boot application

This spring boot based demo application demonstrates authenticating 2 different set of URLs
using Basic and JWT based authentications.

## Steps to create and start this poc:

1. Go to https://start.spring.io/, create a project with added dependencies of Web and **Security**. JWT(JsonWebToken) is not available here so will be added to pom separately.
2. Load the project into your IDE and add JWT dependency.
3. Create the necessary config and security files, and bring the application up. Note how 2 security config files have been created using @Order for Basic and JWT authentications respectively. Also note in config files, WebSecurityConfigurerAdapter class being extended, and the annotations used are from spring-security dependency.
4. Bring the application up, and using postman, make get request to JWT based url.

		http://localhost:8080/rest/hello
	
Notice 401 ‘Unauthorized’ is returned in response.

5. Now make a post request to get the token (in actual scenario, this token will be created by auth server):
		
		http://localhost:8080/token
Body: 
```
{
	"userName":"Ramit",
	"id": 123,
	"role": "admin"
}
```
In the response you will get the JWT authorization token. 

6. Copy the token and paste it in header against key “Authorization” and redo request 4 above, and see the response coming.
7. Time to test Basic auth now. Try to hit the following url:
	```
	http://localhost:8080/basic/hello
	```
	And see the authentication fail.
8. Now try to login using Basic auth, providing the user name and un-encrypted password as present in 
BasicSecurityConfig.java. ie. user/password

Note the authorization token in the header with encoded Basic token. 

9. Now hit the admin url using admin's credential:admin/adminpass

```
http://localhost:8080/admin/hello
```
Take note of how role based basic authentication is configured in BasicSecurityConfig.java

Ideally the original passwords (youtube/adminpass/password) should not be kept in source code or anywhere else, but have been shown in the code in this poc for understanding purposes only. 

10. In memory user details as used in BasicSecurityconfig are good for demo purposes only. Better way is to use an implementation of UserDetailsService interface as provided by Spring security. This interface has one method - loadUserByName(). So your implementation would have code to fetch the user by name from the database, and return the user as an object of UserDetail class (again provided by Spring Security). See BasicUserDetailsConfig for the setup and hit below url to test the user using Basic auth: user/password
```
http://localhost:8080/user/hello
```

11. You can fetch the logged in user details in subsequent operations using Spring security provided @AuthenticationPrincipal, and the User class. See BasicHelloController -> getLoggedInUser(). After you are logged in as a user in previous step, hit below url, to get the user name:
```
http://localhost:8080/user/getLoggedInUser
```
12. Spring also provides @Secured annotation to tell what toles are required to call a specific method:
```
	@Secured({"ADMIN", "SUPER_USER"}) 
	public void someMethod() {
		
	}
```

## Theory

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

