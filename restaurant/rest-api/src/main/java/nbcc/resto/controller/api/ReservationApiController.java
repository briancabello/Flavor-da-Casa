package nbcc.resto.controller.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.ReservationDto;
import nbcc.resto.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
