package view;

import javax.swing.*;
import model.Player;
import model.Observer;

public class PlayerInfoView extends JPanel implements Observer {
    private JLabel nameLabel;
    private JLabel balanceLabel;
    private JLabel propertiesLabel;
    private Player player;

    public PlayerInfoView(Player player) {
        this.player = player;
        nameLabel = new JLabel("Jogador: " + player.getName());
        balanceLabel = new JLabel("Saldo: " + player.getBalance());
        propertiesLabel = new JLabel("Propriedades: " + player.getProperties().size());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(nameLabel);
        add(balanceLabel);
        add(propertiesLabel);

        player.addObserver(this);  // Registra-se como observador
    }

    @Override
    public void update() {
        // Atualiza os dados sempre que o estado do Player mudar
        balanceLabel.setText("Saldo: " + player.getBalance());
        propertiesLabel.setText("Propriedades: " + player.getProperties().size());
    }
}
