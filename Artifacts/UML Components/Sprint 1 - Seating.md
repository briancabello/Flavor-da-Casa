```mermaid
classDiagram
  namespace Domain_Layer {
    class DiningTable {
      -Long id
      -String name
      -Integer capacity
      -LocalDateTime createdDate
    }

    class Seating {
      -Long id
      -Long eventId
      -String name
      -LocalDateTime startDateTime
      -Integer duration
      -LocalDateTime createdDate
      -Collection~DiningTable~ tables
      -Set~Long~ selectedTableIds
      -boolean status
      -LocalDateTime updatedDate
      +getEndDateTime() LocalDateTime
    }
    
    class ValidationError {
      +String field
      +String message
    }
  }

  namespace Application {
    class DiningTableService { <<interface>>
      +getAll() Result~Collection~DiningTable~~
      +get(Long id) ValidatedResult~DiningTable~
      +create(DiningTable table) ValidatedResult~DiningTable~
      +update(DiningTable table) ValidatedResult~DiningTable~
      +delete(Long id) ValidatedResult~Void~
    }

    class SeatingService { <<interface>>
      +getAll() Result~Collection~Seating~~
      +getByEvent(Long eventId) Result~Collection~Seating~~
      +getSeatingsByTableId(Long tableId) Result~Collection~Seating~~
      +create(Seating seating) ValidatedResult~Seating~
      +get(Long id) Result~Seating~
      +update(Seating seating) ValidatedResult~Seating~
      +delete(Long id) ValidatedResult~Seating~
    }

    class DiningTableValidationService {
      +validate(DiningTable table) Collection~ValidationError~
      -validateCapacity(DiningTable table) Collection~ValidationError~
    }

    class SeatingValidationService {
      +validate(Seating seating) Collection~ValidationError~
      -validateTableSelection(Seating seating) Collection~ValidationError~
      -validateTableOverlap(Seating seating) Collection~ValidationError~
    }

    class DiningTableRepository { <<interface>>
      +getAll() Collection~DiningTable~
      +get(Long id) Optional~DiningTable~
      +create(DiningTable table) DiningTable
      +update(DiningTable table) DiningTable
      +delete(Long id) void
      +exists(String name) boolean
    }

    class SeatingRepository { <<interface>>
      +getAll() Collection~Seating~
      +getByEvent(Long eventId) Collection~Seating~
      +getSeatingsByTableId(Long tableId) Collection~Seating~
      +create(Seating seating) Seating
      +get(Long id) Optional~Seating~
      +update(Seating seating) Seating
      +delete(Long id) void
      +exists(String name, Long eventId) boolean
    }
  }

  namespace Persistence {
    class DiningTableEntity {
      -long id
      -String name
      -int capacity
      -LocalDateTime createdDate
    }

    class SeatingEntity {
      -long id
      -long eventId
      -boolean status
      -String name
      -LocalDateTime startDateTime
      -int duration
      -LocalDateTime createdDate
      -LocalDateTime updatedDate
    }

    class SeatingTableEntity {
      -long id
      -SeatingEntity seating
      -DiningTableEntity diningTable
    }
  }

  %% Relationships
  DiningTableService ..> DiningTable
  SeatingService ..> Seating
  DiningTableValidationService ..> DiningTable
  SeatingValidationService ..> Seating
  SeatingValidationService ..> SeatingRepository : Uses to check overlaps
  
  DiningTableRepository ..> DiningTable
  SeatingRepository ..> Seating
  
  Seating --> DiningTable : tables (Collection)
  SeatingTableEntity --> SeatingEntity : ManyToOne
  SeatingTableEntity --> DiningTableEntity : ManyToOne