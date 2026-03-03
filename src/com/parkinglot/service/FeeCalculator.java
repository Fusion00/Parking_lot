package com.parkinglot.service;

import com.parkinglot.models.Ticket;
import java.time.Duration;

public class FeeCalculator {
    public double calculate(Ticket ticket) {
        Duration duration = Duration.between(ticket.getEntryTime(), ticket.getExitTime());
        long hours = Math.max(1, (long) Math.ceil(duration.toMinutes() / 60.0));

        double ratePerHour = switch (ticket.getVehicle().getType()) {
            case MOTORCYCLE -> 10.0;
            case CAR        -> 20.0;
            case BUS        -> 50.0;
        };

        return hours * ratePerHour;
    }
}