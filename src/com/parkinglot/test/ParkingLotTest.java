package com.parkinglot.test;

import com.parkinglot.enums.VehicleType;
import com.parkinglot.models.*;
import com.parkinglot.service.ParkingLot;
import com.parkinglot.exception.ParkingLotException;
import java.util.*;

public class ParkingLotTest {

    static ParkingLot lot;

    public static void main(String[] args) throws InterruptedException {
        setup();
        testCarCheckIn();
        testMotorcycleCheckIn();
        testCheckOut();
        testNoSpotAvailable();
        testConcurrentCheckIn();
        System.out.println("\n✅ All tests completed!");
    }

    static void setup() {
        List<ParkingSpot> spots = new ArrayList<>();
        spots.add(new ParkingSpot("F1-M1", VehicleType.MOTORCYCLE));
        spots.add(new ParkingSpot("F1-C1", VehicleType.CAR));
        spots.add(new ParkingSpot("F1-C2", VehicleType.CAR));
        spots.add(new ParkingSpot("F1-B1", VehicleType.BUS));

        ParkingFloor floor1 = new ParkingFloor(1, spots);
        lot = ParkingLot.getInstance(List.of(floor1));
        System.out.println("🔧 Setup done\n");
    }

    // Test 1: Car check-in
    static void testCarCheckIn() {
        System.out.println("--- Test 1: Car Check-In ---");
        Vehicle car = new Vehicle("CAR-001", VehicleType.CAR);
        Ticket ticket = lot.checkIn(car);
        assert ticket != null : "Ticket should not be null";
        System.out.println("PASS ✅\n");
    }

    // Test 2: Motorcycle check-in
    static void testMotorcycleCheckIn() {
        System.out.println("--- Test 2: Motorcycle Check-In ---");
        Vehicle bike = new Vehicle("BIKE-001", VehicleType.MOTORCYCLE);
        Ticket ticket = lot.checkIn(bike);
        assert ticket != null : "Ticket should not be null";
        System.out.println("PASS ✅\n");
    }

    // Test 3: Check-out and fee calculation
    static void testCheckOut() throws InterruptedException {
        System.out.println("--- Test 3: Check-Out & Fee ---");
        Vehicle car = new Vehicle("CAR-002", VehicleType.CAR);
        lot.checkIn(car);
        Thread.sleep(1000); // simulate parking time
        double fee = lot.checkOut("CAR-002");
        assert fee > 0 : "Fee should be greater than 0";
        System.out.println("Fee charged: ₹" + fee);
        System.out.println("PASS ✅\n");
    }

    // Test 4: No spot available (exception case)
    static void testNoSpotAvailable() {
        System.out.println("--- Test 4: No Spot Available ---");
        try {
            // BUS spot is still free, book it first
            Vehicle bus1 = new Vehicle("BUS-001", VehicleType.BUS);
            lot.checkIn(bus1);
            // Try booking another bus — should fail
            Vehicle bus2 = new Vehicle("BUS-002", VehicleType.BUS);
            lot.checkIn(bus2);
            System.out.println("FAIL - Should have thrown exception");
        } catch (ParkingLotException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
            System.out.println("PASS ✅\n");
        }
    }

    // Test 5: Concurrent check-ins
    static void testConcurrentCheckIn() throws InterruptedException {
        System.out.println("--- Test 5: Concurrent Check-In ---");

        // Free up CAR-001 spot first
        lot.checkOut("CAR-001");

        Thread t1 = new Thread(() -> {
            Vehicle car = new Vehicle("CAR-T1", VehicleType.CAR);
            try {
                lot.checkIn(car);
                System.out.println("Thread 1: CAR-T1 checked in");
            } catch (ParkingLotException e) {
                System.out.println("Thread 1: No spot - " + e.getMessage());
            }
        });

        Thread t2 = new Thread(() -> {
            Vehicle car = new Vehicle("CAR-T2", VehicleType.CAR);
            try {
                lot.checkIn(car);
                System.out.println("Thread 2: CAR-T2 checked in");
            } catch (ParkingLotException e) {
                System.out.println("Thread 2: No spot - " + e.getMessage());
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("PASS ✅\n");
    }
}