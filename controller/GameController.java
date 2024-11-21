package controller;

import model.Board;
import model.BoardPosition;
import model.Player;
import model.Property;
import model.Observer;
import view.GameView;

import javax.swing.*;
import java.util.List;

public class GameController implements Observer {
    private List<Player> players;
    private GameView gameView;
    private int currentPlayerIndex = 0;
    private Board board;

    public GameController(List<Player> players, GameView gameView) {
        this.players = players;
        this.gameView = gameView;
        this.board = Board.getInstance();

        // Registrar o GameController como observador de cada jogador
        for (Player player : players) {
            player.addObserver(this);
        }

        // Configurar ações dos botões
        gameView.getRollDiceButton().addActionListener(e -> rollDice());
        gameView.getBuyPropertyButton().addActionListener(e -> buyProperty());
    }

    public void startGame() {
        // Posicionar todos os jogadores na posição inicial
        for (Player player : players) {
            gameView.getBoardView().updatePlayerPosition(player, player.getPosition());
        }

        // Atualizar a interface para o jogador atual
        updatePlayerTurn();
    }

    @Override
    public void update() {
        // Atualizar a interface quando um jogador muda de estado
        Player currentPlayer = players.get(currentPlayerIndex);
        gameView.getBoardView().updatePlayerPosition(currentPlayer, currentPlayer.getPosition());
        gameView.updatePlayerInfo(players);
    }

    public void rollDice() {
        Player currentPlayer = players.get(currentPlayerIndex);
        int diceTotal = currentPlayer.rollDice();

        // Mover o jogador
        currentPlayer.move(diceTotal);

        // Atualizar a posição no tabuleiro
        gameView.getBoardView().updatePlayerPosition(currentPlayer, currentPlayer.getPosition());

        // Ativar o botão de comprar propriedade se aplicável
        BoardPosition currentPosition = board.getSpace(currentPlayer.getPosition());
        if (currentPosition.getType() == BoardPosition.PositionType.PROPERTY) {
            Property property = (Property) currentPosition;
            if (property.getOwner() == null) {
                gameView.enableBuyPropertyButton(true);
            } else {
                gameView.enableBuyPropertyButton(false);
            }
        } else {
            gameView.enableBuyPropertyButton(false);
        }
    }

    public void buyProperty() {
        Player currentPlayer = players.get(currentPlayerIndex);
        BoardPosition currentPosition = board.getSpace(currentPlayer.getPosition());

        if (currentPosition.getType() == BoardPosition.PositionType.PROPERTY) {
            Property property = (Property) currentPosition;
            if (property.getOwner() == null) {
                int propertyCost = property.getPrice();
                if (currentPlayer.getBalance() >= propertyCost) {
                    currentPlayer.updateBalance(-propertyCost);
                    property.setOwner(currentPlayer);
                    currentPlayer.addProperty(property);
                    JOptionPane.showMessageDialog(gameView, currentPlayer.getName() + " comprou " + property.getName() + " por R$" + propertyCost);
                } else {
                    JOptionPane.showMessageDialog(gameView, currentPlayer.getName() + " não tem saldo suficiente para comprar esta propriedade.");
                }
            } else {
                JOptionPane.showMessageDialog(gameView, property.getName() + " já está comprada por " + property.getOwner().getName());
            }
        }

        // Desativar o botão após a compra
        gameView.enableBuyPropertyButton(false);
    }

    private void updatePlayerTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        gameView.displayPlayerTurn(currentPlayer);
    }

    public void endTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        updatePlayerTurn();
    }
}
