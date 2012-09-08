import java.applet.*;
import java.awt.*;
import java.util.*;

public class Bullet
{
    private int x, y, vX, d, f, count, drawX, drawY;
    private boolean remove;
    private Rectangle box;
    private ArrayList<Enemy> enemies;
    private ArrayList<Wall> walls;

    public Bullet(int vx, int X, int Y, int far, int damage)
    {
        vX = vx;
        x = X;
        y = Y;
        d = damage;
        f = far;
        count = 0;
        box = new Rectangle(x,y,16,8);
    }

    public void move(ArrayList<Enemy> enemie, ArrayList<Wall> wals, int scrollX, int scrollY)
    {
        walls = wals;
        enemies = enemie;

        // Will destroy bullet if gone too 
        if(count == f)
            remove = true;

        // Checks
        for(int i = 0;i<enemies.size();i++){
            if(remove)
                continue;
            if(enemies.get(i).getBox().intersects(box)){
                enemies.get(i).hurt(d);
                remove = true;
            }
        }
        for(int i = 0;i<walls.size();i++){
            if(remove)
                continue;
            if(walls.get(i).getWall().intersects(box))
                remove = true;
        }

        //Move Bullets
        if(remove != true){
            x+=(int)vX;
            box.translate(vX,0);
            count++;
        }

        // Check Again
        for(int i = 0;i<enemies.size();i++){
            if(remove)
                continue;
            if(enemies.get(i).getBox().intersects(box)){
                enemies.get(i).hurt(d);
                remove = true;
            }
        }
        
        drawX = x - scrollX;
        drawY = y - scrollY;
    }

    public void draw(Graphics2D g2)
    {
        if(vX<0){
            int[] xA = new int[9];
            int[] yA = new int[9];
            xA[0] = drawX+12;
            xA[1] = drawX+15;
            xA[2] = drawX+13;
            xA[3] = drawX+16;
            xA[4] = drawX+14;
            xA[5] = drawX+16;
            xA[6] = drawX+13;
            xA[7] = drawX+14;
            xA[8] = drawX+12;
            yA[0] = drawY;
            yA[1] = drawY+1;
            yA[2] = drawY+2;
            yA[3] = drawY+3;
            yA[4] = drawY+4;
            yA[5] = drawY+5;
            yA[6] = drawY+6;
            yA[7] = drawY+7;
            yA[8] = drawY+8;
            g2.setColor(Color.white);
            g2.fillPolygon(xA,yA,9);
            g2.setColor(Color.orange);
            //g2.fillPolygon(xA,yA,5);
            g2.fillArc(drawX,drawY,24,8,90,180);
            g2.setColor(Color.black);
            //g2.drawPolygon(xA,yA,5);
            g2.drawArc(drawX,drawY,24,8,90,180);
        } 
        else{
            int[] xA = new int[9];
            int[] yA = new int[9];
            xA[0] = drawX+4;
            xA[1] = drawX+1;
            xA[2] = drawX+3;
            xA[3] = drawX;
            xA[4] = drawX+2;
            xA[5] = drawX;
            xA[6] = drawX+3;
            xA[7] = drawX+2;
            xA[8] = drawX+4;
            yA[0] = drawY;
            yA[1] = drawY+1;
            yA[2] = drawY+2;
            yA[3] = drawY+3;
            yA[4] = drawY+4;
            yA[5] = drawY+5;
            yA[6] = drawY+6;
            yA[7] = drawY+7;
            yA[8] = drawY+8;
            g2.setColor(Color.white);
            g2.fillPolygon(xA,yA,9);
            xA = new int[5];
            yA = new int[5];
            xA[0] = drawX+16;
            xA[1] = drawX+12;
            xA[2] = drawX+4;
            xA[3] = drawX+4;
            xA[4] = drawX+12;
            yA[0] = drawY+4;
            yA[1] = drawY;
            yA[2] = drawY;
            yA[3] = drawY+8;
            yA[4] = drawY+8;
            g2.setColor(Color.orange);
            g2.fillArc(drawX-8,drawY,24,8,270,180);
            //g2.fillPolygon(xA,yA,5);
            g2.setColor(Color.black);
            //g2.drawPolygon(xA,yA,5);
            g2.drawArc(drawX-8,drawY,24,8,270,180);
        }
    }

    public void screenSlide(int bobX){x+=bobX; box.translate(bobX, 0); }

    public int getX(){return x;}

    public int getY() {return y;}

    public int getDamage() {return d;}

    public boolean Remove() {return remove;}

    public Rectangle getBox() {return box;}
}