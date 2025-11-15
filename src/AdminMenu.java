import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;
import model.FreeRoom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getInstance();

    public static void displayAdminMenu(Scanner scanner) {
        boolean keepAdminRunning = true;

        while (keepAdminRunning) {
            try {
                printAdminMenu();
                int selection = Integer.parseInt(scanner.nextLine());

                switch (selection) {
                    case 1:
                        seeAllCustomers();
                        break;
                    case 2:
                        seeAllRooms();
                        break;
                    case 3:
                        seeAllReservations();
                        break;
                    case 4:
                        addARoom(scanner);
                        break;
                    case 5:
                        populateTestData();
                        break;
                    case 6:
                        keepAdminRunning = false;
                        break;
                    default:
                        System.out.println("Invalid selection. Please choose 1-6.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error: Please enter a valid number (1-6).");
            }
        }
    }

    private static void printAdminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Populate with Test Data");
        System.out.println("6. Back to Main Menu");
        System.out.print("Please select an option: ");
    }

    private static void populateTestData() {
        adminResource.createTestData();
        System.out.println("Test data populated successfully!");
    }

    private static void addARoom(Scanner scanner) {
        List<IRoom> newRooms = new ArrayList<>();
        boolean addAnotherRoom = true;

        while (addAnotherRoom) {

            String roomNumber = getValidRoomNumber(scanner);
            double price = getValidRoomPrice(scanner);
            RoomType roomType = getValidRoomType(scanner);

            IRoom room;
            if (price == 0.0) {
                room = new FreeRoom(roomNumber, roomType);
            } else {
                room = new Room(roomNumber, price, roomType);
            }

            newRooms.add(room);
            System.out.println("Room added successfully!");

            System.out.print("Add another room? (y/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("y")) {
                addAnotherRoom = false;
            }
        }

        if (!newRooms.isEmpty()) {
            adminResource.addRoom(newRooms);
        }
    }

    private static String getValidRoomNumber(Scanner scanner) {
        while (true) {
            System.out.print("Enter Room Number: ");
            String roomNumber = scanner.nextLine();

            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                System.out.println("Error: Room number cannot be empty.");
            } else if (adminResource.getRoom(roomNumber) != null) {
                System.out.println("Error: A room with this number already exists.");
            } else {
                return roomNumber;
            }
        }
    }

    private static double getValidRoomPrice(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter Price per night: ");
                double price = Double.parseDouble(scanner.nextLine());
                if (price < 0) {
                    System.out.println("Error: Price cannot be negative.");
                } else {
                    return price;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error: Invalid number format for price.");
            }
        }
    }

    private static RoomType getValidRoomType(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter Room Type (1 for SINGLE, 2 for DOUBLE): ");
                int typeInput = Integer.parseInt(scanner.nextLine());
                if (typeInput == 1) {
                    return RoomType.SINGLE;
                } else if (typeInput == 2) {
                    return RoomType.DOUBLE;
                } else {
                    System.out.println("Error: Invalid selection. Please enter 1 or 2.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error: Invalid input. Please enter 1 or 2.");
            }
        }
    }

    private static void seeAllReservations() {
        System.out.println("\n--- All Reservations ---");
        adminResource.displayAllReservations();
    }

    private static void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms in the system.");
            return;
        }
        System.out.println("\n--- All Rooms ---");
        for (IRoom room : rooms) {
            System.out.println(room);
        }
    }

    private static void seeAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers in the system.");
            return;
        }
        System.out.println("\n--- All Customers ---");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }
}