package org.example.models;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.LocationType;
import org.example.enums.ParkingLotStatus;
import org.example.enums.VehicleType;
import org.example.services.fees.FeeCalculationStrategy;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ParkingLot {
    private LocationType locationType;
    private List<ParkingSpot> parkingSpots;
    private ParkingLotStatus parkingLotStatus;
    private FeeCalculationStrategy feeCalculationStrategy;
    private Map<VehicleType, FeeModel> feeModelList;
    private int numSpots;

    public ParkingLot(List<ParkingSpot> parkingSpots, ParkingLotStatus parkingLotStatus, FeeCalculationStrategy feeCalculationStrategy, Map<VehicleType, FeeModel> feeModelList, LocationType locationType, int numSpots) {
        this.parkingSpots = parkingSpots;
        this.parkingLotStatus = parkingLotStatus;
        this.feeCalculationStrategy = feeCalculationStrategy;
        this.feeModelList = feeModelList;
        this.locationType = locationType;
        this.numSpots = numSpots;
    }
}
