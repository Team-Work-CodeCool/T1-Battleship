package ship;

public enum ShipType {
   CARRIER(5, "carrier", 1),
   BATTLESHIP(4, "battleship", 1),
   CRUISER(3, "cruiser", 1),
   DESTROYER(2, "destroyer", 2),
   SUBMARINE(1, "U-boot", 3);

   private final int length;
   private final int shipCount;
   private final String name;

   ShipType(int length, String name, int shipCount) {
      this.length = length;
      this.name = name;
      this.shipCount = shipCount;
   }

   public int getLength() {
      return this.length;
   }

   public String getName() {
      return name;
   }

   public int getShipCount() {
      return shipCount;
   }
}
