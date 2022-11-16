package utils;

import java.io.File;
import java.io.IOException;


public class CreateFile {

   private static final String FILE_PATH = "High Scores";

   @SuppressWarnings("ResultOfMethodCallIgnored")
   public void createFile() {
      try {
         File file = new File(FILE_PATH);
         if (!file.exists()) {
            file.createNewFile();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
