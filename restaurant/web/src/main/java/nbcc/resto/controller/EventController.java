package nbcc.resto.controller;


import nbcc.common.service.LoginService;
import nbcc.resto.dto.Event;
import nbcc.resto.service.EventService;
import nbcc.resto.viewmodels.EventListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
//@PreAuthorize("isAuthenticated()") commented until fix login/register
@RequestMapping("/event")
public class EventController {

    private final Logger logger = LoggerFactory.getLogger(EventController.class);
    private final LoginService loginService;
    private final EventService eventService;

    public EventController(LoginService loginService, EventService eventService) {
        this.loginService = loginService;
        this.eventService = eventService;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("event", new Event());
        return "event/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("event") Event event, BindingResult br) {
        var result = eventService.create(event);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "event");
            return "event/create";
        }


        return "redirect:/";
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public String getAll(@RequestParam(required = false) String name,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model){
        var result = eventService.search(name, startDate, endDate);

        if(result.isError()){
            model.addAttribute("message", "Error retrieving events");
            return "error/errorPage";
        }

        EventListViewModel viewModel = new EventListViewModel(result.getValue(), loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);

        model.addAttribute("searchName", name);
        model.addAttribute("searchStartDate", startDate);
        model.addAttribute("searchEndDate", endDate);
        return "event/list";
    }



}


