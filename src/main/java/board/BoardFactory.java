package board;

import player.Player;
import ship.Ship;
import ship.ShipType;
import square.Square;
import utils.Display;
import utils.Input;
import utils.RandomNumberProvider;

import java.util.ArrayList;
import java.util.List;

import static utils.Input.INVALID_USER_INPUT_MESSAGE;

public class BoardFactory {
   private static final String GAME_BOARD_MESSAGE = " game board arrangement in progress..";
   private static final String AHOY_MESSAGE = " A H O Y   A D M I R A B L E   A D M I R A L  ( ";
   private static final String NOW_LAUNCHING_MESSAGE_START = "\033[1;92m You are now launching your ";
   private static final String NOW_LAUNCHING_MESSAGE_END = " into vast ocean waters.";
   private static final String LAUNCHING_SUCCESS = " has been successfully launched.";
   private final Input input = new Input();
   private final Display display = new Display();
   private final RandomNumberProvider random = new RandomNumberProvider();

   /**
    * Holds ship placement functionality for both manual an automatic arrangement..
    *
    * @param player      - currently active player,
    * @param playerBoard - player Square board.
    */
   public void shipPlacementPerPlayer(Player player, Board playerBoard) {
      display.inform("\033[0;37m Player " + player.getPlayerNumber() + GAME_BOARD_MESSAGE);
      List<ShipType> shipList = shipList();
      if (!player.getOcean().isAutomaticArrangement()) {
         display.inform(AHOY_MESSAGE + player.getPlayerNumber() + " )  !");
         display.printBoard(player.getOcean(), player.getPlayerNumber() - 1, true);
      }
      placeTheShip(shipList, player, playerBoard);
   }

   /**
    * Places the ships contained in ShipType list using random direction and coordinates or using set of user inputs
    * on player's board.
    *
    * @param shipList    - list of ships to add,
    * @param player      - currently active player,
    * @param playerBoard - player Square board.
    */
   private void placeTheShip(List<ShipType> shipList, Player player, Board playerBoard) {
      for (ShipType tempShip : shipList) {
         if (player.getOcean().isAutomaticArrangement()) {
            placeShipOnTheBoard(player, playerBoard, random.randomizeDirection(), tempShip, random.randomCoordinatesGenerator());
         } else {
            display.instruct(NOW_LAUNCHING_MESSAGE_START + tempShip.getLength() + "-square " + tempShip.getName() + NOW_LAUNCHING_MESSAGE_END);
            placeShipOnTheBoard(player, playerBoard, input.askForDirection(), tempShip, input.askForCoordinates());
            display.printBoard(player.getOcean(), player.getPlayerNumber() - 1, true);
            display.inform("\033[0;33m Your " + tempShip.getName() + LAUNCHING_SUCCESS);
            display.inform(Input.PRESS_ENTER);
            display.waitForEnter();
         }
      }
   }

//   TODO refactor with stream?

   /**
    * Provides list of ships to be placed on the board based on ship count in ShipType enum.
    *
    * @return list of ShipType enums that performs as a blueprint for adding Ship object to each player ships list.
    */
   private List<ShipType> shipList() {
      List<ShipType> shipList = new ArrayList<>();
      for (ShipType shipType : ShipType.values()) {
         for (int i = 0; i < shipType.getShipCount(); i++) {
            shipList.add(shipType);
         }
      }
      return shipList;
   }

   /**
    * Recursive ship placement wrapper for both manual an automatic arrangement.
    *
    * @param player      - currently active player,
    * @param playerBoard - player Square board,
    * @param direction   - vertical or horizontal,
    * @param shipType    - ShipType of the ship to be placed on board,
    * @param coordinates - integer array (2 elements) consisting of chosen coordinates (x, y).
    */
   private void placeShipOnTheBoard(Player player, Board playerBoard, Direction direction, ShipType shipType, int[] coordinates) {
      if (placementValidator(isOnBoard(allSquaresToCheck(squaresToBeOccupied(coordinates, direction, shipType)), playerBoard), playerBoard, coordinates, direction, shipType)) {
         setPlacement(player, playerBoard, squaresToBeOccupied(coordinates, direction, shipType), shipType);
      } else {
         if (player.getOcean().isAutomaticArrangement()) {
            placeShipOnTheBoard(player, playerBoard, random.randomizeDirection(), shipType, random.randomCoordinatesGenerator());
         } else {
            display.instruct(INVALID_USER_INPUT_MESSAGE);
            placeShipOnTheBoard(player, playerBoard, input.askForDirection(), shipType, input.askForCoordinates());
         }
      }
   }

