package com.parkinglot;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.models.*;
import com.parkinglot.service.ParkingLot;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Setup Floor 1
        List<ParkingSpot> spots = new ArrayList<>();
        spots.add(new ParkingSpot("F1-M1", VehicleType.MOTORCYCLE));
        spots.add(new ParkingSpot("F1-C1", VehicleType.CAR));
        spots.add(new ParkingSpot("F1-C2", VehicleType.CAR));
        spots.add(new ParkingSpot("F1-B1", VehicleType.BUS));

        ParkingFloor floor1 = new ParkingFloor(1, spots);
        ParkingLot lot = ParkingLot.getInstance(List.of(floor1));

        // Simulate check-in
        Vehicle car1 = new Vehicle("KA-01-1234", VehicleType.CAR);
        Vehicle bike1 = new Vehicle("KA-02-5678", VehicleType.MOTORCYCLE);

        lot.checkIn(car1);
        lot.checkIn(bike1);

        Thread.sleep(2000); // simulate 2 seconds of parking

        lot.checkOut("KA-01-1234");
        lot.checkOut("KA-02-5678");
    }
}