package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class DiceView extends JPanel {
    private int dice1 = 1, dice2 = 1; // Valores dos dados
    private Timer animationTimer;    // Timer para animação
    private int animationStep = 0;   // Etapa da animação
    private Random random = new Random();

    public DiceView() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
        setLayout(null); // Para posicionar os elementos manualmente
    }

    // Inicia o lançamento dos dados
    public void rollDice() {
        animationStep = 0;

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        // Define a animação com 20 quadros
        animationTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationStep++;

                // Simula mudanças aleatórias nos valores durante a animação
                dice1 = random.nextInt(6) + 1;
                dice2 = random.nextInt(6) + 1;

                repaint(); // Atualiza a tela para desenhar os novos valores

                // Finaliza a animação após algumas iterações
                if (animationStep > 20) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        animationTimer.start();
    }

    // Desenha os dados no centro do painel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenha os dados no centro do painel
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int diceSize = 100;

        // Coordenadas para centralizar os dois dados
        int x1 = panelWidth / 2 - diceSize - 10;
        int y1 = panelHeight / 2 - diceSize / 2;
        int x2 = panelWidth / 2 + 10;
        int y2 = panelHeight / 2 - diceSize / 2;

        drawDice(g, x1, y1, diceSize, dice1);
        drawDice(g, x2, y2, diceSize, dice2);
    }

    // Método para desenhar um dado em uma posição específica
    private void drawDice(Graphics g, int x, int y, int size, int value) {
        // Desenha o retângulo do dado
        g.setColor(Color.WHITE);
        g.fillRoundRect(x, y, size, size, 20, 20);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, size, size, 20, 20);

        // Desenha os pontos do dado
        g.setColor(Color.BLACK);
        int dotSize = size / 8;
        int[][] positions = getDotPositions(x, y, size, dotSize);

        // Define quais pontos desenhar para cada valor do dado
        int[][] values = {
                {},                     // Valor 0 (nunca usado)
                {4},                    // Valor 1
                {0, 8},                 // Valor 2
                {0, 4, 8},              // Valor 3
                {0, 2, 6, 8},           // Valor 4
                {0, 2, 4, 6, 8},        // Valor 5
                {0, 1, 2, 6, 7, 8}      // Valor 6
        };

        for (int pos : values[value]) {
            g.fillOval(positions[pos][0], positions[pos][1], dotSize, dotSize);
        }
    }

    // Define as posições dos pontos no dado
    private int[][] getDotPositions(int x, int y, int size, int dotSize) {
        int offset = size / 4;
        return new int[][]{
                {x + offset, y + offset},                  // Topo esquerdo
                {x + size / 2 - dotSize / 2, y + offset},  // Topo centro
                {x + size - offset - dotSize, y + offset}, // Topo direito
                {x + size / 2 - dotSize / 2, y + size / 2 - dotSize / 2}, // Centro
                {x + offset, y + size - offset - dotSize}, // Inferior esquerdo
                {x + size / 2 - dotSize / 2, y + size - offset - dotSize}, // Inferior centro
                {x + size - offset - dotSize, y + size - offset - dotSize}, // Inferior direito
                {x + offset, y + size / 2 - dotSize / 2}, // Esquerda centro
                {x + size - offset - dotSize, y + size / 2 - dotSize / 2} // Direita centro
        };
    }
}