   /**
    * Placement validator checks in order: if the Ship object (to be placed) is within board boundaries,
    * if any ship near or overlapping already exists.
    *
    * @param allInboardSquaresToCheck - list of Square objects containing a Ship squares and surrounding on board squares,
    * @param playerBoard              - player Square board,
    * @param coordinates              - integer array (2 elements) consisting of chosen coordinates (x, y),
    * @param direction                - vertical or horizontal,
    * @param shipType                 - ShipType of the ship to be placed on board,
    * @return true if constructing coordinates and direction valid based on game rules.
    */
   private boolean placementValidator(List<Square> allInboardSquaresToCheck, Board playerBoard, int[] coordinates, Direction direction, ShipType shipType) {
      if (isShipOnBoard(squaresToBeOccupied(coordinates, direction, shipType), playerBoard)) {
         int tempX, tempY;
         for (Square tempSquare : allInboardSquaresToCheck) {
            tempX = tempSquare.getPosX();
            tempY = tempSquare.getPosY();
            if (playerBoard.getOcean()[tempX][tempY].isWithShip()) return false;
         }
         return true;
      }
      return false;
   }

   /**
    * Sets Ship object placement on proper player board and adds that object to that player ship list.
    *
    * @param player              - currently active player,
    * @param playerBoard         - player Square board,
    * @param squaresToBeOccupied - list of Square objects generated from starting coordinates,
    *                            ship type (length) and placement direction,
    * @param shipType            - ShipType of the ship to be placed on board.
    */
   private void setPlacement(Player player, Board playerBoard, List<Square> squaresToBeOccupied, ShipType shipType) {
      int tempX, tempY;
      for (Square tempSquare : squaresToBeOccupied) {
         tempX = tempSquare.getPosX();
         tempY = tempSquare.getPosY();
         playerBoard.getOcean()[tempX][tempY].setWithShip(true);
      }
      player.addShip(new Ship(shipType, squaresToBeOccupied));
   }

   /**
    * Validates if Ship object (its estimated Squares) lies within board boundaries.
    *
    * @param estimatedShipSquares - list of Square objects composing a Ship to be placed (Ship proposition),
    * @param playerBoard          - player Square board,
    * @return true if the tested Ship object Squares are within board boundaries.
    */
   private boolean isShipOnBoard(List<Square> estimatedShipSquares, Board playerBoard) {
      for (Square testedSquare : estimatedShipSquares) {
         if (!playerBoard.isSquareOnBoard(testedSquare)) return false;
      }
      return true;
   }

   /**
    * Square filter. Limits the list of Square objects to only within board boundaries.
    *
    * @param allNeighbouringPositions - list of Square objects of all directly neighbouring a ship with the ship coordinates included,
    * @param playerBoard              - player Square board,
    * @return only on board squares.
    */
   private List<Square> isOnBoard(List<Square> allNeighbouringPositions, Board playerBoard) {
      List<Square> squaresOnBoard = new ArrayList<>();
      for (Square testedSquare : allNeighbouringPositions) {
         if (playerBoard.isSquareOnBoard(testedSquare)) {
            squaresOnBoard.add(testedSquare);
         }
      }
      return squaresOnBoard;
   }

   /**
    * Provides List of Square objects used as placeholders for coordinates.
    *
    * @param squareList - list of Square objects composing a Ship to be placed (Ship proposition),
    * @return list of all neighbouring Square objects to perform limiter isOnBoard method on.
    */
   private List<Square> allSquaresToCheck(List<Square> squareList) {
      List<Square> allSquaresToCheck = new ArrayList<>();
      int tempX, tempY;
      for (Square tempSquare : squareList) {
         tempX = tempSquare.getPosX();
         tempY = tempSquare.getPosY();
         allSquaresToCheck.addAll(allNeighbouringPositions(tempX, tempY));
      }
      return allSquaresToCheck;
   }

   /**
    * Provides 3x3 grid List (9 elements) of Square objects used as placeholders for coordinates.
    *
    * @param x - location x coordinate,
    * @param y - location y coordinate,
    * @return list of all neighbouring positions (initial position included).
    */
   private List<Square> allNeighbouringPositions(int x, int y) {
      ArrayList<Square> listOfAllNeighbouringPositions = new ArrayList<>();
      int X, Y, tempX, tempY;
      X = x - 1;
      Y = y - 1;
      for (int i = 0; i < 3; i++) {
         tempX = X;
         tempX += i;
         for (int j = 0; j < 3; j++) {
            tempY = Y;
            tempY += j;
            listOfAllNeighbouringPositions.add(new Square(tempX, tempY));
         }
      }
      return listOfAllNeighbouringPositions;
   }

   /**
    * Returns squares to be occupied by the ship.
    *
    * @param coordinates - integer array (2 elements) consisting of chosen coordinates (x, y),
    * @param direction   - vertical or horizontal,
    * @param shipType    - ShipType of the ship to be placed on board,
    * @return list of Square objects to be occupied by the ship.
    */
   private List<Square> squaresToBeOccupied(int[] coordinates, Direction direction, ShipType shipType) {
      List<Square> squareList = new ArrayList<>();
      int x, y, length;
      x = coordinates[0];
      y = coordinates[1];
      length = shipType.getLength();
      for (int i = 0; i < length; i++) {
         Square tempSquare;
         if (direction == Direction.VERTICAL) {
            tempSquare = new Square(x + i, y);
         } else {
            tempSquare = new Square(x, y + i);
         }
         squareList.add(tempSquare);
      }
      return squareList;
   }
}