import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

public class WikiLinkGroup {
    public static final int PADDING = 5;
    private ArrayList<WikiLink> wikiLinks;
    private String initialInput;
    private int n;

    public WikiLinkGroup(String initialInput, int n) {
        wikiLinks = new ArrayList<WikiLink>();
        this.initialInput = initialInput;
        this.n = n;
    }

    public void add(WikiLink wl) {
        wikiLinks.add(wl);
        Collections.sort(wikiLinks);
    }

    public void paint(Graphics g, JPanel jp) {
        g.setColor(Color.WHITE);
        double theta = (Math.PI * 2) / (double) n;
        double radius = (jp.getHeight() / 2) - 100;
        int length = (n < wikiLinks.size()) ? n : wikiLinks.size();
        double xOrigin = jp.getWidth() / 2;
        double yOrigin = jp.getHeight() / 2;

        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(initialInput, g);

        g.drawString(initialInput, (int) (xOrigin - r.getWidth() / 2), (int) (yOrigin - r.getHeight() / 2));
        g.setColor(Color.GREEN);
        g.drawRect((int) (xOrigin - r.getWidth() / 2) - 3, (int) (yOrigin - (r.getHeight() + PADDING)) - 3, (int) r.getWidth() + PADDING, (int) r.getHeight() + PADDING);
        g.setColor(Color.WHITE);

        if (wikiLinks != null && wikiLinks.size() > 0) {
            for (int i = 0; i < length; i++) {
                double modTheta = (theta * (double) i) + (Math.PI / 2);
                FontMetrics f = g.getFontMetrics();
                Rectangle2D rect = f.getStringBounds(wikiLinks.get(i).getTitle(), g);
                double _x = xOrigin - (radius * Math.cos(modTheta));
                double _y = yOrigin - (radius * Math.sin(modTheta));

                double x = _x - (rect.getWidth() / 2);
                double y = _y - (rect.getHeight() / 2);
                g.setColor(Color.WHITE);
                g.drawString(wikiLinks.get(i).getTitle(), (int) x, (int) y);
                int rectX = (int) x - 3;
                int rectY = (int) y - (PADDING * 3);
                int rectW = (int) rect.getWidth() + PADDING;
                int rectH = (int) rect.getHeight() + PADDING;
                g.setColor(Color.GREEN);
                g.drawRect(rectX, rectY, rectW, rectH);
                int lineX2 = (int) ((((double) rectX) + ((double) rectX + (double) rectW)) / 2);
                int lineY2 = (int) ((((double) rectY) + ((double) rectY + (double) rectH)) / 2);
                double rad = Math.sqrt(Math.pow(r.getWidth() / 2, 2) + Math.pow(r.getHeight() / 2, 2)) + 1d;
                int lineX1 = (int) (xOrigin - (Math.cos(modTheta) * rad));
                int lineY1 = (int) (yOrigin - (Math.sin(modTheta) * rad));


                g.setColor(Color.GRAY);
                g.drawLine(lineX1, lineY1, lineX2, lineY2);
            }
        }
    }
}
