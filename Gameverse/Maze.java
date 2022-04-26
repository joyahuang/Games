import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Maze extends Game {

  public static final int W = 30, H = 20;
  public static final int xM = 50, yM = 50, c = 30;
  public static int[] next = new int[W + 1];//pointer to next
  public static int[] prev = new int[W + 1];//pointer back to prev
  public static int y;
  public static Graphics gg;

  public Maze() {
    super("Maze", 1000, 800);
  }


  @Override
  public void paintComponent(Graphics g) {
    gg = g;
    gg.setColor(Color.white);
    gg.fillRect(0, 0, 5000, 5000);
    gg.setColor(Color.black);
    G.RANDOM.setSeed(1000);
    hRow0();// draw top h row
    mid();// alternating between vrow and hrow
    vLast();// last vertical row
    hLast();
  }

  public void hRow0() {
    y = yM;
    singletonCycle(0);
    for (int i = 0; i < W; i++) {
      singletonCycle(i + 1);
      hLine(i);
    }
  }

  public void hRule(int i) {
    // is legal to connect i, i+1
    if (!sameCycle(i, i + 1) && pH()) {
      hLine(i);
    }
  }

  public void vRule(int i) {
    if (next[i] == i || pV()) {
      // do not abandon lonely guys
      vLine(i);
    } else {
      // not drawing v line
      noVLine(i);
    }
  }

  public void hRow() {
    for (int i = 0; i < W; i++) {
      hRule(i);
    }
  }

  public void vRow() {
    for (int i = 1; i < W; i++) {
      vRule(i);
    }
    vLine(0);
    vLine(W);
  }

  public void noVLine(int i) {
    split(i);
  }

  public void mid() {
    for (int i = 0; i < H - 1; i++) {
      vRow();
      y += c;
      hRow();
    }
  }

  public void vLast() {
    vLine(0);
    vLine(W);
    for (int i = 1; i < W; i++) {
      if (!sameCycle(i, 0)) {
        merge(i, 0);
        vLine(i);
      }
    }
  }

  public void hLast() {
    y += c;
    for (int i = 0; i < W; i++) {
      hLine(i);
    }
  }

  public int x(int i) {
    return xM + i * c;
  }

  public void hLine(int i) {
    gg.drawLine(x(i), y, x(i + 1), y);
    merge(i, i + 1);
  }

  public void merge(int i, int j) {
    int ip = prev[i], jp = prev[j];
    next[ip] = j;
    next[jp] = i;
    prev[i] = jp;
    prev[j] = ip;
  }

  public void split(int i) {
    int ip = prev[i], in = next[i];
    next[ip] = in;
    prev[in] = ip;
    next[i] = i;
    prev[i] = i;
  }

  public void singletonCycle(int i) {
    next[i] = i;
    prev[i] = i;
  }

  public boolean sameCycle(int i, int j) {
    int n = next[i];
    while (n != i) {
      if (n == j) {
        return true;
      }
      n = next[n];
    }
    return false;
  }

  public static boolean pV() {
    // probability of making vertical connection
    return G.rnd(100) < 33;
  }

  public static boolean pH() {
    // probability of making horizontal connection
    return G.rnd(100) < 47;
  }

  public void vLine(int i) {
    gg.drawLine(x(i), y, x(i), y + c);
  }

  @Override
  public void endGame() {

  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {

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
}
