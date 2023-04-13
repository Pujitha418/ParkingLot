package org.example.services.fees;

import org.example.enums.LocationType;
import org.example.enums.VehicleType;
import org.example.models.FeeModel;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.logging.Logger;

public class FeeModelCreationServiceTest {
    private FeeModelCreationService feeModelCreationService = new FeeModelCreationService();
    private Logger logger = Logger.getLogger(FeeModelCreationServiceTest.class.getName());

    @Test
    public void FeeModelCreationService() {
        logger.info("Running FeeModelCreationService test");
        Map<VehicleType, FeeModel> mallFeeModelMap = feeModelCreationService.create(LocationType.MALL);
        assertTrue(mallFeeModelMap.size() == 3);

        Map<VehicleType, FeeModel> stadiumFeeModelMap = feeModelCreationService.create(LocationType.STADIUM);
        assertTrue(stadiumFeeModelMap.size() == 2);

        Map<VehicleType, FeeModel> airportFeeModelMap = feeModelCreationService.create(LocationType.AIRPORT);
        assertTrue(stadiumFeeModelMap.size() == 2);

        logger.info("Created a fee model map for mall, stadium and airport");

        assertTrue(stadiumFeeModelMap.containsKey(VehicleType.SMALL));
        assertTrue(stadiumFeeModelMap.containsKey(VehicleType.MEDIUM));
        assertFalse(stadiumFeeModelMap.containsKey(VehicleType.BIG));

        assertTrue(airportFeeModelMap.containsKey(VehicleType.SMALL));
        assertTrue(airportFeeModelMap.containsKey(VehicleType.MEDIUM));
        assertFalse(airportFeeModelMap.containsKey(VehicleType.BIG));

        logger.info("All FeeModelCreationService testcases passed");
    }
}
