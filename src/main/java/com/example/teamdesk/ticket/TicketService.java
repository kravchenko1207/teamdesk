package com.example.teamdesk.ticket;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    private final List<TicketResponse> tickets = new ArrayList<>();
    private long nextId = 1;

    public synchronized List<TicketResponse> getTickets() {
        return List.copyOf(tickets);
    }

    public synchronized TicketResponse createTicket(
            CreateTicketRequest request
    ) {
        TicketResponse ticket = new TicketResponse(
                nextId++,
                request.title(),
                request.description(),
                TicketStatus.OPEN
        );

        tickets.add(ticket);

        return ticket;
    }
}
