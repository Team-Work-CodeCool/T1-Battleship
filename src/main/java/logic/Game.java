package logic;

import board.Board;
import board.BoardFactory;
import player.ComputerPlayer;
import player.Player;
import ship.Ship;
import square.Square;
import utils.*;

import java.util.List;

public class Game {
   private static final String ENDGAME_DISPLAY_INFORM_LITERAL = "\n                  Do you want to go back to main menu?";
   private static final String ENDGAME_DISPLAY_EMPTYSTRING_LITERAL_1 = "                    [ \u001B[34m1\u001B[0m ] - Back to main menu.";
   private static final String ENDGAME_DISPLAY_EMPTYSTRING_LITERAL_2 = "                    [ \u001B[34m2\u001B[0m ] - Exit the game.";
   private static final String BACK_TO_MAIN = "Backing to main menu.";
   private static final String FIRING_ROUND_MESSAGE = " firing round starts now!";
   private static final String YOUR_SHIPS_MESSAGE = "Your ships on board:";
   private static final String ENEMY_SHIPS_MESSAGE = "Enemy ships on board:";
   private static final String NEUTRALISED_MESSAGE = " has been neutralized!";
   private static final String HIT_MESSAGE = " has been hit!";
   private static final String SQUARE_ALREADY_SHOT = "Square already shot - change your aim!";
   private static final String PERFORM_ADDITIONAL_SHOT = "Perform your additional shot!";
   private final Player[] players = new Player[2];
   private final ComputerPlayer[] computerPlayers = new ComputerPlayer[2];
   private final Input input = new Input();
   private final Display display = new Display();
   private final BoardFactory boardFactory = new BoardFactory();
   private int roundsPlayed = 0;
   private final HiScores hiScores = new HiScores();
   private final Timer timer = new Timer();

   /**
    * Holds turn based gameplay for two player battleships board game.
    *
    * @return end game information.
    */
   public boolean playGame() {
//      TODO refactor to always create only two objects of specific class (2x arti 1 arti + 1 human or 2x human players)
      roundSetup();
      boolean bothPlayersAlive = true;
      int currentPlayer = 0;
      int oppositePlayer = 1;
      timer.start();
      while (bothPlayersAlive) {
         switch (currentPlayer) {
            case 0 -> {
               playRound(currentPlayer);
               bothPlayersAlive = players[oppositePlayer].isAlive();
               if (bothPlayersAlive) {
                  currentPlayer = 1;
                  oppositePlayer = 0;
               }
            }
            case 1 -> {
               playRound(currentPlayer);
               bothPlayersAlive = players[oppositePlayer].isAlive();
               if (bothPlayersAlive) {
                  currentPlayer = 0;
                  oppositePlayer = 1;
               }
            }
         }
      }
      timer.stop();
      display.printResult(currentPlayer);
      String playerName = input.askForSaving();
      if (playerName != null) {
         hiScores.saveScore(playerName, HiScores.FILE_NAME, roundsPlayed, hiScores.getAllLivesLeft(players, currentPlayer), timer.getSeconds());
      }
      return endGame();
   }

   private void roundSetup() {
      players[0] = new Player(1);
      players[1] = new Player(2);
      computerPlayers[0] = new ComputerPlayer(1);
      computerPlayers[1] = new ComputerPlayer(2);
      computerPlayerChoice();
      computerPlayerDifficulty();
      boardArrangement();
   }

   private void computerPlayerChoice() {
      display.selectComputerPlayerMenu(1);
      selectComputerPlayer(0, input.checkUserInput(1, 2));
      display.selectComputerPlayerMenu(2);
      selectComputerPlayer(1, input.checkUserInput(1, 2));
   }

   private void computerPlayerDifficulty() {
      if (players[0].isComputerPlayer() || players[1].isComputerPlayer()) {
         display.setComputerPlayerDifficultyMenu();
         setComputerPlayerDifficulty(input.checkUserInput(1, 2));
      }
   }

   private void boardArrangement() {
      if (!players[0].isComputerPlayer() || !players[1].isComputerPlayer()) {
         display.selectArrangementMenu();
         selectArrangementAndPlaceShipsOnBoard(input.checkUserInput(1, 2));
      } else {
         selectArrangementAndPlaceShipsOnBoard(2);
      }
   }

