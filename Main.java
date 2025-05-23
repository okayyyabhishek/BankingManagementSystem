import ui.BankingUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankingUI ui = new BankingUI();
            ui.setVisible(true);
        });
    }
}
