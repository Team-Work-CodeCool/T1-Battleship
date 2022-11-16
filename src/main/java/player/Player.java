package player;

import board.Board;
import ship.Ship;
import square.Square;
import square.SquareStatus;

import java.util.ArrayList;
import java.util.List;

public class Player {

   //    TODO check if boolean type adequate (variable size matters).
   private final int playerNumber;
   private boolean computerPlayer = false;
   private final List<Ship> ships;
   private final Board ocean;

   public Player(int playerNumber) {
      this.playerNumber = playerNumber;
      this.ships = new ArrayList<>();
      this.ocean = new Board();
   }

   public int getPlayerNumber() {
      return playerNumber;
   }

   public boolean isComputerPlayer() {
      return computerPlayer;
   }

   public void setComputerPlayer(boolean computerPlayer) {
      this.computerPlayer = computerPlayer;
   }

   public Board getOcean() {
      return ocean;
   }

   public List<Ship> getShips() {
      return ships;
   }

   public void addShip(Ship ship) {
      this.ships.add(ship);
   }

   /**
    * Performs shot action on a Square object at a given location
    * revealing its contents, setting its status based on ship position.
    *
    * @param testedSquare - Square tested coordinates holder,
    * @return true if shot performed on an empty square.
    */
   public boolean performShotAtSquare(Square testedSquare) {
      if (!testedSquare.isRevealed()) {
         testedSquare.setRevealed(true);
         if (testedSquare.isWithShip()) {
            testedSquare.setWithHit(true);
         } else {
            testedSquare.setWithMiss(true);
         }
         testedSquare.updateSquareStatus();
         return true;
      }
      return false;
   }

   /**
    * Performs shot action on a Ship object part (Square object) at a given location
    * subtracting its life and setting it to sunken if lives left drop to 0.
    *
    * @param ships - opposite player list of Ship objects,
    * @param x     - location x coordinate,
    * @param y     - location y coordinate,
    * @return Ship class object for further shot validation performed in Game class.
    */
   public Ship performShotAtShip(List<Ship> ships, int x, int y) {
      for (Ship testedShip : ships) {
         for (Square testedSquare : testedShip.getSquares()) {
            if (testedSquare.getPosX() == x && testedSquare.getPosY() == y) {
               testedShip.setLivesLeft(testedShip.getLivesLeft() - 1);
               if (testedShip.getLivesLeft() == 0) {
                  testedShip.sink();
                  return testedShip;
               }
               return testedShip;
            }
         }
      }
      return null;
   }

   /**
    * Removes a neutralised Ship object from appropriate players ships list
    * and sets appropriate board squares to display neutralised status.
    */
   public void removeShipIfNeutralised() {
      int tempX, tempY;
      for (Ship testedShip : ships) {
         if (testedShip.isSunk()) {
            for (Square testedSquare : testedShip.getSquares()) {
               tempX = testedSquare.getPosX();
               tempY = testedSquare.getPosY();
               ocean.getOcean()[tempX][tempY].setSquareStatus(SquareStatus.NEUTRALIZED);
            }
         }
      }
      ships.removeIf(Ship::isSunk);
   }

   public boolean isAlive() {
      return !ships.isEmpty();
   }
}
