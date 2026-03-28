package nbcc.common.viewmodel;

public class NavViewModel {

    private final String username;

    private final boolean showLoginLink;
    private final boolean showLogoutButton;
    private final boolean showRegisterLink;
    private final boolean showUserName;

    private final boolean showEvents;
    private final boolean showTables;
    private final boolean showSeatings;
    private final boolean showReservations;
    private final boolean showMenus;

    public NavViewModel(boolean isLoggedIn, String username) {

        this.username = username;
        this.showUserName = username != null && !username.isBlank();

        this.showLoginLink = !isLoggedIn;
        this.showLogoutButton = isLoggedIn;
        this.showRegisterLink = !isLoggedIn;

        this.showEvents = isLoggedIn;
        this.showTables = isLoggedIn;
        this.showSeatings = isLoggedIn;
        this.showMenus = isLoggedIn;
        this.showReservations = true;
    }

    public boolean isShowUserName() {
        return showUserName;
    }

    public String getUsername() {
        return username;
    }

    public boolean isShowLoginLink() {
        return showLoginLink;
    }

    public boolean isShowLogoutButton() {
        return showLogoutButton;
    }

    public boolean isShowRegisterLink() {
        return showRegisterLink;
    }

    public boolean isShowEvents() {
        return showEvents;
    }

    public boolean isShowTables() {
        return showTables;
    }

    public boolean isShowSeating() {
        return showSeatings;
    }

    public boolean isShowReservations() {
        return showReservations;
    }
    public boolean isShowMenus() {
        return showMenus;
    }
}
