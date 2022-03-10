
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class Crypto extends Window {

  public static final int dCode = 20, dGuess = 40,//distance of Code and Guess
      xM = 50, yM = 50,// margin
      lineGap = 10,// gap between lines
      W = 20, H = 45;
  public static final G.V SPACE = new G.V(W, 0),// space between cells
      START = new G.V(xM, yM),//start position
      NL=new G.V(0,lineGap+H);//new line
  public static Cell.List cells = new Cell.List();
  public static Font font = new Font("Verdana", Font.PLAIN, 20);

  public Crypto() {
    super("Cryptogram", 1000, 800);
    // test
    Cell c = new Cell(Pair.alpha[0]);
    c.p.guess = "B";
    cells.add(c);
    cells.add(new Cell(Pair.alpha[1]));
    Cell.newLine();
    cells.add(new Cell(Pair.alpha[2]));
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
    public G.V loc = new G.V();
    public static G.V nextLoc=new G.V(START);
    public static G.V nextLine=new G.V(START);
    public static G.VS vs=new G.VS(0,0,W,H);

    public Cell(Pair p) {
      this.p = p;
      loc.set(nextLoc);
      nextLoc.add(SPACE);
    }

    public void show(Graphics g) {
      vs.loc.set(loc);
      vs.fill(g,Color.RED);
      g.setColor(Color.BLACK);
      g.drawString("" + p.code, loc.x, loc.y + dCode);
      g.drawString(p.guess, loc.x, loc.y + dGuess);
    }
    public boolean hit(int x,int y){
      vs.loc.set(loc);
      return vs.hit(x,y);
    }
    public static void newLine(){
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
    }
  }


}
