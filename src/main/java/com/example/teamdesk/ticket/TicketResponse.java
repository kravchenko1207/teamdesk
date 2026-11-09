package com.example.teamdesk.ticket;

public record TicketResponse(
        Long id,
        String title,
        String description,
        TicketStatus status
) {
}
