package nbcc.resto.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.EventDto;
import nbcc.resto.service.EventService;
import nbcc.resto.service.MenuService;
import nbcc.resto.service.SeatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static nbcc.resto.controller.api.result.ResultHandler.handleResult;

@Tag(name = "Event API", description = "Event API Operations")
@RestController
@RequestMapping("/api/event")
public class EventApiController {

    public final EventService eventService;
    private final SeatingService seatingService;
    private final MenuService menuService;

    public EventApiController(EventService eventService, SeatingService seatingService, MenuService menuService) {
        this.eventService = eventService;
        this.seatingService = seatingService;
        this.menuService = menuService;
    }

    @Operation(summary = "Get all Events")
    @GetMapping
    public ResponseEntity<Result<Collection<EventDto>>> getAll() {
        var result = eventService.getAll();
        return handleResult(result, HttpStatus.OK);
    }

    @Operation(summary = "Get Event by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ValidatedResult<EventDto>> get(@PathVariable Long id) {
        var result = eventService.get(id);
        return handleResult(result, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }
}
