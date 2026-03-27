package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.common.service.LoginService;
import nbcc.resto.dto.Seating;
import nbcc.resto.service.DiningTableService;
import nbcc.resto.service.EventService;
import nbcc.resto.service.ReservationService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.viewmodels.SeatingListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/seating")
public class SeatingController {

    private static final Logger logger = LoggerFactory.getLogger(SeatingController.class);

    private final LoginService loginService;
    private final SeatingService seatingService;
    private final EventService eventService;
    private final DiningTableService diningTableService;
    private final ReservationService reservationService;

    public SeatingController(
            LoginService loginService,
            SeatingService seatingService,
            EventService eventService,
            DiningTableService diningTableService,
            ReservationService reservationService
    ) {
        this.loginService = loginService;
        this.seatingService = seatingService;
        this.eventService = eventService;
        this.diningTableService = diningTableService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public String getAll(Model model) {
        var eventsResult = eventService.getAll();
        var seatingsResult = seatingService.getAll();

        if (eventsResult.isError() || seatingsResult.isError()) {
            model.addAttribute("message", "Error retrieving seatings");
            return "error/errorPage";
        }

        var events = eventsResult.getValue();
        var seatings = seatingsResult.getValue();

        var viewModel = new SeatingListViewModel(events, seatings, loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);
        return "seating/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        var eventsResult = eventService.getAll();
        var tablesResult = diningTableService.getAll();

        if (eventsResult.isError() || tablesResult.isError()) {
            model.addAttribute("message", "Error loading form data");
            return "error/errorPage";
        }

        model.addAttribute("seating", new Seating());
        model.addAttribute("events", eventsResult.getValue());
        model.addAttribute("tables", tablesResult.getValue());
        return "seating/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("seating") Seating seating,
                         BindingResult br,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        var result = seatingService.create(seating);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "seating");
            loadFormData(model);
            return "seating/create";
        }

        var eventResult = eventService.get(seating.getEventId());
        if (eventResult.hasValue()) {
            redirectAttributes.addFlashAttribute("eventName", eventResult.getValue().getName());
        }

        redirectAttributes.addFlashAttribute("action", "added seating to");

        return "redirect:/seating";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        var seatingResult = seatingService.get(id);

        if (seatingResult.isError() || seatingResult.isEmpty()) {
            model.addAttribute("message", "Seating not found");
            return "error/errorPage";
        }

        var seating = seatingResult.getValue();

        // Pre-populate selectedTableIds from the existing tables
        if (seating.getTables() != null && seating.getSelectedTableIds().isEmpty()) {
            for (var table : seating.getTables()) {
                seating.getSelectedTableIds().add(table.getId());
            }
        }

        model.addAttribute("seating", seating);
        model.addAttribute("isEditing", true);
        loadFormData(model);
        return "seating/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute("seating") Seating seating, BindingResult br, Model model) {
        seating.setId(id);


        var result = seatingService.update(seating);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "seating");
            model.addAttribute("isEditing", true);
            loadFormData(model);
            return "seating/edit";
        }

        return "redirect:/seating";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        var seatingResult = seatingService.get(id);

        if (seatingResult.isError() || seatingResult.isEmpty()) {
            model.addAttribute("message", "Seating not found");
            return "error/errorPage";
        }

        var seating = seatingResult.getValue();
        model.addAttribute("seating", seating);

        var eventResult = eventService.get(seating.getEventId());
        if (eventResult.hasValue()) {
            model.addAttribute("eventName", eventResult.getValue().getName());
        }

        model.addAttribute("isArchive", reservationService.existsBySeatingId(id));

        return "seating/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        var result = seatingService.delete(id);

        if (result.isError()) {
            model.addAttribute("message", "Error deleting seating");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            model.addAttribute("message", "Seating not found");
            return "error/errorPage";
        }

        var seating = result.getValue();
        if (!seating.isStatus()) {
            redirectAttributes.addFlashAttribute("action", "archived seating from");
        } else {
            redirectAttributes.addFlashAttribute("action", "deleted seating from");
        }

        var eventResult = eventService.get(seating.getEventId());
        if (eventResult.hasValue()) {
            redirectAttributes.addFlashAttribute("eventName", eventResult.getValue().getName());
        }

        return "redirect:/seating";
    }

    private void loadFormData(Model model) {
        var eventsResult = eventService.getAll();
        var tablesResult = diningTableService.getAll();
        if (eventsResult.hasValue()) {
            model.addAttribute("events", eventsResult.getValue());
        }
        if (tablesResult.hasValue()) {
            model.addAttribute("tables", tablesResult.getValue());
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request) {
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}
