# Security-Demo
## Security demo with JWT authentication and Spring Boot

This spring boot based demo application demonstrates authenticating 2 different set of URLs
using Basic and JWT based authentications.

## Steps to create and start this poc:

1. Go to https://start.spring.io/, create a project with added dependencies of Web and Security. JWT(JsonWebToken) is not available here so will be added to pom separately.
2. Load the project into your IDE and add JWT dependency.
3. Create the necessary config and security files, and bring the application up. Note how 2 security config files have been created using @Order for Basic and JWT authentications respectively. 
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
In the repsone you will get the JWT autorization token. 

6. Copy the token and paste it in header agains key “Authorisation” and redo request 4 above, and see the response coming.
7. Time to test Basic auth now. Try to hit the following url:
	```
	http://localhost:8080/basic/hello
	```
	And see the authentication fail.
8. Now try to login using Basic auth, providing the user name and un-encrypted password as present in 
BasicSecurityConfig.java. 
ie. user/password
Note the authorization token in the header with encoded Basic token. 
9. Now hit the admin url using admin's credential:admin/adminpass

```
http://localhost:8080/admin/hello
```
Take not of how role based basic authentication is covered in BasicSecurityConfig.java

Ideally the original passwords (youtube/adminpass/password) should not be kept in source code or anywhere else, but have been shown in the code in this poc for understanding purposes only. 

## Theory

### What is Basic Authentication?

Basic authentification is a standard HTTP header with the user and password encoded in base64 : Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==.The userName and password is encoded in the format username:password. This is one of the simplest technique to protect the REST resources because it does not require cookies. session identifiers or any login pages.

### What is JWT?
JSON Web Token (JWT) is a means of representing claims to be transferred between two parties. The claims in a JWT are encoded as a JSON object that is digitally signed using JSON Web Signature (JWS) and/or encrypted using JSON Web Encryption (JWE).

### How JWT works?
During login the user sends a user/password to the server. The server checks the password, and sends a token to the user. This token states: "this is user <user_id> and his role is <role1, role2>". It is signed by the server, so the server can check later that its content is not altered. For the next requests, the client will only send his token, not his username or password. The server will check the token's validity, and can assume that the information it contains is valid because it signed the token itself!

JWT along with SSL (HTTPS) provides very good security for the application.

### JWT vs Basic Auth:
JWT also help protect you against CSRF attacks.
Basic auth is based on shared username and password wich have high risk of being hacked.
In case of basic authentication, the username and password is only encoded with Base64, but not encrypted or hashed in any way. Hence, it can be compromised by any man in the middle. Hence, it is always recommended to authenticate rest API calls by this header over a ssl connection.

### Learn more about Security:
http://www.devglan.com/spring-security/spring-boot-security-rest-basic-authentication
https://jwt.io/introduction/
https://codeburst.io/jwt-to-authenticate-servers-apis-c6e179aa8c4e
https://medium.com/@rahulgolwalkar/pros-and-cons-in-using-jwt-json-web-tokens-196ac6d41fb4
http://www.baeldung.com/spring-security-multiple-entry-points
Online tool for bCrypt password generator:
http://www.devglan.com/online-tools/bcrypt-hash-generator

