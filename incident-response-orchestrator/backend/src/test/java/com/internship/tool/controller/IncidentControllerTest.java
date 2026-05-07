package com.internship.tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.entity.Incident;
import com.internship.tool.repository.IncidentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IncidentRepository repository;

    private Incident incident;

    @BeforeEach
    void setup() {

        repository.deleteAll();

        incident = new Incident();

        incident.setTitle("Server Down");
        incident.setDescription("API issue");
        incident.setStatus("OPEN");
        incident.setPriority("HIGH");
        incident.setAssignedTo("admin1");

        repository.save(incident);
    }

    // GET ALL INCIDENTS

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllIncidents() throws Exception {

        mockMvc.perform(
                get("/incidents?page=0&size=2")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").exists());
    }

    // CREATE INCIDENT

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateIncident() throws Exception {

        Incident newIncident = new Incident();

        newIncident.setTitle("Database Error");
        newIncident.setDescription("DB issue");
        newIncident.setStatus("OPEN");
        newIncident.setPriority("MEDIUM");
        newIncident.setAssignedTo("admin1");

        mockMvc.perform(
                post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(newIncident)
                        )
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title")
                .value("Database Error"));
    }

    // UPDATE INCIDENT

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateIncident() throws Exception {

        incident.setTitle("Updated Incident");

        mockMvc.perform(
                put("/incidents/" + incident.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(incident)
                        )
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title")
                .value("Updated Incident"));
    }

    // DELETE INCIDENT

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteIncident() throws Exception {

        mockMvc.perform(
                delete("/incidents/" + incident.getId())
        )
        .andExpect(status().isOk());
    }

    // EXPORT CSV

    @Test
    @WithMockUser(roles = "ADMIN")
    void testExportCsv() throws Exception {

        mockMvc.perform(
                get("/incidents/export")
        )
        .andExpect(status().isOk())
        .andExpect(content().string(
                containsString("Title")
        ));
    }
}