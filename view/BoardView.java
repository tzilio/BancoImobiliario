package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import model.Board;
import model.BoardPosition;
import model.Player;

public class BoardView extends JPanel {
    private Board board;
    private Map<Integer, JButton> spaceButtons;
    private Map<Player, Integer> playerPositions;

    public BoardView(Board board) {
        this.board = board;
        this.spaceButtons = new HashMap<>();
        this.playerPositions = new HashMap<>();
        setLayout(new BorderLayout());

        // Top panel
        add(createTopPanel(), BorderLayout.NORTH);

        // Left and Right panels in a container to ensure correct layout
        JPanel sidesPanel = new JPanel(new BorderLayout());
        sidesPanel.add(createLeftPanel(), BorderLayout.WEST);
        sidesPanel.add(createRightPanel(), BorderLayout.EAST);
        add(sidesPanel, BorderLayout.CENTER);

        // Bottom panel
        add(createBottomPanel(), BorderLayout.SOUTH);

        // Center panel (empty)
        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(200, 200));
        centerPanel.setBackground(Color.LIGHT_GRAY);
        sidesPanel.add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new GridLayout(1, 11));
        for (int i = 0; i <= 10; i++) {
            JButton spaceButton = createSpaceButton(i);
            topPanel.add(spaceButton);
            spaceButtons.put(i, spaceButton);
        }
        return topPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new GridLayout(9, 1));
        for (int i = 11; i <= 19; i++) {
            JButton spaceButton = createSpaceButton(i);
            rightPanel.add(spaceButton);
            spaceButtons.put(i, spaceButton);
        }
        return rightPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 11));
        for (int i = 30; i >= 20; i--) {
            JButton spaceButton = createSpaceButton(i);
            bottomPanel.add(spaceButton);
            spaceButtons.put(i, spaceButton);
        }
        return bottomPanel;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new GridLayout(9, 1));
        for (int i = 39; i >= 31; i--) {
            JButton spaceButton = createSpaceButton(i);
            leftPanel.add(spaceButton);
            spaceButtons.put(i, spaceButton);
        }
        return leftPanel;
    }

    private JButton createSpaceButton(int position) {
        BoardPosition boardPosition = board.getSpace(position);
        String label = boardPosition != null ? boardPosition.getClass().getSimpleName() : "Space";
        JButton spaceButton = new JButton(label + " " + position);
        spaceButton.setEnabled(false);
        return spaceButton;
    }

    public void updatePlayerPosition(Player player, int newPosition) {
        if (playerPositions.containsKey(player)) {
            int previousPosition = playerPositions.get(player);
            JButton previousButton = spaceButtons.get(previousPosition);
            BoardPosition previousSpace = board.getSpace(previousPosition);
            String label = previousSpace != null ? previousSpace.getClass().getSimpleName() : "Space";
            previousButton.setText(label + " " + previousPosition);
            previousButton.setBackground(null);
        }

        playerPositions.put(player, newPosition);
        JButton currentButton = spaceButtons.get(newPosition);
        currentButton.setText("Player " + player.getName());
        currentButton.setBackground(Color.YELLOW);
    }
}
