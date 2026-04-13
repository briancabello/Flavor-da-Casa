```mermaid
classDiagram
  namespace Domain {
    class EventDto {
      -Long id
      -Menu menu
      -String name
      -String description
      -LocalDate startDate
      -LocalDate endDate
      -Integer duration
      -BigDecimal price
      -boolean active
      -boolean archived
      -LocalDateTime createdDate
      -LocalDateTime lastUpdatedDate
      +getMenuId() Long
    }

    class Menu {
      -Long id
      -String name
      -String description
      -LocalDateTime createdDate
    }

    class MenuItem {
      -Long id
      -Menu menu
      -String name
      -String description
      -LocalDateTime createdDate
    }
  }

  namespace Application {
    class EventService { <<interface>>
      +getAll() Result~Collection~EventDto~~
      +getActive() Result~Collection~EventDto~~
      +get(Long id) ValidatedResult~EventDto~
      +create(EventDto eventDto) ValidatedResult~EventDto~
      +update(EventDto eventDto) ValidatedResult~EventDto~
      +delete(Long id) ValidatedResult~EventDto~
      +search(String query, LocalDate start, LocalDate end) ValidatedResult~Collection~EventDto~~
    }

    class MenuService { <<interface>>
      +getAll() Result~Collection~Menu~~
      +get(Long id) ValidatedResult~Menu~
      +create(Menu menu) ValidatedResult~Menu~
      +update(Menu menu) ValidatedResult~Menu~
      +delete(Long id) ValidatedResult~Void~
      +searchByName(String name) Result~Collection~Menu~~
    }

    class MenuItemService { <<interface>>
      +getAll() Result~Collection~MenuItem~~
      +getByMenuId(Long menuId) Result~Collection~MenuItem~~
      +get(Long id) ValidatedResult~MenuItem~
      +create(MenuItem menuItem) ValidatedResult~MenuItem~
      +update(MenuItem menuItem) ValidatedResult~MenuItem~
      +delete(Long id) ValidatedResult~Void~
    }

    class EventValidationService { <<interface>>
      +validate(EventDto eventDto) Collection~ValidationError~
    }

    class MenuValidationService {
      +validate(Menu menu) Collection~ValidationError~
    }

    class MenuItemValidationService {
      +validate(MenuItem menuItem) Collection~ValidationError~
    }

    class EventRepository { <<interface>>
      +getAll() Collection~EventDto~
      +get(Long id) Optional~EventDto~
      +create(EventDto eventDto) EventDto
      +update(EventDto eventDto) EventDto
      +delete(Long id) void
      +search(String name, LocalDateTime start, LocalDateTime end) Collection~EventDto~
      +exists(String name) boolean
    }

    class MenuRepository { <<interface>>
      +getAll() Collection~Menu~
      +get(Long id) Optional~Menu~
      +create(Menu menu) Menu
      +update(Menu menu) Menu
      +delete(Long id) void
      +searchByName(String name) Collection~Menu~
      +exists(String name) boolean
    }

    class MenuItemRepository { <<interface>>
      +getAll() Collection~MenuItem~
      +getByMenuId(Long menuId) Collection~MenuItem~
      +get(Long id) Optional~MenuItem~
      +create(MenuItem menuItem) MenuItem
      +update(MenuItem menuItem) MenuItem
      +delete(Long id) void
      +exists(String name, Long menuId) boolean
    }
  }

  namespace Persistence {
    class EventEntity {
      -long id
      -Long menuId
      -String name
      -String description
      -LocalDate startDate
      -LocalDate endDate
      -int duration
      -BigDecimal price
      -boolean active
      -boolean archived
      -LocalDateTime createdDate
      -LocalDateTime lastUpdatedDate
    }
    class MenuEntity {
      -long id
      -String name
      -String description
      -LocalDateTime createdDate
    }
    class MenuItemEntity {
      -long id
      -long menuId
      -String name
      -String description
      -LocalDateTime createdDate
    }
  }

  EventService ..> EventDto
  MenuService ..> Menu
  MenuItemService ..> MenuItem
  
  EventValidationService ..> EventDto
  MenuValidationService ..> Menu
  MenuItemValidationService ..> MenuItem

  EventRepository ..> EventDto
  MenuRepository ..> Menu
  MenuItemRepository ..> MenuItem

  %% Relationships
  EventEntity --> "1" MenuEntity : uses
  MenuEntity --> "1..*" MenuItemEntity : contains
  
  EventDto --> Menu : Holds full Menu object
  MenuItem --> Menu : Holds full Menu object