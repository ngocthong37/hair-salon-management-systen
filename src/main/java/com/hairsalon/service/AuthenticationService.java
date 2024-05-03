package com.hairsalon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hairsalon.Enum.Role;
import com.hairsalon.entity.*;
import com.hairsalon.Enum.TokenType;
import com.hairsalon.respository.CartRepository;
import com.hairsalon.respository.TokenRepository;
import com.hairsalon.respository.UserRepository;
import com.hairsalon.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final EmailSendService emailSendService;

  @Autowired
  private CartRepository cartRepository;


  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .userName(request.getUserName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
            .status("OK")
            .phoneNumber("null")
            .address("null")
            .fullName("null")
        .build();
    var savedUser = repository.save(user);
    Cart cart = new Cart();
    cart.setCustomer(savedUser);
    cartRepository.save(cart);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
            .role(savedUser.getRole().toString())
            .accountId(savedUser.getId())
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse addEmployee(String json) {
    JsonNode jsonNode;
    JsonMapper jsonMapper = new JsonMapper();
    try {
      jsonNode = jsonMapper.readTree(json);
      Random random = new Random();
      var password = String.format("%08d", random.nextInt(100000000));
      String fullName = jsonNode.get("fullName") != null ? jsonNode.get("fullName").asText() : "";
      String email = jsonNode.get("email") != null ? jsonNode.get("email").asText() : "";
      String address = jsonNode.get("address") != null ? jsonNode.get("address").asText() : null;
      String phoneNumber = jsonNode.get("phoneNumber") != null ? jsonNode.get("phoneNumber").asText() : "";
      var user = User.builder()
              .userName("username")
              .email(email)
              .password(passwordEncoder.encode(password))
              .role(Role.EMPLOYEE)
              .address(address)
              .fullName(fullName)
              .phoneNumber(phoneNumber)
              .status("OK")
              .build();
      var savedUser = repository.save(user);
      if (savedUser.getPassword() != null) {
        String[] cc = {};
        Map<String, Object> model = new HashMap<>();
        model.put("userName", savedUser.getUsername());
        model.put("password", password);
        emailSendService.sendMail(savedUser.getEmail(), cc, "Tài khoản truy cập Hair Salon của bạn đã được tạo", model);
      }
      var jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);
      saveUserToken(savedUser, jwtToken);
      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .build();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .role(String.valueOf(user.getRole()))
            .accountId(user.getId())
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
