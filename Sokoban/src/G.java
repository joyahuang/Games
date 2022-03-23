import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class G {

  public static Random Random = new Random();

  public static int rnd(int k) {
    return Random.nextInt(k);
  }
  public static void whiteBackground(Graphics g){
    g.setColor(Color.WHITE);
    g.fillRect(0,0,5000,5000);

  }
  //------------V---------------
  // Vector class
  public static class V{
    public int x=0,y=0;

    public V(int x, int y) {
      this.x = x;
      this.y = y;
    }
    public V(){

    }

    public V(V v) {
      this.x = v.x;
      this.y = v.y;
    }
    public void add(V v){
      this.x=x+v.x;
      this.y=y+v.y;
    }
    public void set(V v){
      this.x=v.x;
      this.y=v.y;
    }
  }
  //------------VS--------------
  // Vector Size class
  public static class VS{
    public V loc,size;
    public VS(int x,int y, int w,int h) {
      this.loc = new V(x,y);
      this.size = new V(w,h);
    }
    public void fill(Graphics g,Color c){
      g.setColor(c);
      g.fillRect(loc.x,loc.y,size.x,size.y);
    }
    public void draw(Graphics g,Color c){
      // only the border, outline
      g.setColor(c);
      g.drawRect(loc.x,loc.y,size.x,size.y);
    }
    public boolean hit(int x,int y){
      return x>loc.x&&y>loc.y&&x< loc.x+size.x&&y< loc.y+size.y;
    }
  }

}
