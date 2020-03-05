package by.arhor.university.web.api.v1;

import static by.arhor.university.Constants.REST_API_V_1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arhor.university.repository.UserRepository;
import by.arhor.university.web.api.model.SignInRequest;
import by.arhor.university.web.api.model.SignUpRequest;
import by.arhor.university.web.security.JwtProvider;
import by.arhor.university.web.security.JwtResponse;

@RestController
@RequestMapping(path = REST_API_V_1 + "/auth")
public class AuthController extends ApiController {

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);

  private final AuthenticationManager authManager;
  private final UserRepository userRepository;
  private final PasswordEncoder encoder;
  private final JwtProvider jwtProvider;

  @Autowired
  public AuthController(
      AuthenticationManager authManager,
      UserRepository userRepository,
      PasswordEncoder encoder,
      JwtProvider jwtProvider) {
    this.authManager = authManager;
    this.userRepository = userRepository;
    this.encoder = encoder;
    this.jwtProvider = jwtProvider;
  }

  @PostMapping("/signin")
  public JwtResponse authenticateUser(@RequestBody SignInRequest signInRequest) {
    Authentication authentication =
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(), signInRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return JwtResponse.withToken(jwtProvider.generateJwtToken(authentication));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
    if (userRepository.countByEmail(signUpRequest.getEmail()) != 0) {
      return new ResponseEntity<>("Fail -> Email is already taken!", HttpStatus.BAD_REQUEST);
    }

    userRepository.createNewUser(
        signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()),
        signUpRequest.getFirstName(),
        signUpRequest.getLastName());

    return new ResponseEntity<>("Successfully registered", HttpStatus.CREATED);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/refresh")
  public JwtResponse refresh(Authentication authentication) {
    return JwtResponse.withToken(jwtProvider.generateJwtToken(authentication));
  }
}
