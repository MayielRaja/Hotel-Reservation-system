# Hotel Reservation System (Java CLI)

### Description

This is a comprehensive, command-line (CLI) application for managing hotel reservations, built entirely in Java. The project simulates a real-world booking system, providing distinct interfaces and functionalities for both customers and hotel administrators.

### Key Features

**For Customers:**
* **Create Account:** Register as a new customer with regex-based email validation.
* **Find & Reserve Rooms:** Search for available rooms using valid check-in and check-out dates. The system enforces all date logic (e.g., check-out must be after check-in, no past dates).
* **Advanced Room Search:** Filter room searches by "Paid," "Free," or "Any" room type.
* **Smart Recommendations:** If no rooms are available, the system automatically prompts the user to search again on recommended dates (e.g., "search 7 days later," with the number of days being user-selectable).
* **View Reservations:** See a complete list of all personal reservations.

**For Administrators:**
* **Admin Dashboard:** Access a separate admin menu with privileged actions.
* **System-Wide Views:** See complete lists of *all* customers, *all* rooms, and *all* reservations in the system.
* **Add Rooms:** Add new rooms (both paid and free) with robust validation that prevents:
    * Duplicate room numbers
    * Negative prices
    * Empty room numbers
    * Invalid room type (Enum) inputs
* **Populate Test Data:** A dedicated menu option to instantly populate the application with sample customers, rooms, and reservations for easy testing.

### Technical Design
* **Architecture:** Built using a three-tier architecture (UI, API/Resource, Service) to separate concerns and logic.
* **OOP Principles:** Fully utilizes Object-Oriented principles, including **Encapsulation** (private model classes), **Inheritance** (`FreeRoom` extends `Room`), and **Polymorphism** (`IRoom` interface).
* **Design Patterns:** Uses the **Singleton pattern** for all service and resource classes (`CustomerService`, `ReservationService`, etc.) to ensure a single, shared, in-memory data source.
* **Data Handling:** Manages all application data in-memory using Java Collections (Maps and Lists).
* **Robustness:** Features comprehensive exception handling for all user input (`Scanner` and `try-catch`) to prevent runtime crashes and validate all data.

### How to Run
1.  Compile all `.java` files from the `src` directory.
2.  Run the `HotelApplication` class.
