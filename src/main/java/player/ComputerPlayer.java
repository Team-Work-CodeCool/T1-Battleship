package player;

import square.Square;
import square.SquareStatus;

import java.util.ArrayList;
import java.util.Collections;

public class ComputerPlayer extends Player {
   //   TODO to enum with that!
   private static final String LEFT = "LEFT";
   private static final String RIGHT = "RIGHT";
   private static final String DOWN = "DOWN";
   private static final String UP = "UP";
   private final ArrayList<Square> listOfAvailableFields = new ArrayList<>();
   private final ArrayList<Square> listOfExcludedFields = new ArrayList<>();
   private final ArrayList<Square> listOfFieldsPriorToShot = new ArrayList<>();
   private boolean hard = false;
   private int[] lastHit = {-1, -1};
   private int[] redirectHit = {-1, -1};
   private String direction = "NONE";

   public ComputerPlayer(int playerNumber) {
      super(playerNumber);
      fieldsListGenerator();
   }

//   TODO refactor with Stream?
   private void fieldsListGenerator() {
      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            listOfAvailableFields.add(new Square(i, j));
         }
      }
      Collections.shuffle(listOfAvailableFields);
   }

   public void listOfFieldsPriorToShotResetter() {
      listOfFieldsPriorToShot.clear();
   }

   public void setHard(boolean hard) {
      this.hard = hard;
   }

   public void setLastHit(int[] lastHit) {
      this.lastHit = lastHit;
   }

   public void setRedirectHit(int[] redirectHit) {
      this.redirectHit = redirectHit;
   }

   public void setDirection(String direction) {
      this.direction = direction;
   }

   public boolean isLastHitSaved() {
      return lastHit[0] != -1 && lastHit[1] != -1;
   }

   public boolean isRedirectHitSaved() {
      return redirectHit[0] != -1 && redirectHit[1] != -1;
   }

   public boolean isDirectionSaved() {
      return !direction.equals("NONE");
   }

