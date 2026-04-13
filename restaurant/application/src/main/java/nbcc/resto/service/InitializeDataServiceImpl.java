package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.*;
import nbcc.resto.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class InitializeDataServiceImpl implements InitializeDataService {

    private final Logger logger = LoggerFactory.getLogger(InitializeDataServiceImpl.class);

    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;
    private final EventRepository eventRepository;
    private final DiningTableRepository diningTableRepository;
    private final SeatingRepository seatingRepository;

    public InitializeDataServiceImpl(MenuRepository menuRepository,
                                     MenuItemRepository menuItemRepository,
                                     EventRepository eventRepository,
                                     DiningTableRepository diningTableRepository,
                                     SeatingRepository seatingRepository) {
        this.menuRepository = menuRepository;
        this.menuItemRepository = menuItemRepository;
        this.eventRepository = eventRepository;
        this.diningTableRepository = diningTableRepository;
        this.seatingRepository = seatingRepository;
    }

    @Override
    public Result<Void> initialize() {
        try {
            initializeMenus();
            initializeMenuItem();
            initializeDiningTable();
            initializeEvents();
            initializeSeatings();

            return ValidationResults.success();
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }

    // MENUS
    private void initializeMenus() {
        for (var menu : getDefaultMenus()) {
            if (!menuRepository.exists(menu.getName())) {
                var created = menuRepository.create(menu);
                menu.setId(created.getId());
                logger.info("Menu '{}' created", menu.getName());
            }
        }
    }

    // MENU ITEMS
    private void initializeMenuItem() {
        for (var item : getDefaultMenuItems()) {
            if (!menuItemRepository.exists(item.getName(), item.getMenu().getId())) {
                var created = menuItemRepository.create(item);
                item.setId(created.getId());
                logger.info("MenuItem '{}' created", item.getName());
            }
        }
    }

    // DINING TABLES
    private void initializeDiningTable() {
        for (var table : getDefaultDiningTables()) {
            if (!diningTableRepository.exists(table.getName())) {
                var created = diningTableRepository.create(table);
                table.setId(created.getId());
                logger.info("DiningTable '{}' created", table.getName());
            }
        }
    }

    // EVENTS
    private void initializeEvents() {
        for (var event : getDefaultEvents()) {
            if (!eventRepository.exists(event.getName())) {
                var created = eventRepository.create(event);
                event.setId(created.getId());
                logger.info("Event '{}' created", event.getName());
            }
        }
    }

    // SEATINGS
    private void initializeSeatings() {
        for (var seating : getDefaultSeatings()) {
            if (!seatingRepository.exists(seating.getName(), seating.getEventId())) {
                var created = seatingRepository.create(seating);
                seating.setId(created.getId());
                logger.info("Seating '{}' created", seating.getName());
            }
        }
    }

    // MENU DATA
    private Menu menuChefTasting;

    private Menu getMenuChefTasting() {
        if (menuChefTasting == null)
            menuChefTasting = new Menu()
                    .setName("Chef's Tasting Menu")
                    .setDescription("A 5-course seasonal tasting menu by Chef Juliette.");
        return menuChefTasting;
    }

    private List<Menu> getDefaultMenus() {
        return List.of(getMenuChefTasting());
    }

    private MenuItem menuItemAmuseBouche;

    private MenuItem getMenuItemAmuseBouche() {
        if (menuItemAmuseBouche == null)
            menuItemAmuseBouche = new MenuItem()
                    .setMenu(getMenuChefTasting())
                    .setName("Amuse-Bouche")
                    .setDescription("Smoked salmon tartare on rye crisp.");
        return menuItemAmuseBouche;
    }

    private MenuItem menuItemBisque;

    private MenuItem getMenuItemBisque() {
        if (menuItemBisque == null)
            menuItemBisque = new MenuItem()
                    .setMenu(getMenuChefTasting())
                    .setName("Bisque")
                    .setDescription("Lobster bisque with crème fraîche.");
        return menuItemBisque;
    }

    private MenuItem menuItemMain;

    private MenuItem getMenuItemMain() {
        if (menuItemMain == null)
            menuItemMain = new MenuItem()
                    .setMenu(getMenuChefTasting())
                    .setName("Main")
                    .setDescription("Pan-seared duck breast, cherry jus.");
        return menuItemMain;
    }

    private List<MenuItem> getDefaultMenuItems() {
        return List.of(getMenuItemAmuseBouche(), getMenuItemBisque(), getMenuItemMain());
    }

    // DINING TABLES DATA
    private DiningTable diningTableT01;

    private DiningTable getDiningTableT01() {
        if (diningTableT01 == null)
            diningTableT01 = new DiningTable().setName("T-01").setCapacity(2);
        return diningTableT01;
    }

    private DiningTable diningTableT02;

    private DiningTable getDiningTableT02() {
        if (diningTableT02 == null)
            diningTableT02 = new DiningTable().setName("T-02").setCapacity(4);
        return diningTableT02;
    }

    private DiningTable diningTableT03;

    private DiningTable getDiningTableT03() {
        if (diningTableT03 == null)
            diningTableT03 = new DiningTable().setName("T-03").setCapacity(6);
        return diningTableT03;
    }

    private List<DiningTable> getDefaultDiningTables() {
        return List.of(getDiningTableT01(), getDiningTableT02(), getDiningTableT03());
    }

    // EVENT DATA
    private EventDto eventSpringPopUp;

    private EventDto getEventSpringPopUp() {
        if (eventSpringPopUp == null)
            eventSpringPopUp = new EventDto()
                    .setName("Spring Pop-Up Dinner")
                    .setDescription("One-night-only spring tasting event.")
                    .setStartDate(LocalDate.now().plusDays(7))
                    .setEndDate(LocalDate.now().plusDays(7))
                    .setDuration(240)
                    .setPrice(new BigDecimal("125.00"))
                    .setActive(true)
                    .setArchived(false)
                    .setMenu(getMenuChefTasting());
        return eventSpringPopUp;
    }

    private List<EventDto> getDefaultEvents() {
        return List.of(getEventSpringPopUp());
    }

    // SEATING DATA
    private Seating seatingFirstSeating;

    private Seating getSeatingFirstSeating() {
        if (seatingFirstSeating == null)
            seatingFirstSeating = new Seating()
                    .setName("First Seating")
                    .setEventId(getEventSpringPopUp().getId())
                    .setStartDateTime(LocalDateTime.now().plusDays(7).withHour(18).withMinute(0).withSecond(0))
                    .setDuration(120)
                    .setStatus(true)
                    .setSelectedTableIds(Set.of(
                            getDiningTableT01().getId(),
                            getDiningTableT02().getId(),
                            getDiningTableT03().getId()
                    ));
        return seatingFirstSeating;
    }

    private List<Seating> getDefaultSeatings() {
        return List.of(getSeatingFirstSeating());
    }
}
