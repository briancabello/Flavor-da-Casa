package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.common.service.LoginService;
import nbcc.resto.dto.Menu;
import nbcc.resto.dto.MenuItem;
import nbcc.resto.service.MenuItemService;
import nbcc.resto.service.MenuService;
import nbcc.resto.viewmodels.MenuListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/menu")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    private final LoginService loginService;
    private final MenuService menuService;
    private final MenuItemService menuItemService;

    public MenuController(LoginService loginService, MenuService menuService, MenuItemService menuItemService) {
        this.loginService = loginService;
        this.menuService = menuService;
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String getAll(@RequestParam(required = false) String search, Model model) {

        Collection<Menu> menus;
        MenuListViewModel viewModel;

        if (search != null && !search.isBlank()) {
            var result = menuService.searchByName(search);

            if (result.isError()) {
                model.addAttribute("message", "Error searching menus");
                return "error/errorPage";
            }

            menus = result.getValue();
            viewModel = new MenuListViewModel(menus, loginService.isLoggedIn(), search);
        } else {
            var result = menuService.getAll();

            if (result.isError()) {
                model.addAttribute("message", "Error retrieving menus");
                return "error/errorPage";
            }

            menus = result.getValue();
            viewModel = new MenuListViewModel(menus, loginService.isLoggedIn());
        }

        // Fetch Menu Item (counts)
        Map<Long, Integer> itemCounts = menus.stream()
                .collect(Collectors.toMap(
                        Menu::getId,
                        menu -> {
                            var itemsResult = menuItemService.getByMenuId(menu.getId());
                            return (!itemsResult.isError() && itemsResult.getValue() != null)
                                    ? itemsResult.getValue().size()
                                    : 0;
                        }
                ));

        model.addAttribute("viewModel", viewModel);
        model.addAttribute("itemCounts", itemCounts);
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

        // Fetch Menu Items
        var itemsResult = menuItemService.getByMenuId(id);
        model.addAttribute("menuItems", itemsResult.isError()
                ? List.of()
                : itemsResult.getValue());

        return "menu/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var result = menuService.get(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The menu you are trying to edit was not found");
            return "error/errorPage";
        }

        model.addAttribute("menu", result.getValue());

        // Load menu items for display on edit page
        var itemsResult = menuItemService.getByMenuId(id);
        model.addAttribute("menuItems", itemsResult.isError()
                ? List.of()
                : itemsResult.getValue());

        // Blank menu item for the add form
        model.addAttribute("menuItem", new MenuItem());

        return "menu/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @ModelAttribute("menu") Menu menu,
                       BindingResult br,
                       Model model) {
        menu.setId(id);
        var result = menuService.update(menu);

        if (result.isError()) {
            return "error/errorPage";
        }
        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "menu");

            // Reload Menu Items
            var itemsResult = menuItemService.getByMenuId(id);
            model.addAttribute("menuItems", itemsResult.isError()
                    ? List.of()
                    : itemsResult.getValue());
            model.addAttribute("menuItem", new MenuItem());

            return "menu/edit";
        }

        return "redirect:/menu";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        var result = menuService.get(id);

        if (result.isError()) {
            return "error/errorPage";
        }
        if (result.isEmpty()) {
            model.addAttribute("message", "The menu you are trying to delete was not found");
            return "error/errorPage";
        }

        model.addAttribute("menu", result.getValue());

        // Fetch Menu Items
        var itemsResult = menuItemService.getByMenuId(id);
        model.addAttribute("menuItems", itemsResult.isError()
                ? List.of()
                : itemsResult.getValue());

        return "menu/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        var result = menuService.delete(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/menu";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request) {
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}
