package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import model.Player;
import model.Observer;

public class PlayerInfoView extends JPanel implements Observer {
    private JLabel nameLabel;
    private JLabel balanceLabel;
    private JLabel propertiesLabel;
    private Player player;

    // Mapa de cores para jogadores (nome da cor como chave)
    private static final Map<String, Color> playerColors = new HashMap<>();

    static {
        playerColors.put("Vermelho", new Color(220, 20, 60));
        playerColors.put("Azul", new Color(30, 144, 255));
        playerColors.put("Verde", new Color(34, 139, 34));
        playerColors.put("Amarelo", new Color(255, 215, 0));
        playerColors.put("Branco", Color.WHITE);
        playerColors.put("Preto", Color.BLACK);
        // Adicione mais cores conforme necessário
    }

    public PlayerInfoView(Player player) {
        this.player = player;
        this.player.addObserver(this); // Registra-se como observador

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 100));
        setMaximumSize(new Dimension(300, 100));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setBackground(getColorFromPlayer());

        // Inicialização dos Labels
        nameLabel = new JLabel("Jogador: " + player.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(contrastColor(getColorFromPlayer()));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        balanceLabel = new JLabel("Saldo: R$" + player.getBalance());
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        balanceLabel.setForeground(contrastColor(getColorFromPlayer()));
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        propertiesLabel = new JLabel("Propriedades: " + player.getProperties().size());
        propertiesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        propertiesLabel.setForeground(contrastColor(getColorFromPlayer()));
        propertiesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adicionando componentes com espaçamento
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(nameLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(balanceLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(propertiesLabel);
    }

    @Override
    public void update() {
        balanceLabel.setText("Saldo: R$" + player.getBalance());
        propertiesLabel.setText("Propriedades: " + player.getProperties().size());
    }

    // Método auxiliar para obter a cor do jogador
    private Color getColorFromPlayer() {
        return playerColors.getOrDefault(player.getColor(), Color.GRAY);
    }

    // Método para determinar a cor de texto contrastante
    private Color contrastColor(Color bgColor) {
        // Cálculo de luminância
        double luminance = (0.299 * bgColor.getRed() + 0.587 * bgColor.getGreen() + 0.114 * bgColor.getBlue()) / 255;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }
}