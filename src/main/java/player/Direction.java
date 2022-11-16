package player;

import utils.RandomNumberProvider;

import java.util.List;

public enum Direction {
   UP(0, -1, 'v'),
   DOWN(0, 1, 'v'),
   RIGHT(1, 0, 'h'),
   LEFT(-1, 0, 'h');
   public final int dirX;
   public final int dirY;
   private final char horizontalOrVertical;
   RandomNumberProvider random = new RandomNumberProvider();
   private final List<Direction> directions = List.of(Direction.values());

   Direction(int dirX, int dirY, char horizontalOrVertical) {
      this.dirX = dirX;
      this.dirY = dirY;
      this.horizontalOrVertical = horizontalOrVertical;
   }

   public List<Direction> getDirections() {
      return directions;
   }

   public Direction randomizeDirection() {
      int randomDirectionNumber = random.randomIntegerGenerator(3);
      return directions.get(randomDirectionNumber);
   }
}