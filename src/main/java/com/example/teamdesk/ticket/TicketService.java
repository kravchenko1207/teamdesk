package com.example.teamdesk.ticket;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TicketService {

  private final TicketRepository ticketRepository;

  public TicketService(TicketRepository ticketRepository) {
    this.ticketRepository = ticketRepository;
  }

  public List<TicketResponse> getTickets() {
      return ticketRepository.findAll(Sort.by("id"))
              .stream()
              .map(this::toResponse)
              .toList();
    }

    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request) {
        Ticket ticket = new Ticket(
                request.title(),
                request.description()
        );

        Ticket savedTicket = ticketRepository.save(ticket);

        return toResponse(savedTicket);
    }

    public TicketResponse getTicketById(Long id) {
      Ticket ticket = ticketRepository.findById(id)
              .orElseThrow(() -> new TicketNotFoundException(id));

      return toResponse(ticket);
    }

    public TicketResponse toResponse(Ticket ticket) {
      return new TicketResponse(
              ticket.getId(),
              ticket.getTitle(),
              ticket.getDescription(),
              ticket.getStatus()
      );
    }
}
