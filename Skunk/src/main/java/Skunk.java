import java.awt.*;

public class Skunk extends Window{

    public static G.Button PASS = new G.Button(null, "pass") {

        @Override
        public void add() {
            System.out.println("pass");
        }
    };

    public Skunk() {
        super("Skunk", 1000, 800);
    }
    public void paintComponent(Graphics g) {
        G.whiteBackground(g);
        PASS.show(g);
    }

    public static void main(String[] args) {
        PANEL = new Skunk();
        PANEL.launch();
    }
}
