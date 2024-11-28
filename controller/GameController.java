package controller;

import model.*;
import view.GameView;
import view.PlayerInfoView;
import view.SpaceView;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

public class GameController {
    private List<Player> players;
    private Board board;
    private Dice dice;
    private int currentPlayerIndex;
    private GameView view;
    private Prison prison;
    private boolean awaitingTurnEnd;

    public GameController(List<Player> players, GameView view) {
        this.players = players;
        this.board = Board.getInstance();
        this.dice = Dice.getInstance();
        this.view = view;
        this.currentPlayerIndex = 0;
        this.prison = Prison.getInstance(board.getJailPosition());
        this.awaitingTurnEnd = false; // Inicializa o estado do turno
        initializePlayers();
        setupRollDiceAction();
        setupBuyPropertyAction();
        setupPassTurnAction();
        setupManualMoveAction();
        setupSellPropertyAction();
        setupMortgagePropertyAction();
    }

    private void setupManualMoveAction() {
        view.addMovePlayerListener(steps -> {
            Player currentPlayer = players.get(currentPlayerIndex);
            movePlayer(currentPlayer, steps);
            BoardPosition currentSpace = board.getSpace(currentPlayer.getPosition());
            applySpaceEffect(currentPlayer, currentSpace);
            view.displayMessage(currentPlayer.getName() + " moveu " + steps + " casas para a posição "
                    + currentPlayer.getPosition());
        });
    }

    private void updateBuildableProperties() {
        Player currentPlayer = players.get(currentPlayerIndex);
        List<Property> buildableProperties = new ArrayList<>();

        for (Property property : currentPlayer.getProperties()) {
            ArrayList<Property> propertiesInCategory = board.getPropertiesInCategory(property.getCategory());
            if (property.canBuildHouse(currentPlayer, propertiesInCategory)) {
                buildableProperties.add(property);
            }
        }

        if (buildableProperties.isEmpty()) {
            System.out.println("Nenhuma propriedade disponível para construção.");
        }
    }

    public void startGame() {
        view.displayMessage("Jogo iniciado!");
        displayCurrentPlayerTurn();
    }

    private void initializePlayers() {
        for (Player player : players) {
            view.getBoardView().updatePlayerPosition(player, player.getPosition());
        }
    }

