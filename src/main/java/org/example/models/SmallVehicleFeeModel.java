package org.example.models;

import org.example.enums.LocationType;
import org.example.enums.VehicleType;

import java.util.List;
import java.util.Map;

public class SmallVehicleFeeModel extends FeeModel {
    public SmallVehicleFeeModel(LocationType locationType, List<HourFeeMap> priceMap) {
        super(VehicleType.SMALL, locationType, priceMap);
    }
}
