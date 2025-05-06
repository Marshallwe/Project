package com.shanzhu.em.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.User;
import com.shanzhu.em.entity.form.LoginForm;
import com.shanzhu.em.entity.vo.UserVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withExposedPorts(3306)
            .withCommand(
                    "--default-authentication-plugin=mysql_native_password",
                    "--character-set-server=utf8mb4",
                    "--collation-server=utf8mb4_unicode_ci");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () ->
                String.format("jdbc:mysql://%s:%d/testdb?useSSL=false",
                        mysql.getHost(),
                        mysql.getFirstMappedPort()));
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    private String authToken;

    @BeforeEach
    void setup() {
        // Create test user
        ResponseEntity<R<User>> registerResponse = restTemplate.exchange(
                "/register",
                HttpMethod.POST,
                new HttpEntity<>(new LoginForm("testuser", "password")),
                new ParameterizedTypeReference<R<User>>() {}
        );
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Get authentication token
        ResponseEntity<R<UserVo>> loginResponse = restTemplate.exchange(
                "/login",
                HttpMethod.POST,
                new HttpEntity<>(new LoginForm("testuser", "password")),
                new ParameterizedTypeReference<R<UserVo>>() {}
        );
        authToken = loginResponse.getBody().getData().getToken();
    }

    @Test
    void shouldPerformFullUserLifecycle() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        // Test user registration
        ResponseEntity<R<User>> registerResponse = restTemplate.exchange(
                "/register",
                HttpMethod.POST,
                new HttpEntity<>(new LoginForm("newuser", "newpass")),
                new ParameterizedTypeReference<R<User>>() {}
        );
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(registerResponse.getBody().getData().getUsername())
                .isEqualTo("newuser");

        // Test user login
        ResponseEntity<R<UserVo>> loginResponse = restTemplate.exchange(
                "/login",
                HttpMethod.POST,
                new HttpEntity<>(new LoginForm("newuser", "newpass")),
                new ParameterizedTypeReference<R<UserVo>>() {}
        );
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody().getData().getUsername())
                .isEqualTo("newuser");

        // Test get user by username
        ResponseEntity<R<User>> userResponse = restTemplate.exchange(
                "/userinfo/newuser",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<R<User>>() {}
        );
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponse.getBody().getData().getUsername())
                .isEqualTo("newuser");

        // Test update user
        User userToUpdate = userResponse.getBody().getData();
        userToUpdate.setNickname("Updated Nickname");
        ResponseEntity<R<Void>> updateResponse = restTemplate.exchange(
                "/user",
                HttpMethod.POST,
                new HttpEntity<>(userToUpdate, headers),
                new ParameterizedTypeReference<R<Void>>() {}
        );
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify update
        ResponseEntity<R<User>> updatedUserResponse = restTemplate.exchange(
                "/userinfo/newuser",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<R<User>>() {}
        );
        assertThat(updatedUserResponse.getBody().getData().getNickname())
                .isEqualTo("Updated Nickname");

        // Test batch delete
        ResponseEntity<R<Void>> deleteResponse = restTemplate.exchange(
                "/user/del/batch",
                HttpMethod.POST,
                new HttpEntity<>(List.of(userToUpdate.getId()), headers),
                new ParameterizedTypeReference<R<Void>>() {}
        );
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify deletion
        ResponseEntity<R<User>> deletedUserResponse = restTemplate.exchange(
                "/userinfo/newuser",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<R<User>>() {}
        );
        assertThat(deletedUserResponse.getStatusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldPaginateUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        // Create test data
        for (int i = 0; i < 15; i++) {
            restTemplate.exchange(
                    "/register",
                    HttpMethod.POST,
                    new HttpEntity<>(new LoginForm("user" + i, "pass" + i)),
                    new ParameterizedTypeReference<R<User>>() {}
            );
        }

        // Test pagination
        ResponseEntity<R<IPage<User>>> pageResponse = restTemplate.exchange(
                "/user/page?pageNum=2&pageSize=10",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<R<IPage<User>>>() {}
        );

        assertThat(pageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(pageResponse.getBody().getData().getCurrent()).isEqualTo(2);
        assertThat(pageResponse.getBody().getData().getSize()).isEqualTo(10);
        assertThat(pageResponse.getBody().getData().getTotal()).isGreaterThan(15);
    }

    @Test
    void shouldHandleAuthenticationFailure() {
        // Test invalid login
        ResponseEntity<R<UserVo>> response = restTemplate.exchange(
                "/login",
                HttpMethod.POST,
                new HttpEntity<>(new LoginForm("wronguser", "wrongpass")),
                new ParameterizedTypeReference<R<UserVo>>() {}
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldGetCurrentUserId() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        ResponseEntity<Long> response = restTemplate.exchange(
                "/userid",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Long.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isPositive();
    }

    @Test
    void shouldHandleInvalidUserOperations() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        // Test delete non-existent user
        ResponseEntity<R<Void>> deleteResponse = restTemplate.exchange(
                "/user/9999",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<R<Void>>() {}
        );
        assertThat(deleteResponse.getStatusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}