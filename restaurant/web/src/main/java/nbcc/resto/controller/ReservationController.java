package nbcc.resto.controller;

import nbcc.common.service.LoginService;
import nbcc.resto.dto.ReservationDto;
import nbcc.resto.service.EventService;
import nbcc.resto.service.MenuService;
import nbcc.resto.service.ReservationService;
import nbcc.resto.service.SeatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
