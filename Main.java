import javax.swing.SwingUtilities;
import controller.GameController;
import model.Board;
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
            players.add(new Player("Jogador 3", 1500));
            players.add(new Player("Jogador 4", 1500));

            Board board = Board.getInstance();

            GameView gameView = new GameView(board);
            gameView.setVisible(true);  

            GameController gameController = new GameController(players, gameView);
            gameController.startGame();
        });
    }
}
