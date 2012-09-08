import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Character
{
    private int x, y, vx, vy, health,life;
    private Color c;
    private Rectangle box;
    private boolean die;
    private ArrayList<enemyBullet> bullets;

    public Character()
    { 
        x = 375;
        y = 375;
        bullets = new ArrayList<enemyBullet>();
        box = new Rectangle(x,y,30,30);
        health = 100;
        life = 3;
    }

    public void move(Applet app,boolean slowA, boolean slowD, boolean a,
    boolean d, boolean slowS, boolean slowW, boolean w, boolean s, ArrayList<enemyBullet> bullet)
    {
        int lasty = y;
        int lastx = x;
        bullets = bullet;

        // Checks Collision
        for(int i = 0; i<bullets.size();i++)
        {
            if(box.intersects(bullets.get(i).getBox()))
            {
                damage(bullets.get(i).getDamage());
                bullets.remove(i);
            }
        }

        // Moves Character's X
        if(slowA== false || slowD == false)
        {
            if(slowA== false)
            {
                if(a == true)
                {
                    if(vx>-6)
                        vx-=1;
                }
            }
            if(slowD == false)
            {
                if(d == true)
                {
                    if(vx<6)
                        vx+=1;
                }
            }
        }
        else 
        {
            // Slows down Character X
            if(vx>0)
                vx-=1;
            else if(vx<0)
                vx+=1;
        }

        // Moves Character Y
        if(slowW == false || slowS == false)
        {
            if(slowW== false)
            {
                if(w == true)
                {
                    if(vy>-6)
                        vy-=1;
                }
            }
            if(slowS == false)
            {
                if(s == true)
                {
                    if(vy<6)
                        vy+=1;
                }
            }
        }
        else 
        {
            // Slows down Character Y
            if(vy>0)
                vy-=1;
            else if(vy<0)
                vy+=1;
        }

        // Makes sure character is in box;
        y+=vy;
        if(y<55)
        {
            y=55;
            vy=y-lasty;
        }
        else if(y>(670))
        {
            y=670;
            vy=y-lasty;
        }
        x+=vx;
        if(x<0)
        {
            x=0;
            vx=x-lastx;
        }
        else if(x>(770))
        {
            x=770;
            vx=x-lastx;
        }

        box.translate(vx,vy);
    }

    public void draw(Graphics g2)
    {
        // Body
        c = new Color(250,200,125);
        g2.setColor(c);
        g2.fillRect(x,y,30,30);
        g2.setColor(Color.black);
        g2.drawRect(x,y,30,30);

        // Eyes
        g2.setColor(Color.black);
        g2.fillRect(x+5,y+15,5,5);
        g2.fillRect(x+20,y+15,5,5);

        //Mouth
        g2.setColor(Color.white);
        g2.fillOval(x+12,y+22,6,6);
        g2.setColor(Color.red);
        g2.drawOval(x+12,y+22,6,6);
    }

    public void death(Graphics g2)
    {
        g2.setColor(Color.red);
        g2.drawString("You dead son, Press Enter to Reload the Game and Try Again", 250,375);
    }

    public void damage(int d) 
    {
        health-=d;
        if(health<=0)
        {
            life--;
            if(life==0)
                die = true;
            else
                health = 100;
        }
        else if(health>100)
        {
            health = health%100;
            life++;
        }
    }

    public boolean getDead() {return die;}
    public int getLife() {return life;}
    public int getX() {return x;}
    public int getY() {return y;}
    public int getVX() {return vx;}
    public int getVY() {return vy;}
    public int getCX() {return x+15;}
    public int getCY() {return y+15;}
    public int getH() {return health;}
    public Rectangle getBox() {return box;}
}
