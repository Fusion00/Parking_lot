package com.parkinglot.models;
import com.parkinglot.enums.*;

public class ParkingSpot {
    private String spotId;
    private VehicleType supportedType;
    private SpotStatus status;

    public ParkingSpot(String spotId, VehicleType supportedType) {
        this.spotId = spotId;
        this.supportedType = supportedType;
        this.status = SpotStatus.AVAILABLE;
    }

    public synchronized boolean occupy() {
        if (status == SpotStatus.AVAILABLE) {
            status = SpotStatus.OCCUPIED;
            return true;
        }
        return false;
    }

    public synchronized void free() { status = SpotStatus.AVAILABLE; }
    public SpotStatus getStatus() { return status; }
    public VehicleType getSupportedType() { return supportedType; }
    public String getSpotId() { return spotId; }
}