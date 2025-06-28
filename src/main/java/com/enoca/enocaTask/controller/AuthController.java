package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.entity.Customer;
import com.enoca.enocaTask.security.AuthRequest;
import com.enoca.enocaTask.security.JwtService;
import com.enoca.enocaTask.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;


    public AuthController(CustomerService customerService, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwtToken = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(Map.of("token", jwtToken));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
