```mermaid
classDiagram
  namespace Domain {
    class ReservationDto {
      -long id
      -UUID uuid
      -Long eventId
      -Long seatingId
      -String guestFirstName
      -String guestLastName
      -String email
      -Integer groupSize
      -ReservationStatus status
      -Long assignedTableId
    }

    class ReservationStatus { <<enumeration>> }
  }

  namespace Application {
    class ReservationService { <<interface>>
      +getAll() Result~Collection~ReservationDto~~
      +get(long id) ValidatedResult~ReservationDto~
      +getByEvent(long eventId) Result~Collection~ReservationDto~~
      +getByUuid(UUID uuid) ValidatedResult~ReservationDto~
      +requestReservation(ReservationDto reservation) ValidatedResult~ReservationDto~
      +approve(long reservationId, long tableId) ValidatedResult~ReservationDto~
      +deny(long reservationId) ValidatedResult~ReservationDto~
      +getConfirmedByEvent(long eventId) Result~Collection~ReservationDto~~
      +existsBySeatingId(long seatingId) boolean
      +existsByEventId(long eventId) boolean
    }

    class ReservationEmailService { <<interface>>
      +sendReservationReceived(ReservationDto reservation) void
      +sendReservationStatusUpdate(ReservationDto reservation) void
    }

    class ReservationValidationService {
      +validate(ReservationDto reservation) Collection~ValidationError~
      -validateEventExists(ReservationDto reservation) Collection~ValidationError~
      -validateSeatingExists(ReservationDto reservation) Collection~ValidationError~
    }

    class ReservationRepository { <<interface>>
      +getAll() Collection~ReservationDto~
      +get(long id) Optional~ReservationDto~
      +getByEvent(long eventId) Collection~ReservationDto~
      +getByUuid(UUID uuid) Optional~ReservationDto~
      +create(ReservationDto reservation) ReservationDto
      +updateStatus(long id, ReservationStatus status, Long tableId) ReservationDto
      +getConfirmedByEvent(long eventId) Collection~ReservationDto~
      +existsBySeatingId(long seatingId) boolean
      +existsByEventId(long eventId) boolean
      +isTableAssignedForSeating(long seatingId, long tableId) boolean
    }

    class EventRepository { <<interface>> }
    
    class SeatingRepository { <<interface>> }
  }

  namespace Persistence_Layer {
    class ReservationEntity {
      -long id
      -UUID uuid
      -long eventId
      -long seatingId
      -String guestFirstName
      -String guestLastName
      -String email
      -int groupSize
      -String status
      -Long assignedTableId
    }
  }

  %% Relationships
  ReservationService ..> ReservationDto
  ReservationValidationService ..> ReservationDto
  ReservationRepository ..> ReservationDto
  
  ReservationValidationService ..> EventRepository : Checks existence
  ReservationValidationService ..> SeatingRepository : Checks existence
  
  ReservationDto --> ReservationStatus : Enum
  ReservationService ..> ReservationEmailService : Delegates Email Delivery