import javax.swing.SwingUtilities;
import controller.GameController;
import model.Player;
import view.GameView;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<Player> players = new ArrayList<>();
            players.add(new Player("Jogador 1", 1500));
            players.add(new Player("Jogador 2", 1500));

            GameView gameView = new GameView();
            gameView.setVisible(true);  

            GameController gameController = new GameController(players, gameView);

            gameController.startGame();
        });
    }
}
