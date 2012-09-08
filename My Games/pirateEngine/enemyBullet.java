import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class enemyBullet
{
    private int vX, vY, x, y, d,f, count;
    private RainbowGun gun;
    private Color c;
    private boolean remove;
    private Rectangle box;

    public enemyBullet(int vx, int vy, int X, int Y, int far, int damage, Color C)
    {
        vX = vx;
        vY = vy;
        x = X+15;
        y = Y+15;
        d = damage;
        f = far;
        count = 0;
        c = C;
        box = new Rectangle(x,y,5,5);
    }

    public void move()
    {
        if(count == f)
            remove = true;
        if(remove != true)
        {
            x+=vX;
            y+=vY;
            box.translate(vX,vY);
            count++;
        }
    }

    public void draw(Graphics2D g2)
    {
        g2.setColor(c);
        g2.fillOval(x,y,5,5);
    }

    public int getX(){ return x;}

    public int getY() { return y;}

    public int getDamage() { return d;}

    public boolean Remove() {return remove;}

    public Rectangle getBox() {return box;}
}
