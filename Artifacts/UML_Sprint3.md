
```mermaid
classDiagram
  %% Domain Layer: Entities and Value Objects
  namespace Domain {
    class Event {
      +long id
      +Long menuId
      +String name
      +String description
      +DateTime startDate
      +DateTime endDate
      +int duration
      +BigDecimal price
      +boolean active
      +DateTime createdDate
      +DateTime lastUpdatedDate
    }

    class DiningTable {
      +long id
      +String name
      +int capacity
      +DateTime createdDate
    }

    class Seating {
      +long id
      +long eventId
      +boolean status
      +String name
      +DateTime startDateTime
      +int duration
      +DateTime createdDate
      +DateTime updatedDate
      +Collection~DiningTable~ tables
    }

    class Menu {
      +long id
      +String name
      +String description
      +DateTime createdDate
    }

    class MenuItem {
      +long id
      +long menuId
      +String name
      +String description
    }

    class Reservation {
      +long id
      +UUID uuid
      +long eventId
      +long seatingId
      +String guestFirstName
      +String guestLastName
      +String email
      +int groupSize
      +String status
      +Long assignedTableId
    }

    class Result~T~ {
      <<interface>>
      +getValue() T
      +getValidationErrors() Collection~ValidationError~
      +isEmpty() boolean
      +hasValue() boolean
      +hasValidationErrors() boolean
      +isSuccessfull() boolean
      +isError() boolean
    }

    class ValidationError {
      +String field
      +String message
      +Object value
    }
  }

  %% Application Layer: Interfaces for Services and Repositories
  namespace Application {

    class EventService{
      <<interface>>
      +getAll() Result~Collection~Event~~
      +get(long id) Result~Event~
      +create(Event eventDto) Result~Event~
      +update(Event eventDto) Result~Event~
      +delete(long id) Result~Event~
      +search(String query, DateTime start, DateTime end) Result~Collection~Event~~
    }

    class EventValidationService {
      <<interface>>
      +validate(Event eventDto) Collection~ValidationError~
    }

    class DiningTableService {
      <<interface>>
      +getAll() Result~Collection~DiningTable~~
      +get(long id) Result~DiningTable~
      +create(DiningTable table) Result~DiningTable~
      +update(DiningTable table) Result~DiningTable~
      +delete(long id) Result~DiningTable~
    }

    class SeatingService {
      <<interface>>
      +getAll() Result~Collection~Seating~~
      +getByEvent(long eventId) Result~Collection~Seating~~
      +getSeatingsByTableId(long tableId) Result~Collection~Seating~~
      +create(Seating seating) Result~Seating~
      +delete(long id) Result~Seating~
    }

    class MenuService {
      <<interface>>
      +getAll() Result~Collection~Menu~~
      +get(long id) Result~Menu~
      +create(Menu menu) Result~Menu~
      +update(Menu menu) Result~Menu~
      +delete(long id) Result~Menu~
      +search(String query) Result~Collection~Menu~~
    }


    class MenuItemService {
      <<interface>>
      +getByMenu(long menuId) Result~Collection~MenuItem~~
      +create(MenuItem item) Result~MenuItem~
      +update(MenuItem item) Result~MenuItem~
      +delete(long id) Result~MenuItem~
    }


    class ReservationService {
      <<interface>>
      +getByEvent(long eventId) Result~Collection~Reservation~~
      +getConfirmedReservations(long eventId) Result~Collection~Reservation~~
      +getByUuid(UUID uuid) Result~Reservation~
      +requestReservation(Reservation reservation) Result~Reservation~
      +approve(long reservationId, long tableId) Result~Reservation~
      +deny(long reservationId) Result~Reservation~
    }

    class EmailService {
      <<interface>>
      +sendReservationReceived(Reservation reservation) void
      +sendReservationStatusUpdate(Reservation reservation) void
    }

    class DiningTableRepository {
      <<interface>>
      +getAll() Collection~DiningTable~
      +get(long id) Optional~DiningTable~
      +create(DiningTable table) DiningTable
      +update(DiningTable table) DiningTable
      +delete(long id) void
    }

    class SeatingRepository {
      <<interface>>
      +getAll() Collection~Seating~
      +getByEvent(long eventId) Collection~Seating~
      +getSeatingsByTableId(long tableId) Collection~Seating~
      +create(Seating seating) Seating
      +delete(long id) void
      +getOverlappingSeatings(Collection~DiningTable~ tables, DateTime start, DateTime end) Collection~Seating~
    }

    class EventRepository {
      <<interface>>
      +getAll() Collection~Event~
      +get(long id) Optional~Event~
      +create(Event eventDto) Event
      +update(Event eventDto) Event
      +delete(long id) void
      +search(String name, DateTime start, DateTime end) Collection~Event~
      +exists(String name) boolean
    }

    class MenuRepository {
      <<interface>>
      +getAll() Collection~Menu~
      +get(long id) Optional~Menu~
      +create(Menu menu) Menu
      +update(Menu menu) Menu
      +delete(long id) void
      +search(String name) Collection~Menu~
    }

    class MenuItemRepository {
      <<interface>>
      +getByMenu(long menuId) Collection~MenuItem~
      +create(MenuItem item) MenuItem
      +update(MenuItem item) MenuItem
      +delete(long id) void
    }

    class ReservationRepository {
      <<interface>>
      +getByEvent(long eventId) Collection~Reservation~
      +getConfirmedReservations(long eventId) Collection~Reservation~
      +getByUuid(UUID uuid) Optional~Reservation~
      +create(Reservation reservation) Reservation
      +updateStatus(long id, String status, Long tableId) Reservation
    }

  }
  %% Persistence Layer: Database Entities
  namespace Persistence {
    class EventEntity {
      +long id
      +Long menuId
      +String name
      +String description
      +DateTime startDate
      +DateTime endDate
      +int duration
      +BigDecimal price
      +boolean active
      +DateTime createdDate
      +DateTime lastUpdatedDate
    }

    class DiningTableEntity {
      +long id
      +String name
      +int capacity
      +DateTime createdDate
    }

    class SeatingEntity {
      +long id
      +long eventId
      +boolean status
      +String name
      +DateTime startDateTime
      +int duration
      +DateTime createdDate
      +DateTime updatedDate
    }

    class SeatingTableEntity {
      +long id
      +SeatingEntity seating
      +DiningTableEntity diningTable
    }

    class SeatingTableJpaRepository {
      <<interface>>
      +findSeatingsByTableId(long tableId) List~SeatingEntity~
      +findBySeatingId(long seatingId) List~SeatingTableEntity~
      +deleteByDiningTableId(long diningTableId) void
    }

    class MenuEntity {
      +long id
      +String name
      +String description
      +DateTime createdDate
    }

    class MenuItemEntity {
      +long id
      +long menuId
      +String name
      +String description
    }


    class ReservationEntity {
      +long id
      +UUID uuid
      +long eventId
      +long seatingId
      +String guestFirstName
      +String guestLastName
      +String email
      +int groupSize
      +String status
      +Long assignedTableId
    }
  }

  %% Web/API Layer: Controllers (Sprint 3 Rest Api)
  namespace Web_API {
    class EventApiController {
      +getEvents() Result~Collection~EventDTO~~
      +getEvent(long eventId) Result~EventDTO~
    }

    class MenuApiController {
      +getMenus() Result~Collection~MenuDTO~~
      +getMenu(long menuId) Result~MenuDTO~
    }

    class ReservationApiController {
      +requestReservation(long eventId, long seatingId, String firstName, String lastName, String emaill, int groupSize) Result~ReservationDTO~
      +getConfirmedReservations(long eventId) Result~Collection~ReservationDTO~~
    }

  }

  %% Relationships
  EventService ..> Event : Uses
  DiningTableService ..> DiningTable : Uses
  SeatingService ..> Seating : Uses
  EventRepository ..> Event : Uses
  EventValidationService ..> Event : Validates

  MenuService ..> Menu : Uses
  MenuItemService ..> MenuItem : Uses
  MenuRepository ..> Menu : Uses
  MenuItemRepository ..> MenuItem : Uses

  ReservationService ..> Reservation : Uses
  ReservationRepository ..> Reservation : Uses
  EmailService ..> Reservation : Uses
  ReservationApiController ..> ReservationService : Uses
  EventApiController ..> EventService : Uses
  MenuApiController ..> MenuService : Uses

  Seating --> DiningTable : Has available
  Seating --> Event : Belongs to
  SeatingTableEntity --> SeatingEntity : ManyToOne
  SeatingTableEntity --> DiningTableEntity : ManyToOne
  MenuItem --> Menu : Belongs to
  Reservation --> Seating : Requests

  ```
