package org.example.exceptions;

public class SpotNotAvailableException extends Exception {
    public SpotNotAvailableException(String vehicleType) {
        super("Spot not available for vehicle type "+vehicleType);
    }
}
