package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.resto.dto.MenuItem;
import nbcc.resto.service.MenuItemService;
import nbcc.resto.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/menu/{menuId}/item")
public class MenuItemController {

    private static final Logger logger = LoggerFactory.getLogger(MenuItemController.class);

    private final MenuItemService menuItemService;
    private final MenuService menuService;

    public MenuItemController(MenuItemService menuItemService, MenuService menuService) {
        this.menuItemService = menuItemService;
        this.menuService = menuService;
    }

    @PostMapping("/create")
    public String create(@PathVariable Long menuId,
                         @ModelAttribute("menuItem") MenuItem menuItem,
                         BindingResult br,
                         Model model) {

        logger.debug("Attempting to create menu item for menu {}: {}", menuId, menuItem);

        var menuResult = menuService.get(menuId);
        if (menuResult.isError() || menuResult.isEmpty()) {
            model.addAttribute("message", "There was a problem trying to retrieve the menu you are trying to add an item to.");
            return "error/errorPage";
        }

        menuItem.setMenu(menuResult.getValue());

        // Create the MenuItem
        var result = menuItemService.create(menuItem);

        if (result.isError()) {
            model.addAttribute("message", "There was a problem trying to create the menu item");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return loadEditPage(menuId, model);
        }

        return "redirect:/menu/edit/" + menuId;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long menuId, @PathVariable Long id, Model model) {

        var result = menuItemService.get(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The menu item you are trying to edit was not found");
            return "error/errorPage";
        }

        model.addAttribute("menuItem", result.getValue());
        model.addAttribute("menuId", menuId);
        return "menu/item/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long menuId,
                       @PathVariable Long id,
                       @ModelAttribute("menuItem") MenuItem menuItem,
                       BindingResult br,
                       Model model) {

        menuItem.setId(id);

        var menuResult = menuService.get(menuId);
        if (menuResult.isError() || menuResult.isEmpty()) {
            model.addAttribute("message", "There was a problem trying to retrieve the menu you are trying to update an item for.");
            return "error/errorPage";
        }

        menuItem.setMenu(menuResult.getValue());

        var result = menuItemService.update(menuItem);

        if (result.isError()) {
            model.addAttribute("message", "There was a problem trying to update the menu item");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            model.addAttribute("menuId", menuId);
            return "menu/item/edit";
        }

        return "redirect:/menu/edit/" + menuId;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long menuId, @PathVariable Long id, Model model) {

        var result = menuItemService.get(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The menu item you are trying to delete was not found");
            return "error/errorPage";
        }

        model.addAttribute("menuItem", result.getValue());
        model.addAttribute("menuId", menuId);
        return "menu/item/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long menuId, @PathVariable Long id) {

        var result = menuItemService.delete(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/menu/edit/" + menuId;
    }

    private String loadEditPage(Long menuId, Model model) {
        var menuResult = menuService.get(menuId);

        if (menuResult.isError() || menuResult.isEmpty()) {
            model.addAttribute("message", "Menu not found");
            return "error/errorPage";
        }

        model.addAttribute("menu", menuResult.getValue());

        var itemsResult = menuItemService.getByMenuId(menuId);

        model.addAttribute("menuItems", itemsResult.isError()
                ? List.of()
                : itemsResult.getValue());

        if (!model.containsAttribute("menuItem")) {
            model.addAttribute("menuItem", new MenuItem());
        }

        return "menu/edit";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request) {
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}
