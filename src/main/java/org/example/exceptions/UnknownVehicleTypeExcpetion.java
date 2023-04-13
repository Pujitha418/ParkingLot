package org.example.exceptions;

public class UnknownVehicleTypeExcpetion extends Exception {
    public UnknownVehicleTypeExcpetion() {
        super("Unknown vehicle Type passed");
    }
}
