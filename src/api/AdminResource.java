package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import model.FreeRoom;
import model.RoomType;
import service.CustomerService;
import service.ReservationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class AdminResource {

    private static final AdminResource INSTANCE = new AdminResource();
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private AdminResource() {}

    public static AdminResource getInstance() {
        return INSTANCE;
    }

    // ADD THIS METHOD:
    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms) {
        for (IRoom room : rooms) {
            reservationService.addRoom(room);
        }
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void displayAllReservations() {
        reservationService.printAllReservation();
    }

    public void createTestData() {
        customerService.addCustomer("john@domain.com", "John", "Doe");
        customerService.addCustomer("jane@domain.com", "Jane", "Smith");
        customerService.addCustomer("guest@domain.com", "Test", "Guest");

        List<IRoom> rooms = new ArrayList<>();
        rooms.add(new Room("101", 150.0, RoomType.SINGLE));
        rooms.add(new Room("102", 200.0, RoomType.DOUBLE));
        rooms.add(new FreeRoom("103", RoomType.SINGLE));
        rooms.add(new Room("104", 250.0, RoomType.DOUBLE));
        this.addRoom(rooms);

        try {
            SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yyyy");
            Date checkIn = parser.parse("11/10/2025");
            Date checkOut = parser.parse("11/15/2025");

            Customer customer = customerService.getCustomer("john@domain.com");
            IRoom room = reservationService.getARoom("101");

            reservationService.reserveARoom(customer, room, checkIn, checkOut);
        } catch (ParseException ex) {
            System.out.println("Error parsing test data dates.");
        }
    }
}
