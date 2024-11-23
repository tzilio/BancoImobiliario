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
    private Prison prison;

    public GameController(List<Player> players, GameView view) {
        this.players = players;
        this.board = Board.getInstance();
        this.dice = Dice.getInstance();
        this.view = view;
        this.currentPlayerIndex = 0;
        this.prison = Prison.getInstance(board.getJailPosition());
        initializePlayers();
        setupRollDiceAction();
        setupBuyPropertyAction();
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

    private void setupBuyPropertyAction() {
        view.getBuyPropertyButton().addActionListener(e -> buyProperty());
    }

    private void nextTurn() {
        view.enableBuyPropertyButton(false);
        Player currentPlayer = players.get(currentPlayerIndex);

        if (isPlayerInJail(currentPlayer)) {
            return;
        }

        int roll = rollDiceAndDisplay();

        movePlayer(currentPlayer, roll);

        if (isPlayerSentToJail(currentPlayer)) {
            movePlayer(currentPlayer, board.getJailPosition());
            return;
        }

        BoardPosition currentSpace = getPlayerCurrentSpace(currentPlayer);
        applySpaceEffect(currentPlayer, currentSpace);
        moveToNextPlayer();
    }

    private boolean isPlayerInJail(Player player) {
        if (player.isInJail()) {
            handleJailTurn(player);
            moveToNextPlayer();
            return true;
        }
        return false;
    }

    private int rollDiceAndDisplay() {
        int roll = dice.roll();
        view.displayDiceRoll(dice.getDice1(), dice.getDice2());
        return roll;
    }

    private void movePlayer(Player player, int roll) {
        processPlayerMove(player, roll);
    }

    private boolean isPlayerSentToJail(Player player) {
        if (player.getPosition() == board.getGoToJailPosition()) {
            sendPlayerToJail(player);
            moveToNextPlayer();
            return true;
        }
        return false;
    }

    private BoardPosition getPlayerCurrentSpace(Player player) {
        return board.getSpace(player.getPosition());
    }

    private void applySpaceEffect(Player player, BoardPosition space) {
        if (space instanceof Property) {
            handleProperty((Property) space, player);
        } else if (space instanceof NewsSpace) {
            handleNewsSpace((NewsSpace) space, player);
        } else if (space instanceof ShareSpace) {
            handleShareSpace((ShareSpace) space, player);
        }
    }

    private void handleJailTurn(Player player) {
        view.displayMessage(player.getName() + " está na prisão.");
        dice.roll();
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

    private void handleProperty(Property property, Player player) {
        if (property.getOwner() == null) {
            view.displayMessage(
                    player.getName() + " pode comprar " + property.getName() + " por " + property.getPrice());
            view.enableBuyPropertyButton(true);
        } else if (!property.getOwner().equals(player)) {
            chargeRent(player, property);
        } else {
            view.displayMessage(player.getName() + " parou em sua própria propriedade " + property.getName());
        }
    }

    private void handleNewsSpace(NewsSpace newsSpace, Player player) {
        view.displayMessage(player.getName() + " parou em Notícias!");
        newsSpace.onLand(player);
    }

    private void handleShareSpace(ShareSpace shareSpace, Player player) {
        view.displayMessage(player.getName() + " parou em " + shareSpace.getName() + "!");
        shareSpace.onLand(player);
    
        if (!shareSpace.isOwned()) {
            view.displayMessage(player.getName() + " pode comprar " + shareSpace.getName() + " por " + shareSpace.getPrice());
            view.enableBuyPropertyButton(true);
        }
    }    

    private void processShareSpacePurchase(ShareSpace shareSpace, Player player) {
        int price = shareSpace.getPrice();
        if (player.getBalance() >= price) {
            player.updateBalance(-price);
            shareSpace.setOwner(player);
            view.displayMessage(player.getName() + " comprou " + shareSpace.getName() + " por " + price);
            view.enableBuyPropertyButton(false);
        } else {
            view.displayMessage(player.getName() + " não tem saldo suficiente para comprar " + shareSpace.getName());
        }
    }    

    private void processPropertyPurchase(Property property, Player player) {
        if (player.getBalance() >= property.getPrice()) {
            player.updateBalance(-property.getPrice());
            property.setOwner(player);
            player.addProperty(property);
            view.displayMessage(player.getName() + " comprou " + property.getName() + " por " + property.getPrice());
            view.enableBuyPropertyButton(false);
        } else {
            view.displayMessage(player.getName() + " não tem saldo suficiente para comprar " + property.getName());
        }
    }    

    private void buyProperty() {
        Player currentPlayer = players.get(currentPlayerIndex);
        BoardPosition currentPosition = board.getSpace(currentPlayer.getPosition());
    
        if (currentPosition instanceof Property) {
            Property property = (Property) currentPosition;
            processPropertyPurchase(property, currentPlayer);
        } else if (currentPosition instanceof ShareSpace) {
            ShareSpace shareSpace = (ShareSpace) currentPosition;
            processShareSpacePurchase(shareSpace, currentPlayer);
        }
    }    

    private void chargeRent(Player player, Property property) {
        int rent = property.getRent();
        Player owner = property.getOwner();

        if (player.getBalance() >= rent) {
            player.updateBalance(-rent);
            owner.updateBalance(rent);
            view.displayMessage(player.getName() + " pagou " + rent + " de aluguel a " + owner.getName());
        } else {
            view.displayMessage(player.getName() + " não tem saldo suficiente para pagar o aluguel!");
        }
    }

    private void sendPlayerToJail(Player player) {
        prison.sendToJail(player);
        view.displayMessage(player.getName() + " foi enviado para a prisão!");
        view.getBoardView().updatePlayerPosition(player, player.getPosition());
    }

    private void moveToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        displayCurrentPlayerTurn();
    }

    private void displayCurrentPlayerTurn() {
        view.displayPlayerTurn(players.get(currentPlayerIndex));
    }
}
