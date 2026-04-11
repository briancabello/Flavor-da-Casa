package nbcc.resto.controller.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.ReservationDto;
import nbcc.resto.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static nbcc.resto.controller.api.result.ResultHandler.handleResult;

@Tag(name = "Reservation API", description = "Reservation API Operations")
@RestController
@RequestMapping("/api/reservation")
public class ReservationApiController {
    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Request a reservation")
    @PostMapping("/request")
    public ResponseEntity<ValidatedResult<ReservationDto>> requestReservation(@RequestBody ReservationDto reservation){
        var result = reservationService.requestReservation(reservation);
        return handleResult(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Get approved reservations for an event")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/event/{eventId}/confirmed")
    public ResponseEntity<Result<Collection<ReservationDto>>> getConfirmedByEvent(@PathVariable Long eventId) {
        var result = reservationService.getConfirmedByEvent(eventId);
        return handleResult(result, HttpStatus.OK);
    }
}
