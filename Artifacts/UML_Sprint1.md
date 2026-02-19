
```mermaid
classDiagram
  %% Domain Layer: Entities and Value Objects
  namespace Domain {
    class Event {
      +long id
      +String name
      +String description
      +DateTime startDate
      +DateTime endDate
      +int duration
      +double price
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
      +Collection~DiningTable~ tables
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
      +create(Event event) Result~Event~
      +update(Event event) Result~Event~
      +delete(long id) Result~Event~
      +search(String query, DateTime start, DateTime end) Result~Collection~Event~~
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
      +create(Seating seating) Result~Seating~
      +delete(long id) Result~Seating~
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
      +create(Seating seating) Seating
      +delete(long id) void
      +getOverlappingSeatings(Collection~DiningTable~ tables, DateTime start, DateTime end) Collection~Seating~
    }

    class EventRepository {
      <<interface>>
      +getAll() Collection~Event~
      +get(long id) Optional~Event~
      +create(Event event) Event
      +update(Event event) Event
      +delete(long id) void
      +search(String name, DateTime start, DateTime end) Collection~Event~
      +exists(String name) boolean
    }

    class EventService {
      <<interface>>
      +getAll() Result~Collection~Event~~
      +get(long id) Result~Event~
      +create(Event event) Result~Event~
      +update(Event event) Result~Event~
      +delete(long id) Result~Event~
      +search(String query, DateTime start, DateTime end) Result~Collection~Event~~
    }

    class EventValidationService {
      <<interface>>
      +validate(Event event) Collection~ValidationError~
    }
  }
  %% Persistence Layer: Database Entities
  namespace Persistence {
    class EventEntity {
      +long id
      +String name
      +String description
      +DateTime startDate
      +DateTime endDate
      +int duration
      +double price
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
      +String name
      +DateTime startDateTime
      +int duration
      +DateTime createdDate
    }
  }

  %% Relationships
  EventService ..> Event : Uses
  DiningTableService ..> DiningTable : Uses
  SeatingService ..> Seating : Uses
  EventRepository ..> Event : Uses
  EventValidationService ..> Event : Validates

  Seating --> DiningTable : Has available
  Seating --> Event : Belongs to
