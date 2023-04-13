package org.example.services.fees;

import org.example.models.ParkingLot;
import org.example.models.ParkingTicket;

import java.util.Date;

public interface FeeCalculationStrategy {
    Double calculate(ParkingLot parkingLot, ParkingTicket ticket, Date exitTime);
}
