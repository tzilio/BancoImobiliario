package view;

import javax.swing.*;
import model.Board;
import model.BoardPosition;
import java.awt.*;

public class BoardView extends JPanel {
    private Board board;

    public BoardView(Board board) {
        this.board = board;
        setLayout(new GridLayout(4, 10));  // tabuleiro 4x10
        setupBoard();
    }

    private void setupBoard() {
        for (int i = 0; i < board.BOARD_SIZE; i++) {
            BoardPosition position = board.getSpace(i);
            JButton spaceButton = new JButton(position.getClass().getSimpleName() + " " + i);
            spaceButton.setEnabled(false); 
            add(spaceButton);
        }
    }
}
