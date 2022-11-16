package board;

public enum Direction {
    VERTICAL("v"),
    HORIZONTAL("h"),
    NONE("n");

    private final String name;

    Direction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // To think about
    public Direction fromString(String text) {
        for (Direction direction : Direction.values()) {
            if (direction.getName().equalsIgnoreCase(text)) {
                return direction;
            }
        }
        return null;
    }
}
