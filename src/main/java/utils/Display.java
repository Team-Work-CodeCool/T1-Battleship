package utils;

import board.Board;
import player.Player;
import ship.Ship;
import ship.ShipType;
import square.SquareStatus;

import java.util.*;


public class Display {
   private static final String SAVED_SCORE_STRING_FORMAT = "Name: %-20s | Rounds: %3s | Lives: %3s | Score: %5d | Time in seconds: %5s | %n";
   private static final String PRESS_ENTER_EMPTY_SPACE = "\n                                  ";
   private static final String COMPUTER_PLAYER_DIFFICULTY_QUESTION = "Do you want easy or hard artificial intelligence mode?";
   private static final String DISPLAY_ASK_LITERAL = "\n            How do you want to place your ships on the board?";
   private static final String DISPLAY_EMPTY_STRING_FIRST_LITERAL = "                          [ \u001B[34m1\u001B[0m ] - Manually";
   private static final String DISPLAY_EMPTY_STRING_SECOND_LITERAL = "                          [ \u001B[34m2\u001B[0m ] - Automatically";

   public final String RESET = "\033[0m";

   public final String RED = "\033[0;31m";
   public final String GREEN = "\033[0;32m";
   public final String YELLOW = "\033[0;33m";
   public final String BLUE = "\033[0;34m";
   public final String PURPLE = "\033[0;35m";
   public final String CYAN = "\033[0;36m";
   public final String WHITE = "\033[0;37m";
   public final String PURPLE_BOLD = "\033[1;35m";
   public final String CYAN_BOLD = "\033[1;36m";
   public final String BLUE_BOLD = "\033[1;34m";
   public final String RED_BOLD_BRIGHT = "\033[1;91m";
   public final String GREEN_BOLD_BRIGHT = "\033[1;92m";
   public final String YELLOW_BOLD_BRIGHT = "\033[1;93m";
   public final String BLUE_BOLD_BRIGHT = "\033[1;94m";
   public final String PURPLE_BOLD_BRIGHT = "\033[1;95m";
   public final String CYAN_BOLD_BRIGHT = "\033[1;96m";
   public final String WHITE_BOLD_BRIGHT = "\033[1;97m";

   public static final String YELLOW_UNDERLINED = "\033[4;33m";

   private byte anim;
   private String lastLine = "";

   public void inform(String information) {
      System.out.printf("%s  %s%s%n", CYAN, information, RESET);
   }

   public void informSameLine(String information) {
      System.out.printf("%s  %s%s", CYAN, information, RESET);
   }

   public void instruct(String instruction) {
      System.out.printf("%s  %s%s%n", RED, instruction, RESET);
   }

   public void emptyString(String defaultString) {
      System.out.printf("%s%s%n", defaultString, RESET);
   }

   public void ask(String description) {
      System.out.printf("%s  %s%s%n", GREEN, description, RESET);
   }


