
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Crypto extends Window {

  public static final int dCode = 20, dGuess = 40,//distance of Code and Guess
      xM = 50, yM = 50,// margin
      lineGap = 10,// gap between lines
      W = 20, H = 45;
  public static final G.V SPACE = new G.V(W, 0),// space between cells
      START = new G.V(xM, yM),//start position
      NL = new G.V(0, lineGap + H);//new line
  public static Cell.List cells = new Cell.List();
  public static Font font = new Font("Verdana", Font.PLAIN, 20);

  public Crypto() {
    super("Cryptogram", 1000, 800);
    // test
    Cell c = new Cell(Pair.alpha[0]);
    c.p.guess = "B";
    new Cell(Pair.alpha[1]);
    Cell.newLine();
    new Cell(Pair.alpha[2]);
    Cell.selected = c;
  }

  public static void main(String[] args) {
    Window.PANEL = new Crypto();
    Window.launch();
  }

  public void paintComponent(Graphics g) {
    G.whiteBackground(g);
    g.setFont(font);
    cells.show(g);
  }

  @Override
  public void keyTyped(KeyEvent ke) {
    char c = ke.getKeyChar();
    if (c >= 'a' && c <= 'z') {
      c = (char) (c - 'a' + 'A');// shift lower case to upper case
    }
    if (Cell.selected != null) {
      Cell.selected.p.guess = (c >= 'A' && c <= 'Z') ? "" + c : "";
      repaint();
    }

  }

  @Override
  public void keyPressed(KeyEvent ke) {
    int vk = ke.getKeyCode();//virtual key
    if(Cell.selected!=null){
      if (vk == KeyEvent.VK_LEFT) {
        Cell.selected.left();
      } else if (vk == KeyEvent.VK_RIGHT) {
        Cell.selected.right();
      }
      repaint();
    }

  }

  @Override
  public void mouseClicked(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    Cell.selected = cells.hit(x, y);
    repaint();
  }

  //--------------Pair----------------
  public static class Pair {

    public char actual, code;
    public String guess;// can be null
    public static Pair[] alpha = new Pair[26];

    static {
      for (int i = 0; i < 26; i++) {
        alpha[i] = new Pair((char) ('A' + i));
      }
    }

    public Pair(char c) {
      this.actual = c;
      this.code = c;
      guess = "";
    }
  }

  //-------------Cell----------------
  public static class Cell {

    public Pair p;
    public int idx;
    public G.V loc = new G.V();
    public static G.V nextLoc = new G.V(START);
    public static G.V nextLine = new G.V(START);
    public static G.VS vs = new G.VS(0, 0, W, H);
    public static Cell selected = null;


    public Cell(Pair p) {
      this.p = p;
      idx = cells.size();
      cells.add(this);
      loc.set(nextLoc);
      nextLoc.add(SPACE);
    }

    public void show(Graphics g) {
      if (this == Cell.selected) {
        vs.loc.set(loc);
        vs.draw(g, Color.RED);
      }
      g.setColor(Color.BLACK);
      g.drawString("" + p.code, loc.x, loc.y + dCode);
      g.drawString(p.guess, loc.x, loc.y + dGuess);
    }

    public boolean hit(int x, int y) {
      vs.loc.set(loc);
      return vs.hit(x, y);
    }

    public void left() {
      if (idx > 0) {
        Cell.selected = cells.get(idx - 1);
      }
    }
    public void right() {
      if (idx <cells.size()-1) {
        Cell.selected = cells.get(idx + 1);
      }
    }

    public static void newLine() {
      nextLine.add(NL);
      nextLoc.set(nextLine);
    }

    //--------------Cell.List-----------
    public static class List extends ArrayList<Cell> {

      public void show(Graphics g) {
        for (Cell c : this) {

          c.show(g);
        }
      }

      public Cell hit(int x, int y) {
        for (Cell c : this) {
          if (c.hit(x, y)) {
            return c;
          }
        }
        return null;
      }
    }
  }


}
