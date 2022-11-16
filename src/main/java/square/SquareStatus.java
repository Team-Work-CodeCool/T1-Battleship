package square;

public enum SquareStatus {
   EMPTY("\033[1;34m░▒█"),
   SHIP("\033[1;97m███"),
   HIT("\033[1;91m[X]"),
   NEUTRALIZED("\033[1;95mXXX"),
   MISSED("\033[1;93m(-)");


   private final String statusChar;

   SquareStatus(String statusChar) {
      this.statusChar = statusChar;
   }

   public String getCharacter() {
      return statusChar;
   }

}


//empty, destroyed, hit ❌, missed Ⓜ)