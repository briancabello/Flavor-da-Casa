package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.common.service.LoginService;
import nbcc.resto.dto.Menu;
import nbcc.resto.service.MenuService;
import nbcc.resto.viewmodels.DiningTableListViewModel;
import nbcc.resto.viewmodels.MenuListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/menu")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    private final LoginService loginService;
    private final MenuService menuService;

    public MenuController(LoginService loginService, MenuService menuService) {
        this.loginService = loginService;
        this.menuService = menuService;
    }

    @GetMapping
    public String getAll(Model model) {
        var result = menuService.getAll();

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving menus");
            return "error/errorPage";
        }

        MenuListViewModel viewModel = new MenuListViewModel(result.getValue(), loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);
        return "menu/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("menu", new Menu());
        return "menu/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("menu") Menu menu, BindingResult br) {
        logger.debug("Attempting to create menu: {}", menu);

        var result = menuService.create(menu);

        if (result.isError()) {
            return "error/errorPage";
        }
        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "menu/create";
        }

        return "redirect:/menu";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        var result = menuService.get(id);

        if (result.isError()) {
            return "error/errorPage";
        }
        if (result.isEmpty()) {
            model.addAttribute("message", "The menu you are looking for was not found");
            return "error/errorPage";
        }

        model.addAttribute("menu", result.getValue());
        return "menu/details";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request) {
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}
