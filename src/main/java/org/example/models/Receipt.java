package org.example.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Receipt {
    private ParkingTicket parkingTicket;
    private Date exitTime;
    private double fees;

    public Receipt(ParkingTicket parkingTicket, Date exitTime, double fees) {
        this.parkingTicket = parkingTicket;
        this.exitTime = exitTime;
        this.fees = fees;
    }
}
