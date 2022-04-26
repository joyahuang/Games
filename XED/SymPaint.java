import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SymPaint extends Game {

  public static int W = 1000, H = 800;
  public static PolyLine currentLine;
  public static PolyLine.List all = new PolyLine.List();
  public static boolean debug = true;

  public SymPaint() {
    super("SimPaint", W, H);
  }

  @Override
  public void paintComponent(Graphics g) {
    g.setColor(Color.white);
    g.fillRect(0, 0, 5000, 5000);
    g.setColor(Color.black);
    all.show(g);
    if (debug) {
      g.setColor(Color.red);
      String name =
          "r " + PolyLine.rVal + " " + (PolyLine.isM ? "M" : "");
      name += " " + (PolyLine.isG ? (PolyLine.hg == 0 ? "H" : "G") : "");
      g.drawString(name, 30, 30);
    }
  }

  //------------------PolyLine-------------------
  public static class PolyLine extends ArrayList<Point> {

    public static Point c = new Point(W / 2, H / 2);
    public static Point a = new Point(), b = new Point();// raw line segment
    // rotation
    public static Point ra = new Point(), rb = new Point();// rotated line segment of a and b
    public static int rVal = 1;//rotation count
    public static final double twoPi = Math.PI * 2;
    // mirror
    public static Point ma = new Point(), mb = new Point();// mirror of ra and rb
    public static boolean isM = true;
    public static int md = 0;// displacement of verticle mirror;
    // glide
    public static Point ga = new Point(), gb = new Point();// mirror of ra and rb
    public static boolean isG = true;
    public static int hg = 0;// horizontal extra glide translation, 0 if exact horizontal mirror
    public static int hTLo, hTHi;// loop limits for horizontal translation, ie, how many copies of the horizontal
    public static Point hT = new Point(200, 0);

    private void show(Graphics g) {
      setTransLimits();
      for (int i = 1; i < this.size(); i++) {
        a = get(i - 1);
        b = get(i);
        for (int iR = 0; iR < rVal; iR++) {
          setR(iR);
          for (int h = hTLo; h < hTHi; h++) {
            int tx = h * hT.x, ty = h * hT.y;
            g.drawLine(ra.x + tx, ra.y + ty, rb.x + tx, rb.y + ty);
            if (isM) {
              setM();
              g.drawLine(ma.x + tx, ma.y + ty, mb.x + tx, mb.y + ty);
            }
            if (isG) {
              setG();
              g.drawLine(ga.x + tx, ga.y + ty, gb.x + tx, gb.y + ty);
            }
          }


        }
      }
    }

    private void setR(int iR) {
      double ith = iR * twoPi / rVal, cos = Math.cos(ith), sin = Math.sin(ith);
      // rotation of i theta about center point (c)
      ra.x = (int) ((a.x - c.x) * cos + (a.y - c.y) * sin + (c.x));
      ra.y = (int) ((a.x - c.x) * -sin + (a.y - c.y) * cos + (c.y));
      rb.x = (int) ((b.x - c.x) * cos + (b.y - c.y) * sin + (c.x));
      rb.y = (int) ((b.x - c.x) * -sin + (b.y - c.y) * cos + (c.y));
    }

    public void setM() {
      ma.x = 2 * c.x + md - ra.x;
      ma.y = ra.y;
      mb.x = 2 * c.x + md - rb.x;
      mb.y = rb.y;
    }

    public void setG() {
      ga.x = ra.x + hg;
      ga.y = 2 * c.y + md - ra.y;
      gb.x = rb.x + hg;
      gb.y = 2 * c.y + md - rb.y;
    }

    public void setTransLimits() {
      if (hT.x == 0 && hT.y == 0) {
        hTLo = 0;
        hTHi = 1;
      } else {
        hTLo = -6;
        hTHi = 6;
      }
    }

    public void add(int x, int y) {
      add(new Point(x, y));
    }

    //------------------List-------------------
    public static class List extends ArrayList<PolyLine> {

      public void show(Graphics g) {
        for (PolyLine pl : this) {
          pl.show(g);
        }
      }
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
    int vk = ke.getKeyCode();
    if (vk == KeyEvent.VK_UP) {
      if (PolyLine.rVal < 20) {
        PolyLine.rVal++;
      }
    } else if (vk == KeyEvent.VK_DOWN) {
      if (PolyLine.rVal > 0) {
        PolyLine.rVal--;
      }
    } else if (vk == KeyEvent.VK_BACK_SPACE) {
      all.clear();
      currentLine = null;
    }
    char ch = ke.getKeyChar();
    if (ch == 'M' || ch == 'm') {
      PolyLine.isM = !PolyLine.isM;
    } else if (ch == 'H' || ch == 'h') {
      PolyLine.isG = !PolyLine.isG;
      PolyLine.hg = 0;
    } else if (ch == 'G' || ch == 'g') {
      PolyLine.isG = true;
      PolyLine.hg = PolyLine.hT.x / 2;
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
  public void mousePressed(MouseEvent me) {
    currentLine = new PolyLine();
    currentLine.add(me.getX(), me.getY());
    all.add(currentLine);
    repaint();
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
  public void mouseDragged(MouseEvent me) {
    currentLine.add(me.getX(), me.getY());
    repaint();
    
  }

  @Override
  public void mouseMoved(MouseEvent e) {

  }
}
