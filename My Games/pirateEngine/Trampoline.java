import java.awt.*;

public class Trampoline extends Wall{
    private int x,y,h,w,vx,vy,count,drawX,drawY;
    private Rectangle box;

    public Trampoline(int xx, int yy, int width, int height){
        super(xx,yy,width,height);
        x = xx;
        y = yy;
        w = width;
        h = height;
        vy = 0;
        vx = 0;
        count = 0;
        box = new Rectangle(x,y,w,h);
    }

    public void draw(Graphics2D g){
        int columns = w/30;
        int rows = h/15;
        int width = 30;
        int height = 15;
        Color c = new Color(106,97,70);
        g.setColor(Color.green);
        g.fillRect(drawX,drawY,w,h);
    }

    public void move(int scrollX, int scrollY){
        drawX = x - scrollX;
        drawY = y - scrollY;
    }

    public Color getColor(){return Color.red;}

    public void screenSlide(int bobX){x+=bobX; box.translate(bobX,0);}

    public Rectangle getWall(){return box;}

    public int getX(){return x;}

    public int getY(){return y;}

    public int getVY(){return vy;}

    public int getVX(){return vx;}

    public int getHeight(){return h;}

    public int getWidth(){return w;}
}