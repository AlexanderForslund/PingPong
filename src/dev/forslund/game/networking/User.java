package dev.forslund.game.networking;

public class User {
    private ClientManager clientManager;
    private int yCoordinate;

    public User(ClientManager clientManager) {
        this.clientManager = clientManager;
        this.yCoordinate = 10;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
