package nbcc.resto.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.MenuItem;
import nbcc.resto.service.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static nbcc.resto.controller.api.result.ResultHandler.handleResult;

@Tag(name = "Menu Item API", description = "Menu Item API Operations")
@RestController
@RequestMapping("/api/menuItem")
public class MenuItemApiController {

    private final MenuItemService menuItemService;

    public MenuItemApiController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @Operation(summary = "Get all menu items for a specific menu")
    @GetMapping("/menu/{menuId}")
    public ResponseEntity<Result<Collection<MenuItem>>> getByMenuId(@PathVariable Long menuId) {
        var result = menuItemService.getByMenuId(menuId);
        return handleResult(result, HttpStatus.OK);
    }

    @Operation(summary = "Get a menu item by id")
    @GetMapping("/{id}")
    public ResponseEntity<ValidatedResult<MenuItem>> get(@PathVariable Long id) {
        var result = menuItemService.get(id);
        return handleResult(result, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }
}
