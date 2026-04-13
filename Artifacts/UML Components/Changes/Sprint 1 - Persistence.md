```mermaid
classDiagram

%% ================= OLD =================
namespace OLD {
  class DiningTableEntity_Old {
    +long id
    +String name
    +int capacity
    +DateTime createdDate
  }

  class SeatingEntity_Old {
    +long id
    +long eventId
    +String name
    +DateTime startDateTime
    +int duration
    +DateTime createdDate
  }
}

%% ================= NEW =================
namespace NEW {
  class DiningTableEntity_New {
    -long id
    -String name
    -int capacity
    -LocalDateTime createdDate
  }

  class SeatingEntity_New {
    -long id
    -long eventId
    -boolean status
    -String name
    -LocalDateTime startDateTime
    -int duration
    -LocalDateTime createdDate
    -LocalDateTime updatedDate
  }

  class SeatingTableEntity_New {
    -long id
    -SeatingEntity_New seating
    -DiningTableEntity_New diningTable
  }
}

%% ================= RELATION (CHANGE INDICATOR) =================
DiningTableEntity_Old ..> DiningTableEntity_New 
SeatingEntity_Old ..> SeatingEntity_New 
SeatingEntity_New --> SeatingTableEntity_New 
DiningTableEntity_New --> SeatingTableEntity_New 