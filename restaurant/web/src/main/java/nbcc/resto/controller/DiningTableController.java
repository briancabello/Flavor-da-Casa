package nbcc.resto.controller;

import nbcc.common.service.LoginService;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.service.DiningTableService;
import nbcc.resto.viewmodels.DiningTableListViewModel;
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
@RequestMapping("/table")
public class DiningTableController {

    private final Logger logger = LoggerFactory.getLogger(DiningTableController.class);
    private final LoginService loginService;
    private final DiningTableService tableService;

    public DiningTableController(LoginService loginService, DiningTableService tableService) {
        this.loginService = loginService;
        this.tableService = tableService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public String getAll(Model model) {
        var result = tableService.getAll();

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving dining tables");
            return "error/errorPage";
        }

        DiningTableListViewModel viewModel = new DiningTableListViewModel(result.getValue(), loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);
        return "table/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("diningTable", new DiningTable());
        return "table/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("diningTable") DiningTable diningTable, BindingResult br) {
        var result = tableService.create(diningTable);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "table/create";
        }

        return "redirect:/table";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var result = tableService.get(id);

        if (result.isError() || result.isEmpty()) {
            model.addAttribute("message", "Dining table not found");
            return "error/errorPage";
        }

        model.addAttribute("diningTable", result.getValue());
        return "table/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @ModelAttribute("diningTable") DiningTable diningTable,
                       BindingResult br) {
        diningTable.setId(id);
        var result = tableService.update(diningTable);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "table/edit";
        }

        return "redirect:/table";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirm(@PathVariable Long id, Model model) {
        var result = tableService.get(id);

        if (result.isError() || result.isEmpty()) {
            model.addAttribute("message", "Dining table not found");
            return "error/errorPage";
        }

        model.addAttribute("diningTable", result.getValue());
        return "table/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        var result = tableService.delete(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/table";
    }

//    private void loadModel(DiningTable diningTable, Model model) {
//        loadModel(diningTable, model, false);
//    }
//
//    private void loadModel(DiningTable diningTable, Model model, boolean showManage) {
//
//    }
}
