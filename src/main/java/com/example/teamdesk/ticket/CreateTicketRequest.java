package com.example.teamdesk.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(

        @NotBlank
        @Size(max = 150)
        String title,

        @NotBlank
        @Size(max = 5000)
        String description
) {
}
