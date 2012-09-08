import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class enemyCannon
{
    private int vX, x, y, d, f, enemyState, count, drawX, drawY, width, height;
    private Color c;
    private boolean remove;
    private Rectangle box;

    public enemyCannon(int vx, int X, int Y, int far, int damage, int enemy)
    {
        vX = vx;
        x = X;
        y = Y;
        d = damage;
        f = far;
        count = 0;
        enemyState = enemy;
        remove = false;
        if(enemyState == 1){
            width = 12;
            height = 10;
        }
        else if(enemyState == 2){
            width = 20;
            height = 20;
        }
        else if(enemyState == 3){
            width = 10;
            height = 10;
        }
        box = new Rectangle(x,y,width,height);
    }

    public void move(ArrayList<Wall> walls, int scrollX, int scrollY)
    {
        // Will destroy bullet if gone too 
        if(count == f)
            remove = true;
            
        // Moves Bullet
        if(!remove)
        {
            x+=vX;
            box.translate(vX,0);
            count++;
        }

        // Collison Detection
        for(int i = 0;i<walls.size();i++){
            if(remove)
                continue;
            if(walls.get(i).getWall().intersects(box))
                remove = true;
        }

        drawX = x - scrollX;
        drawY = y - scrollY;
    }

    public void draw(Graphics2D g2)
    {
        if(enemyState == 1)
        c = Color.green;
        else if(enemyState == 2)
        c = Color.black;
        else if(enemyState == 3)
        c = Color.blue;
        g2.setColor(c);
        g2.fillOval(drawX,drawY,width,height);
        g2.setColor(Color.black);
        g2.drawOval(drawX,drawY,width,height);
    }

    public int getX(){ return x;}

    public int getY() { return y;}

    public int getDamage() {return d;}
    
    public int getVX() {return vX;}

    public boolean Remove() {return remove;}
    
    public void makeRemoved() {remove = true;}

    public Rectangle getBox() {return box;}
}
