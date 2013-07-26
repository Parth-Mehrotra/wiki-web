import java.awt.*;

/**
 * @author Parth Mehrotra
 * ProgressLine is a simple progress updater
 */
public class ProgressLine {

    private double status;
    private Color innerColor;
    private Color frameColor;
    private int x, y, w;

    public ProgressLine(Color innerColor, Color frameColor) {
        this.innerColor = innerColor;
        this.frameColor = frameColor;
    }

    public ProgressLine(Color innerColor, Color frameColor, int x, int y, int w) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.innerColor = innerColor;
        this.frameColor = frameColor;
    }

    public void setDim(int x, int y, int w) {
        this.x = x;
        this.y = y;
        this.w = w;
    }

    public void paint(Graphics g) {
        Color before = g.getColor();
        g.setColor(frameColor);
        g.drawRect(x, y, w, 5);
        g.setColor(innerColor);
        g.fillRect(x+2, y+2, (int) (status * w) - 4, 2);

        g.setColor(before);
    }

    public void setStatus(double status) {
        this.status = status;
    }

    public double getStatus(double status) {
        return status;
    }
}
