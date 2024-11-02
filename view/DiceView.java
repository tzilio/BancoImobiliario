package view;

import javax.swing.*;
import java.awt.*;

public class DiceView extends JPanel {
    private JLabel diceResultLabel;

    public DiceView() {
        diceResultLabel = new JLabel("Resultado dos Dados: ");
        add(diceResultLabel);
    }

    public void setDiceResult(int dice1, int dice2) {
        diceResultLabel.setText("Resultado dos Dados: " + dice1 + " e " + dice2);
    }
}
