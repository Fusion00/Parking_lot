package com.parkinglot.service;
import com.parkinglot.models.*;
import com.parkinglot.exception.ParkingLotException;
import java.util.*;
import java.util.concurrent.*;

public class ParkingLot {
    private static ParkingLot instance;
    private List<ParkingFloor> floors;
    private Map<String, Ticket> activeTickets = new ConcurrentHashMap<>();
    private FeeCalculator feeCalculator = new FeeCalculator();
    private int ticketCounter = 0;

    private ParkingLot(List<ParkingFloor> floors) { this.floors = floors; }

    public static synchronized ParkingLot getInstance(List<ParkingFloor> floors) {
        if (instance == null) instance = new ParkingLot(floors);
        return instance;
    }

    public synchronized Ticket checkIn(Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            Optional<ParkingSpot> spot = floor.getAvailableSpot(vehicle.getType());
            if (spot.isPresent() && spot.get().occupy()) {
                String ticketId = "TKT-" + (++ticketCounter);
                Ticket ticket = new Ticket(ticketId, vehicle, spot.get());
                activeTickets.put(vehicle.getLicensePlate(), ticket);
                System.out.println("✅ Check-in: " + vehicle.getLicensePlate() +
                    " → Spot " + spot.get().getSpotId() + " | Ticket: " + ticketId);
                return ticket;
            }
        }
        throw new ParkingLotException("No available spot for vehicle type: " + vehicle.getType());
    }

    public double checkOut(String licensePlate) {
        Ticket ticket = activeTickets.remove(licensePlate);
        if (ticket == null) throw new ParkingLotException("No active ticket for: " + licensePlate);
        ticket.checkout();
        ticket.getSpot().free();
        double fee = feeCalculator.calculate(ticket);
        System.out.println("🚗 Check-out: " + licensePlate + " | Fee: ₹" + fee);
        return fee;
    }
}