//   TODO refactor based on artificial player difficulty (in classes).
   public int[] provideComputerCoordinates() {
      if (!hard) {
         return availableCoordinates(listOfAvailableFields);
      } else {
         if (listOfFieldsPriorToShot.isEmpty()) {
            return availableCoordinates(listOfAvailableFields);
         } else {
            return availableCoordinates(listOfFieldsPriorToShot);
         }
      }
   }

   private int[] availableCoordinates(ArrayList<Square> listOfAvailableFields) {
      int[] tempArr = new int[2];
      Square tempSquare = listOfAvailableFields.get(0);
      tempArr[0] = tempSquare.getPosX();
      tempArr[1] = tempSquare.getPosY();
      listOfAvailableFields.remove(tempSquare);
      return tempArr;
   }

   public void setListOfFieldsPriorToShot(Player oppositePlayer, int[] coordinates) {
      int x, y;
      x = coordinates[0];
      y = coordinates[1];
      SquareStatus squareStatus = oppositePlayer.getOcean().getOcean()[x][y].getSquareStatus();
      if (squareStatus == SquareStatus.HIT) {
         squaresToShootAtFirst(x, y);
         squaresToShootAtFilter();
      } else if (squareStatus == SquareStatus.NEUTRALIZED) {
         listOfExcludedFields.addAll(listOfFieldsPriorToShot);
         listOfFieldsPriorToShot.clear();
      }
   }

   //   TODO foreach through enums refactor
   private void squaresToShootAtFirst(int x, int y) {
      listOfFieldsPriorToShot.add(new Square(x - 1, y));
      listOfFieldsPriorToShot.add(new Square(x + 1, y));
      listOfFieldsPriorToShot.add(new Square(x, y - 1));
      listOfFieldsPriorToShot.add(new Square(x, y + 1));
      Collections.shuffle(listOfFieldsPriorToShot);
   }

   private void squaresToShootAtFilter() {
      ArrayList<Square> tempList = (ArrayList) listOfFieldsPriorToShot.clone();
      listOfFieldsPriorToShot.clear();
      for (Square testedSquare : tempList) {
         if (inBoardFilter(testedSquare)) listOfFieldsPriorToShot.add(testedSquare);
      }
   }

   private boolean inBoardFilter(Square testedSquare) {
      int tempX, tempY;
      tempX = testedSquare.getPosX();
      tempY = testedSquare.getPosY();
      return tempX >= 0 && tempX < 10 && tempY >= 0 && tempY < 10;
   }

   public void determineDirection(int[] coordinates) {
      int testX = coordinates[0] - lastHit[0];
      int testY = coordinates[1] - lastHit[1];

      if (testX == 0 && testY < 0) {
         direction = LEFT;
      } else if (testX == 0 && testY > 0) {
         direction = RIGHT;
      } else if (testX > 0 && testY == 0) {
         direction = DOWN;
      } else if (testX < 0 && testY == 0) {
         direction = UP;
      }
   }

   public void gatherSquaresIfHit(int[] coordinates) {
      switch (direction) {
         case RIGHT -> directionRight(coordinates);
         case LEFT -> directionLeft(coordinates);
         case UP -> directionUp(coordinates);
         case DOWN -> directionDown(coordinates);
      }
   }

   public void gatherSquaresIfMiss() {
      switch (direction) {
         case RIGHT -> {
            direction = LEFT;
            directionLeft(redirectHit);
         }
         case LEFT -> {
            direction = RIGHT;
            directionRight(redirectHit);
         }
         case UP -> {
            direction = DOWN;
            directionDown(redirectHit);
         }
         case DOWN -> {
            direction = UP;
            directionUp(redirectHit);
         }
      }
   }

   //need if missed on the end of a series method to switch directions
   private void directionRight(int[] coordinates) {
      int[] testCord;
      Square testSquare;
      testCord = new int[]{coordinates[0], coordinates[1] + 1};
      testSquare = findSquareByCoordinates(testCord);
      if (testSquare == null || testSquare.isWithMiss()) {
         direction = RIGHT;
         directionLeft(redirectHit);
      }
      if (testSquare != null && !testSquare.isWithMiss()) listOfFieldsPriorToShot.add(testSquare);
   }

   private void directionLeft(int[] coordinates) {
      int[] testCord;
      Square testSquare;
      testCord = new int[]{coordinates[0], coordinates[1] - 1};
      testSquare = findSquareByCoordinates(testCord);
      if (testSquare == null || testSquare.isWithMiss()) {
         direction = LEFT;
         directionRight(redirectHit);
      }
      if (testSquare != null && !testSquare.isWithMiss()) listOfFieldsPriorToShot.add(testSquare);
   }

   private void directionUp(int[] coordinates) {
      int[] testCord;
      Square testSquare;
      testCord = new int[]{coordinates[0] - 1, coordinates[1]};
      testSquare = findSquareByCoordinates(testCord);
      if (testSquare == null || testSquare.isWithMiss()) {
         direction = UP;
         directionDown(redirectHit);
      }
      if (testSquare != null && !testSquare.isWithMiss()) listOfFieldsPriorToShot.add(testSquare);
   }

   private void directionDown(int[] coordinates) {
      int[] testCord;
      Square testSquare;
      testCord = new int[]{coordinates[0] + 1, coordinates[1]};
      testSquare = findSquareByCoordinates(testCord);
      if (testSquare == null || testSquare.isWithMiss()) {
         direction = DOWN;
         directionUp(redirectHit);
      }
      if (testSquare != null && !testSquare.isWithMiss()) listOfFieldsPriorToShot.add(testSquare);
   }

   private Square findSquareByCoordinates(int[] coordinates) {
      Square testedSquare;
      int x, y;
      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            testedSquare = getOcean().getOcean()[i][j];
            x = testedSquare.getPosX();
            y = testedSquare.getPosY();
            if (x == coordinates[0] && y == coordinates[1]) return testedSquare;
         }
      }
      return null;
   }

   private int[] squareToCoordinates(Square square) {
      int[] tempArr = new int[2];
      tempArr[0] = square.getPosX();
      tempArr[1] = square.getPosY();
      return tempArr;
   }
}