package controller;

import model.*;
import view.GameView;

import java.util.List;

public class GameController {
    private List<Player> players;
    private Board board;
    private Dice dice;
    private int currentPlayerIndex;
    private GameView view;

    public GameController(List<Player> players, GameView view) {
        this.players = players;
        this.board = new Board();
        this.dice = new Dice();
        this.view = view;
        this.currentPlayerIndex = 0;
    }

    public void startGame() {
        view.displayMessage("Jogo iniciado!");
        nextTurn();
    }

    private void nextTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        view.displayPlayerTurn(currentPlayer);

        int roll = dice.roll();
        view.displayDiceRoll(dice.getDie1(), dice.getDie2());
        currentPlayer.move(roll);
        Space currentSpace = board.getSpace(currentPlayer.getPosition());

        currentSpace.onLand(currentPlayer);

        checkGameEnd();

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    //private void checkGameEnd() { ve se o jogo acabou, quem ganhou
    //}
}
