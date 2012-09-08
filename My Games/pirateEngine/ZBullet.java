import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class ZBullet
{
    private int x, y, d,f, count,firstX,firstY;
    private double vX, vY;
    private RainbowGun gun;
    private Color c;
    private boolean remove;
    private Rectangle box;

    public ZBullet(double vx, double vy, int X, int Y, int far, int damage, Color C, RainbowGun Ralph,int fx, int fy)
    {
        gun = Ralph;
        vX = vx;
        vY = vy;
        firstX = fx;
        firstY = fy;
        x = X;
        y = Y;
        d = damage;
        f = far;
        count = 0;
        c = C;
        if(gun.getGun() == "Just a Black Hole Gun Nothing More")
            box = new Rectangle(x-150,y-150,300,300);
        else if(gun.getGun() != "Blue LAZER OF DOOM")
            box = new Rectangle(x,y,5,5);
        else
            box = new Rectangle(x,y,10,10);
    }

    public void move()
    {
        if(count == f)
            remove = true;
        if(remove != true)
        {
            if(count == 0)
            {
                x+=(int)vX;
                y+=(int)vY;
                box.translate((int)vX,(int)vY);
            }
            else
            {
                if(gun.getGun() != "Rainbow Machine Gun" && gun.getGun() != "Blue LAZER OF DOOM")
                {
                    if(Math.random() < .5 && vX < 11)
                        vX += (int)(Math.random()*2);
                    else if( vY > -15)
                        vX -= (int)(Math.random()*2);
                    if(Math.random() < .5 && vY < 11)
                        vY += (int)(Math.random()*2);
                    else if(vY > -15)
                        vY -= (int)(Math.random()*2);
                }
                x-=(int)(count*vX);
                y-=(int)(count*vY);
                box.translate((int)(-1*count*vX),(int)(-1*count*vY));
                x+=(int)((count+1)*vX);
                y+=(int)((count+1)*vY);
                box.translate((int)((count+1)*vX),(int)((count+1)*vY));
            }    
            count++;
        }
    }

    public void draw(Graphics2D g2)
    {
        g2.setColor(c);
        if(c == Color.blue)
        {
            int[] xp = new int[4];
            int[] yp = new int[4];
            xp[0] = x;
            xp[1] = x+(int)vX*100+firstX;
            xp[2] = x+10+(int)vX*100+firstX;
            xp[3] = x+10;
            yp[0] = y+10;
            yp[1] = y+(int)vY*100+10+firstY;
            yp[2] = y+(int)vY*100+firstY;
            yp[3] = y;

            Polygon p = new Polygon(xp,yp,4);
            g2.fillPolygon(p);
        }
        else
            g2.fillOval(x,y,5,5);
    }

    public int getX(){return x;}

    public int getY() {return y;}

    public int getDamage() {return d;}

    public Color getColor() {return c;}

    public boolean Remove() {return remove;}

    public Rectangle getBox() {return box;}
}
