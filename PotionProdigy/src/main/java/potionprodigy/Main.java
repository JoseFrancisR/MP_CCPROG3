package potionprodigy;

import javax.swing.SwingUtilities;
import gui.MainFrame;

/**
 * 
 * Main for running the actual program
 */
public class Main {

    /** 
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
