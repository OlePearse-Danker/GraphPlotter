import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

@SuppressWarnings("serial")
public class GraphPlotter extends JPanel implements MouseWheelListener {
    private Graphics g;

    private int screenMaxX;
    private int screenMaxY;

    private double xMin, yMin, xMax, yMax;
    private double xUnit, yRange;
    private double xRange; // Added variable declaration
    private double yUnit; // Added variable declaration

    public GraphPlotter(int screenMaxX, int screenMaxY) {
        this.screenMaxX = screenMaxX;
        this.screenMaxY = screenMaxY;

        xMax = 1 * Math.PI;
        xMin = -xMax;
        yMax = 1.5;
        yMin = -yMax;

        xRange = (xMax - xMin); // Initialize xRange
        yRange = (yMax - yMin);

        xUnit = xRange / screenMaxX;
        yUnit = yRange / screenMaxY;

        // Add the mouse wheel listener to enable zooming
        this.addMouseWheelListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;

        coordinateSystem();
        plot();
    }

    private void drawPoint(Point p, Color c) {
        g.setColor(c);
        g.drawLine(p.x, p.y, p.x, p.y);
    }

    private void coordinateSystem() {
        g.drawLine(0, scrrenMidY(), screenMaxX, scrrenMidY());
        g.drawLine(scrrenMidX(), 0, scrrenMidX(), screenMaxY);

        // Add labels for x-axis
        for (int xScreen = 0; xScreen < screenMaxX; xScreen += 50) {
            double xValue = xMin + (xScreen * xUnit);
            g.drawString(String.format("%.2f", xValue), xScreen, scrrenMidY() + 15);
        }

        // Add labels for y-axis
        for (int yScreen = 0; yScreen < screenMaxY; yScreen += 50) {
            double yValue = yMax - ((double) yScreen / screenMaxY) * yRange;
            g.drawString(String.format("%.2f", yValue), scrrenMidX() + 5, yScreen + 15);
        }
    }

    private void plot() {
        double x = xMin;
        for (int xScreen = 0; xScreen < screenMaxX; xScreen++) {
            x = x + xUnit;
            int yScreen = scrrenMidY() - ((int) ((f(x) / yRange) * screenMaxY));
            drawPoint(new Point(xScreen, yScreen), Color.BLUE);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            // Zoom in
            zoomIn();
        } else {
            // Zoom out
            zoomOut();
        }
    }

    private void zoomIn() {
        // Adjust the x-axis and y-axis ranges to zoom in
        xRange /= 2;
        yRange /= 2;

        // Adjust the x-unit and y-range values accordingly
        xUnit = xRange / screenMaxX;
        yUnit = yRange / screenMaxY;

        // Repaint the panel
        repaint();
    }

    private void zoomOut() {
        // Adjust the x-axis and y-axis ranges to zoom out
        xRange *= 2;
        yRange *= 2;

        // Adjust the x-unit and y-range values accordingly
        xUnit = xRange / screenMaxX;
        yUnit = yRange / screenMaxY;

        // Repaint the panel
        repaint();
    }

    private int scrrenMidX() {
        return screenMaxX / 2;
    }

    private int scrrenMidY() {
        return screenMaxY / 2;
    }

    private double f(double x) {
        return Math.sin(x);
        //return Math.exp(x);
    }
}
