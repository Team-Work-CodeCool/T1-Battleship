package utils;

import board.Direction;

import java.util.Scanner;

public class Input {
    public static final String INVALID_USER_INPUT_MESSAGE = "Invalid input, please try again:";
    public static final String PRESS_ENTER = "Press ENTER to continue.";
    private static final String ENTER_X_MESSAGE = "Enter X coordinate:";
    private static final String ENTER_Y_MESSAGE = "Enter Y coordinate:";
    private static final String ASK_FOR_DIR_LITERAL = "Type [ H ] to choose horizontal placement or [ V ] to choose vertical.";
    private static final String ASK_FOR_SAVING = "Do you want to save your score? Type [ Y ] or [ N ] ";
    private static final String ASK_FOR_NAME = "Enter your name:";
    private static final Scanner scanner = new Scanner(System.in);
    private final Display display = new Display();

    /**
     * Collects, validates and passes user integer value input.
     *
     * @param bottom - bottom border for integer validation,
     * @param top    - top border for integer validation,
     * @return validated integer value.
     */
    public int checkUserInput(int bottom, int top) {
        int userChoice;
        while (true) {
            if (scanner.hasNextInt()) {
                userChoice = scanner.nextInt();
                if (userChoice >= bottom && userChoice <= top) {
                    return userChoice;
                } else {
                    display.instruct(INVALID_USER_INPUT_MESSAGE);
                }
            } else {
                scanner.next();
                display.instruct(INVALID_USER_INPUT_MESSAGE);
            }
        }
    }

//    TODO do not ask for h/v if ship length.equals(1) square.

    /**
     * Collects, validates and passes user string value input.
     *
     * @return ship placement direction based on proper string input.
     */
    public Direction askForDirection() {
        display.inform(ASK_FOR_DIR_LITERAL);
        Direction direction;
        while (true) {
            if (scanner.hasNext()) {
                String answer = scanner.next().toLowerCase();
                if (answer.matches("^h$") || answer.matches("^v$")) {
                    if (answer.equals("h")) {
                        direction = Direction.HORIZONTAL;
                        return direction;
                    } else if (answer.equals("v")) {
                        direction = Direction.VERTICAL;
                        return direction;
                    }
                } else {
                    display.instruct(INVALID_USER_INPUT_MESSAGE);
                }
            } else {
                scanner.next();
                display.instruct(INVALID_USER_INPUT_MESSAGE);
            }
        }
    }

    public int[] askForCoordinates() {
        display.inform(ENTER_X_MESSAGE);
        int x = checkUserInput(1, 10) - 1;
        display.inform(ENTER_Y_MESSAGE);
        int y = checkUserInput(1, 10) - 1;
        return new int[]{x, y};
    }


    public String askForSaving() {
        display.inform(ASK_FOR_SAVING );
        while (true) {
            if (scanner.hasNext()) {
                String answer = scanner.next().toLowerCase();
                if (answer.matches("^y$") || answer.matches("^n$")) {
                    if (answer.equals("y")) {
                        display.inform(ASK_FOR_NAME);
                        return scanner.next();
                    } else if (answer.equals("n")) {
                        return null;
                    }
                } else {
                    display.instruct(INVALID_USER_INPUT_MESSAGE);
                }
            } else {
                scanner.next();
                display.instruct(INVALID_USER_INPUT_MESSAGE);
            }
        }
    }
}

