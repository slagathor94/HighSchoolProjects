import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class zomShoot extends BasicEnemy
{
    private int px,py,health,strength,count,score,bx,by,bcount;
    private double vx, vy, speed, x, y;
    private ArrayList<enemyBullet> ebullets;
    private ArrayList<ZBullet> bullets;
    private Character bob;
    private Rectangle box;
    private boolean remove,blackHole;
    private Color c;

    public zomShoot(int X, int Y)
    {
        super(X,Y);
        x = X;
        y = Y;
        score = 50;
        count = 0;
        bullets = new ArrayList<ZBullet>();
        ebullets = new ArrayList<enemyBullet>();
        box = new Rectangle((int)x,(int)y,20,20);
        speed = 3;
        strength = 0;
        health = 60;
        remove = false;
    }

    public void draw(Graphics2D g2)
    {
        // Body
        c = Color.orange;
        g2.setColor(c);
        g2.fillRect((int)x,(int)y,20,20);
        g2.setColor(Color.black);
        g2.drawRect((int)x,(int)y,20,20);

        // Eyes
        c = new Color(116,24,6);
        g2.setColor(Color.white);
        g2.fillRect((int)x+3,(int)y+5,5,5);
        g2.fillRect((int)x+13,(int)y+5,5,5);
        g2.setColor(Color.black);
        g2.drawRect((int)x+3,(int)y+5,5,5);
        g2.drawRect((int)x+13,(int)y+5,5,5);
    }

    public void hurt(int d)
    {
        health-=d;
        if(health<=0)
            remove=true;
    }

    public void move(int charX, int charY, ArrayList<ZBullet> bullet, Character Bob, ArrayList<enemyBullet> ebullet)
    {
        // Bob's x & y
        px = (int)x - charX;
        py = (int)y - charY;
        bob = Bob;
        double distance = Math.sqrt(px*px+py*py);

        // Le Bullets
        bullets = bullet;
        ebullets = ebullet;

        // Gets the starting x and y
        int startX = (int)x;
        int startY = (int)y;

        // Collision Detection
        if(box.intersects(bob.getBox()))
            bob.damage(strength);
        for(int i = 0; i<bullets.size();i++)
        {
            if(box.intersects(bullets.get(i).getBox()) && bullets.get(i).getColor() == Color.black)
            {
                bx = bullets.get(i).getX();
                by = bullets.get(i).getY();
                blackHole = true;
            }
            else if(box.intersects(bullets.get(i).getBox()))
            {
                hurt(bullets.get(i).getDamage());
                bullets.remove(i);
            }
        }

        // Moves the Zombie towards le Bob
        if(blackHole == false)
        {
            if(distance > 200){
                vx = -speed*px/Math.sqrt(px*px+py*py);
                vy = -speed*py/Math.sqrt(px*px+py*py);
                x+=vx;
                y+= vy;
                box.translate((int)x-startX,(int)y-startY);

                if(distance < 300 && count>30)
                {
                    bulletCreator((int)x,(int)y,px,py,ebullets);
                    count = 0;
                }
                else
                    count++;
            }
            else{
                vx = .75*speed*px/Math.sqrt(px*px+py*py);
                vy = .75*speed*py/Math.sqrt(px*px+py*py);
                x+=vx;
                y+= vy;
                box.translate((int)x-startX,(int)y-startY);

                if(distance < 300 && count>30)
                {
                    bulletCreator((int)x,(int)y,px,py,ebullets);
                    count = 0;
                }
                else
                    count++;
            }
        }        
        else
        {
            if(bcount == 15)
                remove = true;
            if(bcount <= 10){
                x += (bx-x)/4.0;
                y += (by-y)/4.0;
            }
            else{
                x = bx;
                y = by;
            }
            bcount++;
        }

        if(score > 25)
            score--;
    }

    public void bulletCreator(int x, int y, double mouseX, double mouseY, ArrayList<enemyBullet> bullet)
    {
        ebullets = bullet;
        c = Color.red;
        double bobX = x - bob.getX();
        double bobY = y - bob.getY();
        int sinX = (int)(-7*bobX/Math.sqrt(bobX*bobX+bobY*bobY));
        int sinY = (int)(-7*bobY/Math.sqrt(bobX*bobX+bobY*bobY));
        enemyBullet bull = new enemyBullet(sinX,sinY,x,y,150,5,c);
        ebullets.add(bull);
    }

    public int getX(){return (int)x;}

    public int getY(){return (int)y;}

    public int getH(){return health;}

    public int getScore() {return score;}

    public boolean Remove() {return remove;}

    public Character getC() {return bob;}

    public ArrayList<ZBullet> getB() {return bullets;}

    public ArrayList<enemyBullet> getEB() {return ebullets;}
}
