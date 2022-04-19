import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Sokoban extends Game {

  public Board board = new Board();
  public static Point LEFT = new Point(-1, 0);
  public static Point RIGHT = new Point(1, 0);
  public static Point UP = new Point(0, -1);
  public static Point DOWN = new Point(0, 1);

  public Sokoban() {
    super("Sokoban", 1000, 800);
    board.loadStringArray(level1);
  }

  public void paintComponent(Graphics g) {
    g.setColor(Color.white);
    g.fillRect(0, 0, 5000, 5000);
    board.show(g);
    if (board.done()) {
      g.setColor(Color.black);
      g.drawString("Nice job!!", 20, 20);
    }
  }

  @Override
  public void endGame() {

  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent ke) {
    int vk = ke.getKeyCode();//virtual key
    if (vk == KeyEvent.VK_LEFT) {
      board.go(LEFT);
    }
    if (vk == KeyEvent.VK_RIGHT) {
      board.go(RIGHT);
    }
    if (vk == KeyEvent.VK_DOWN) {
      board.go(DOWN);
    }
    if (vk == KeyEvent.VK_UP) {
      board.go(UP);
    }
    repaint();
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  @Override
  public void mouseDragged(MouseEvent e) {

  }

  @Override
  public void mouseMoved(MouseEvent e) {

  }

  //---------------Board-----------------
  public static class Board {

    public static final int N = 25;
    public char[][] b = new char[N][N];
    public Point person = new Point(0, 0);
    public static boolean onGoal = false;//tracks if person on the goal area
    public static Point dest = new Point(0, 0);//destination
    /*
    W: wall
    P: person
    C: container
    G: goal
    g: goal on different groud
    E: error
     */
    public static String boardStates = " WPCGgE";
    /*
     * white: ground
     * darkGray: wall
     * green: player
     * orange: container
     * cyan: goal area
     * blue: container on goal area
     * red: error
     */
    public static Color[] colors = {Color.white, Color.darkGray, Color.green, Color.orange,
        Color.cyan, Color.blue, Color.red};
    public static final int xM = 50, yM = 50, W = 40;

    public Board() {
      clear();
    }

    public char ch(Point p) {
      return b[p.x][p.y];
    }

    public void set(Point p, char c) {
      b[p.x][p.y] = c;
    }

    public void movePerson() {
      // simple move to an empty square
      boolean res = ch(dest) == 'G';
      set(person, onGoal ? 'G' : ' ');// set value if person is leaving
      set(dest, 'P');
      person.setLocation(dest);
      onGoal = res;
    }

    public void go(Point direction) {
      dest.setLocation(person.x + direction.x, person.y + direction.y);
      if (ch(dest) == 'W' || ch(dest) == 'E') {
        // run into wall or error
        return;
      }
      if (ch(dest) == ' ' || ch(dest) == 'G') {
        movePerson();
        return;
      }
      if (ch(dest) == 'C' || ch(dest) == 'g') {
        // moving container
        dest.setLocation(dest.x + direction.x,
            dest.y + direction.y);//changing dest to box destination
        if (ch(dest) != ' ' && ch(dest) != 'G') {
          // only move box if there's space or goal
          return;
        }
        set(dest, ch(dest) == 'G' ? 'g' : 'C');
        dest.setLocation(dest.x - direction.x,
            dest.y - direction.y);// set dest back to person's destination
        set(dest, ch(dest) == 'g' ? 'G' : ' ');
        movePerson();
      }
    }

    public boolean done() {
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          if (b[i][j] == 'C') {
            return false;
          }
        }
      }
      return true;
    }

    public void clear() {
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          b[i][j] = ' ';
        }
      }
    }

    public void show(Graphics g) {
      for (int r = 0; r < N; r++) {
        for (int c = 0; c < N; c++) {
          int ndx = boardStates.indexOf(b[c][r]);
          g.setColor(colors[ndx]);
          g.fillRect(xM + c * W, yM + r * W, W, W);
        }
      }
    }

    public void loadStringArray(String[] a) {
      person.setLocation(0, 0);
      for (int r = 0; r < a.length; r++) {
        String s = a[r];
        for (int c = 0; c < s.length(); c++) {
          char ch = s.charAt(c);
          b[c][r] = boardStates.indexOf(ch) > -1 ? ch : 'E';// detect illegal characters
          if (ch == 'P' && person.x == 0) {
            person.x = c;
            person.y = r;
          }
        }
      }
    }
  }


  public static String[] level1 = {
      "  WWWWW",
      "WWW   W",
      "WGPC  W",
      "WWW CGW",
      "WGWWC W",
      "W W G WW",
      "WC gCCGW",
      "W   G  W",
      "WWWWWWWW"
  };
  
}
