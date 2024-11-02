package controller;

import model.*;
import view.GameView;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        view.getRollDiceButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextTurn(); 
            }
        });
    }

    public void startGame() {
        view.displayMessage("Jogo iniciado!");
        nextTurn();
    }

    private void nextTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        
        int roll = dice.roll();
        view.displayDiceRoll(dice.getDice1(), dice.getDice2());
        
        currentPlayer.move(roll);
        BoardPosition currentSpace = board.getSpace(currentPlayer.getPosition());
        currentSpace.onLand(currentPlayer);

        view.displayMessage(currentPlayer.getName() + " rolou " + roll + " e se moveu para a posição " + currentPlayer.getPosition());

        //checkGameEnd();

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        view.displayPlayerTurn(players.get(currentPlayerIndex));
    }

    //private void checkGameEnd() { ve se o jogo acabou, quem ganhou
    //}
}
