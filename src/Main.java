import javax.swing.JFrame;
import java.awt.Color;

public class Main {
    public static void main(String[] args) {
        int screenMaxX = 800; // Set your preferred screen width here
        int screenMaxY = 600; // Set your preferred screen height here

        GraphPlotter graphPlotter = new GraphPlotter(screenMaxX, screenMaxY);

        JFrame frame = new JFrame("Graph Plotter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenMaxX, screenMaxY);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.add(graphPlotter);
        frame.setVisible(true);
    }
}