   public void printBoard(Board boardToPrint, int playerNumber, boolean showAll) {
      String c0, c1, c2, c3;
//============================================================
// TO REFACTOR: meaningful names...                         ==
// AS OF NOW: c0-triangles, c1-indexes, c2-box, c3-shadow   ==
//============================================================
      if (playerNumber == 0) {
         c0 = WHITE;
         c1 = YELLOW_BOLD_BRIGHT;
         c2 = GREEN;
         c3 = BLUE_BOLD;
      } else {
         c0 = WHITE;
         c1 = WHITE_BOLD_BRIGHT;
         c2 = PURPLE;
         c3 = RED_BOLD_BRIGHT;
      }
      int size = boardToPrint.getOceanSideLength();
      System.out.print("\n      " + c1);
      for (int i = 0; i < size; i++)
         System.out.printf("%6s", "Y");
      System.out.print("\n      " + c0);
      for (int i = 0; i < size; i++)
         System.out.printf("%6s", "▽");
      System.out.print("\n      " + c1);
      for (int i = 1; i < size; i++)
         System.out.printf("%6d", i);
      System.out.printf("%7d", size);
      System.out.print("\n");
      displayLine(c2 + "        ╔══", "═══╤══", "═══╗", size);
      for (int i = 0; i < size - 1; i++) {
         System.out.printf(c1 + "%s%3d%s", "X" + c0 + " ▷" + c1, i + 1, c2 + "  ║ ");
         for (int j = 0; j < size - 1; j++) {
            if (!showAll && (!(boardToPrint.getOcean()[i][j].isRevealed())))
               System.out.print(SquareStatus.EMPTY.getCharacter() + c2 + c2 + " │ ");
            else System.out.print(boardToPrint.getOcean()[i][j].toString() + c2 + " │ ");
         }
         if (!showAll && (!(boardToPrint.getOcean()[i][size - 1].isRevealed())))
            System.out.print(SquareStatus.EMPTY.getCharacter() + c2 + " ");
         else System.out.print(boardToPrint.getOcean()[i][size - 1].toString() + c2 + " ");
         if (i == 0) System.out.print("╟" + c3 + "─┐\n" + c2);
         else System.out.print("║" + c3 + " │\n" + c2);
         displayLine("        ╟──", "───┼──", "───╢" + c3 + " │" + c2, size);
      }
      System.out.printf(c1 + "%s%3d%s", "X" + c0 + " ▷" + c1, size, c2 + "  ║ ");
      for (int k = 0; k < size - 1; k++) {
         if (!showAll && (!(boardToPrint.getOcean()[size - 1][k].isRevealed())))
            System.out.print(SquareStatus.EMPTY.getCharacter() + c2 + " │ ");
         else System.out.print(boardToPrint.getOcean()[size - 1][k].toString() + c2 + " │ ");
      }
      if (!showAll && (!(boardToPrint.getOcean()[size - 1][size - 1].isRevealed())))
         System.out.print(SquareStatus.EMPTY.getCharacter() + c2 + " ║" + c3 + " │\n" + c2);
      else System.out.print(boardToPrint.getOcean()[size - 1][size - 1].toString() + c2 + " ║" + c3 + " │\n" + c2);
      displayLine("        ╚╤═", "═══╧══", "═══╝" + c3 + " │", size);
      displayLine("         └─", "──────", "─────┘" + RESET, size);
   }

   private static void displayLine(String leftCorner, String line, String rightCorner, int width) {
      System.out.print(leftCorner);
      for (int i = 0; i < width - 1; i++) System.out.print(line);
      System.out.println(rightCorner);
   }

   public void printResult(int currentPlayer) {
      fakeCLS();
      printVictory();
      System.out.println("\n        Player " + "\u001B[35m" + (currentPlayer + 1) + "\u001B[0m" + " has won the game by destroying all enemy ships. Congratulations!\n");
   }

