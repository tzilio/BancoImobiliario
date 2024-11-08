package view;

import javax.swing.*;
import java.awt.*;
import model.Player;
import model.Board;

public class GameView extends JFrame {
    private JLabel messageLabel;
    private JLabel diceRollLabel;
    private JButton rollDiceButton;
    private BoardView boardView;  // Usar BoardView para o tabuleiro

    public GameView(Board board) {
        setupLayout(board);
        setTitle("Banco Imobiliário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
    }

    private void setupLayout(Board board) {
        setLayout(new BorderLayout());

        // Área de mensagem no topo
        messageLabel = new JLabel("Bem-vindo ao Banco Imobiliário!");
        add(messageLabel, BorderLayout.NORTH);

        // Exibição do resultado dos dados
        diceRollLabel = new JLabel("Resultado dos Dados: ");
        add(diceRollLabel, BorderLayout.NORTH);

        // Botão para rolar os dados na parte inferior
        rollDiceButton = new JButton("Rolar Dados");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(rollDiceButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Cria o BoardView e adiciona ao centro do BorderLayout
        boardView = new BoardView(board);
        add(boardView, BorderLayout.CENTER);
    }

    // Método para atualizar a mensagem exibida
    public void displayMessage(String message) {
        messageLabel.setText(message);
    }

    // Método para exibir o resultado do lançamento dos dados
    public void displayDiceRoll(int die1, int die2) {
        diceRollLabel.setText("Resultado dos Dados: " + die1 + " e " + die2);
    }

    // Método para mostrar de quem é a vez
    public void displayPlayerTurn(Player player) {
        displayMessage("É a vez de " + player.getName());
    }

    // Retorna o botão de rolar dados para adicionar a lógica de evento no controlador
    public JButton getRollDiceButton() {
        return rollDiceButton;
    }

    // Método para acessar o BoardView (para atualizar a posição do jogador)
    public BoardView getBoardView() {
        return boardView;
    }
}
