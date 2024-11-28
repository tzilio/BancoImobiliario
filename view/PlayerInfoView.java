package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import model.Player;
import model.Property;
import model.Bank;
import model.Board;
import model.Observer;

public class PlayerInfoView extends JPanel implements Observer {
    private JLabel nameLabel;
    private JLabel balanceLabel;
    private JPanel propertiesPanel;

    private Player player;

    // Mapa de cores para jogadores (nome da cor como chave)
    private static final Map<String, Color> playerColors = new HashMap<>();

    private boolean isCurrentPlayer;

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
        setPreferredSize(new Dimension(300, 200)); // Altura ajustada para mais propriedades
        setMaximumSize(new Dimension(300, 200));
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

        // Adicionando componentes
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(nameLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(balanceLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));

        setupPropertiesPanel();
    }

    private void setupPropertiesPanel() {
        propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(propertiesPanel); // Crie o JScrollPane uma vez
        add(scrollPane); // Adicione ao painel principal
    }

    private void updatePropertiesPanel() {
        propertiesPanel.removeAll(); // Limpa as propriedades anteriores
    
        for (Property property : player.getProperties()) { // Atualiza com a lista atual do jogador
            JPanel propertyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel propertyLabel = new JLabel(property.getName());
    
            Board board = Board.getInstance();
    
            // Botão Construir Casa
            JButton buildHouseButton = new JButton("Construir casa");
            buildHouseButton.addActionListener(e -> {
                property.buildHouse(player, board.getPropertiesInCategory(property.getCategory()));
            
                GameView gameView = (GameView) SwingUtilities.getWindowAncestor(this); 
                if (gameView != null) {
                    SpaceView spaceView = gameView.getBoardView().getSpaceView(property.getPosition());
                    if (spaceView != null) {
                        spaceView.updateHouses(property.getHouses(), property.hasHotel());
                    }
                }
            });
            buildHouseButton.setEnabled(isCurrentPlayer && !Bank.getInstance().isMortgaged(property)
                    && property.canBuildHouse(player, board.getPropertiesInCategory(property.getCategory())));
    
            // Calcula os valores dinâmicos
            int mortgageValue = property.getPrice() / 2;
            int sellValue = property.getPrice() / 2;
            int repurchaseCost = (int) (property.getPrice() * 1.2);
    
            // Botão Hipotecar
            JButton mortgageButton = new JButton("Hipotecar (+" + mortgageValue + ")");
            mortgageButton.addActionListener(e -> fireMortgagePropertyEvent(property));
            mortgageButton.setEnabled(isCurrentPlayer && !Bank.getInstance().isMortgaged(property));
    
            // Botão Vender
            JButton sellButton = new JButton("Vender (+" + sellValue + ")");
            sellButton.addActionListener(e -> fireSellPropertyEvent(property));
            sellButton.setEnabled(isCurrentPlayer && !Bank.getInstance().isMortgaged(property));
    
            // Botão Recomprar
            JButton repurchaseButton = new JButton("Recomprar (-" + repurchaseCost + ")");
            repurchaseButton.addActionListener(e -> fireRepurchasePropertyEvent(property));
            repurchaseButton.setEnabled(isCurrentPlayer && Bank.getInstance().isMortgaged(property));
    
            // Adiciona os componentes ao painel
            propertyPanel.add(propertyLabel);
            propertyPanel.add(buildHouseButton);
            propertyPanel.add(mortgageButton);
            propertyPanel.add(sellButton);
            propertyPanel.add(repurchaseButton);
    
            propertiesPanel.add(propertyPanel);
        }
    
        propertiesPanel.revalidate();
        propertiesPanel.repaint();
    }
    

    public void setCurrentPlayer(boolean isCurrentPlayer) {
        this.isCurrentPlayer = isCurrentPlayer;
        updatePropertiesPanel(); // Atualiza a exibição ao alterar o estado
    }

    // Eventos para hipotecar e vender propriedades
    private transient List<java.util.function.Consumer<Property>> mortgageListeners = new ArrayList<>();
    private transient List<java.util.function.Consumer<Property>> sellListeners = new ArrayList<>();
    private transient List<java.util.function.Consumer<Property>> repurchaseListeners = new ArrayList<>();

    public void addRepurchaseListener(java.util.function.Consumer<Property> listener) {
        repurchaseListeners.add(listener);
    }
    
    private void fireRepurchasePropertyEvent(Property property) {
        for (var listener : repurchaseListeners) {
            listener.accept(property);
        }
    }
    
    public void addMortgageListener(java.util.function.Consumer<Property> listener) {
        mortgageListeners.add(listener);
    }

    public void addSellListener(java.util.function.Consumer<Property> listener) {
        sellListeners.add(listener);
    }

    private void fireMortgagePropertyEvent(Property property) {
        System.out.println("Evento hipotecar disparado para: " + property.getName());
        for (var listener : mortgageListeners) {
            listener.accept(property);
        }
    }

    private void fireSellPropertyEvent(Property property) {
        System.out.println("Evento vender disparado para: " + property.getName());
        for (var listener : sellListeners) {
            listener.accept(property);
        }
    }

    @Override
    public void update() {
        balanceLabel.setText("Saldo: R$" + player.getBalance());
        updatePropertiesPanel();
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
