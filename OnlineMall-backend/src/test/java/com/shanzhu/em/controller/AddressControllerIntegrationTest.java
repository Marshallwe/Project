package com.shanzhu.em.controller;

import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Address;
import com.shanzhu.em.entity.form.LoginForm;
import com.shanzhu.em.entity.vo.UserVo;
import com.shanzhu.em.service.AddressService;
import org.junit.jupiter.api.AfterAll;
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
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
public class AddressControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AddressService addressService;

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withExposedPorts(3306)
            .withStartupTimeout(Duration.ofMinutes(2))
            .withConnectTimeoutSeconds(120)
            .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1))
            .withCommand(
                    "--default-authentication-plugin=mysql_native_password",
                    "--skip-ssl",
                    "--character-set-server=utf8mb4",
                    "--collation-server=utf8mb4_unicode_ci",
                    "--max_allowed_packet=256M",
                    "--innodb_flush_log_at_trx_commit=0"
            );

    @AfterAll
    static void cleanup() {
        if (mysql.isRunning()) {
            mysql.stop();
        }
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () ->
                String.format(
                        "jdbc:mysql://%s:%d/testdb?" +
                                "useSSL=false&" +
                                "allowPublicKeyRetrieval=true&" +
                                "useUnicode=true&" +
                                "characterEncoding=utf8&" +
                                "serverTimezone=UTC",
                        mysql.getHost(),
                        mysql.getFirstMappedPort()
                )
        );
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    }

    private String authToken;

    @BeforeEach
    void setupAuth() {
        // Create test user
        restTemplate.postForEntity("/api/user/register",
                new LoginForm("testuser", "password"),
                UserVo.class);

        // Get authentication token
        ResponseEntity<UserVo> response = restTemplate.postForEntity(
                "/api/user/login",
                new LoginForm("testuser", "password"),
                UserVo.class
        );
        authToken = response.getBody().getToken();
    }

    @Test
    void fullAddressLifecycleTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        // 1. Create address
        Address newAddress = new Address();
        newAddress.setLinkUser("John Doe");
        newAddress.setLinkAddress("123 Main St");
        newAddress.setLinkPhone("555-0100");
        newAddress.setUserId(1L);

        ResponseEntity<R<Void>> createResponse = restTemplate.exchange(
                "/api/address",
                HttpMethod.POST,
                new HttpEntity<>(newAddress, headers),
                new ParameterizedTypeReference<>() {}
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 2. Query all addresses
        ResponseEntity<R<List<Address>>> listResponse = restTemplate.exchange(
                "/api/address",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody().getData()).hasSize(1);

        Long addressId = listResponse.getBody().getData().get(0).getId();

        // 3. Update address
        Address updatedAddress = listResponse.getBody().getData().get(0);
        updatedAddress.setLinkAddress("456 Oak Ave");

        ResponseEntity<R<Void>> updateResponse = restTemplate.exchange(
                "/api/address",
                HttpMethod.PUT,
                new HttpEntity<>(updatedAddress, headers),
                new ParameterizedTypeReference<>() {}
        );
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 4. Verify update
        ResponseEntity<R<Address>> getResponse = restTemplate.exchange(
                "/api/address/" + addressId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );
        assertThat(getResponse.getBody().getData().getLinkAddress())
                .isEqualTo("456 Oak Ave");

        // 5. Delete address
        ResponseEntity<R<Void>> deleteResponse = restTemplate.exchange(
                "/api/address/" + addressId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 6. Verify deletion
        ResponseEntity<R<List<Address>>> finalCheck = restTemplate.exchange(
                "/api/address",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );
        assertThat(finalCheck.getBody().getData()).isEmpty();
    }
}