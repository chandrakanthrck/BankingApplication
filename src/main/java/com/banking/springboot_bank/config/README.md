1. User Requests a Protected Resource:
   A client (such as a web browser or mobile app) makes a request to a protected resource on the server (e.g., /api/protected-resource).
   This request includes a JWT token in the Authorization header, typically formatted as Bearer <token>.
2. The Request Passes Through Security Filters:
   The incoming request first passes through the Spring Security filter chain, which has been configured in your SecurityConfig class.
   The SecurityFilterChain bean defines the rules for which endpoints are accessible and which require authentication. For example, it allows anyone to access the /api/user endpoint but requires authentication for any other endpoint.
3. JWT Authentication Filter (JwtAuthenticationFilter):
   The JwtAuthenticationFilter is a custom filter that is executed once per request because it extends OncePerRequestFilter.
   Inside the doFilterInternal method the filter will:
   Extract the JWT: The filter looks for the JWT token in the Authorization header of the request.
   Validate the JWT: The filter uses the JwtTokenProvider class to validate the JWT. It checks whether the token is properly signed, whether it has expired, etc.
   Load User Details: If the JWT is valid, the filter extracts the username from the token and loads the user details using the UserDetailsService.
   Set Authentication in Security Context: The user is then authenticated in the current security context, which means the request is considered authenticated for the rest of its processing.
   Continue the Request: If the JWT is valid and the user is authenticated, the filter calls filterChain.doFilter(request, response) to continue processing the request. If the JWT is invalid or missing, the filter might stop processing and return an error.
4. Handling Unauthorized Access (JwtAuthenticationEntryPoint):
   If a request to a protected endpoint is made without a valid JWT, or if there’s an authentication failure (e.g., due to an invalid token), the request is intercepted by the JwtAuthenticationEntryPoint.
   The JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, which is a Spring Security interface for handling authentication errors.
   The commence method is called, sending a 401 Unauthorized HTTP response to the client, along with an error message indicating that authentication is required or that the provided credentials were invalid.
5. Generating JWT Token (JwtTokenProvider):
   Separately, when a user successfully logs in (using an endpoint like /api/user/login), the application generates a JWT using the JwtTokenProvider class.
   The generateToken method takes an Authentication object, retrieves the username, and creates a JWT signed with the secret key and valid for a set amount of time (as configured in the jwt.expiration property).
   The generated JWT is returned to the client, which then includes it in the Authorization header for subsequent requests.
6. User Accesses Protected Resource Again:
   With a valid JWT in hand, the client can now access protected resources by including the JWT in the Authorization header of each request.
   The JwtAuthenticationFilter validates the token on each request, ensuring that only authenticated users with valid tokens can access protected endpoints.
7. Password Encryption:
   When users register or change their password, the application uses the PasswordEncoder bean from the SecurityConfig class to hash passwords with bcrypt before storing them in the database. This ensures that passwords are stored securely.
   Putting It All Together:
   User Requests Access:

The client requests access to a protected resource with a JWT in the Authorization header.
JWT Authentication:

JwtAuthenticationFilter extracts, validates the JWT, and sets the authentication context if valid.
Resource Access:

If the JWT is valid, the request is passed along the filter chain to access the resource. If not, JwtAuthenticationEntryPoint handles the error and sends a 401 response.
Error Handling:

If there’s an issue with authentication (e.g., invalid token), the JwtAuthenticationEntryPoint sends back an unauthorized error.
Secure Password Handling:

When users register or update passwords, bcrypt hashing is used to securely store passwords.
