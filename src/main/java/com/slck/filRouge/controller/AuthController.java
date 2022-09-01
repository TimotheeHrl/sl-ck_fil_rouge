package com.slck.filRouge.controller;

import com.slck.filRouge.model.ERole;
import com.slck.filRouge.model.Role;
import com.slck.filRouge.model.User;
import com.slck.filRouge.payload.request.LoginRequest;
import com.slck.filRouge.payload.request.SignupRequest;
import com.slck.filRouge.payload.response.JwtResponse;
import com.slck.filRouge.payload.response.MessageResponse;
import com.slck.filRouge.repository.RoleRepository;
import com.slck.filRouge.repository.UserRepository;
import com.slck.filRouge.security.CurrentUser;
import com.slck.filRouge.security.jwt.JwtUtils;
import com.slck.filRouge.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;




  @PostMapping(value = "/signin", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, 
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                         roles));
  }


  @PostMapping(value = "/signup", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), 
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));

    Set<Role> roles = new HashSet<>();

      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);


    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @RolesAllowed({"ROLE_ADMIN"})
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserAdmin(@PathVariable(value = "id") Long id,
                                           @Valid @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id : " + id));

  if(userDetails.getEmail() != null) {
    user.setEmail(userDetails.getEmail());
  }
  if(userDetails.getRoles() != null) {
    user.setRoles(userDetails.getRoles());
  }
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }


    @RolesAllowed({"ROLE_MODERATOR"})
      @PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> updateUserModerator(@Valid @RequestBody User userDetails,@CurrentUser UserDetails currentUser) {
   String userName = currentUser.getUsername();
User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found with username : " + userName));


    if(userDetails.getEmail() != null) {
      user.setEmail(userDetails.getEmail());
    }
  if(userDetails.getPassword() != null) {
     user.setPassword(userDetails.getPassword());
  }
  userRepository.save(user);
          return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
      }

}
