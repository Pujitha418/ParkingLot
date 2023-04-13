package org.example.exceptions;

public class UnknownLocationTypeExcpetion extends Exception {
    public UnknownLocationTypeExcpetion() {
        super("Unknown location Type passed");
    }
}
