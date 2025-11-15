package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationService {

    private final Map<String, IRoom> rooms = new HashMap<>();
    private final List<Reservation> reservations = new ArrayList<>();

    private static final ReservationService INSTANCE = new ReservationService();

    private ReservationService() {}

    public static ReservationService getInstance() {
        return INSTANCE;
    }

    public void addRoom(IRoom room) {
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, IRoom room,
                                    Date checkInDate, Date checkOutDate) {

        if (isRoomBooked(room, checkInDate, checkOutDate)) {
            System.out.println("Error: Room " + room.getRoomNumber() + " is already booked.");
            return null;
        }

        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate, int preference) {

        List<IRoom> bookedRooms = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservationOverlaps(reservation, checkInDate, checkOutDate)) {
                bookedRooms.add(reservation.getRoom());
            }
        }

        Collection<IRoom> availableRooms = rooms.values().stream()
                .filter(room -> !bookedRooms.contains(room))
                .collect(Collectors.toList());

        if (preference == 1) {
            return availableRooms.stream()
                    .filter(room -> !room.isFree())
                    .collect(Collectors.toList());
        } else if (preference == 2) {
            return availableRooms.stream()
                    .filter(IRoom::isFree)
                    .collect(Collectors.toList());
        }

        return availableRooms;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {

        return reservations.stream()
                .filter(reservation -> reservation.getCustomer().equals(customer))
                .collect(Collectors.toList());
    }

    public void printAllReservation() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        for (Reservation reservation : reservations) {
            System.out.println(reservation + "\n");
        }
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public Collection<IRoom> findRecommendedRooms(Date checkInDate, Date checkOutDate, int preference, int daysToSearch) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(checkInDate);
        calendar.add(Calendar.DAY_OF_MONTH, daysToSearch);
        Date recommendedCheckIn = calendar.getTime();

        calendar.setTime(checkOutDate);
        calendar.add(Calendar.DAY_OF_MONTH, daysToSearch);
        Date recommendedCheckOut = calendar.getTime();

        return findRooms(recommendedCheckIn, recommendedCheckOut, preference);
    }

    private boolean isRoomBooked(IRoom room, Date checkInDate, Date checkOutDate) {
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().equals(room)) {
                if (reservationOverlaps(reservation, checkInDate, checkOutDate)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean reservationOverlaps(Reservation existingReservation,
                                Date newCheckIn, Date newCheckOut) {
        return newCheckIn.before(existingReservation.getCheckOutDate()) &&
                newCheckOut.after(existingReservation.getCheckInDate());
    }
}
