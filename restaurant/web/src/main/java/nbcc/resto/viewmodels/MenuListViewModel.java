package nbcc.resto.viewmodels;

import nbcc.resto.dto.Menu;

import java.util.Collection;

public class MenuListViewModel {

    private final Collection<Menu> menus;

    private final boolean canAdd;
    private final boolean canEdit;
    private final boolean canDelete;

    private final String searchTerm;
    private String message;

    public MenuListViewModel(Collection<Menu> menus, boolean canManage) {
        this(menus, canManage, null);
    }

    public MenuListViewModel(Collection<Menu> menus, boolean canManage, String searchTerm) {
        this.menus = menus;
        this.canAdd = canManage;
        this.canEdit = canManage;
        this.canDelete = canManage;
        this.searchTerm = searchTerm;
        this.message = message;
    }

    public Collection<Menu> getMenus() {
        return menus;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty() {
        return menus.isEmpty();
    }

    public boolean isSearching() {
        return searchTerm != null && !searchTerm.isBlank();
    }
}
