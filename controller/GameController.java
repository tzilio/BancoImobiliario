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
        this.board = Board.getInstance(); // Usando o Singleton de Board
        this.dice = Dice.getInstance();   // Usando o Singleton de Dice
        this.view = view;
        this.currentPlayerIndex = 0;
        initializePlayers();
        setupRollDiceAction();
    }

    public void startGame() {
        view.displayMessage("Jogo iniciado!");
        displayCurrentPlayerTurn();
    }

    private void initializePlayers() {
        for (Player player : players) {
            player.setPosition(0);
            view.getBoardView().updatePlayerPosition(player, 0);
        }
    }

    private void setupRollDiceAction() {
        view.getRollDiceButton().addActionListener(e -> nextTurn());
    }

    private void nextTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);

        if (currentPlayer.isInJail()) {
            handleJailTurn(currentPlayer);
            moveToNextPlayer();
            return;
        }

        int roll = dice.roll();
        view.displayDiceRoll(dice.getDice1(), dice.getDice2());
        processPlayerMove(currentPlayer, roll);
        handleSpaceEffect(currentPlayer, board.getSpace(currentPlayer.getPosition()));
        moveToNextPlayer();
    }

    private void handleJailTurn(Player player) {
        view.displayMessage(player.getName() + " está na prisão.");
        int roll = dice.roll();
        view.displayDiceRoll(dice.getDice1(), dice.getDice2());

        if (dice.isDouble()) {
            player.setInJail(false);
            view.displayMessage(player.getName() + " tirou um número duplo e saiu da prisão!");
            nextTurn();
        } else {
            view.displayMessage(player.getName() + " não conseguiu sair da prisão.");
        }
    }

    private void processPlayerMove(Player player, int roll) {
        player.move(roll);
        view.getBoardView().updatePlayerPosition(player, player.getPosition());
        view.displayMessage(player.getName() + " rolou " + roll + " e se moveu para a posição " + player.getPosition());
    }

    private void handleSpaceEffect(Player player, BoardPosition space) {
        if (space instanceof Property) {
            handleProperty((Property) space, player);
        } else if (space instanceof GoToJailSpace) {
            sendPlayerToJail(player);
        } else {
            space.onLand(player);
        }
    }

    private void handleProperty(Property property, Player player) {
        if (property.getOwner() == null) {
            attemptToBuyProperty(player, property);
        } else if (!property.getOwner().equals(player)) {
            chargeRent(player, property);
        }
    }

    private void attemptToBuyProperty(Player player, Property property) {
        if (player.getBalance() >= property.getPrice()) {
            player.updateBalance(-property.getPrice());
            player.addProperty(property);
            property.setOwner(player);
            view.displayMessage(player.getName() + " comprou " + property.getName() + " por " + property.getPrice());
        } else {
            view.displayMessage(player.getName() + " não tem saldo suficiente para comprar " + property.getName());
        }
    }

    private void chargeRent(Player player, Property property) {
        int rent = property.getRent();
        if (player.getBalance() >= rent) {
            player.updateBalance(-rent);
            property.getOwner().updateBalance(rent);
            view.displayMessage(player.getName() + " pagou " + rent + " de aluguel a " + property.getOwner().getName());
        } else {
            view.displayMessage(player.getName() + " não tem saldo suficiente para pagar o aluguel!");
        }
    }

    private void sendPlayerToJail(Player player) {
        Prison prison = Prison.getInstance(board.getJailPosition());
        prison.sendToJail(player);
        view.displayMessage(player.getName() + " foi enviado para a prisão!");
        view.getBoardView().updatePlayerPosition(player, prison.getPosition());
    }

    private void moveToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        displayCurrentPlayerTurn();
    }

    private void displayCurrentPlayerTurn() {
        view.displayPlayerTurn(players.get(currentPlayerIndex));
    }
}
