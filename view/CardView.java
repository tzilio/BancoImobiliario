package view;

import javax.swing.*;
import model.Card;
import java.awt.BorderLayout;

public class CardView extends JDialog {
    private JLabel cardDescriptionLabel;
    private JButton closeButton;

    public CardView(Card card) {
        setTitle("Carta");
        setModal(true);
        setSize(300, 200);
        setLocationRelativeTo(null);

        cardDescriptionLabel = new JLabel(card.getDescription());
        closeButton = new JButton("Fechar");
        closeButton.addActionListener(e -> dispose());

        setLayout(new BorderLayout());
        add(cardDescriptionLabel, BorderLayout.CENTER);
        add(closeButton, BorderLayout.SOUTH);
    }

    public void showCard() {
        setVisible(true);
    }
}
