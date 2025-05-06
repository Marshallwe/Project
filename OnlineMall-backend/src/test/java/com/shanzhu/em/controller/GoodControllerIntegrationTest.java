package com.shanzhu.em.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Good;
import com.shanzhu.em.entity.Standard;
import com.shanzhu.em.entity.vo.GoodVo;
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

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
public class GoodControllerIntegrationTest {

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

    private Long createdGoodId;

    @BeforeEach
    void setup() {
        // Create initial test product
        Good testGood = new Good();
        testGood.setName("Test Product");
        testGood.setPrice(new BigDecimal("99.99"));

        ResponseEntity<R<Long>> response = restTemplate.exchange(
                "/api/good",
                HttpMethod.POST,
                new HttpEntity<>(testGood),
                new ParameterizedTypeReference<R<Long>>() {}
        );

        createdGoodId = response.getBody().getData();
    }

    @Test
    void shouldPerformFullProductLifecycle() {
        // Test retrieve product by ID
        ResponseEntity<R<Good>> getResponse = restTemplate.exchange(
                "/api/good/" + createdGoodId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<R<Good>>() {}
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getData().getName()).isEqualTo("Test Product");

        // Test product update
        Good updatedGood = getResponse.getBody().getData();
        updatedGood.setDescription("Updated Description");

        ResponseEntity<R<Void>> updateResponse = restTemplate.exchange(
                "/api/good",
                HttpMethod.PUT,
                new HttpEntity<>(updatedGood),
                new ParameterizedTypeReference<R<Void>>() {}
        );
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify update
        ResponseEntity<R<Good>> verifyResponse = restTemplate.exchange(
                "/api/good/" + createdGoodId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<R<Good>>() {}
        );
        assertThat(verifyResponse.getBody().getData().getDescription())
                .isEqualTo("Updated Description");

        // Test product deletion
        ResponseEntity<R<Void>> deleteResponse = restTemplate.exchange(
                "/api/good/" + createdGoodId,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<R<Void>>() {}
        );
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify deletion
        ResponseEntity<R<Good>> deletedResponse = restTemplate.exchange(
                "/api/good/" + createdGoodId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<R<Good>>() {}
        );
        assertThat(deletedResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldManageProductVariants() {
        // Create product variants
        Standard variant1 = new Standard();
        variant1.setName("Size");
        variant1.setValue("Large");
        variant1.setPrice(new BigDecimal("29.99"));
        variant1.setStore(100);

        Standard variant2 = new Standard();
        variant2.setName("Color");
        variant2.setValue("Red");
        variant2.setPrice(new BigDecimal("35.99"));
        variant2.setStore(50);

        ResponseEntity<R<Void>> saveResponse = restTemplate.exchange(
                "/api/good/standard?goodId=" + createdGoodId,
                HttpMethod.POST,
                new HttpEntity<>(List.of(variant1, variant2)),
                new ParameterizedTypeReference<R<Void>>() {}
        );
        assertThat(saveResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify variant creation
        ResponseEntity<R<String>> variantResponse = restTemplate.exchange(
                "/api/good/standard/" + createdGoodId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<R<String>>() {}
        );
        assertThat(variantResponse.getBody().getData())
                .contains("Size", "Color", "29.99", "35.99");

        // Test variant removal
        Standard deleteVariant = new Standard();
        deleteVariant.setGoodId(createdGoodId.intValue());
        deleteVariant.setName("Color");

        ResponseEntity<R<Void>> deleteResponse = restTemplate.exchange(
                "/api/good/standard",
                HttpMethod.DELETE,
                new HttpEntity<>(deleteVariant),
                new ParameterizedTypeReference<R<Void>>() {}
        );
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldPaginateProducts() {
        // Generate test data
        for (int i = 0; i < 15; i++) {
            Good product = new Good();
            product.setName("Product " + i);
            product.setPrice(new BigDecimal(i * 100));

            restTemplate.exchange(
                    "/api/good",
                    HttpMethod.POST,
                    new HttpEntity<>(product),
                    new ParameterizedTypeReference<R<Long>>() {}
            );
        }

        // Test pagination functionality
        ResponseEntity<R<IPage<GoodVo>>> pageResponse = restTemplate.exchange(
                "/api/good/page?pageNum=2&pageSize=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<R<IPage<GoodVo>>>() {}
        );

        assertThat(pageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(pageResponse.getBody().getData().getCurrent()).isEqualTo(2);
        assertThat(pageResponse.getBody().getData().getSize()).isEqualTo(10);
    }

    @Test
    void shouldHandleFeaturedProducts() {
        // Mark product as featured
        ResponseEntity<R<Void>> featureResponse = restTemplate.exchange(
                "/api/good/recommend?id=" + createdGoodId + "&isRecommend=true",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<R<Void>>() {}
        );
        assertThat(featureResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify featured status
        ResponseEntity<R<Good>> productResponse = restTemplate.exchange(
                "/api/good/" + createdGoodId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<R<Good>>() {}
        );
        assertThat(productResponse.getBody().getData().getRecommend()).isTrue();
    }

    @Test
    void shouldRetrieveSalesLeaderboard() {
        ResponseEntity<R<List<Good>>> leaderboardResponse = restTemplate.exchange(
                "/api/good/rank?num=5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<R<List<Good>>>() {}
        );
        assertThat(leaderboardResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(leaderboardResponse.getBody().getData()).hasSizeLessThanOrEqualTo(5);
    }
}