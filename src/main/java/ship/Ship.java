package ship;

import square.Square;

import java.util.List;

public class Ship {
   private final ShipType shipType;
   private final List<Square> squares;

   //    TODO isSunk removal to consider, base on SquareStatus.
   private boolean isSunk;
   private int livesLeft;

   public Ship(ShipType shipType, List<Square> squares) {
      this.shipType = shipType;
      this.squares = squares;
      this.isSunk = false;
      this.livesLeft = squares.size();
   }

   public ShipType getShipType() {
      return shipType;
   }

   public List<Square> getSquares() {
      return squares;
   }

   public boolean isSunk() {
      return this.isSunk;
   }

   public void sink() {
      this.isSunk = true;
   }

   public int getLivesLeft() {
      return livesLeft;
   }

   public void setLivesLeft(int livesLeft) {
      this.livesLeft = livesLeft;
   }
}
