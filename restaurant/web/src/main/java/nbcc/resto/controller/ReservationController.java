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
        var eventsResult = eventService.getAll();
        var seatingsResult = seatingService.getAll();
        var menusResult = menuService.getAll();

        if (eventsResult.isError() || seatingsResult.isError()) {
            model.addAttribute("message", "Error loading reservation data");
            return "error/errorPage";
        }

        model.addAttribute("reservation", new ReservationDto());
        model.addAttribute("events", eventsResult.getValue());
        model.addAttribute("seatings", seatingsResult.getValue());
        if (menusResult.hasValue()) {
            model.addAttribute("menu", menusResult.getValue());
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
            model.addAttribute("message", "Error loading events");
            return "error/errorPage";
        }

        var reservationsResult = (eventId != null)
                ? reservationService.getByEvent(eventId)
                : reservationService.getAll();

        if (reservationsResult.isError()) {
            model.addAttribute("message", "Error loading reservations");
            return "error/errorPage";
        }

        var reservations = reservationsResult.getValue();

        if (status != null && !status.isBlank()) {
            reservations = reservations.stream()
                    .filter(r -> status.equalsIgnoreCase(r.getStatus()))
                    .toList();
        }

        var viewModel = new ReservationListViewModel(
                eventsResult.getValue(), reservations, loginService.isLoggedIn());

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
            model.addAttribute("message", "Reservation not found");
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
            }
        }

        return "reservation/details";
    }

    private void loadFormData(Model model) {
        var eventsResult = eventService.getAll();
        var seatingsResult = seatingService.getAll();
        var menusResult = menuService.getAll();
        if (eventsResult.hasValue()) {
            model.addAttribute("events", eventsResult.getValue());
        }
        if (seatingsResult.hasValue()) {
            model.addAttribute("seatings", seatingsResult.getValue());
        }
        if (menusResult.hasValue()) {
            model.addAttribute("menu", menusResult.getValue());
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
