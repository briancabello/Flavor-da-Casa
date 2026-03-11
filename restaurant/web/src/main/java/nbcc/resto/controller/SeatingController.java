package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.common.service.LoginService;
import nbcc.resto.dto.Seating;
import nbcc.resto.service.DiningTableService;
import nbcc.resto.service.EventService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.viewmodels.SeatingListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller

@RequestMapping("/seating")
public class SeatingController {

    private static final Logger logger = LoggerFactory.getLogger(SeatingController.class);

    private final LoginService loginService;
    private final SeatingService seatingService;
    private final EventService eventService;
    private final DiningTableService diningTableService;

    public SeatingController(
            LoginService loginService,
            SeatingService seatingService,
            EventService eventService,
            DiningTableService diningTableService
    ) {
        this.loginService = loginService;
        this.seatingService = seatingService;
        this.eventService = eventService;
        this.diningTableService = diningTableService;
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
                         Model model) {
        var result = seatingService.create(seating);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            loadFormData(model);
            return "seating/create";
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
