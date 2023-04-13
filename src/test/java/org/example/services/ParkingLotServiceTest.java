package org.example.services;

import org.example.enums.LocationType;
import org.example.enums.ParkingSpotStatus;
import org.example.enums.ParkingSpotType;
import org.example.enums.VehicleType;
import org.example.exceptions.SpotNotAvailableException;
import org.example.exceptions.UnknownLocationTypeExcpetion;
import org.example.exceptions.UnknownVehicleTypeExcpetion;
import org.example.models.ParkingLot;
import org.example.models.ParkingSpot;
import org.example.models.ParkingTicket;
import org.example.models.Receipt;
import org.example.services.fees.FeeModelCreationService;
import org.example.services.spotsavailability.FirstAvailableSpotsStrategy;

import java.util.*;
import java.util.logging.Logger;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParkingLotServiceTest {
    private final Logger logger = Logger.getLogger(ParkingLotServiceTest.class.getName());
    private FirstAvailableSpotsStrategy availableSpotsStrategy = new FirstAvailableSpotsStrategy();
    private FeeModelCreationService feeModelCreationService = new FeeModelCreationService();
    private ParkingLotService parkingLotService = new ParkingLotService(availableSpotsStrategy, feeModelCreationService);
    final long ONE_MINUTE_IN_MILLIS = 60000;

    public Date addMinutesToDate(int minutes, Date beforeTime) {
        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs
                + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    @Test
    public void createMallParkingLot() throws UnknownVehicleTypeExcpetion, SpotNotAvailableException, UnknownLocationTypeExcpetion {
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ParkingSpot spot = parkingLotService.createParkingSpot(LocationType.MALL,"motorcycle", "S"+i);
            parkingSpots.add(spot);
        }

        for (int i = 0; i < 80; i++) {
            ParkingSpot spot = parkingLotService.createParkingSpot(LocationType.MALL,"car", "M"+i);
            parkingSpots.add(spot);
        }

        for (int i = 0; i < 40; i++) {
            ParkingSpot spot = parkingLotService.createParkingSpot(LocationType.MALL,"bus", "B"+i);
            parkingSpots.add(spot);
        }

        ParkingLot lot = parkingLotService.createParkingLot(parkingSpots, LocationType.MALL);
        assertTrue(lot.getFeeModelList().size()==3);
        assertTrue(lot.getParkingSpots().size() == 220);
        assertTrue(lot.getFeeModelList().containsKey(VehicleType.SMALL));
        assertTrue(lot.getFeeModelList().containsKey(VehicleType.MEDIUM));
        assertTrue(lot.getFeeModelList().containsKey(VehicleType.BIG));

        logger.info("Running Park/Unpark/FeeCalculation tests for Mall parking lot");
        parkSmallVehicleMallParkingLotTest(lot);
        parkMediumVehicleMallParkingLotTest(lot);
        parkBigVehicleMallParkingLotTest(lot);
    }

    public void parkSmallVehicleMallParkingLotTest(ParkingLot lot) throws SpotNotAvailableException, UnknownVehicleTypeExcpetion {
        ParkingTicket ticket = null;
        ParkingSpot spot = null;
        int numSpots = lot.getParkingSpots().stream()
                                            .filter(parkingSpot -> parkingSpot.getSpotType().equals(ParkingSpotType.SMALL)
                                            ).toList().size();
        for (int i = 0; i < numSpots; i++) {
            ticket = parkingLotService.createParkingTicket(lot, "motorcycle", "A123"+i);
            spot = ticket.getParkingSpot();
            assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.FILLED));
        }

        logger.info("Running spot availability test");
        try {
            parkingLotService.createParkingTicket(lot, "motorcycle", "A123");
            fail("SpotNotAvailableException not invoked");
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Spot not available for vehicle type motorcycle"));
        }

        logger.info("Running Fee Calculation test1");
        Date exitTime = addMinutesToDate(60, new Date());
        Receipt bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 10.0);

        try {
            parkingLotService.createParkingTicket(lot, "motorcycle", "A123");
        } catch (Exception e) {
            fail("SpotNotAvailableException is invoked though spots available");
        }
    }

    public void parkMediumVehicleMallParkingLotTest(ParkingLot lot) throws SpotNotAvailableException, UnknownVehicleTypeExcpetion  {
        ParkingTicket ticket = null;
        ParkingSpot spot = null;

        int numSpots = lot.getParkingSpots().stream()
                .filter(parkingSpot -> parkingSpot.getSpotType().equals(ParkingSpotType.MEDIUM)
                ).toList().size();
        for (int i = 0; i < numSpots; i++) {
            ticket = parkingLotService.createParkingTicket(lot, "car", "A123"+i);
            spot = ticket.getParkingSpot();
            assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.FILLED));
        }

        logger.info("Running spot availability test");
        try {
            parkingLotService.createParkingTicket(lot, "car", "A123");
            fail("SpotNotAvailableException not invoked for car");
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Spot not available for vehicle type car"));
        }

        //SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.ENGLISH);
        //String dateInString = "12-04-2023 12:15:55 PM";
        //Date exitTime = formatter.parse(dateInString);
        logger.info("Running Fee Calculation test1");
        Date exitTime = new Date(new Date().getTime()+(60*ONE_MINUTE_IN_MILLIS));
        Receipt bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 20.0);
    }

    public void parkBigVehicleMallParkingLotTest(ParkingLot lot) throws SpotNotAvailableException, UnknownVehicleTypeExcpetion  {
        ParkingTicket ticket = null;
        ParkingSpot spot = null;

        int numSpots = lot.getParkingSpots().stream()
                .filter(parkingSpot -> parkingSpot.getSpotType().equals(ParkingSpotType.BIG)
                ).toList().size();

        for (int i = 0; i < numSpots; i++) {
            ticket = parkingLotService.createParkingTicket(lot, "bus", "A123"+i);
            spot = ticket.getParkingSpot();
            assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.FILLED));
        }

        logger.info("Running spot availability test");
        try {
            parkingLotService.createParkingTicket(lot, "bus", "A123");
            fail("SpotNotAvailableException not invoked for bus");
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Spot not available for vehicle type bus"));
        }

        logger.info("Running Fee Calculation test1");
        Date exitTime = new Date(new Date().getTime()+(120*ONE_MINUTE_IN_MILLIS));
        Receipt bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 50.0);
    }

    @Test
    public void createStadiumParkingLot() throws UnknownVehicleTypeExcpetion, SpotNotAvailableException, UnknownLocationTypeExcpetion {
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ParkingSpot spot = parkingLotService.createParkingSpot(LocationType.STADIUM,"motorcycle", "S"+i);
            parkingSpots.add(spot);
        }

        for (int i = 0; i < 80; i++) {
            ParkingSpot spot = parkingLotService.createParkingSpot(LocationType.STADIUM,"car", "M"+i);
            parkingSpots.add(spot);
        }

        try {
            for (int i = 0; i < 40; i++) {
                ParkingSpot spot = parkingLotService.createParkingSpot(LocationType.STADIUM,"bus", "B"+i);
                parkingSpots.add(spot);
            }
            fail("Bus parking spots should not be created for Stadium");
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Stadium/Airport Locations dont support Big Vehicles"));
        }


        ParkingLot lot = parkingLotService.createParkingLot(parkingSpots, LocationType.STADIUM);
        assertTrue(lot.getFeeModelList().size()==2);
        assertTrue(lot.getParkingSpots().size()==180);
        assertTrue(lot.getFeeModelList().containsKey(VehicleType.SMALL));
        assertTrue(lot.getFeeModelList().containsKey(VehicleType.MEDIUM));
        assertFalse(lot.getFeeModelList().containsKey(VehicleType.BIG));

        logger.info("Running Park/Unpark/FeeCalculation tests for Stadium parking lot");
        parkSmallVehicleStadiumParkingLotTest(lot);
        parkMediumVehicleStadiumParkingLotTest(lot);
    }

    public void parkSmallVehicleStadiumParkingLotTest(ParkingLot lot) throws SpotNotAvailableException, UnknownVehicleTypeExcpetion {
        ParkingTicket ticket = null;
        ParkingSpot spot = null;
        int numSpots = lot.getParkingSpots().stream()
                .filter(parkingSpot -> parkingSpot.getSpotType().equals(ParkingSpotType.SMALL)
                ).toList().size();
        for (int i = 0; i < numSpots; i++) {
            ticket = parkingLotService.createParkingTicket(lot, "motorcycle", "A123"+i);
            spot = ticket.getParkingSpot();
            assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.FILLED));
        }

        logger.info("Running spot availability test");
        try {
            parkingLotService.createParkingTicket(lot, "motorcycle", "A123");
            fail("SpotNotAvailableException not invoked in stadium lot");
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Spot not available for vehicle type motorcycle"));
        }

        Date exitTime;
        Receipt bill;

        logger.info("Running Fee Calculation test1");
        exitTime = addMinutesToDate(60*3, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 30.0);

        logger.info("Running Fee Calculation test2");
        exitTime = addMinutesToDate(60*15, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 90.0+300.0);

        try {
            parkingLotService.createParkingTicket(lot, "motorcycle", "A123");
        } catch (Exception e) {
            fail("SpotNotAvailableException is invoked though spots available in stadium");
        }
    }

    public void parkMediumVehicleStadiumParkingLotTest(ParkingLot lot) throws SpotNotAvailableException, UnknownVehicleTypeExcpetion {
        ParkingTicket ticket = null;
        ParkingSpot spot = null;
        int numSpots = lot.getParkingSpots().stream()
                .filter(parkingSpot -> parkingSpot.getSpotType().equals(ParkingSpotType.MEDIUM)
                ).toList().size();
        for (int i = 0; i < numSpots; i++) {
            ticket = parkingLotService.createParkingTicket(lot, "car", "A123"+i);
            spot = ticket.getParkingSpot();
            assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.FILLED));
        }

        logger.info("Running spot availability test");
        try {
            parkingLotService.createParkingTicket(lot, "car", "A123");
            fail("SpotNotAvailableException not invoked in stadium lot");
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Spot not available for vehicle type car"));
        }

        Date exitTime;
        Receipt bill;

        logger.info("Running Fee Calculation test1");
        exitTime = addMinutesToDate(60*11, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 180.0);

        logger.info("Running Fee Calculation test2");
        exitTime = addMinutesToDate(60*14, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 580.0);

        try {
            parkingLotService.createParkingTicket(lot, "car", "A123");
        } catch (Exception e) {
            fail("SpotNotAvailableException is invoked though spots available in stadium");
        }
    }

    @Test
    public void createAirportParkingLot() throws UnknownVehicleTypeExcpetion, SpotNotAvailableException, UnknownLocationTypeExcpetion {
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ParkingSpot spot = parkingLotService.createParkingSpot(LocationType.AIRPORT,"motorcycle", "S"+i);
            parkingSpots.add(spot);
        }

        for (int i = 0; i < 80; i++) {
            ParkingSpot spot = parkingLotService.createParkingSpot(LocationType.AIRPORT,"car", "M"+i);
            parkingSpots.add(spot);
        }

        try {
            for (int i = 0; i < 40; i++) {
                ParkingSpot spot = parkingLotService.createParkingSpot(LocationType.AIRPORT,"bus", "B"+i);
                parkingSpots.add(spot);
            }
            fail("Bus parking spots should not be created for Stadium");
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Stadium/Airport Locations dont support Big Vehicles"));
        }

        ParkingLot lot = parkingLotService.createParkingLot(parkingSpots, LocationType.AIRPORT);
        assertTrue(lot.getFeeModelList().size()==2);
        assertTrue(lot.getParkingSpots().size()==180);
        assertTrue(lot.getFeeModelList().containsKey(VehicleType.SMALL));
        assertTrue(lot.getFeeModelList().containsKey(VehicleType.MEDIUM));
        assertFalse(lot.getFeeModelList().containsKey(VehicleType.BIG));

        logger.info("Running Park/Unpark/FeeCalculation tests for Mall parking lot");
        parkSmallVehicleAirportParkingLotTest(lot);
        parkMediumVehicleAirportParkingLotTest(lot);
    }

    public void parkSmallVehicleAirportParkingLotTest(ParkingLot lot) throws SpotNotAvailableException, UnknownVehicleTypeExcpetion {
        ParkingTicket ticket = null;
        ParkingSpot spot = null;
        int numSpots = lot.getParkingSpots().stream()
                .filter(parkingSpot -> parkingSpot.getSpotType().equals(ParkingSpotType.SMALL)
                ).toList().size();
        for (int i = 0; i < numSpots; i++) {
            ticket = parkingLotService.createParkingTicket(lot, "motorcycle", "A123"+i);
            spot = ticket.getParkingSpot();
            assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.FILLED));
        }

        logger.info("Running spot availability test");
        try {
            parkingLotService.createParkingTicket(lot, "motorcycle", "A123");
            fail("SpotNotAvailableException not invoked in stadium lot");
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Spot not available for vehicle type motorcycle"));
        }

        Date exitTime;
        Receipt bill;

        logger.info("Running Fee Calculation test1");
        exitTime = addMinutesToDate(60*0, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 0.0);

        logger.info("Running Fee Calculation test2");
        exitTime = addMinutesToDate(60*1, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 40.0);

        logger.info("Running Fee Calculation test3");
        exitTime = addMinutesToDate(60*9, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 60.0);

        logger.info("Running Fee Calculation test4");
        exitTime = addMinutesToDate(60*24, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 80.0);

        logger.info("Running Fee Calculation test5");
        exitTime = addMinutesToDate(60*48, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 80.0*2);

        try {
            parkingLotService.createParkingTicket(lot, "motorcycle", "A123");
        } catch (Exception e) {
            fail("SpotNotAvailableException is invoked though spots available in stadium");
        }
    }

    public void parkMediumVehicleAirportParkingLotTest(ParkingLot lot) throws SpotNotAvailableException, UnknownVehicleTypeExcpetion {
        ParkingTicket ticket = null;
        ParkingSpot spot = null;
        int numSpots = lot.getParkingSpots().stream()
                .filter(parkingSpot -> parkingSpot.getSpotType().equals(ParkingSpotType.MEDIUM)
                ).toList().size();
        for (int i = 0; i < numSpots; i++) {
            ticket = parkingLotService.createParkingTicket(lot, "car", "A123"+i);
            spot = ticket.getParkingSpot();
            assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.FILLED));
        }

        logger.info("Running spot availability test");
        try {
            parkingLotService.createParkingTicket(lot, "car", "A123");
            fail("SpotNotAvailableException not invoked in airport lot");
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Spot not available for vehicle type car"));
        }

        Date exitTime;
        Receipt bill;

        logger.info("Running Fee Calculation test1");
        exitTime = addMinutesToDate(60*1, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 60.0);

        logger.info("Running Fee Calculation test2");
        exitTime = addMinutesToDate(60*12, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 80.0);

        logger.info("Running Fee Calculation test3");
        exitTime = addMinutesToDate(60*24, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 100.0);

        logger.info("Running Fee Calculation test4");
        exitTime = addMinutesToDate(60*48, new Date());
        bill = parkingLotService.createReceipt(lot, ticket, exitTime);
        assertTrue(spot.getSpotStatus().equals(ParkingSpotStatus.VACANT));
        assertTrue(bill.getFees() == 100.0*2);

        try {
            parkingLotService.createParkingTicket(lot, "car", "A123");
        } catch (Exception e) {
            fail("SpotNotAvailableException is invoked though spots available in airport");
        }
    }
}
