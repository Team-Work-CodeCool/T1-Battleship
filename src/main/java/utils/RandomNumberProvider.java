package utils;

import board.Direction;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static java.security.SecureRandom.getInstanceStrong;

public class RandomNumberProvider {

   private static final String RANDOM_NUMBER_PROVIDER_ERROR_MESSAGE = "Failed to instantiate random number generator.";
   private final Random random;

   public RandomNumberProvider() {
      try {
         this.random = getInstanceStrong();
      } catch (
            NoSuchAlgorithmException e) {
         throw new RuntimeException(RANDOM_NUMBER_PROVIDER_ERROR_MESSAGE, e);
      }
   }

   public int[] randomCoordinatesGenerator() {
      return new int[]{random.nextInt(10), random.nextInt(10)};
   }

   public Direction randomizeDirection() {
      return Direction.values()[random.nextInt(2)];
   }

   public int randomIntegerGenerator(int range) {
      return random.nextInt(range);
   }
}
