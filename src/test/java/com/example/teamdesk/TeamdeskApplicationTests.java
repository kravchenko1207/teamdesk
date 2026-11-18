package com.example.teamdesk;

import com.example.teamdesk.ticket.TicketResponse;
import com.example.teamdesk.ticket.TicketStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TeamdeskApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:17")
                    .withDatabaseName("teamdesk_test")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateTicketAndRetrieveItById() throws Exception {
        String requestBody = """
                {
                "title": "Cannot connect to VPN",
                "description": "VPN connection fails when working from home."
                }
                """;

        String createResponseBody = mockMvc.perform(
                post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TicketResponse createdTicket = jsonMapper.readValue(
                createResponseBody,
                TicketResponse.class
        );

        assertThat(createdTicket.id()).isNotNull().isPositive();
        assertThat(createdTicket.title())
                .isEqualTo("Cannot connect to VPN");
        assertThat(createdTicket.description());
        assertThat(createdTicket.status()).isEqualTo(TicketStatus.OPEN);
        String getResponseBody = mockMvc.perform(
                get("/api/tickets/{id}", createdTicket.id())
        )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TicketResponse retrievedTicket = jsonMapper.readValue(
                getResponseBody,
                TicketResponse.class
        );

        assertThat(retrievedTicket).isEqualTo(createdTicket);
    }
}
