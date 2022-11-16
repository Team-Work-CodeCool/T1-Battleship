package utils;

import player.Player;
import ship.Ship;

import java.io.*;
import java.util.*;

public class HiScores {
   public static final String FILE_NAME = "High Scores";
   //    TODO model class!
   public List<List<String>> listOfScores = new ArrayList<>();
   public List<String> singleScore = new ArrayList<>();
   String list;
   Display display = new Display();

   public void saveScore(String playerName, String fileName, int roundsPlayed, int allLivesLeft, long timeSeconds) {
      try (FileWriter file = new FileWriter(fileName, true); BufferedWriter writer = new BufferedWriter(file)) {
         writer.write(playerName + " " + roundsPlayed + " " + allLivesLeft + " " +
               scoreCalculation(roundsPlayed, allLivesLeft, timeSeconds) + " " + timeSeconds + "\n");
      } catch (IOException exception) {
         exception.printStackTrace();
      }
   }

   private int scoreCalculation(int roundsPlayed, int allLivesLeft, long timeSeconds) {
      return (int) (10000 * (allLivesLeft * (roundsPlayed / 2)) - (timeSeconds * 10));
   }

   public int getAllLivesLeft(Player[] players, int currentPlayer) {
      List<Ship> playerShips = players[currentPlayer].getShips();
      int allLivesLeft = 0;
      for (Ship ship : playerShips) {
         int livesLeft = ship.getLivesLeft();
         allLivesLeft = allLivesLeft + livesLeft;
      }
      return allLivesLeft;
   }

   public List<List<String>> loadScore() {
      try (FileInputStream scoreFile = new FileInputStream(FILE_NAME); Scanner scanner = new Scanner(scoreFile)) {
         listOfScores.clear();
         if (scanner.hasNextLine()) {
            while (scanner.hasNext()) {
               for (int i = 0; i < 5; i++) {
                  singleScore.add(scanner.next());
               }
               List<String> list = new ArrayList<>(singleScore);
               listOfScores.add(list);
               singleScore.clear();
            }
         }
         return listOfScores;
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
