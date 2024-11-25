package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class DiceView extends JFrame {
    private DiceRollListener diceRollListener;

    public DiceView() {
        super("Rolando Dois Dados");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Fecha a janela sem encerrar o programa
        setPreferredSize(new Dimension(700, 700));
        pack();
        setResizable(false);
        setLocationRelativeTo(null);

        addGuiComponents();
    }

    // Interface para capturar o resultado dos dados
    public interface DiceRollListener {
        void onDiceRolled(int dice1, int dice2);
    }

    public void addDiceRollListener(DiceRollListener listener) {
        this.diceRollListener = listener;
    }

    private void addGuiComponents() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);

        // Dados
        JLabel diceOneImg = new JLabel(new ImageIcon("resources/dice1.png"));        diceOneImg.setBounds(100, 200, 200, 200);
        jPanel.add(diceOneImg);

        JLabel diceTwoImg = new JLabel(new ImageIcon("resources/dice1.png"));
        diceTwoImg.setBounds(390, 200, 200, 200);
        jPanel.add(diceTwoImg);

        // Botão para rolar
        Random rand = new Random();
        JButton rollButton = new JButton("Roll!");
        rollButton.setBounds(250, 550, 200, 50);
        rollButton.addActionListener(e -> {
            rollButton.setEnabled(false); // Desativa o botão durante a rolagem

            // Inicia a thread para animar a rolagem
            long startTime = System.currentTimeMillis();
            Thread rollThread = new Thread(() -> {
                long endTime = System.currentTimeMillis();
                int finalDiceOne = 1;
                int finalDiceTwo = 1;
                try {
                    while ((endTime - startTime) / 1000F < 2) {
                        // Rola os dados
                        int diceOne = rand.nextInt(6) + 1;
                        int diceTwo = rand.nextInt(6) + 1;

                        // Atualiza as imagens dos dados
                        diceOneImg.setIcon(new ImageIcon("resources/dice" + diceOne + ".png"));
                        diceTwoImg.setIcon(new ImageIcon("resources/dice" + diceTwo + ".png"));
                        repaint();
                        revalidate();

                        // Define os valores finais
                        finalDiceOne = diceOne;
                        finalDiceTwo = diceTwo;

                        // Dorme a thread
                        Thread.sleep(60);

                        endTime = System.currentTimeMillis();
                    }

                    // Envia os valores finais ao listener
                    if (diceRollListener != null) {
                        diceRollListener.onDiceRolled(finalDiceOne, finalDiceTwo);
                    }

                    rollButton.setEnabled(true); // Reativa o botão após a rolagem
                } catch (InterruptedException ex) {
                    System.err.println("Threading Error: " + ex.getMessage());
                }
            });
            rollThread.start();
        });

        jPanel.add(rollButton);

        this.getContentPane().add(jPanel);
    }
}
