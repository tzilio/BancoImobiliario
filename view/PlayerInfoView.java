package view;

import javax.swing.*;
import model.Player;
import model.Observer;

import java.util.stream.Collectors;

public class PlayerInfoView extends JPanel implements Observer {
    private JLabel nameLabel;
    private JLabel balanceLabel;
    private JLabel propertiesLabel;
    private Player player;

    public PlayerInfoView(Player player) {
        this.player = player;

        nameLabel = new JLabel("Jogador: " + player.getName());
        balanceLabel = new JLabel("Saldo: " + player.getBalance());
        propertiesLabel = new JLabel("Propriedades: " + getPlayerProperties());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(nameLabel);
        add(balanceLabel);
        add(propertiesLabel);

        player.addObserver(this);
    }

    @Override
    public void update() {
        balanceLabel.setText("Saldo: " + player.getBalance());
        propertiesLabel.setText("Propriedades: " + getPlayerProperties());
    }

    private String getPlayerProperties() {
        if (player.getProperties().isEmpty()) {
            return "Nenhuma";
        }
        return player.getProperties()
                     .stream()
                     .map(property -> property.getName()) 
                     .collect(Collectors.joining(", ")); 
    }
}
