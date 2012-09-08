import java.applet.*;
import java.awt.*;
import java.util.*;

public class Sword
{
    private int x, y, vX, d, f, count, animationCounter;
    private boolean remove, right, up, jumping;
    private Rectangle box;
    private ArrayList<Wall> walls;

    public Sword(int X, int Y, int damage, boolean r, boolean u, boolean j)
    {
        x = X;
        y = Y;
        right = r;
        up = u;
        jumping = j;
        d = damage;
        count = 0;
        box = new Rectangle(x,y,16,8);
    }

    public void move(ArrayList<Enemy> enemies)
    {
        // Will destroy sword after swing
        if(count == animationCounter)
            remove = true;

        // Checks
        for(int i = 0;i<enemies.size();i++){
            if(remove)
                continue;
            if(enemies.get(i).getBox().intersects(box))
                enemies.get(i).hurt(d);
        }
    }

    public void draw(Graphics2D g2)
    {   
        if(jumping){
            if(right){

            }
            else{

            }

            if(up){

            }
            else{

            }
        }
        else{
            if(right){

            }
            else{

            }

            if(up){

            }
            else{

            }
        }
    }

    public void screenSlide(int bobX){x+=bobX; box.translate(bobX, 0); }

    public int getX(){return x;}

    public int getY() {return y;}

    public int getDamage() {return d;}

    public boolean Remove() {return remove;}

    public Rectangle getBox() {return box;}
}
