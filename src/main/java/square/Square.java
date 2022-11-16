package square;

public class Square {

   private final int posX;
   private final int posY;

   private SquareStatus squareStatus;

   //    TODO booleans to enum.
   //     This might be not worth it due to player shot validation based on this boolean statuses.
   private boolean revealed;
   private boolean withShip;
   private boolean withHit;
   private boolean withMiss;

   public Square(int posX, int posY) {
      this.posX = posX;
      this.posY = posY;
      this.squareStatus = SquareStatus.EMPTY;
      this.revealed = false;
      this.withShip = false;
      this.withHit = false;
      this.withMiss = false;
   }

   public int getPosX() {
      return posX;
   }

   public int getPosY() {
      return posY;
   }

   public SquareStatus getSquareStatus() {
      return squareStatus;
   }

   public void setSquareStatus(SquareStatus squareStatus) {
      this.squareStatus = squareStatus;
   }

   public boolean isRevealed() {
      return revealed;
   }

   public void setRevealed(boolean revealed) {
      this.revealed = revealed;
   }

   public boolean isWithShip() {
      return withShip;
   }

   public void setWithShip(boolean withShip) {
      this.withShip = withShip;
      setSquareStatus(SquareStatus.SHIP);
   }

   public boolean isWithHit() {
      return withHit;
   }

   public void setWithHit(boolean withHit) {
      this.withHit = withHit;
      setSquareStatus(SquareStatus.HIT);
   }

   public boolean isWithMiss() {
      return withMiss;
   }

   public void setWithMiss(boolean withMiss) {
      this.withMiss = withMiss;
      setSquareStatus(SquareStatus.MISSED);
   }

   @Override
   public String toString() {
      return String.valueOf(this.squareStatus.getCharacter());
   }

   public void updateSquareStatus() {
      if (withHit) squareStatus = SquareStatus.HIT;
      if (withMiss) squareStatus = SquareStatus.MISSED;
   }
}
