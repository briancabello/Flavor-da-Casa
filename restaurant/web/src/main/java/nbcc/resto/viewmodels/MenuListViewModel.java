package nbcc.resto.viewmodels;

import nbcc.resto.dto.Menu;
import nbcc.resto.dto.MenuItem;

import java.util.Collection;

public class MenuListViewModel {

    private final Collection<Menu> menus;
    private final Collection<MenuItem> menuItems;

    private final boolean canAdd;
    private final boolean canEdit;
    private final boolean canDelete;

    private final String searchTerm;
    private String message;

    public MenuListViewModel(Collection<Menu> menus, boolean canManage) {
        this(menus, null, canManage, null);
    }

    public MenuListViewModel(Collection<Menu> menus,
                             Collection<MenuItem> menuItems,
                             boolean canManage,
                             String searchTerm) {
        this.menus = menus;
        this.menuItems = menuItems;
        this.canAdd = canManage;
        this.canEdit = canManage;
        this.canDelete = canManage;
        this.searchTerm = searchTerm;
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


    public int getItemCount(Long menuId) {
        if (menuItems == null || menuId == null) {
            return 0;
        }

        int count = 0;

        for (MenuItem item : menuItems) {
            if (item.getMenu() != null && item.getMenu().getId().equals(menuId)) {
                count++;
            }
        }

        return count;
    }
}
