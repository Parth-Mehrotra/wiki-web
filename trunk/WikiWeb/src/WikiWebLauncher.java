import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WikiWebLauncher extends JFrame implements KeyListener {

    private WikiWebVisualizer wikiWebVisualizer;

    public WikiWebLauncher() throws Exception {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setUndecorated(true);
        wikiWebVisualizer = new WikiWebVisualizer();
        addKeyListener(this);
        wikiWebVisualizer.addKeyListener(this);
        add(wikiWebVisualizer);
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new WikiWebLauncher();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        wikiWebVisualizer.keyTyped(keyEvent);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}
