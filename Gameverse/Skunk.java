import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Skunk extends Game {

  public static String AIName = "Archie";
  public static int maxScore = 100, strategyScore = 10;
  public static G.Button.List cmds = new G.Button.List();
  public static G.Button PASS = new G.Button(cmds, "pass") {
    @Override
    public void act() {
      pass();
    }
  };
  public static G.Button ROLL = new G.Button(cmds, "roll") {
    @Override
    public void act() {
      roll();
    }
  };
  public static G.Button AGAIN = new G.Button(cmds, "again") {
    @Override
    public void act() {
      playAgain();
    }
  };
  /*
    static {
      PASS.set(100, 100);
      PASS.enable = true;
      ROLL.set(150, 100);
      ROLL.enable = true;
    }
  */
  public static int M = 0, E = 0, H = 0;// me, enemy, hand score
  public static boolean myTurn = true;
  public static int D1, D2;// two dices
  public static int xM = 50, yM = 50;

  public Skunk() {
    super("Skunk", 1000, 800);
//    converge(8000000);// can use a timer here
    playAgain();
  }

  public static void playAgain() {
    M = 0;
    E = 0;
    H = 0;
    myTurn = G.rnd(2) == 0;
    PASS.set(100, 100);
    ROLL.set(150, 100);
    AGAIN.set(-100, -100);
  }

  public static void roll() {
    D1 = G.rnd(6) + 1;
    D2 = G.rnd(6) + 1;
    analyseDice();
  }

  public static String skunkMsg = "";

  public static void showRoll(Graphics g) {
    g.setColor(Color.BLACK);
    String playerName = myTurn ? "Your" : AIName + "'s";
    g.drawString(playerName + " Roll: " + D1 + ", " + D2 + skunkMsg, xM, yM + 20);
  }


  public static void analyseDice() {
    PASS.enable = true;
    ROLL.enable = true;
    if (D1 == 1 && D2 == 1) {
      totalSkunk();
      skunkMsg = ". Totally skunked!";
    } else if (D1 == 1 || D2 == 1) {
      skunked();
      skunkMsg = ". Skunked!";
    } else {
      skunkMsg = ".";
      normalHand();
    }
  }

  public static String scoreStr() {
    return "hand score: " + H + ",   your score: " + M + ",   " + AIName + "'s score: " + E
        + ".";
  }

  public static void showScore(Graphics g) {
    g.setColor(Color.black);
    g.drawString(scoreStr(), xM, yM + 40);
  }

  public static String gameOverMsg() {
    String res = "";
    int total = H + (myTurn ? M : E);
    if (total > maxScore) {
      res = (myTurn ? "You" : AIName) + " wins!";
      gameOver();
    }
    return res;
  }

  public static void gameOver() {
    PASS.set(-100, -100);
    ROLL.set(-100, -100);
    AGAIN.set(100, 100);
  }

  public static void pass() {
    if (myTurn) {
      M += H;
    } else {
      E += H;
    }
    H = 0;
    ROLL.enable = true;
    myTurn = !myTurn;
    roll();
  }

  public void paintComponent(Graphics g) {

    G.whiteBackground(g);
    if (showStrategy) {
      converge(1000000);
      showAll(g);
    } else {
      if (gameOverMsg().length() == 0) {
        showRoll(g);
        showScore(g);
      } else {
        g.setColor(Color.black);
        g.drawString(gameOverMsg(), xM, yM);
        gameOver();
      }
      cmds.showAll(g);
    }
  }

  @Override
  public void endGame() {
  }

  public static void totalSkunk() {
    if (myTurn) {
      M = 0;
    } else {
      E = 0;
    }
    skunked();
  }

  public static void skunked() {
    H = 0;
    ROLL.enable = false;
  }

  public static void normalHand() {
    H += (D2 + D1);
    setAIButtons();
  }

  public static boolean gottaRoll() {
    // if rolling is illegal
    wOptimal(E, M, H);
    return ROLL.enable && !shouldPass;
  }

  public static void setAIButtons() {
    if (!myTurn) {
      if (gottaRoll()) {
        PASS.enable = false;
      } else {
        ROLL.enable = false;
      }
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  public void mousePressed(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    if (cmds.click(x, y)) {
      repaint();
      return;
    }
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


  //------------------ AI -----------------------
  public static double[][][] P = new double[100][100][100];

  public static double p(int m, int e, int h) {
    if (m + h >= 100) {
      return 1.0;
    } else if (e >= 100) {
      return 0.0;
    }
    return P[m][e][h];
  }

  public static double wPass(int m, int e, int h) {
    //win possibility if pass
    return 1.0 - p(e, m + h, 0);
  }

  public static double wTS(int m, int e, int h) {
    //win possibility if total skunk
    return 1.0 - p(e, 0, 0);
  }

  public static double wS(int m, int e, int h) {
    // win possibility if skunk
    return 1.0 - p(e, m, 0);
  }

  public static double wRoll(int m, int e, int h) {
    // win possibility if keep rolling
    double res = wTS(m, e, h) / 36 +// if total skunked
        wS(m, e, h) / 3.6;//if skunk
    for (int d1 = 2; d1 <= 6; d1++) {
      for (int d2 = 2; d2 <= 6; d2++) {
        res += p(m, e, h + d1 + d2) / 36;
      }
    }
    return res;
  }

  public static boolean shouldPass;//set by side effect

  public static double wOptimal(int m, int e, int h) {
    // possibility of winning if you do the optimal move
    double wP = wPass(m, e, h), wR = wRoll(m, e, h);
    return (shouldPass = (wP > wR)) ? wP : wR;
  }

  public static void converge(int n) {
    for (int i = 0; i < n; i++) {
      int m = G.rnd(100), e = G.rnd(100), h = G.rnd(100);
      P[m][e][h] = wOptimal(m, e, h);
    }
  }
  //-------------------- Visualization ------------------------

  public static final int W = 7;
  public static boolean showStrategy = true;

  public static void showAll(Graphics g) {
    showStops(g);
    showGrid(g);
    showColorMap(g);
  }

  public static final int nC = 45;//number of color
  public static Color[] stopColor = new Color[nC];

  static {
    for (int i = 0; i < nC; i++) {
      stopColor[i] = new Color(G.rnd(255), G.rnd(255), G.rnd(255));
    }
  }

  public static void showStops(Graphics g) {
    for (int m = 0; m < 100; m++) {
      for (int e = 0; e < 100; e++) {
        int k = firstStop(m, e);
        g.setColor(stopColor[k]);
        g.fillRect(xM + W * m, yM + W * e, W, W);
      }
    }
  }

  public static int firstStop(int m, int e) {
    int h = 0;
    for (h = 0; h < 100 - m; h++) {
      wOptimal(m, e, h);
      if (shouldPass) {
        return h >= nC ? 0 : h;
      }
    }
    return 0;
  }

  public static void showGrid(Graphics g) {
    g.setColor(Color.black);
    for (int k = 0; k < 10; k++) {
      int d = 10 * W * k;
      g.drawLine(xM, yM + d, xM + 100 * W, yM + d);
      g.drawLine(xM + d, yM, xM + d, yM + 100 * W);
    }
  }


  public static void showColorMap(Graphics g) {
    int x = xM + 100 * W + 30;
    for (int i = 0; i < nC; i++) {
      g.setColor(stopColor[i]);
      g.fillRect(x, yM + 15 * i, 15, 10);
      g.setColor(Color.black);
      g.drawString("" + i, x + 20, yM + 15 * i + 10);
    }
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
  public void mouseDragged(MouseEvent e) {

  }

  @Override
  public void mouseMoved(MouseEvent e) {

  }
}
