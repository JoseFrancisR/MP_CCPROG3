package potionprodigy;

import javax.swing.SwingUtilities;
import gui.MainFrame;
import gui.components.Theme;

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
            Theme.configureDefaults(); // setup color theme
            MainFrame frame = new MainFrame();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
