package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import model.BoardPosition;
import model.Player;
import model.Property;

public class SpaceView extends JPanel {
    private BoardPosition boardPosition;
    private int position;
    private JPanel playerTokensPanel;

    public SpaceView(BoardPosition boardPosition, int position) {
        this.boardPosition = boardPosition;
        this.position = position;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        configureSpaceView();

        playerTokensPanel = new JPanel();
        playerTokensPanel.setOpaque(false);
        playerTokensPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        add(playerTokensPanel, BorderLayout.CENTER);
    }

    private void configureSpaceView() {
        setBackground(getSpaceColor());

        JLabel nameLabel = new JLabel("<html><center>" + boardPosition.getName() + "</center></html>");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 10));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(nameLabel, BorderLayout.NORTH);
    }

    private Color getSpaceColor() {
        if (boardPosition instanceof Property) {
            Property property = (Property) boardPosition;
            switch (property.getCategory()) {
                case "Roxo": return new Color(150, 75, 0); // Marrom
                case "Ciano": return Color.CYAN;
                case "Rosa": return Color.PINK;
                case "Laranja": return Color.ORANGE;
                case "Vermelho": return Color.RED;
                case "Amarelo": return Color.YELLOW;
                case "Verde": return Color.GREEN;
                case "Azul": return new Color(0, 0, 128); // Azul escuro
                default: return Color.LIGHT_GRAY; // Cor padrão para categorias desconhecidas
            }
        }
        return Color.LIGHT_GRAY; // Se não for Property
    }

    public void clearPlayerTokens() {
        playerTokensPanel.removeAll();
        revalidate();
        repaint();
    }

    public void addPlayerToken(Player player) {
        String colorName = player.getColor();
        String imagePath = "resources/peoes/peao_" + colorName + ".png"; // Caminho baseado na cor
        ImageIcon icon = loadImage(imagePath);
    
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            JLabel tokenLabel = new JLabel(new ImageIcon(img));
            tokenLabel.setName(player.getName()); // Identificação do token
            playerTokensPanel.add(tokenLabel);
            revalidate();
            repaint();
        } else {
            System.err.println("Imagem não encontrada: " + imagePath);
        }
    }
    

    private ImageIcon loadImage(String path) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            return new ImageIcon(path);
        } else {
            System.err.println("Erro: Caminho da imagem inválido - " + path);
            return null;
        }
    }

    public void removePlayerToken(Player player) {
        for (Component component : playerTokensPanel.getComponents()) {
            if (component instanceof JLabel && player.getName().equals(component.getName())) {
                playerTokensPanel.remove(component);
                break;
            }
        }
        revalidate();
        repaint();
    }
}
