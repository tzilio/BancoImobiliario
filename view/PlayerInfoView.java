package view;

import javax.swing.*;
import model.Player;

public class PlayerInfoView extends JPanel {
    private JLabel nameLabel;
    private JLabel balanceLabel;
    private JLabel propertiesLabel;

    public PlayerInfoView(Player player) {
        nameLabel = new JLabel("Jogador: " + player.getName());
        balanceLabel = new JLabel("Saldo: " + player.getBalance());
        propertiesLabel = new JLabel("Propriedades: " + player.getProperties().size());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));  
        add(nameLabel);
        add(balanceLabel);
        add(propertiesLabel);
        update(player);
    }

    public void update(Player player) {
        balanceLabel.setText("Saldo: " + player.getBalance());
        propertiesLabel.setText("Propriedades: " + player.getProperties().size());
    }
}
