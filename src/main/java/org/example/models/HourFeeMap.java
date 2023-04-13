package org.example.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HourFeeMap {
    //private ParkingLot parkingLot;
    private int hour;
    private Double price;

    public HourFeeMap(int hour, Double price) {
        this.hour = hour;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Hour : " + hour + "\nPrice : " + price;
    }
}
