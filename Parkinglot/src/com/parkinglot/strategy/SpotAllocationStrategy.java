package com.parkinglot.strategy;

import com.parkinglot.models.ParkingSpot;
import com.parkinglot.models.Vehicle;
import java.util.List;
import java.util.Optional;

public interface SpotAllocationStrategy {
    Optional<ParkingSpot> allocate(List<ParkingSpot> spots, Vehicle vehicle);
}