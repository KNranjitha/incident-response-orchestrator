package com.internship.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.entity.Incident;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.*;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
public class IntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>("redis:7")
                    .withExposedPorts(6379);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFullCrudFlow() throws Exception {

        // CREATE INCIDENT

        Incident incident = new Incident();

        incident.setTitle("Server Down");
        incident.setDescription("API Failure");
        incident.setStatus("OPEN");
        incident.setPriority("HIGH");
        incident.setAssignedTo("admin1");

        ResponseEntity<String> createResponse =
                restTemplate.postForEntity(
                        "/incidents",
                        incident,
                        String.class
                );

        assertEquals(
                HttpStatus.OK,
                createResponse.getStatusCode()
        );

        Incident createdIncident =
                objectMapper.readValue(
                        createResponse.getBody(),
                        Incident.class
                );

        assertNotNull(createdIncident.getId());

        // GET INCIDENTS

        ResponseEntity<String> getResponse =
                restTemplate.getForEntity(
                        "/incidents?page=0&size=2",
                        String.class
                );

        assertEquals(
                HttpStatus.OK,
                getResponse.getStatusCode()
        );

        // UPDATE INCIDENT

        createdIncident.setTitle("Updated Incident");

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(
                        objectMapper.writeValueAsString(createdIncident),
                        headers
                );

        ResponseEntity<String> updateResponse =
                restTemplate.exchange(
                        "/incidents/" + createdIncident.getId(),
                        HttpMethod.PUT,
                        request,
                        String.class
                );

        assertEquals(
                HttpStatus.OK,
                updateResponse.getStatusCode()
        );

        // DELETE INCIDENT

        ResponseEntity<String> deleteResponse =
                restTemplate.exchange(
                        "/incidents/" + createdIncident.getId(),
                        HttpMethod.DELETE,
                        null,
                        String.class
                );

        assertEquals(
                HttpStatus.OK,
                deleteResponse.getStatusCode()
        );
    }
}