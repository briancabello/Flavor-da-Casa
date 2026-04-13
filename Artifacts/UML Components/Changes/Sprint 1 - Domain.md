```mermaid
classDiagram
  namespace OLD_Domain_Draft {
    class DiningTable_Old {
      +long id
      +String name
      +int capacity
      +DateTime createdDate
    }

    class Seating_Old {
      +long id
      +long eventId
      +String name
      +DateTime startDateTime
      +int duration
      +DateTime createdDate
      +Collection~DiningTable~ tables
    }
  }

  namespace NEW_Domain_Final {
    class DiningTable_New {
      -Long id
      -String name
      -Integer capacity
      -LocalDateTime createdDate
    }

    class Seating_New {
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
    }
  }

  DiningTable_Old ..> DiningTable_New
  Seating_Old ..> Seating_New