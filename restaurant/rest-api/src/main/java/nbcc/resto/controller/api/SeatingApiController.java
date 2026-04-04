package nbcc.resto.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.resto.dto.Seating;
import nbcc.resto.service.SeatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static nbcc.resto.controller.api.result.ResultHandler.handleResult;

@Tag(name = "Seating API", description = "Seating API Operations")
@RestController
@RequestMapping("/api/seating")
public class SeatingApiController {

    private final SeatingService seatingService;

    public SeatingApiController(SeatingService seatingService) {
        this.seatingService = seatingService;
    }

    @Operation(summary = "Get all seatings for a specific event")
    @GetMapping("/event/{eventId}")
    public ResponseEntity<Result<Collection<Seating>>> getByEvent(@PathVariable Long eventId) {
        var result = seatingService.getByEvent(eventId);
        return handleResult(result, HttpStatus.OK);
    }

    @Operation(summary = "Get a seating by id")
    @GetMapping("/{id}")
    public ResponseEntity<Result<Seating>> get(@PathVariable Long id) {
        var result = seatingService.get(id);
        return handleResult(result, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }
}
