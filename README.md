* **Parkinglot** is highest level entity. It consists of multiple slots of each Slot type SMALL/MEDIUM/BIG mapped to
  vehicle types.
* **parkingSpotFactory** class defines mapping methods for vehicle type/location type.
* Along with spots, Parking lot holds fee model and will have FeeCalculationStrategy associated.
* In addition, Pakinglot shows status FULL/AVAILABLE which will be set during parking/unparking.
* feeModelList attribute in ParkingLot entity is k,v pairs where key=vehicleType & value=feeModel
* **FeeModel:**
    * this entity has locationType, vehicleType, priceMap attributes
    * priceMap is List<HourFeeMap> which contains a tuple/class of startInterval and price.
* FeeCalculation will depend on the strategy associated with Parkinglot. (follows Strategy design pattern)
    * **FeeCalculationStrategy** is interface. Implementations are:
        * WindowFeeCalculationStrategy --> AIRPORT & MALL will fit into this model as per given problem statement.
            * This follows binary search logic to find the nearest hour in order to compute fees in O(logn) time where
              n=size of fee model of lot.
        * SumBracketsFeeCalculationStrategy --> Stadium will fit into this strategy.
            * this traverses feemodel array to compute each applicable bracket and sum at each stage.
    * FeeCalculationStrategyFactory maps locationType to calculationStrategy.
* **Finding AvailableSpots:**
    * We can find avilable spots based on the vehicleType (helps in determining spotType) in different ways.
    * Hence followed strategy DP here. As of now, defined one strategy called FirstAvailableSpotsStrategy that runs in
      linear time.
    * It traverses from start of parkingSpots list and return first VACANT spot.
    * _Note:_
        * In production code, this status should be fetched from db and since no two vehicles should get same spot, we
          need to take exclusive lock on spot while checking and updating status to FILLED.
        * Since repository layer(db) is omitted, synchronized is on variable in this code. Else, it would be on
          ParkingSpots db row.
* **Other Services:**
    * _ParkingLotService_:
        * deps - findAvailableSpotsStrategy, feeModelCreationService
        * This service contains below methods:
            * createParkingLot - creates and returns ParkingLot object with inputs locationType and ParkingSpots.
            * createParkingSpot - creates and returns ParkingSpot object with given spotNumber based on vehicleType
              parameter.
            * createParkingTicket - creates and returns ParkingTicket object when vehicle is parked
            * createReceipt - creates and returns final Receipt object with fees calculated as per the
              FeeCalculationStrategy obtained from parkingLot i/p object.
    * _fees_:
        * FeeModelCreationService - creates registry of fee models for each vehicle type based on locationType.
        * FeeCalculationStrategy implementations as discussed above.
* **Tests**:
    * FeeModelCreationServiceTest - tests FeeModelCreationService to verify the registry map of vehicleType and prices
      maintained in ascending order.
    * ParkingLotServiceTest - tests creation, park, unpark, TicketCreation and ReceiptCreation features.
* **Improvements**:
    * Bulk spots creation method could be supported in ParkingLotService taking spotNumber format in params.
    * FeeModelCreationService - As per current codepack, it is defining fixed feee model. Ideally it should take it as
      parameter and create FeeModel object.
    * ParkingLotServiceTest could be modularized.
    * ParkingLotStatus needs to be set whenever a new vehicle is parked.
    * For simplicity, ParkingFloor entity is omitted.
    * There could be another enum for vehicle types like motorcycle.