   /**
    * Holds single turn gameplay order.
    *
    * @param currentPlayer - currently active player.
    */
   private void playRound(int currentPlayer) {
      display.inform("\033[0;37m Player " + (currentPlayer + 1) + FIRING_ROUND_MESSAGE);
      if (currentPlayer == 0) roundsPlayed++;
      int oppositePlayer = oppositePlayer(currentPlayer);
      Board opponentsBoard = players[oppositePlayer].getOcean();
      display.printBoard(opponentsBoard, oppositePlayer, false);
      shipStatusDisplay(currentPlayer, oppositePlayer);
      shotValidator(currentPlayer, opponentsBoard);
      shipStatusDisplay(currentPlayer, oppositePlayer);
      endRoundStatement(currentPlayer);
   }

   private void shipStatusDisplay(int currentPlayer, int oppositePlayer) {
      display.inform(YOUR_SHIPS_MESSAGE);
      display.shipListPrintIterator(players[currentPlayer]);
      display.inform(ENEMY_SHIPS_MESSAGE);
      display.shipListPrintIterator(players[oppositePlayer]);
   }

   private void endRoundStatement(int currentPlayer) {
      if (players[currentPlayer].isComputerPlayer()) display.simulateThinking();
      else {
         display.inform(Input.PRESS_ENTER);
         display.waitForEnter();
      }
   }

   /**
    * Holds shot validation based on appropriate rules.
    *
    * @param currentPlayer  - currently active player,
    * @param opponentsBoard - opposite players Square board.
    */
   private void shotValidator(int currentPlayer, Board opponentsBoard) {
      int[] coordinates;
      int x, y;
      boolean isValid = false;
      while (!isValid) {
         coordinates = playerCoordinatesProvider(currentPlayer);
         System.out.println(coordinates[0] + " " + coordinates[1]);
         x = coordinates[0];
         y = coordinates[1];
         int oppositePlayer = oppositePlayer(currentPlayer);
         if (players[currentPlayer].performShotAtSquare(opponentsBoard.getOcean()[x][y])) {
            Ship testedShip = players[currentPlayer].performShotAtShip(players[oppositePlayer(currentPlayer)].getShips(), x, y);
            firingRoundProvider(testedShip, currentPlayer, oppositePlayer, opponentsBoard, coordinates);
            isValid = true;
         } else {
            display.instruct(SQUARE_ALREADY_SHOT);
            missComputerPlayer(currentPlayer);
            display.printBoard(opponentsBoard, oppositePlayer, false);
         }
      }
   }

   private int[] playerCoordinatesProvider(int currentPlayer) {
      if (players[currentPlayer].isComputerPlayer()) {
         display.simulateThinking();
         return computerPlayers[currentPlayer].provideComputerCoordinates();
      } else return input.askForCoordinates();
   }

   private void firingRoundProvider(Ship testedShip, int currentPlayer, int oppositePlayer, Board opponentsBoard, int[] coordinates) {
      if (testedShip != null) {
         if (testedShip.isSunk()) {
            shipSunk(testedShip, oppositePlayer);
            shipSunkComputerPlayer(currentPlayer);
         } else if (isShipHit(testedShip.getSquares(), coordinates[0], coordinates[1])) {
            shipHit(testedShip);
            shipHitComputerPlayer(currentPlayer, oppositePlayer, coordinates);
         }
         additionalShot(currentPlayer, opponentsBoard, oppositePlayer);
      } else {
         miss(opponentsBoard, oppositePlayer);
         missComputerPlayer(currentPlayer);
      }
   }

   private void shipSunk(Ship testedShip, int oppositePlayer) {
      display.printShots();
      display.printNeutralized();
      display.inform(testedShip.getShipType().getName() + NEUTRALISED_MESSAGE);
      players[oppositePlayer].removeShipIfNeutralised();
   }

   private void shipSunkComputerPlayer(int currentPlayer) {
      if (players[currentPlayer].isComputerPlayer()) {
//            AI clear last hit, next hit, direction and array.
         int[] hitReset = {-1, -1};
         computerPlayers[currentPlayer].setLastHit(hitReset);
         computerPlayers[currentPlayer].setRedirectHit(hitReset);
         computerPlayers[currentPlayer].setDirection("NONE");
         computerPlayers[currentPlayer].listOfFieldsPriorToShotResetter();
      }
   }

   private void shipHit(Ship testedShip) {
      display.printShots();
      display.printHit();
      display.inform(testedShip.getShipType().getName() + HIT_MESSAGE);
   }

