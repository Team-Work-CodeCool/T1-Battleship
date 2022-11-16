package player;

public enum Players {

    PLAYER1(1),
    PLAYER2(2);

    final int playerNumber;

    Players(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
