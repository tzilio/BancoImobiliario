import javax.swing.SwingUtilities;
import view.MenuView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuView menuView = new MenuView();
            menuView.setVisible(true);
        });
    }
}
