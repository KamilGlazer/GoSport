package com.kamilglazer.gosport.controllerTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.exception.UnauthorizedException;
import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.domain.USER_ROLE;
import com.kamilglazer.gosport.dto.request.LoginRequest;
import com.kamilglazer.gosport.dto.request.RegisterRequest;
import com.kamilglazer.gosport.dto.response.JwtResponse;
import com.kamilglazer.gosport.exception.UserNotFoundException;
import com.kamilglazer.gosport.model.User;
import com.kamilglazer.gosport.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(MockitoExtension.class)
public class AuthControllerIntegrationTest {

    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.34")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("app.secret-key", () -> "test-secret-key-test-secret-key-test-secret-key-test-secret-key-");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_WhenValidRequest_ReturnsJwtResponse() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "john.doe@example.com",
                "Doe",
                "John",
                "password",
                "123456789"
        );

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        JwtResponse jwtResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(), JwtResponse.class
        );

        assertThat(jwtResponse.getToken()).isNotBlank();
        assertThat(jwtResponse.getMessage()).isNotBlank();
        assertThat(jwtResponse.getRole()).isEqualTo(USER_ROLE.USER);

        String extractedEmail = jwtService.extractUsername(jwtResponse.getToken());
        assertThat(extractedEmail).isEqualTo("john.doe@example.com");
    }

    @Test
    void registerUser_WhenEmailExists_ReturnsBadRequest() throws Exception {
        User existingUser = User.builder()
                .firstName("Existing")
                .lastName("User")
                .email("existing@example.com")
                .password(passwordEncoder.encode("password"))
                .build();
        userRepository.save(existingUser);

        RegisterRequest request = new RegisterRequest(
                "existing@example.com",
                "Doe",
                "Johnson",
                "password",
                "123456789"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void loginUser_WhenValidRequest_ReturnsJwtResponse() throws Exception {
        User user  = User.builder()
                .firstName("John")
                .lastName("User")
                .email("john.doe@example.com")
                .password(passwordEncoder.encode("password"))
                .build();
        userRepository.save(user);

        LoginRequest request = new LoginRequest("john.doe@example.com","password");

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        JwtResponse jwtResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JwtResponse.class);

        assertThat(jwtResponse.getToken()).isNotBlank();
        assertThat(jwtResponse.getMessage()).isNotBlank();
        assertThat(jwtResponse.getRole()).isEqualTo(USER_ROLE.USER);
    }


    @Test
    void loginUser_WhenInvalidRequest_ReturnsUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("john.doe@example.com","password");

       mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserNotFoundException.class));
    }

    @Test
    void validateToken_WhenValidToken_ReturnsJwtResponse() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "validate@example.com", "Test", "Valid", "password", "987654321"
        );

        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        JwtResponse jwtResponse = objectMapper.readValue(registerResult.getResponse().getContentAsString(), JwtResponse.class);

        String token = jwtResponse.getToken();

        mockMvc.perform(get("/api/auth/validate")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}