import java.awt.*;
import java.awt.event.MouseEvent;

public class Skunk extends Window {

  public static String AIName = "Archie";
  public static int maxScore = 30, strategyScore = 15;
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
  }

  public static void showRoll(Graphics g) {
    g.setColor(Color.BLACK);
    String playerName = myTurn ? "Your" : AIName + "'s";
    g.drawString(playerName + " Roll: " + D1 + ", " + D2 + skunkMsg(), xM, yM + 20);
  }


  public static String skunkMsg() {
    if (D1 == 1 && D2 == 1) {
      totalSkunk();
      return ". Totally skunked!";
    } else if (D1 == 1 || D2 == 1) {
      skunked();
      return ". Skunked!";
    }
    normalHand();
    return ".";
  }

  public static String scoreStr() {
    return "hand score: " + H + ",   your score: " + M + ",   " + AIName + "'s score: " + E + ".";
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

  //--------------AI---------------
  public static boolean gottaRoll() {
    // if rolling is illegal
    return ROLL.enable && H < strategyScore;
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

  public void mousePressed(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    if (cmds.click(x, y)) {
      PASS.enable = true;
      ROLL.enable = true;
      repaint();
      return;
    }
  }

  public static void main(String[] args) {
    PANEL = new Skunk();
    PANEL.launch();
  }
}
