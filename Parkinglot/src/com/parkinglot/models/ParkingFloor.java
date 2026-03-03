package com.parkinglot.models;
import com.parkinglot.enums.*;
import java.util.*;

public class ParkingFloor {
    private int floorNumber;
    private List<ParkingSpot> spots;

    public ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;
        this.spots = spots;
    }

    public Optional<ParkingSpot> getAvailableSpot(VehicleType type) {
        return spots.stream()
            .filter(s -> s.getSupportedType() == type && s.getStatus() == SpotStatus.AVAILABLE)
            .findFirst();
    }

    public int getFloorNumber() { return floorNumber; }
}