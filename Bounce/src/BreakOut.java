
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.Timer;

public class BreakOut extends Window implements ActionListener {

  public static final int H = 16, W = 50, PW = 100, nBrick = 13, PV = 8;//paddle width, paddle velocity
  public static final int LEFT = 100, RIGHT = LEFT + nBrick * W, TOP = 50, BOT = 700;
  public static final int MAX_LIFE = 3;
  public static int lives = MAX_LIFE, score = 0;
  public static final int GAP = 3 * H;
  public static int rowCount = 1;
  public static Paddle paddle = new Paddle();
  public static Ball ball = new Ball();
  public Timer timer;

  public BreakOut() {
    super("Breakout", 1000, 800);
    timer = new Timer(30, this);
    timer.start();
    startGame();
  }

  public void paintComponent(Graphics g) {
    G.whiteBackground(g);
    g.setColor(Color.black);
    g.fillRect(LEFT, TOP, RIGHT - LEFT, BOT - TOP);
    g.drawString("Lives: " + lives, LEFT + 20, 30);
    g.drawString("Score: " + score, RIGHT - 80, 30);
    ball.show(g);
    paddle.show(g);
    Brick.List.ALL.show(g);
  }

  public static void startGame() {
    lives = MAX_LIFE;
    score = 0;
    rowCount = 0;
    startNewRows();
  }

  public static void startNewRows() {
    rowCount++;
    Brick.List.ALL.clear();
    Brick.newBrickRow(rowCount);
    ball.init();
  }

  @Override
  public void keyPressed(KeyEvent ke) {
    int vk = ke.getKeyCode();
    if (vk == KeyEvent.VK_LEFT) {
      paddle.left();
    }
    if (vk == KeyEvent.VK_RIGHT) {
      paddle.right();
    }
    if (ke.getKeyChar() == ' ') {
      paddle.dxStuck = -1;
    }
    repaint();
  }

  public static void main(String[] args) {
    PANEL = new BreakOut();
    PANEL.launch();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    ball.move();
    repaint();
  }

  //------------------Brick-------------------
  public static class Brick extends G.VS {

    public static Color[] colors = {Color.RED, Color.yellow, Color.blue, Color.green, Color.orange,
        Color.cyan};
    public Color color;

    public Brick(int x, int y) {
      super(x, y, W, H);
      color = colors[G.rnd(colors.length)];
      Brick.List.ALL.add(this);
    }

    public void show(Graphics g) {
      fill(g, color);
      draw(g, Color.black);
    }

    public boolean hit(int x, int y) {
      return (x < loc.x + W && x + H > loc.x && y > loc.y && y < loc.y + H);
    }

    public static void newBrickRow(int n) {
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < nBrick; j++) {
          new Brick(LEFT + j * W, TOP + GAP + i * H);
        }
      }
    }

    public void destroy() {
      ball.dy = -ball.dy;
      Brick.List.ALL.remove(this);
      score += 17;
      if (Brick.List.ALL.isEmpty()) {
        startNewRows();
      }
    }

    public static class List extends ArrayList<Brick> {

      public static List ALL = new List();

      public void show(Graphics g) {
        for (Brick b : ALL) {
          b.show(g);
        }
      }

      public static void ballHitBrick() {
        int x = ball.loc.x, y = ball.loc.y;
        for (Brick b : ALL) {
          if (b.hit(x, y)) {
            b.destroy();
            return;
          }
        }
      }
    }
  }

  //------------------Ball---------------------
  public static class Ball extends G.VS {

    public static final int DY_START = -11;
    public Color color = Color.white;
    public int dx = 11, dy = DY_START;//velocity

    public Ball() {
      super(LEFT, BOT - 2 * H, H, H);
    }

    public void init() {
      paddle.dxStuck = PW / 2 - H / 2;
      loc.set(paddle.loc.x + paddle.dxStuck, BOT - 2 * H);
      dx = 0;
      dy = DY_START;
    }

    public void show(Graphics g) {
      this.fill(g, color);
    }

    public void move() {
      if (paddle.dxStuck < 0) {
        loc.x += dx;
        loc.y += dy;
        wallBounce();
        Brick.List.ballHitBrick();
      }
    }

    public void wallBounce() {
      if (loc.x < LEFT) {
        loc.x = LEFT;
        dx = -dx;
      }
      if (loc.x + H > RIGHT) {
        loc.x = RIGHT - H;
        dx = -dx;
      }
      if (loc.y < TOP) {
        loc.y = TOP;
        dy = -dy;
      }
      if (loc.y + 1 * H > BOT) {
        paddle.hitBall();
      }
    }

  }

  //------------------Paddle---------------------
  public static class Paddle extends G.VS {

    public Color color = Color.yellow;
    public int dxStuck = 10;

    public Paddle() {
      super(LEFT, BOT - H, PW, H);
    }

    public void show(Graphics g) {
      this.fill(g, color);
    }

    public void left() {
      this.loc.x -= PV;// paddle velocity
      limitX();
    }

    public void right() {
      this.loc.x += PV;// paddle velocity
      limitX();
    }

    public void limitX() {
      if (loc.x < LEFT) {
        loc.x = LEFT;
      }
      if (loc.x + PW > RIGHT) {
        loc.x = RIGHT - PW;
      }
      if (dxStuck >= 0) {
        // change where the ball is
        ball.loc.set(loc.x + dxStuck, BOT - 2 * H);
      }
    }

    public void hitBall() {
      if (ball.loc.x < loc.x || ball.loc.x > loc.x + PW) {
        // ball did not land on paddle, live lost
        lives--;
        if (lives == 0) {
          startGame();
        } else {
          ball.init();
        }

      } else {
        // ball need to bounce
        ball.dy = -ball.dy;
        ball.dx += dxAdjust();
      }
    }

    public int dxAdjust() {
      int cp = paddle.loc.x + PW / 2;
      return (ball.loc.x + H / 2 - cp) / 10;// sign
    }
  }
  //-------------------
}
