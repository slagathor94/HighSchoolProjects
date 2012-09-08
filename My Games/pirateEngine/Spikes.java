import java.awt.*;

public class Spikes extends Wall{
    private int x,y,h,w,vx,vy,count,drawX,drawY;
    private int[] zstarsx, zstarsy, zstarsd, zstarsc;
    private Rectangle box;

    public Spikes(int xx, int yy, int width, int height){
        super(xx,yy,width,height);
        x = xx;
        y = yy;
        w = width;
        h = height;
        vy = 0;
        vx = 0;
        count = 0;
        box = new Rectangle(x,y,w,h);
        zstarsx = new int[50*w/30*h/30];
        zstarsy = new int[50*w/30*h/30];
        zstarsd = new int[50*w/30*h/30];
        zstarsc = new int[50*w/30*h/30];
        for(int i = 0;i<zstarsx.length;i++)
        {
            zstarsx[i] = (int)(Math.random()*w);
            zstarsy[i] = (int)(Math.random()*h);
            zstarsd[i] = (int)(Math.random()*1+1);
            //zstarsc[i] = (int)(Math.random()*1+2);
        }
    }

    public void draw(Graphics2D g){
        g.setColor(Color.red);

        g.fillRect(drawX,drawY,w,h);
        for(int i = 0; i<zstarsx.length;i++){
            if(Math.random()*1 < .5)
                g.setColor(Color.yellow);
            else
                g.setColor(Color.orange);
            g.fillRect(zstarsx[i]+drawX,zstarsy[i]+drawY,zstarsd[i],zstarsd[i]);
        }
    }

    public void move(int scrollX, int scrollY){
        drawX = x - scrollX;
        drawY = y - scrollY;
        count++;
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