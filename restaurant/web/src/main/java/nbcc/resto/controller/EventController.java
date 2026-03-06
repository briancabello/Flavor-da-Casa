package nbcc.resto.controller;


import nbcc.common.service.LoginService;
import nbcc.resto.dto.Event;
import nbcc.resto.service.EventService;
import nbcc.resto.viewmodels.EventListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/events")
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

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        var result = eventService.get(id);

        if(result.isError() || result.isEmpty()){
            model.addAttribute("message", "Event not found");
            return "error/errorPage";
        }

        model.addAttribute("event", result.getValue());
        return "event/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute("event") Event event, BindingResult br){
        event.setId(id);

        var result = eventService.update(event);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "event");
            return "event/edit";
        }

        return "redirect:/events";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirm(@PathVariable Long id, Model model){
        var result = eventService.get(id);

        if(result.isError() || result.isEmpty()){
            model.addAttribute("message", "Event not found");
            return "error/errorPage";
        }

        model.addAttribute("event", result.getValue());
        return "event/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        var result = eventService.delete(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/events";
    }




    @PreAuthorize("permitAll()")
    @GetMapping
    public String getAll(Model model){
        var result = eventService.getAll();

        if(result.isError()){
            model.addAttribute("message", "Error retrieving events");
            return "error/errorPage";
        }

        EventListViewModel viewModel = new EventListViewModel(result.getValue(), loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);
        return "event/list";
    }



}


