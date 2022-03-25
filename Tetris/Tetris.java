import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;


public class Tetris extends Window implements ActionListener {

  public static Timer timer;
  public static final int H = 20, W = 10, C = 25;// C is the cell. W and H are the width and height of the wall.
  public static final int xM = 50, yM = 50;
  public static final int iBkColor = 7;// index of background color
  public static int[][] well = new int[W][H];
  public static Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.CYAN,
      Color.YELLOW,
      Color.MAGENTA, Color.BLACK};
  public static Shape[] shapes = {Shape.Z, Shape.S, Shape.J, Shape.L, Shape.I, Shape.O, Shape.T};
  public static Shape shape;

  public Tetris() {
    super("Tetris", 1000, 800);
    timer = new Timer(30, this);
    timer.start();
    shape = shapes[G.rnd(7)];
    clearWell();
  }

  public static void clearWell() {
    for (int i = 0; i < W; i++) {
      for (int j = 0; j < H; j++) {
        well[i][j] = iBkColor;
      }
    }
  }

  public static void showWell(Graphics g) {
    for (int x = 0; x < W; x++) {
      for (int y = 0; y < H; y++) {
        g.setColor(colors[well[x][y]]);
        int xX = xM + C * x, yY = yM + C * y;
        g.fillRect(xX, yY, C, C);
        g.setColor(colors[iBkColor]);
        g.drawRect(xX, yY, C, C);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Window.PANEL = new Tetris();
    Window.launch();
  }

  public static int time = 1, iShape = 0;

  public void paintComponent(Graphics g) {
    G.whiteBackground(g);
    // every 30 frames, drop the shape

//        time++;
//        if(time == 30){
//            shapes[iShape].rotate();
//        }
//        if (time == 60) {
//            time = 0;
//            iShape = (iShape + 1) % 7;
//        }
//        shapes[iShape].show(g);
    showWell(g);
    shape.show(g);
  }

  public static class Shape {

    public static Shape Z, S, J, L, I, O, T;
    public G.V[] a = new G.V[4];
    public int iCol;// color index
    public static Shape cds = new Shape(new int[]{0, 0, 0, 0, 0, 0, 0, 0,}, 0);
    public G.V loc = new G.V(0, 0);

    static {
      Z = new Shape(new int[]{0, 0, 1, 0, 1, 1, 2, 1}, 0);
      S = new Shape(new int[]{0, 1, 1, 0, 1, 1, 2, 0}, 1);
      J = new Shape(new int[]{0, 0, 0, 1, 1, 1, 2, 1}, 2);
      L = new Shape(new int[]{0, 1, 1, 1, 2, 1, 2, 0}, 3);
      I = new Shape(new int[]{0, 0, 1, 0, 2, 0, 3, 0}, 4);
      O = new Shape(new int[]{0, 0, 1, 0, 0, 1, 1, 1}, 5);
      T = new Shape(new int[]{0, 1, 1, 0, 1, 1, 2, 1}, 6);
    }

    public static G.V temp = new G.V(0, 0);

    public Shape(int[] xy, int iC) {
      for (int i = 0; i < 4; i++) {
        a[i] = new G.V(xy[i * 2], xy[i * 2 + 1]);
      }
      iCol = iC;
    }

    public void show(Graphics g) {
      g.setColor(colors[iCol]);
      for (int i = 0; i < 4; i++) {
        g.fillRect(x(i), y(i), C, C);
      }
      g.setColor(Color.BLACK);// border color
      for (int i = 0; i < 4; i++) {
        g.drawRect(x(i), y(i), C, C);
      }
    }

    public int x(int i) {
      return xM + C * (a[i].x + loc.x);
    }

    public int y(int i) {
      return yM + C * (a[i].y + loc.y);
    }

    public void drop() {
      cdsSet();
      cdsAdd(G.DOWN);

      if (collisionDetected()) {
        // copy the four cells into the well
        // set shape back to 0,0
        return;
      }
      loc.add(G.DOWN);
    }

    public void rotate() {
      temp.set(0, 0);// track min x, y
      for (int i = 0; i < 4; i++) {
        a[i].set(-a[i].y, a[i].x);// rotate 90 degree
        if (temp.x > a[i].x) {
          temp.x = a[i].x;
        }
        if (temp.y > a[i].y) {
          temp.y = a[i].y;
        }
      }
      temp.set(-temp.x, -temp.y);
      for (int i = 0; i < 4; i++) {
        a[i].add(temp);
      }

    }
    public void safeRotate(){
      rotate();
      cdsSet();
      if(collisionDetected()){
        rotate();
        rotate();
        rotate();
        return;
      }
    }

    public void slide(G.V v) {
      cdsSet();
      cdsAdd(v);
      if (collisionDetected()) {
        return;
      }
      loc.add(v);
    }

    public void cdsSet() {
      for (int i = 0; i < 4; i++) {
        cds.a[i].set(a[i]);
        cds.a[i].add(loc);
      }
    }

    public void cdsGet() {
      for (int i = 0; i < 4; i++) {
        a[i].set(cds.a[i]);
      }
    }

    public void cdsAdd(G.V v) {
      for (int i = 0; i < 4; i++) {
        cds.a[i].add(v);
      }
    }

    public static boolean collisionDetected() {
      for (int i = 0; i < 4; i++) {
        G.V v = cds.a[i];
        System.out.println(v.x+" "+v.y);
        // hit wall
        if (v.x < 0 || v.x >= W || v.y < 0 || v.y >= H) {
          return true;
        }
        // hit other block
        if (well[v.x][v.y] != iBkColor) {
          return true;
        }
      }
      return false;
    }


  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
  }

  @Override
  public void keyPressed(KeyEvent ke) {
    int vk = ke.getKeyCode();
    if (vk == KeyEvent.VK_UP) {
      shape.safeRotate();
    }
    if (vk == KeyEvent.VK_LEFT) {
      shape.slide(G.LEFT);
    }
    if (vk == KeyEvent.VK_RIGHT) {
      shape.slide(G.RIGHT);
    }
    if (vk == KeyEvent.VK_DOWN) {
      shape.drop();
    }
    repaint();
  }
}
