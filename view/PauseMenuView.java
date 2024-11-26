package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PauseMenuView extends JFrame {
    private JButton resumeButton;
    private JButton saveGameButton;
    private JButton loadGameButton;
    private JButton exitButton;

    public PauseMenuView() {
        setTitle("Menu de Pausa");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JLabel pauseLabel = new JLabel("Jogo Pausado", SwingConstants.CENTER);
        pauseLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(pauseLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        resumeButton = new JButton("Continuar");
        saveGameButton = new JButton("Salvar Jogo");
        loadGameButton = new JButton("Carregar Jogo");
        exitButton = new JButton("Sair");

        buttonPanel.add(resumeButton);
        buttonPanel.add(saveGameButton);
        buttonPanel.add(loadGameButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    public void addResumeButtonListener(ActionListener listener) {
        resumeButton.addActionListener(listener);
    }

    public void addSaveGameButtonListener(java.util.function.Consumer<String> onSaveGame) {
        saveGameButton.addActionListener(e -> {
            String fileName = JOptionPane.showInputDialog(
                this,
                "Digite o nome do arquivo para salvar:",
                "Salvar Jogo",
                JOptionPane.QUESTION_MESSAGE
            );
    
            if (fileName != null && !fileName.trim().isEmpty()) {
                onSaveGame.accept(fileName.trim());
            }
        });
    }    
    
    public void addLoadGameButtonListener(ActionListener listener) {
        loadGameButton.addActionListener(listener);
    }

    public void addExitButtonListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    // Métodos para exibir e ocultar o menu
    public void showMenu() {
        setVisible(true);
    }

    public void hideMenu() {
        setVisible(false);
    }
}