   public void printMainMenu() {
      fakeCLS();
      printGameTitle();
      System.out.println(PURPLE_BOLD_BRIGHT + "\n                    ░M░ ░A░ ░I░ ░N░   ░M░ ░E░ ░N░ ░U░");
      System.out.println(RED + "                     ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
      System.out.println(YELLOW + "                  " +
            "        [ " + BLUE + "1" + YELLOW + " ] - Start new game");
      System.out.println("                          [ " + BLUE_BOLD_BRIGHT + "2" + YELLOW + " ] - Show hi-score");
      System.out.println("                          [ " + BLUE_BOLD_BRIGHT + "3" + YELLOW + " ] - Exit the game");
      System.out.print(WHITE + "\n                t y p e   i n   y o u r   c h o i c e . . .");
   }

   public void printIntro() {
//        animateGameName();
//        printTeamONE();
   }

   private void animateGameName() throws RuntimeException {
      for (int i = 0; i < 21; i++) {
         char[] b = "battleships team one ".toCharArray();
         b[i] = Character.toUpperCase(b[i]);
         System.out.print("\r" + new String(b));
         waitFor(200);
      }
      for (int i = 0; i < 3; i++) {
         System.out.print("\rbattleships team one");
         waitFor(200);
         System.out.print("\rBATTLESHIPS TEAM ONE");
         waitFor(200);
      }
   }

   public void print(String line) {
      //clear the last line if longer
      if (lastLine.length() > line.length()) {
         String temp = "";
         for (int i = 0; i < lastLine.length(); i++) {
            temp += " ";
         }
         if (temp.length() > 1)
            System.out.print("\r" + temp);
      }
      System.out.print("\r" + line);
      lastLine = line;
   }

   public void simulateThinking() {
      // ===================================================================
      //     THIS IS FOR AI PLAYER THINKING  ;)
      for (int i = 0; i < 11; i += 1) {
         animate(i * 10 + "% ...thinking... ");
         waitFor(30);
      }
   }

   public void animate(String line) {
      switch (anim) {
         case 1 -> print("[ \\ ] " + line);
         case 2 -> print("[ | ] " + line);
         case 3 -> print("[ / ] " + line);
         default -> {
            anim = 0;
            print("[ - ] " + line);
         }
      }
      anim++;
   }

   private void printItSlow(String sayWhat, int delay) {
      for (int i = 0; i < sayWhat.length(); i++) {
         System.out.print(sayWhat.charAt(i));
         if (sayWhat.charAt(i) == ' ') continue;
         waitFor(delay);
      }
   }

   public static void fakeCLS() {
      for (int i = 0; i < 10; i++)
         System.out.println("\n\n\n\n");
   }

   public void printTeamONE() {
      fakeCLS();
      System.out.println(YELLOW_BOLD_BRIGHT);
      printItSlow("   ████████╗███████╗ █████╗ ███╗   ███╗     ██████╗ ███╗   ██╗███████╗", 3);
      System.out.println(BLUE_BOLD_BRIGHT);
      printItSlow("   ╚══██╔══╝██╔════╝██╔══██╗████╗ ████║    ██╔═══██╗████╗  ██║██╔════╝", 3);
      System.out.println(GREEN_BOLD_BRIGHT);
      printItSlow("      ██║   █████╗  ███████║██╔████╔██║    ██║   ██║██╔██╗ ██║█████╗  ", 3);
      System.out.println(YELLOW_BOLD_BRIGHT);
      printItSlow("      ██║   ██╔══╝  ██╔══██║██║╚██╔╝██║    ██║   ██║██║╚██╗██║██╔══╝  ", 3);
      System.out.println(BLUE_BOLD_BRIGHT);
      printItSlow("      ██║   ███████╗██║  ██║██║ ╚═╝ ██║    ╚██████╔╝██║ ╚████║███████╗", 3);
      System.out.println(PURPLE_BOLD_BRIGHT);
      printItSlow("      ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚═════╝ ╚═╝  ╚═══╝╚══════╝", 3);
      System.out.println();
      System.out.println(CYAN_BOLD_BRIGHT + "\n");
      printItSlow("             ..... A N N A .. P A N C E R Z .................", 4);
      System.out.println(GREEN + "\n");
      printItSlow("             ....... K A C P E R .. M I L A  ................", 4);
      System.out.println(CYAN_BOLD_BRIGHT + "\n");
      printItSlow("             ......... S E B A S T I A N .. S O S I N .......", 4);
      System.out.println(GREEN + "\n");
      printItSlow("             ........... M A R C I N .. S Z U W A L S K I ...", 4);
      System.out.println(CYAN_BOLD_BRIGHT + "\n");
      printItSlow("             ............. B A R T O S Z .. J .. W O L A K ..", 4);
      System.out.println(RESET);
      for (int i = 0; i < 2; i++) {
         System.out.println();
         waitFor(899 * i);
      }
      for (int i = 0; i < 2; i++) {
         System.out.println();
         waitFor(123);
      }
      for (int i = 0; i < 2; i++) {
         System.out.println();
         waitFor(234);
      }
      System.out.println(RED_BOLD_BRIGHT);
      printItSlow("             ...... press [ENTER] if you love Team ONE ......" + RESET, 10);
      waitForEnter();
   }

   public void printShots() {
      System.out.println("\n" + WHITE_BOLD_BRIGHT +
            "_____________  _______________________    _________________________________________     ______\n" +
            "__  ___/__  / / /_  __ \\__  __/_  ___/    ___  ____/___  _/__  __ \\__  ____/__  __ \\    ___  /\n" +
            "_____ \\__  /_/ /_  / / /_  /  _____ \\     __  /_    __  / __  /_/ /_  __/  __  / / /    __  / \n" +
            "____/ /_  __  / / /_/ /_  /   ____/ /     _  __/   __/ /  _  _, _/_  /___  _  /_/ /      /_/  \n" +
            "/____/ /_/ /_/  \\____/ /_/    /____/      /_/      /___/  /_/ |_| /_____/  /_____/      (_)   \n" +
            RESET);
   }

   public void printHit() {
      System.out.println("\n" + PURPLE_BOLD +
            "██╗████████╗███████╗     █████╗     ██╗  ██╗██╗████████╗██╗\n" +
            "██║╚══██╔══╝██╔════╝    ██╔══██╗    ██║  ██║██║╚══██╔══╝██║\n" +
            "██║   ██║   ███████╗    ███████║    ███████║██║   ██║   ██║\n" +
            "██║   ██║   ╚════██║    ██╔══██║    ██╔══██║██║   ██║   ╚═╝\n" +
            "██║   ██║   ███████║    ██║  ██║    ██║  ██║██║   ██║   ██╗\n" +
            "╚═╝   ╚═╝   ╚══════╝    ╚═╝  ╚═╝    ╚═╝  ╚═╝╚═╝   ╚═╝   ╚═╝\n" +
            RESET);
   }

   public void printMiss() {
      System.out.println("\n" + CYAN +
            "╦╔╦╗╔═╗  ╔═╗  ╔╦╗╦╔═╗╔═╗┬\n" +
            "║ ║ ╚═╗  ╠═╣  ║║║║╚═╗╚═╗│\n" +
            "╩ ╩ ╚═╝  ╩ ╩  ╩ ╩╩╚═╝╚═╝o\n" + RESET);
   }

   public void printNeutralized() {
      System.out.println("\n" + WHITE +
            " (        )  (    (         )                     (              (     (        )      (       ____ \n" + YELLOW_BOLD_BRIGHT +
            " )\\ )  ( /(  )\\ ) )\\ )   ( /(               *   ) )\\ )    (      )\\ )  )\\ )  ( /(      )\\ )   |   / \n" + RED +
            "(()/(  )\\())(()/((()/(   )\\()) (       (  ` )  /((()/(    )\\    (()/( (()/(  )\\()) (  (()/(   |  /  \n" + RED_BOLD_BRIGHT +
            " /(_))((_)\\  /(_))/(_)) ((_)\\  )\\      )\\  ( )(_))/(_))((((_)(   /(_)) /(_))((_)\\  )\\  /(_))  | /   \n" + RED +
            "(_))   _((_)(_)) (_))    _((_)((_)  _ ((_)(_(_())(_))   )\\ _ )\\ (_))  (_))   _((_)((_)(_))_   |/    \n" + YELLOW_BOLD_BRIGHT +
            "/ __| | || ||_ _|| _ \\  | \\| || __|| | | ||_   _|| _ \\  (_)_\\(_)| |   |_ _| |_  / | __||   \\ (      \n" + WHITE_BOLD_BRIGHT
            +
            "\\__ \\ | __ | | | |  _/  | .` || _| | |_| |  | |  |   /   / _ \\  | |__  | |   / /  | _| | |) |)\\     \n" + BLUE_BOLD_BRIGHT +
            "|___/ |_||_||___||_|    |_|\\_||___| \\___/   |_|  |_|_\\  /_/ \\_\\ |____||___| /___| |___||___/((_)    \n" + RESET);
   }

   public void printVictory() {
      System.out.println("\n" + YELLOW_BOLD_BRIGHT +
            "      888     888 8888888 .d8888b. 88888888888 .d88888b.  8888888b. Y88b   d88P 888 \n" +
            "      888     888   888  d88P  Y88b    888    d88P\" \"Y88b 888   Y88b Y88b d88P  888 \n" +
            "      888     888   888  888    888    888    888     888 888    888  Y88o88P   888 \n" +
            "      Y88b   d88P   888  888           888    888     888 888   d88P   Y888P    888 \n" +
            "       Y88b d88P    888  888           888    888     888 8888888P\"     888     888 \n" +
            "        Y88o88P     888  888    888    888    888     888 888 T88b      888     Y8P \n" +
            "         Y888P      888  Y88b  d88P    888    Y88b. .d88P 888  T88b     888      \"  \n" +
            "          Y8P     8888888 \"Y8888P\"     888     \"Y88888P\"  888   T88b    888     888 \n" +
            RESET);
   }

   public void waitForEnter() {
      Scanner s666 = new Scanner(System.in);
      String ave = s666.nextLine();
   }

   private void waitFor(int duration) {
      try {
         Thread.sleep(duration);
      } catch (InterruptedException e) {
         extracted(e);
      }
   }

   private void extracted(InterruptedException e) {
      throw new RuntimeException(e);
   }

   private void printGameTitle() {
      System.out.println("\n" + YELLOW_BOLD_BRIGHT +
            "\n" +
            "__________         __    __  .__           _________.__    .__              \n" +
            "\\______   \\_____ _/  |__/  |_|  |   ____  /   _____/|  |__ |__|_____  ______\n" +
            " |    |  _/\\__  \\\\   __\\   __\\  | _/ __ \\ \\_____  \\ |  |  \\|  \\____ \\/  ___/\n" +
            " |    |   \\ / __ \\|  |  |  | |  |_\\  ___/ /        \\|   Y  \\  |  |_> >___ \\ \n" +
            " |______  /(____  /__|  |__| |____/\\___  >_______  /|___|  /__|   __/____  >\n" +
            "        \\/      \\/                     \\/        \\/      \\/   |__|       \\/ ");
      System.out.println(GREEN_BOLD_BRIGHT +
            "              _           _       _                         _       \n" +
            "       ___   | |_ ___ _ _| |_ ___| |   ___ ___ _____ ___   | |_ _ _ \n" +
            "      | .'|  | . |  _| | |  _| .'| |  | . | .'|     | -_|  | . | | |\n" +
            "      |__,|  |___|_| |___|_| |__,|_|  |_  |__,|_|_|_|___|  |___|_  |\n" +
            "                                      |___|                    |___|");
      System.out.println(CYAN_BOLD_BRIGHT +
            "                __                          ____  _   _______\n" +
            "               / /____  ____ _____ ___     / __ \\/ | / / ___/\n" +
            "              / __/ _ \\/ __ `/ __ `__ \\   / / / /  |/ / __/   \n" +
            "             / /_/  __/ /_/ / / / / / /  / /_/ / /|  / /___   \n" +
            "             \\__/\\___/\\__,_/_/ /_/ /_/   \\____/_/ |_/_____/   \n" +
            "                                                   \n");
   }

   public void shipListPrintIterator(Player player) {
      List<Ship> ships = player.getShips();
      for (ShipType shipType : ShipType.values()) {
         shipTypeCounter(ships, shipType);
      }
      System.out.println();
   }

   private void shipTypeCounter(List<Ship> ships, ShipType shipType) {
      int count = 0;
      for (Ship testedShip : ships) {
         if (testedShip.getShipType() == shipType) {
            ++count;
         }
      }
      informSameLine(" " + shipType.getName() + " " + count + " ");
   }

   private void hallOfFame() {
      System.out.println(
            YELLOW_BOLD_BRIGHT + "                           ╦ ╦ ╔═╗ ╦   ╦     ╔═╗ ╔═╗   ╔═╗ ╔═╗ ╔╦╗ ╔═╗\n" +
                  GREEN_BOLD_BRIGHT + "                           ╠═╣ ╠═╣ ║   ║     ║ ║ ║╣    ║╣  ╠═╣ ║║║ ║╣\n" +
                  CYAN_BOLD_BRIGHT + "                           ╩ ╩ ╩ ╩ ╩═╝ ╩═╝   ╚═╝ ╚     ╚   ╩ ╩ ╩ ╩ ╚═╝\n"
      );
   }

   public void displayScore(List<List<String>> listOfScores) {
      StringBuilder stringBuilder = new StringBuilder();
//      TODO string format toString in Model class.
      for (List<String> singleScore : listOfScores) {
         String savedScore = String.format(SAVED_SCORE_STRING_FORMAT,
               singleScore.get(0), singleScore.get(1), singleScore.get(2), Integer.parseInt(singleScore.get(3)), singleScore.get(4));
         stringBuilder.append(savedScore);
      }
      hallOfFame();
      System.out.println(stringBuilder + RESET);
      inform(PRESS_ENTER_EMPTY_SPACE + Input.PRESS_ENTER);
   }


   public void selectArrangementMenu() {
      ask(DISPLAY_ASK_LITERAL);
      emptyString(DISPLAY_EMPTY_STRING_FIRST_LITERAL);
      emptyString(DISPLAY_EMPTY_STRING_SECOND_LITERAL);
   }

   public void selectComputerPlayerMenu(int playerNumber) {
      ask("Do you want player " + playerNumber + " to be computer guided?");
      emptyString("1: affirmative,");
      emptyString("2: negative.");
   }

   public void setComputerPlayerDifficultyMenu() {
      ask(COMPUTER_PLAYER_DIFFICULTY_QUESTION);
      emptyString("1: easy,");
      emptyString("2: hard.");
   }
}