    private void setupRollDiceAction() {
        view.getRollDiceButton().addActionListener(e -> {
            if (!awaitingTurnEnd) {
                Dice dice = Dice.getInstance();
                
                // Desativa o botão de rolagem para evitar múltiplos cliques
                view.getRollDiceButton().setEnabled(false);
    
                // Inicia uma nova Thread para a rolagem com delay para animação
                new Thread(() -> {
                    dice.roll();
    
                    int dice1 = dice.getDice1();
                    int dice2 = dice.getDice2();
    
                    // Atualiza a UI após um pequeno delay para simular a animação
                    SwingUtilities.invokeLater(() -> view.displayDiceRoll(dice1, dice2));
    
                    try {
                        Thread.sleep(1000); // Aguarda 1 segundo para simular o término da rolagem
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
    
                    // Move o jogador após a rolagem ser finalizada
                    SwingUtilities.invokeLater(() -> {
                        processDiceResult(dice1, dice2);
                        view.getPassTurnButton().setEnabled(true); // Ativa o botão de passar turno
                        awaitingTurnEnd = true;
                    });
                }).start();
            } else {
                view.displayMessage("Você precisa encerrar o turno atual antes de jogar novamente!");
            }
        });
    }
    


    private void setupBuyPropertyAction() {
        view.getBuyPropertyButton().addActionListener(e -> buyProperty());
    }


    private void setupSellPropertyAction() {
        for (Player player : players) {
            PlayerInfoView infoView = view.getPlayerInfoView(player);
            if (infoView != null) {
                infoView.addSellListener(property -> sellProperty(player, property));
            }
        }
    }

    private void sellProperty(Player player, Property property) {
        BankController bankController = new BankController();
        bankController.sellPropertyToBank(player, property);

        player.removeProperty(property);
        view.updatePlayerInfo(players); // Atualiza a interface gráfica
    }

    private void setupMortgagePropertyAction() {
        for (Player player : players) {
            PlayerInfoView infoView = view.getPlayerInfoView(player);
            if (infoView != null) {
                infoView.addMortgageListener(property -> mortgageProperty(player, property));
            }
        }
    }
    
    
    private void mortgageProperty(Player player, Property property) {
        BankController bankController = new BankController();
        bankController.mortgageProperty(player, property);
        view.updatePlayerInfo(players); // Atualiza a interface gráfica
    }

    private void setupPassTurnAction() {
        view.getPassTurnButton().addActionListener(e -> {
            if (awaitingTurnEnd) {
                endTurn();
            } else {
                view.displayMessage("Você precisa rolar os dados antes de passar o turno!");
            }
        });
    }

    private void nextTurn() {
        view.enableBuyPropertyButton(false);
        view.getRollDiceButton().setEnabled(false); // Desativa "Rolar Dados"
        view.getPassTurnButton().setEnabled(true); // Ativa "Passar Turno"
        awaitingTurnEnd = true;

        Player currentPlayer = players.get(currentPlayerIndex);

        if (isPlayerInJail(currentPlayer)) {
            return;
        }

        if (isPlayerSentToJail(currentPlayer)) {
            return;
        }

        BoardPosition currentSpace = getPlayerCurrentSpace(currentPlayer);
        applySpaceEffect(currentPlayer, currentSpace);
    }

    private boolean isPlayerInJail(Player player) {
        if (player.isInJail()) {
            handleJailTurn(player);
            endTurn();
            return true;
        }
        return false;
    }

    private void processDiceResult(int dice1, int dice2) {
        Player currentPlayer = players.get(currentPlayerIndex);
        if (isPlayerInJail(currentPlayer)) {
            return; // Caso o jogador esteja preso, o movimento será tratado de forma diferente
        }
    
        int roll = dice1 + dice2;
        movePlayer(currentPlayer, roll);
    }
    

    public void movePlayer(Player player, int steps) {
        new Thread(() -> {
            for (int i = 0; i < steps; i++) {
                try {
                    Thread.sleep(500); // Pausa de 500ms entre cada movimento
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                SwingUtilities.invokeLater(() -> {
                    player.setPosition((player.getPosition() + 1) % Board.BOARD_SIZE);
                    view.getBoardView().updatePlayerPosition(player, player.getPosition());
                });
            }
            SwingUtilities.invokeLater(() -> {
                BoardPosition currentSpace = board.getSpace(player.getPosition());
                applySpaceEffect(player, currentSpace);
                view.displayMessage(player.getName() + " chegou à posição " + player.getPosition());
            });
        }).start();
    }


    private boolean isPlayerSentToJail(Player player) {
        if (player.getPosition() == board.getGoToJailPosition()) {
            sendPlayerToJail(player);
            return true;
        }
        return false;
    }

    private BoardPosition getPlayerCurrentSpace(Player player) {
        return board.getSpace(player.getPosition());
    }

    private void applySpaceEffect(Player player, BoardPosition space) {
        if (space instanceof NewsSpace) {
            view.displayNewsPopup(((NewsSpace) space).getCard(player));
            return;
        }

        space.onLand(player);

        if (space instanceof Property) {
            handleProperty((Property) space, player);
        } else if (space instanceof ShareSpace) {
            handleShareSpace((ShareSpace) space, player);
        } else if (space instanceof Tax) {
            view.displayMessage(player.getName() + " pagou o imposto!");
        } else if (space instanceof TaxReturn) {
            view.displayMessage(player.getName() + " recebeu um retorno de imposto!");
        }
    }

    private void handleProperty(Property property, Player player) {
        if (property.getOwner() == null) {
            view.displayMessage(
                    player.getName() + " pode comprar " + property.getName() + " por " + property.getPrice());
            view.enableBuyPropertyButton(true);
        } else if (property.getOwner().equals(player)) {
            view.displayMessage(player.getName() + " parou em sua própria propriedade " + property.getName());
        }
    }

    private void handleShareSpace(ShareSpace shareSpace, Player player) {
        view.displayMessage(player.getName() + " parou em " + shareSpace.getName() + "!");

        if (!shareSpace.isOwned()) {
            view.displayMessage(
                    player.getName() + " pode comprar " + shareSpace.getName() + " por " + shareSpace.getPrice());
            view.enableBuyPropertyButton(true); // Habilita o botão de compra
        }
    }

    private void buyProperty() {
        Player currentPlayer = players.get(currentPlayerIndex);
        BoardPosition currentPosition = board.getSpace(currentPlayer.getPosition());

        if (currentPosition instanceof Property) {
            processPropertyPurchase((Property) currentPosition, currentPlayer);
        } else if (currentPosition instanceof ShareSpace) {
            processShareSpacePurchase((ShareSpace) currentPosition, currentPlayer);
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

    private void sendPlayerToJail(Player player) {
        prison.sendToJail(player);
        view.displayMessage(player.getName() + " foi enviado para a prisão!");
        view.getBoardView().updatePlayerPosition(player, player.getPosition());
    }

    private void endTurn() {
        awaitingTurnEnd = false; // Reseta o estado do turno
        moveToNextPlayer(); // Muda para o próximo jogador
        view.getRollDiceButton().setEnabled(true); // Habilita rolar dados
        view.getPassTurnButton().setEnabled(false); // Desativa passar turno
        view.enableBuyPropertyButton(false); // Garante que a compra seja desativada
        displayCurrentPlayerTurn(); // Exibe o próximo jogador
    }

    private void moveToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        displayCurrentPlayerTurn();
    }

    private void displayCurrentPlayerTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
    
        // Atualiza o estado de cada PlayerInfoView
        for (Player player : players) {
            PlayerInfoView infoView = view.getPlayerInfoView(player);
            infoView.setCurrentPlayer(player == currentPlayer);
        }
    
        view.displayPlayerTurn(currentPlayer);
        updateBuildableProperties();
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
            endTurn();
        }
    }

}