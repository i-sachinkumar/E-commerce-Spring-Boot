package com.ihrsachin.ecommerce.controller;

import com.ihrsachin.ecommerce.model.AppRole;
import com.ihrsachin.ecommerce.model.Role;
import com.ihrsachin.ecommerce.repo.RoleRepository;
import com.ihrsachin.ecommerce.model.User;
import com.ihrsachin.ecommerce.repo.UserRepo;
import com.ihrsachin.ecommerce.security.request.SignupRequest;
import com.ihrsachin.ecommerce.security.jwt.JwtUtils;
import com.ihrsachin.ecommerce.security.request.LoginRequest;
import com.ihrsachin.ecommerce.security.response.MessageResponse;
import com.ihrsachin.ecommerce.security.response.UserInfoResponse;
import com.ihrsachin.ecommerce.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Invalid username or password");
            map.put("status", false);
            //
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie responseCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(userInfoResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepo.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
        }
        if (userRepo.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
        }

        // if role not available
        try {
            Role roleToAdd = new Role();
            roleToAdd.setRoleName(AppRole.ROLE_USER);
            roleRepository.save(roleToAdd);

            roleToAdd = new Role();
            roleToAdd.setRoleName(AppRole.ROLE_ADMIN);
            roleRepository.save(roleToAdd);

            roleToAdd = new Role();
            roleToAdd.setRoleName(AppRole.ROLE_SELLER);
            roleRepository.save(roleToAdd);
        }catch (Exception e){
            System.out.println("Roles already exists");
        }

        //create new user
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));


        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role `admin` is not found"));
                        roles.add(adminRole);
                        break;

                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role `seller` is not found"));
                        roles.add(sellerRole);
                        break;

                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);

                }
            });
        }

        user.setRoles(roles);
        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully");
    }


    @GetMapping()
    public ResponseEntity<?> signOut(){
        ResponseCookie responseCookie = jwtUtils.generateCleanCookie();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(new MessageResponse("You have successfully signed out!!"));
    }

    @GetMapping("/username")
    public String getUsername(Authentication authentication){
        if(authentication != null){
            return authentication.getName();
        }
        else return "NULL";
    }

    @GetMapping("//profile")
    public ResponseEntity<?> getProfile(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();

        return ResponseEntity.ok(userInfoResponse);
    }

}
