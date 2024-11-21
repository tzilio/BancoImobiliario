import controller.GameController;
import model.Board;
import model.Player;
import model.PlayerColor;
import view.GameView;
import view.MenuView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Inicializa o menu para configurar os jogadores
        SwingUtilities.invokeLater(() -> {
            MenuView menuView = new MenuView();
            menuView.setVisible(true);
        });

        // A lógica para iniciar o jogo deve estar dentro do MenuView, após a configuração dos jogadores.
        // O exemplo anterior no MenuView já lida com isso ao clicar no botão "Iniciar Jogo".
    }
}
