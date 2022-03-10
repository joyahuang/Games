import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.Timer;

public class Destructo extends Window implements ActionListener {
  public static final int nR=15,nC =10;// number of rows, columns
  public int[][] grid=new int[nC][nR]; // nC is the x, nR is the y
  public static Color[] color={
      Color.lightGray,
      Color.cyan,
      Color.green,
      Color.yellow,
      Color.red,
      Color.pink,
  };
  public static Random RANDOM=new Random();
  public static int rnd(int k){return RANDOM.nextInt(k);}
  public static Timer timer;
  public int w=50,h=30;//cell
  public int xm=100,ym=100;//margin
  public int brickRemain;

  public Destructo() {
    super("Destructo", 1000, 800);
    rndColors(3);
    timer=new Timer(100,this);// millsecond 1000ms=1s
    timer.start();
  }
  public void paintComponent(Graphics g){
    g.setColor(color[0]);
    g.fillRect(0,0,5000,5000);
    showGrid(g);
    bubbleSort();
    if(slideCol()){xm+=w/2;};
    g.setColor(Color.black);
    g.drawString("Remaining: "+brickRemain,50,50);
  }

  public void rndColors(int k){
    brickRemain=nR*nC;
    for(int c=0;c<nC;c++){
      for(int r=0;r<nR;r++){
        // 0 is the background color(light gray), skip 0
        grid[c][r]=1+rnd(k);
      }
    }
  }
  public void showGrid(Graphics g){
    for(int c=0;c<nC;c++){
      for(int r=0;r<nR;r++){
        // 0 is the background color, skip 0
        g.setColor(color[grid[c][r]]);
        g.fillRect(x(c),y(r),w,h);
      }
    }
  }
  public int x(int c){
    return xm+c*w;
  }
  public int y(int r){
    return ym+r*h;
  }
  public int c(int x){
    return (x-xm)/w;
  }
  public int r(int y){
    return (y-ym)/h;
  }
  public void mouseClicked(MouseEvent me){
    int x=me.getX(),y=me.getY();
    if(x<=xm||y<=ym){
      // click on the margin
      return;
    }
    int r=r(y),c=c(x);
    if(r<nR&&c<nC) {
      rcAction(r, c);
    }
  }
  public void rcAction(int r,int c){
    if(infectable(c,r)) {
      infect(c, r, grid[c][r]);

      repaint();
    }
  }
  public void infect(int c,int r,int v){
    if(grid[c][r]!=v)return;
    grid[c][r]=0;// kill the cell before infecting neighbors
    brickRemain--;
    if(r>0)infect(c,r-1,v);
    if(c>0)infect(c-1,r,v);
    if(r<nR-1)infect(c,r+1,v);
    if(c<nC-1)infect(c+1,r,v);
  }
  public boolean infectable(int c,int r){
    int v=grid[c][r];
    if(v==0)return false;//empty cell
    if(r>0) {
      if(grid[c][r - 1] == v) return true;
    };
    if(c>0){
      if(grid[c-1][r] == v) return true;
    };
    if(r<nR-1){
      if(grid[c][r + 1] == v) return true;
    };
    if(c<nC-1){
      if(grid[c+1][r] == v) return true;
    };
    return false;
  }
  public boolean bubble(int c){
    boolean res=false;
    for(int r=nR-1;r>0;r--){
      if(grid[c][r]==0&&grid[c][r-1]!=0){
        grid[c][r]=grid[c][r-1];
        grid[c][r-1]=0;
        res=true;
      }
    }
    return res;
  }
  public void bubbleSort(){
    for(int c=0;c<nC;c++){
      if(bubble(c)){
        // since this bubbleSort is in "paintComponent", it is already in a while loop
        // in one tick, move one box
        break;// take you out of for loop, finish one column's all movement, then go to next one column
      }
    }
  }
  public boolean columnIsEmpty(int c){
    for(int r=0;r<nR;r++){
      if(grid[c][r]!=0)return false;
    }
    return true;
  }
  public void swapCol(int c){
    for(int r=0;r<nR;r++){
      grid[c-1][r]=grid[c][r];
      grid[c][r]=0;
    }

  }
  public boolean slideCol(){
    boolean res=false;
    for(int c=1;c<nC;c++){
      if(columnIsEmpty(c-1)&&!columnIsEmpty(c)) {
        swapCol(c);
        res=true;
      }
    }
    return res;
  }
  public static void main(String[] args) {
    Window.PANEL=new Destructo();
    Window.launch();
    System.out.println("launched");
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
  }
}
