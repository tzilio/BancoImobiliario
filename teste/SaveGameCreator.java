package teste;

import model.Bank;
import model.Board;
import model.Player;
import model.Property;
import model.SaveGameManager;

import java.util.ArrayList;
import java.util.List;

public class SaveGameCreator {
    public static void main(String[] args) {
        // Criação manual dos jogadores
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player("Jogador 1", 1500, "Vermelho");
        Player player2 = new Player("Jogador 2", 1500, "Azul");

        // Configuração das posições específicas
        player1.setPosition(10); // Jogador 1 na posição 10
        player2.setPosition(15); // Jogador 2 na posição 15

        players.add(player1);
        players.add(player2);

        // Instanciar o tabuleiro
        Board board = Board.getInstance();

        // Exemplo de propriedades que serão atribuídas ao tabuleiro (ajustar conforme implementação)
        List<Property> properties = board.getAllProperties(); // Supondo que este método retorna todas as propriedades do tabuleiro

        // Atribuir propriedades a um jogador
        for (Property property : properties) {
            if (property.getCategory().equals("Roxo")) { // Todas as propriedades da categoria "Roxo"
                property.setOwner(player1);             // Jogador 1 é dono das propriedades "Roxo"
                player1.addProperty(property);
            }
        }

        // Criação do banco
        Bank bank = Bank.getInstance();
        bank.chargePlayer(player1, 500); // Exemplo: Cobrar 500 do Jogador 1
        bank.payPlayer(player2, 300);   // Exemplo: Pagar 300 ao Jogador 2

        // Salvar o jogo
        SaveGameManager.saveGame("teste_savegame.dat", bank, players);

        System.out.println("Savegame criado com sucesso!");
    }
}
