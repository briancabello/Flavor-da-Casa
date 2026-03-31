package nbcc.resto.controller;

import nbcc.common.service.LoginService;
import nbcc.resto.dto.ReservationDto;
import nbcc.resto.service.EventService;
import nbcc.resto.service.MenuService;
import nbcc.resto.service.ReservationService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.viewmodels.ReservationListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final LoginService loginService;
    private final ReservationService reservationService;
    private final EventService eventService;
    private final SeatingService seatingService;
    private final MenuService menuService;

    public ReservationController(LoginService loginService,
                                 ReservationService reservationService,
                                 EventService eventService,
                                 SeatingService seatingService,
                                 MenuService menuService) {
        this.loginService = loginService;
        this.reservationService = reservationService;
        this.eventService = eventService;
        this.seatingService = seatingService;
        this.menuService = menuService;
    }

    @GetMapping
    public String requestForm(Model model) {
        var eventsResult = eventService.getActive();
        var seatingsResult = seatingService.getAll();
        var menusResult = menuService.getAll();

        if (eventsResult.isError() || seatingsResult.isError()) {
            model.addAttribute("message", "We're having a little trouble loading the booking details right now. Please give it another try.");
            return "error/errorPage";
        }

        model.addAttribute("reservation", new ReservationDto());
        model.addAttribute("events", eventsResult.getValue());
        model.addAttribute("seatings", seatingsResult.getValue());
        if (menusResult.hasValue()) {
            model.addAttribute("menus", menusResult.getValue());
        }
        return "reservation/request";
    }

    @PostMapping
    public String submitRequest(@ModelAttribute("reservation") ReservationDto reservation,
                                BindingResult br,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        var result = reservationService.requestReservation(reservation);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "reservation");
            loadFormData(model);
            return "reservation/request";
        }

        redirectAttributes.addFlashAttribute("uuid", result.getValue().getUuid().toString());
        return "redirect:/reservation/confirmation";
    }

    @GetMapping("/confirmation")
    public String confirmation() {
        return "reservation/confirmation";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String getAll(@RequestParam(required = false) Long eventId,
                         @RequestParam(required = false) String status,
                         Model model) {

        var eventsResult = eventService.getAll();

        if (eventsResult.isError()) {
            model.addAttribute("message", "Unable to retrieve event data at this time.");
            return "error/errorPage";
        }

        var seatingsResult = seatingService.getAll();

        if (seatingsResult.isError()) {
            model.addAttribute("message", "Unable to retrieve seating data at this time.");
            return "error/errorPage";
        }

        var reservationsResult = (eventId != null)
                ? reservationService.getByEvent(eventId)
                : reservationService.getAll();

        if (reservationsResult.isError()) {
            model.addAttribute("message", "Unable to retrieve reservation records at this time.");
            return "error/errorPage";
        }

        var reservations = reservationsResult.getValue();

        if (status != null && !status.isBlank()) {
            reservations = reservations.stream()
                    .filter(r -> status.equalsIgnoreCase(r.getStatus()))
                    .toList();
        }

        var viewModel = new ReservationListViewModel(
                eventsResult.getValue(), reservations,
                seatingsResult.getValue(), loginService.isLoggedIn());

        model.addAttribute("viewModel", viewModel);
        model.addAttribute("selectedEventId", eventId);
        model.addAttribute("selectedStatus", status);
        return "reservation/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        var result = reservationService.get(id);

        if (result.isError() || result.isEmpty()) {
            model.addAttribute("message", "The requested reservation record could not be located.");
            return "error/errorPage";
        }

        var reservation = result.getValue();
        model.addAttribute("reservation", reservation);

        var eventResult = eventService.get(reservation.getEventId());
        if (eventResult.hasValue()) {
            model.addAttribute("event", eventResult.getValue());
        }

        if (reservation.getSeatingId() != null) {
            var seatingResult = seatingService.get(reservation.getSeatingId());
            if (seatingResult.hasValue()) {
                model.addAttribute("seating", seatingResult.getValue());
                model.addAttribute("tables", seatingResult.getValue().getTables());
            }
        }

        return "reservation/details";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id,
                          @RequestParam(required = false) Long tableId,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if (tableId == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "A table assignment is required prior to approval.");
            return "redirect:/reservation/details/" + id;
        }

        var result = reservationService.approve(id, tableId);

        if (result.isError()) {
            model.addAttribute("message", "The system encountered an issue while processing this approval.");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    result.getValidationErrors().iterator().next().getMessage());
            return "redirect:/reservation/details/" + id;
        }

        redirectAttributes.addFlashAttribute("reservationName",
                result.getValue().getGuestFirstName() + " " + result.getValue().getGuestLastName());
        redirectAttributes.addFlashAttribute("action", "approved");
        return "redirect:/reservation/details/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deny/{id}")
    public String deny(@PathVariable Long id,
                       RedirectAttributes redirectAttributes,
                       Model model) {

        var result = reservationService.deny(id);

        if (result.isError()) {
            model.addAttribute("message", "The system encountered an issue while processing this denial.");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    result.getValidationErrors().iterator().next().getMessage());
            return "redirect:/reservation/details/" + id;
        }

        redirectAttributes.addFlashAttribute("reservationName",
                result.getValue().getGuestFirstName() + " " + result.getValue().getGuestLastName());
        redirectAttributes.addFlashAttribute("action", "denied");
        return "redirect:/reservation/details/" + id;
    }

    @GetMapping("/track")
    public String track(@RequestParam(required = false) String uuid, Model model) {
        if (uuid == null || uuid.isBlank()) {
            return "reservation/track";
        }

        try {
            var result = reservationService.getByUuid(UUID.fromString(uuid.trim()));

            if (result.hasValue()) {
                var reservation = result.getValue();
                model.addAttribute("reservation", reservation);

                var eventResult = eventService.get(reservation.getEventId());
                if (eventResult.hasValue()) {
                    model.addAttribute("event", eventResult.getValue());
                }

                if (reservation.getSeatingId() != null) {
                    var seatingResult = seatingService.get(reservation.getSeatingId());
                    if (seatingResult.hasValue()) {
                        model.addAttribute("seating", seatingResult.getValue());
                    }
                }
            } else {
                model.addAttribute("notFound", true);
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("notFound", true);
        }

        return "reservation/track";
    }

    private void loadFormData(Model model) {
        var eventsResult = eventService.getActive();
        var seatingsResult = seatingService.getAll();
        var menusResult = menuService.getAll();

        if (eventsResult.hasValue()) {
            model.addAttribute("events", eventsResult.getValue());
        }

        if (seatingsResult.hasValue()) {
            model.addAttribute("seatings", seatingsResult.getValue());
        }

        if (menusResult.hasValue()) {
            model.addAttribute("menus", menusResult.getValue());
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