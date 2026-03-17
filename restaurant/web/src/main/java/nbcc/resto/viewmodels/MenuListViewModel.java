package nbcc.resto.viewmodels;

import nbcc.resto.dto.Menu;

import java.util.Collection;

public class MenuListViewModel {

    private final Collection<Menu> menus;

    private final boolean canAdd;

    private String message;

    public MenuListViewModel(Collection<Menu> menus, boolean canManage) {
        this.menus = menus;
        this.canAdd = canManage;
        this.message = message;
    }

    public Collection<Menu> getMenus() {
        return menus;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty() {
        return menus.isEmpty();
    }
}
