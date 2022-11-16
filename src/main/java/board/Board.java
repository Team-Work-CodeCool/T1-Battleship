package board;

import square.Square;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Board {
   private static final int OCEAN_SIDE_LENGTH = 10;
   private final Square[][] ocean = new Square[OCEAN_SIDE_LENGTH][OCEAN_SIDE_LENGTH];
   private boolean automaticArrangement = false;

   public Board() {
      IntStream.range(0, OCEAN_SIDE_LENGTH)
            .forEach(x -> Arrays.setAll(ocean[x], y -> new Square(x, y)));
   }

   public int getOceanSideLength() {
      return OCEAN_SIDE_LENGTH;
   }

   public Square[][] getOcean() {
      return ocean;
   }

   public boolean isAutomaticArrangement() {
      return automaticArrangement;
   }

   public void setAutomaticArrangement(boolean automaticArrangement) {
      this.automaticArrangement = automaticArrangement;
   }

   protected boolean isSquareOnBoard(Square testedSquare) {
      int tempX, tempY;
      tempX = testedSquare.getPosX();
      tempY = testedSquare.getPosY();
      return tempX >= 0 && tempX < OCEAN_SIDE_LENGTH && tempY >= 0 && tempY < OCEAN_SIDE_LENGTH;
   }
}