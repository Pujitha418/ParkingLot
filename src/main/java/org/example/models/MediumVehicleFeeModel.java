package org.example.models;

import org.example.enums.LocationType;
import org.example.enums.VehicleType;

import java.util.List;
import java.util.Map;

public class MediumVehicleFeeModel extends FeeModel {
    public MediumVehicleFeeModel(LocationType locationType, List<HourFeeMap> priceMap) {
        super(VehicleType.MEDIUM, locationType, priceMap);
    }
}
