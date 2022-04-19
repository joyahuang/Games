
import java.awt.Color;
import java.awt.Graphics;

public class XED extends Window{
  public static EXP s;
  public static EXP.View view;
  public XED(){
    super("XED",1000,800);
    EXP n=EXP.newA0(" 3 ");
    System.out.println(n);
    EXP v=EXP.newA0(" foobar ");
    EXP m=EXP.newA2("*");
    m.kids[0]=v;
    m.kids[1]=v;
    System.out.println(v);
    s=EXP.newA2(" adfalsd;fj;lasdjflasfasjdoipfjasidofj ");
    s.kids[0]=n;
    s.kids[1]=m;
    System.out.println(s);
    view=new EXP.View(s,null);
  }

  public void paintComponent(Graphics g){
    g.setColor(Color.white);
    g.fillRect(0,0,5000,5000);
    view.layout(g,100,100);
    view.show(g);
  }
  public static void main(String[] args) {
    PANEL=new XED();
    PANEL.launch();
  }
  //-------------------------EXP-------------------------
  public static class EXP{
    String name;
    int nKids=0;
    EXP[] kids=null;
    private EXP(String name,int nKids){
      this.name=name;
      this.nKids=nKids;
      this.kids=(nKids>0)?new EXP[nKids]:null;
    }
    public static EXP newA0(String name){return new EXP(name,0);}
    public static EXP newA1(String name) {return new EXP(name, 1);}
    public static EXP newA2(String name) {return new EXP(name, 2);}
    public String toString(){
      String res="";
      for(int i=0;i<nKids;i++){
        res+= " "+kids[i].toString();
      }
      return res+" "+name;
    }

    //-----------------------VIEW-----------------------
    public static class View{
      EXP exp;
      View dad;
      int nKids;
      View[] kids;
      int x,y,w,h;// bounding box
      int dx,dy;//how far from the corner

      public View(EXP exp, View dad) {
        this.exp = exp;
        this.dad = dad;
        this.nKids= exp.nKids;
        this.kids=(this.nKids>0)?new View[this.nKids]:null;
        for(int i=0;i<nKids;i++){
          kids[i]=new View(exp.kids[i],this);
        }
      }
      // header
      public int hW(Graphics g){return g.getFontMetrics().stringWidth(exp.name);}
      public int hH(Graphics g){return g.getFontMetrics().getHeight();}
      // total
      public int width(Graphics g){
        if(w>-1) {return w;}//memorization
        w=Math.max(hW(g),kW(g));// whichever is bigger
        return w;
      }
      public int height(Graphics g){
        if(h>-1){return h;}
        h=hH(g)+maxKH(g);
        return h;
      }
      // kid
      public int kW(Graphics g){// sum of all kids width, not single
        if(nKids==0){return 0;}
        int res=0;
        for(int i=0;i<nKids;i++){
          res+=kids[i].width(g);
        }
        return res;
      }
      public int maxKH(Graphics g){// biggest kid height
        if(nKids==0){return 0;}
        int max=0;
        for(int i=0;i<nKids;i++){
          max=Math.max(max,kids[i].height(g));
        }
        return max;
      }
      public void nuke(){//initialize width and height values
        w=-1;h=-1;
        if(nKids>0){
          for(int i=0;i<nKids;i++){
            kids[i].nuke();
          }
        }
      }
      public void layout(Graphics g,int xx,int yy){// upper corner of the box
        nuke();
        w=width(g);
        h=height(g);
        locate(g,xx,yy);
      }
      public void locate(Graphics g,int xx,int yy){
        x=xx;
        y=yy;
        dx=(w-hW(g))/2;//indent of header
        dy=g.getFontMetrics().getAscent();
        if(nKids==0)return;
        int kx=(w-kW(g))/nKids;// indent of kid
        yy+=hH(g);//update yy to kids
        for(int i=0;i<nKids;i++){
          kids[i].w+=kx;// update kids width
          kids[i].locate(g,xx,yy);
          xx+=kids[i].w;// update x coordinate for the next kid
        }
      }
      public void show(Graphics g){
        g.setColor(Color.cyan);
        g.drawRect(x,y,w,h);
        g.setColor(Color.black);
        rShow(g);// recursive
      }
      public void rShow(Graphics g){
        g.drawString(exp.name,x+dx,y+dy);
        if(nKids>0){
          int ky=kids[0].y;
          g.drawLine(x,ky,x+w,ky);
          kids[0].rShow(g);
          for(int i=1;i<nKids;i++){
            int kx=kids[i].x;
            g.drawLine(kx,ky,kx,y+h);
            kids[i].rShow(g);
          }
        }
      }
    }
  }
}
