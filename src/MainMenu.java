import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {

    private static final HotelResource hotelResource = HotelResource.getInstance();

    public static void start() {
        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            try {
                displayMainMenu();
                int selection = Integer.parseInt(scanner.nextLine());

                switch (selection) {
                    case 1:
                        findAndReserveRoom(scanner);
                        break;
                    case 2:
                        seeMyReservations(scanner);
                        break;
                    case 3:
                        createAccount(scanner);
                        break;
                    case 4:
                        AdminMenu.displayAdminMenu(scanner);
                        break;
                    case 5:
                        System.out.println("Exiting application. Goodbye!");
                        keepRunning = false;
                        break;
                    default:
                        System.out.println("Invalid selection. Please choose 1-5.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error: Please enter a valid number (1-5).");
            } catch (Exception ex) {
                System.out.println("An unexpected error occurred: " + ex.getLocalizedMessage());
            }
        }
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.print("Please select an option: ");
    }

    private static void createAccount(Scanner scanner) {
        System.out.print("Enter Email (e.g., name@domain.com): ");
        String email = scanner.nextLine();
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error creating account: " + ex.getLocalizedMessage());
        }
    }

    private static void seeMyReservations(Scanner scanner) {
        System.out.print("Enter your Customer Email: ");
        String email = scanner.nextLine();

        Customer customer = hotelResource.getCustomer(email);
        if (customer == null) {
            System.out.println("Error: No account found for this email.");
            return;
        }

        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("You have no reservations.");
        } else {
            System.out.println("\n--- Your Reservations ---");
            for (Reservation res : reservations) {
                System.out.println(res + "\n");
            }
        }
    }

    private static int getRoomPreference(Scanner scanner) {
        System.out.println("What type of room would you like?");
        System.out.println("1. Any");
        System.out.println("2. Paid");
        System.out.println("3. Free");
        System.out.print("Please select an option (1-3): ");

        while (true) {
            try {
                int selection = Integer.parseInt(scanner.nextLine());
                if (selection >= 1 && selection <= 3) {
                    if (selection == 1) return 0;
                    if (selection == 2) return 1;
                    if (selection == 3) return 2;
                }
            } catch (NumberFormatException ex) {
            }
            System.out.print("Invalid selection. Please choose 1-3: ");
        }
    }

    private static void findAndReserveRoom(Scanner scanner) {
        Date checkIn = getDateInput(scanner, "Enter Check-In Date (mm/dd/yyyy):", null);
        Date checkOut = getDateInput(scanner, "Enter Check-Out Date (mm/dd/yyyy):", checkIn);

        int preference = getRoomPreference(scanner);

        Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut, preference);

        if (availableRooms.isEmpty()) {
            System.out.println("Sorry, no rooms are available for those dates.");

            System.out.print("No rooms available. How many days later would you like to search? ");
            int daysToSearch = 7;
            try {
                daysToSearch = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number. Defaulting to 7 days.");
            }

            Collection<IRoom> recommendedRooms = hotelResource.findRecommendedRooms(checkIn, checkOut, preference, daysToSearch);

            if (recommendedRooms.isEmpty()) {
                System.out.println("No recommended rooms found for " + daysToSearch + " days after.");
                return;
            } else {
                System.out.println("We found rooms on recommended dates (" + daysToSearch + " days later):");
                checkIn = addDaysToDate(checkIn, daysToSearch);
                checkOut = addDaysToDate(checkOut, daysToSearch);
                System.out.println("New Check-In: " + checkIn);
                System.out.println("New Check-Out: " + checkOut);

                availableRooms = recommendedRooms;
            }
        }

        System.out.println("Available Rooms:");
        for (IRoom room : availableRooms) {
            System.out.println(room);
        }

        System.out.print("Would you like to book a room? (y/n): ");
        if (!scanner.nextLine().equalsIgnoreCase("y")) {
            return;
        }

        System.out.print("Enter your Customer Email: ");
        String email = scanner.nextLine();
        Customer customer = hotelResource.getCustomer(email);

        if (customer == null) {
            System.out.println("Error: No account found. Please create an account first.");
            return;
        }

        System.out.print("Enter the room number you'd like to book: ");
        String roomNumber = scanner.nextLine();
        IRoom roomToBook = hotelResource.getRoom(roomNumber);

        if (roomToBook == null || !availableRooms.contains(roomToBook)) {
            System.out.println("Error: Invalid room number or room is not available.");
            return;
        }

        try {
            Reservation reservation = hotelResource.bookARoom(email, roomToBook, checkIn, checkOut);
            System.out.println("Reservation successful!");
            System.out.println(reservation);
        } catch (Exception ex) {
            System.out.println("Error booking room: " + ex.getLocalizedMessage());
        }
    }

    private static Date getDateInput(Scanner scanner, String prompt, Date minDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);
        Date date = null;

        Date today = null;
        try {
            today = dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
        }

        while (date == null) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                date = dateFormat.parse(line);

                if (minDate == null) {
                    if (date.before(today)) {
                        System.out.println("Error: Date cannot be in the past.");
                        date = null;
                    }
                } else {
                    if (date.before(minDate) || date.equals(minDate)) {
                        System.out.println("Error: Check-out date must be after the check-in date.");
                        date = null;
                    }
                }
            } catch (ParseException ex) {
                System.out.println("Error: Invalid date format. Please use mm/dd/yyyy.");
            }
        }
        return date;
    }

    private static Date addDaysToDate(Date date, int days) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(java.util.Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }
}