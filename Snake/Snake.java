import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.*;

public class Snake extends Window implements ActionListener {
    public static Random RND = new Random();
    public static int rnd(int k) {
        return RND.nextInt(k);
    }
    public static int W = 40, H = 30, C = 20, X_off = 100, Y_off = 100;
    // static means single global variable
    public static Timer timer;
    public static final int snakesize=3;
    public static Chain chain = new Chain(snakesize);
    public static char turn='a';//L=left, R=right, else=straight
    public static final int initialTime=100;
    public static int timeToDeath=initialTime;// timer
    public static int foodValue=initialTime;
    public static Point[] foods={new Point(rnd(W),rnd(H)),new Point(rnd(W),rnd(H)),new Point(rnd(W),rnd(H))};
    public static Color foodColor=Color.pink;
    public Snake() {
        super("Snake", 1000, 800);
        timer = new Timer(50, this);
        timer.start();
    }
    public static void drawCell(Graphics g, Point p, Color c){
        g.setColor(c);
        g.fillRect(p.x * C + X_off, p.y * C + Y_off, C, C);
    }

    public void paintComponent(Graphics g) {
        if(!gameOver)timeToDeath--;
        if(timeToDeath==0) {
            gameOver = true;
            g.drawString("Game Over",400,300,);
        }
        // set the canvas background
        if(timeToDeath<10 && timeToDeath%2==0){
            g.setColor(Color.gray);
        }else{
            g.setColor(Color.white);
        }

        g.fillRect(0, 0, 2000, 1000);
        // draw boundary
        g.setColor(Color.black);
        g.drawRect(X_off,Y_off,W*C,H*C);
        g.drawString("Time: "+timeToDeath,50,50);
        // draw the snake
        g.setColor(Color.RED);
        chain.draw(g);
        for(Point food:foods){
            drawCell(g,food,foodColor);
        }

        if(!gameOver) {
            chain.step();
            // keyboard
            System.out.println(turn);
            if(turn=='L') {
                chain.left();
                turn='a';
            }
            else if(turn=='R') {
                chain.right();
                turn='a';
            }
        }else{
            // game is over
            chain.drawDeadHead(g);
        }

    }
    public void startOver(){
        gameOver=false;
        chain=new Chain(snakesize);
        timeToDeath=initialTime;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
    @Override
    public void keyTyped(KeyEvent ke) {
        char c=ke.getKeyChar();
        if(c==' '){
            startOver();
        }
        else if(c=='j') {
            turn = 'R';
            return;
        }
        else if(c=='k') {
            turn = 'L';
            return;
        }
        turn='a';
        return;
    }

    static boolean gameOver=false;
    public static class Chain extends ArrayList<Point> {
        int head;
        Point dH = new Point(1, 0);// delta H, direction the snake goes in

        public Chain(int n) {
            head = 0;
            int x = rnd(W), y = rnd(H);
            for (int i = 0; i < n; i++) {
                add(new Point(x, y));
            }
        }

        public void draw(Graphics g) {
            for (int i = 0; i < this.size(); i++) {
                Point p = get(i);
                drawCell(g,p,Color.RED);
            }
        }
        public void drawDeadHead(Graphics g){
            Point p=get(head);
            drawCell(g,p,Color.yellow);
        }

        public void step() {
            int tail = (head + 1) % this.size();// circulor
            Point t = get(tail), h = get(head);
            t.x = h.x + dH.x;
            t.y = h.y + dH.y;
            head = tail;
            detectCollisions();
            detectFood();
        }

        public void left() {
            int t = dH.x;
            dH.x = dH.y;
            dH.y = t;
            dH.x = -dH.x;
        };

        public void right() {
            int t = dH.x;
            dH.x = dH.y;
            dH.y = t;
            dH.y = -dH.y;
        };
        public void detectFood(){
            System.out.println("detect food");
            Point h=get(head);
            for(Point food:foods){
                if(h.x==food.x &&h.y==food.y){
                    timeToDeath+=foodValue;
                    food.x=rnd(W);
                    food.y=rnd(H);
                    // add new point in the array
                    chain.add(new Point(h.x,h.y));
                }
            }


        }
        public void detectCollisions(){
            // collide the wall
            Point h=get(head);
            if(h.x<0||h.y<0||h.x>=W||h.y>=H) {
                gameOver = true;
            }
            // self collision
            for(Point p:this){
                if(p!=h){
                    if(p.x==h.x&&p.y==h.y)gameOver=true;
                }
            }
        }
    }
}