   private void shipHitComputerPlayer(int currentPlayer, int oppositePlayer, int [] coordinates) {
      if (players[currentPlayer].isComputerPlayer()) {
//            proceed with AI when hit, first hit assign the last hit value, second hit assign last hit value and direction
//            first hit, write coordinates for firstHit, create and shot the list of 4squares, next hit, determine direction, gather coordinates
         if (!computerPlayers[currentPlayer].isLastHitSaved() && !computerPlayers[currentPlayer].isRedirectHitSaved()) {
            computerPlayers[currentPlayer].setLastHit(coordinates);
            computerPlayers[currentPlayer].setRedirectHit(coordinates); //for determining coords when direction changes DO NOT ASSIGN ANYMORE
//                  set next hit and operate on next hit to determine switching direction if needed based on miss
            computerPlayers[currentPlayer].setListOfFieldsPriorToShot(players[oppositePlayer], coordinates);
         } else if (computerPlayers[currentPlayer].isLastHitSaved() && computerPlayers[currentPlayer].isRedirectHitSaved()) {
//                  next hit may be not needed, determine direction, gather squares,
            computerPlayers[currentPlayer].listOfFieldsPriorToShotResetter();
            computerPlayers[currentPlayer].determineDirection(coordinates);
            computerPlayers[currentPlayer].gatherSquaresIfHit(coordinates);
            computerPlayers[currentPlayer].setLastHit(coordinates);
         }
      }
   }

   private void miss(Board opponentsBoard, int oppositePlayer) {
      display.printShots();
      display.printMiss();
      display.printBoard(opponentsBoard, oppositePlayer, false);
   }

   private void missComputerPlayer(int currentPlayer) {
      if (players[currentPlayer].isComputerPlayer()) {
         if (computerPlayers[currentPlayer].isLastHitSaved() && computerPlayers[currentPlayer].isDirectionSaved()) {
            computerPlayers[currentPlayer].gatherSquaresIfMiss();
         }
      }
   }

   private void additionalShot(int currentPlayer, Board opponentsBoard, int oppositePlayer) {
      display.printBoard(opponentsBoard, oppositePlayer, false);
      if (hiScores.getAllLivesLeft(players, oppositePlayer) != 0) {
         display.inform(PERFORM_ADDITIONAL_SHOT);
         shotValidator(currentPlayer, opponentsBoard);
      }
   }

   /**
    * Tests if any square of the ship is hit.
    *
    * @param testedShip - list of Square objects that create tested ship,
    * @param x          - location x coordinate,
    * @param y          - location y coordinate,
    * @return true if any of the tested Ship object squares were hit.
    */
   private boolean isShipHit(List<Square> testedShip, int x, int y) {
      int tempX, tempY;
      for (Square testedSquare : testedShip) {
         tempX = testedSquare.getPosX();
         tempY = testedSquare.getPosY();
         if (tempX == x && tempY == y) return true;
      }
      return false;
   }

   //    TODO boolean?
   private int oppositePlayer(int currentPlayer) {
      return Math.abs(currentPlayer - 1);
   }

   /**
    * Holds end game summary.
    *
    * @return true if decided to go back to main menu, false if decided to quit the game.
    */
   private boolean endGame() {
      display.inform(ENDGAME_DISPLAY_INFORM_LITERAL);
      display.emptyString(ENDGAME_DISPLAY_EMPTYSTRING_LITERAL_1);
      display.emptyString(ENDGAME_DISPLAY_EMPTYSTRING_LITERAL_2);
      switch (input.checkUserInput(1, 2)) {
         case 1 -> {
            display.inform(BACK_TO_MAIN);
            Display.fakeCLS();
         }
         case 2 -> {
            display.inform("Bye!");
            return false;
         }
         default -> display.instruct(Input.INVALID_USER_INPUT_MESSAGE);
      }
      return true;
   }

   private void selectComputerPlayer(int player, int userInput) {
      if (userInput == 1) players[player].setComputerPlayer(true);
   }

   public void selectArrangementAndPlaceShipsOnBoard(int userInput) {
      if (players[0].isComputerPlayer() || userInput == 2)
         players[0].getOcean().setAutomaticArrangement(true);
      if (players[1].isComputerPlayer() || userInput == 2)
         players[1].getOcean().setAutomaticArrangement(true);
      boardFactory.shipPlacementPerPlayer(players[0], players[0].getOcean());
      boardFactory.shipPlacementPerPlayer(players[1], players[1].getOcean());
   }

   private void setComputerPlayerDifficulty(int userInput) {
      if (userInput == 2) {
         computerPlayers[0].setHard(true);
         computerPlayers[1].setHard(true);
      }
   }
}