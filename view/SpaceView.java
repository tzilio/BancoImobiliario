package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import model.BoardPosition;
import model.Player;

public class SpaceView extends JPanel {
    private BoardPosition boardPosition;
    private int position;
    private JPanel playerTokensPanel;

    public SpaceView(BoardPosition boardPosition, int position) {
        this.boardPosition = boardPosition;
        this.position = position;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Configuração visual do espaço
        configureSpaceView();

        // Painel para os tokens dos jogadores
        playerTokensPanel = new JPanel();
        playerTokensPanel.setOpaque(false);
        playerTokensPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        add(playerTokensPanel, BorderLayout.CENTER);
    }

    private void configureSpaceView() {
        // Definir cor de fundo com base no tipo de propriedade
        setBackground(getSpaceColor());

        // Adicionar nome do espaço
        JLabel nameLabel = new JLabel("<html><center>" + "espaco" + "</center></html>");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 10));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(nameLabel, BorderLayout.NORTH);
    }

    private Color getSpaceColor() {
        // Definir cores baseadas nos grupos de propriedades
        switch (position) {
            case 1:
            case 3: return new Color(150, 75, 0); // Marrom
            case 6:
            case 8:
            case 9: return Color.CYAN;
            case 11:
            case 13:
            case 14: return Color.PINK;
            case 16:
            case 18:
            case 19: return Color.ORANGE;
            case 21:
            case 23:
            case 24: return Color.RED;
            case 26:
            case 27:
            case 29: return Color.YELLOW;
            case 31:
            case 32:
            case 34: return Color.GREEN;
            case 37:
            case 39: return new Color(0, 0, 128); // Azul Escuro
            default: return Color.LIGHT_GRAY; // Cor padrão
        }
    }

    public void addPlayerToken(Player player) {
        // Carregar o ícone do jogador
        ImageIcon icon = loadImage("/home/tiago/paradigmas/BancoImobiliario/view/peao-de-xadrez(1).png");
        if (icon != null) {
            // Redimensionar a imagem para caber no token
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            JLabel tokenLabel = new JLabel(new ImageIcon(img));
            tokenLabel.setName(player.getName()); // Identificação do token
            playerTokensPanel.add(tokenLabel);
            revalidate();
            repaint();
        } else {
            System.err.println("Imagem não encontrada: " + "/home/tiago/paradigmas/BancoImobiliario/view/peao-de-xadrez(1).png");
        }
    }

    private ImageIcon loadImage(String path) {
        // Verifica se a imagem existe e a carrega
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
