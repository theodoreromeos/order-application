package com.theodore.logging.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theodore.logging.entities.LogDetails;
import com.theodore.logging.models.SaveLogDetailsRequest;
import com.theodore.logging.repositories.LogDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoggingControllerIT {

    @ServiceConnection
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LogDetailsRepository logDetailsRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        logDetailsRepository.deleteAll();
    }

    @Test
    void shouldCreateLogDetailsSuccessfully() throws Exception {
        // given
        var logDetail = new SaveLogDetailsRequest.LogDetailsDto();
        logDetail.setOrderId(100L);
        logDetail.setAmount(199.99);
        logDetail.setItemsCount(2);
        logDetail.setDateTime(LocalDateTime.now());

        SaveLogDetailsRequest request = new SaveLogDetailsRequest(List.of(logDetail));

        // when
        mockMvc.perform(post("/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("success"));

        // then
        List<LogDetails> saved = logDetailsRepository.findAll();
        assertThat(saved).hasSize(1);
        assertThat(saved.getFirst().getOrderId()).isEqualTo(100L);
    }

}
