import logic.Game;
import utils.CreateFile;
import utils.Display;
import utils.HiScores;
import utils.Input;

public class Battleship {
   private static final String EXIT_GAME_MESSAGE = "The game will now exit - bye bye!";

   public static void main(String[] args) {
      final Input userInput = new Input();
      final Display display = new Display();
      final CreateFile createFile = new CreateFile();
      final HiScores hiScores = new HiScores();
      createFile.createFile();
      Game game = new Game();
      display.printIntro();

      boolean playing = true;
      while (playing) {
         display.printMainMenu();
         switch (userInput.checkUserInput(1, 3)) {
            case 1:
               playing = game.playGame();
               break;
            case 2:
               Display.fakeCLS();
               display.displayScore(hiScores.loadScore());
               display.waitForEnter();


               break;
            case 3:
               display.inform(EXIT_GAME_MESSAGE);
               playing = false;
               break;
            default:
               break;
         }
      }
   }
}