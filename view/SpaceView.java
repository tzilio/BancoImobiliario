package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import model.BoardPosition;
import model.Player;
import model.Property;
import model.ShareSpace;

public class SpaceView extends JPanel {
    private BoardPosition boardPosition;
    private JPanel playerTokensPanel;
    private JPanel housePanel; // Painel para casas

    public SpaceView(BoardPosition boardPosition, int position) {
        this.boardPosition = boardPosition;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        configureSpaceView();

        playerTokensPanel = new JPanel();
        playerTokensPanel.setOpaque(false);
        playerTokensPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        add(playerTokensPanel, BorderLayout.CENTER);

        housePanel = new JPanel();
        housePanel.setOpaque(false);
        housePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0)); // Espaço entre as casas
        add(housePanel, BorderLayout.SOUTH); // Casas ficam na parte inferior
    }

    private void configureSpaceView() {
        setBackground(getSpaceColor());

        if (boardPosition instanceof Property) {
            Property p = (Property) boardPosition;
            JLabel nameLabel = new JLabel("<html><center>" + boardPosition.getName() + " -> Preço: R$" + p.getPrice() + " -> Aluguel: R$" + p.getRent() + "</center></html>");
            nameLabel.setFont(new Font("Arial", Font.BOLD, 10));
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(nameLabel, BorderLayout.NORTH);
        } else if (boardPosition instanceof ShareSpace) {
            ShareSpace p = (ShareSpace) boardPosition;
            JLabel nameLabel = new JLabel("<html><center>" + boardPosition.getName() + " -> Preço: R$" + p.getPrice() + " -> Multiplicador: R$" + p.getMultiplier() + "</center></html>");
            nameLabel.setFont(new Font("Arial", Font.BOLD, 10));
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(nameLabel, BorderLayout.NORTH);
        } else {
            JLabel nameLabel = new JLabel("<html><center>" + boardPosition.getName() + "</center></html>");
            nameLabel.setFont(new Font("Arial", Font.BOLD, 10));
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(nameLabel, BorderLayout.NORTH);
        }

    }

    private Color getSpaceColor() {
        if (boardPosition instanceof Property) {
            Property property = (Property) boardPosition;
            switch (property.getCategory()) {
                case "Roxo": return new Color(181, 92, 170); // Roxo vibrante
                case "Ciano": return new Color(38, 154, 154); // Ciano brilhante
                case "Rosa": return new Color(231, 111, 179); // Rosa forte
                case "Laranja": return new Color(226, 87, 41); // Laranja vivo
                case "Vermelho": return new Color(216, 56, 54); // Vermelho forte
                case "Amarelo": return new Color(242, 190, 34); // Amarelo brilhante
                case "Verde": return new Color(40, 124, 50); // Verde vibrante
                case "Azul": return new Color(0, 0, 255); // Azul forte
                default: return Color.LIGHT_GRAY; // Cinza para categorias desconhecidas
            }
        }
        return Color.LIGHT_GRAY; // Cinza para espaços genéricos
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

    /* public void updateHouses(int houseCount) {
        housePanel.removeAll(); // Remove casas anteriores
        for (int i = 0; i < houseCount; i++) {
            JLabel houseLabel = new JLabel(loadImage("resources/house.svg"));
            if (houseLabel.getIcon() != null) {
                housePanel.add(houseLabel); // Adiciona a casa ao painel
            }
        }
        revalidate();
        repaint();
    } */

    public void updateHouses(int houseCount, boolean hasHotel) {
        System.out.println("Atualizando casas para " + boardPosition.getName() + ": " + 
                           (hasHotel ? "Hotel" : houseCount + " casas"));
    
        housePanel.removeAll(); 
    
        if (hasHotel) {
            JLabel hotelLabel = new JLabel("H");
            hotelLabel.setFont(new Font("Arial", Font.BOLD, 16)); 
            hotelLabel.setHorizontalAlignment(SwingConstants.CENTER);
            housePanel.add(hotelLabel); 
            System.out.println("Hotel adicionado ao painel.");
        } else {
            for (int i = 0; i < houseCount; i++) {
                JLabel houseLabel = new JLabel("C");
                houseLabel.setFont(new Font("Arial", Font.BOLD, 16)); 
                houseLabel.setHorizontalAlignment(SwingConstants.CENTER);
                housePanel.add(houseLabel);
                System.out.println("Casa adicionada ao painel de debug.");
            }
        }
    
        housePanel.revalidate(); // Atualiza o layout do painel
        housePanel.repaint();    // Re-renderiza o painel
    }    

    private ImageIcon loadImage(String path) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
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
