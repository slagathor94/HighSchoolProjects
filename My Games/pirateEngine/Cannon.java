import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Cannon extends Enemy
{
    private int x,y,vx,vy,health,strength,score,w,h,firstX,firstY,wallStart,wallEnd,drawX,drawY,count;
    private Color c;
    private Pirate bob;
    private Rectangle box;
    private boolean remove, revealed, startFall, right;
    private ArrayList<Wall> walls;
    private ArrayList<enemyCannon> ebullets;

    // When creating this enemy, put a wall underneath with the same dimensions to make it more realistic
    public Cannon(int X, int Y, boolean r)
    {
        super(X,Y);
        // Used for easy moving of the rectangle
        firstY = Y;
        firstX = X;
        x = X;
        y = Y;
        count = 0;
        right = r;

        // Width and height of enemy
        w = 40;
        h = 30;

        // May Implement score counter later
        score = 100;

        // Just stuff
        box = new Rectangle(x,y,w,h);
        bob = new Pirate();
        walls = new ArrayList<Wall>();
        ebullets = new ArrayList<enemyCannon>();
        revealed = true;

        // Speed Implementations
        vy = 0;
        vx = 0;

        // Health
        health = 1000;

        // How much damage it will do every time it hits Pirate
        strength = 0;

        // Used to tell the applet to remove this enemy if it is dead
        remove = false;

        // This will make it so the enemy will not change directions when falling at the beginning of the game
        startFall = true;
    }

    public void draw(Graphics2D g)
    {
        g.setColor(Color.black);
        if(right){
            // Main part
            int[] mainX = {drawX+6,drawX+22,drawX+28,drawX+37,drawX+37,drawX+28,drawX+22,drawX+6};
            int[] mainY = {drawY+4,drawY+4,drawY+5,drawY,drawY+22,drawY+17,drawY+18,drawY+18};
            g.fillPolygon(mainX,mainY,8);
            g.fillOval(drawX+2,drawY+4,8,14);
            g.fillOval(drawX+6,drawY+2,16,4);
            g.fillOval(drawX+6,drawY+16,16,4);
            g.setColor(Color.gray);
            g.fillOval(drawX+34,drawY,6,22);
            g.setColor(Color.black);
            g.drawOval(drawX+34,drawY,6,22);

            // Wheel
            g.setColor(Color.gray);
            g.fillOval(drawX+6,drawY+14,16,16);
            g.setColor(Color.black);
            g.drawOval(drawX+6,drawY+14,16,16);
            g.setColor(Color.yellow);
            g.fillOval(drawX+11,drawY+19,6,6);
            g.setColor(Color.black);
            g.drawOval(drawX+11,drawY+19,6,6);
        }
        else{
            // Main part
            int[] mainX = {drawX+34,drawX+18,drawX+12,drawX+3,drawX+3,drawX+12,drawX+18,drawX+34};
            int[] mainY = {drawY+4,drawY+4,drawY+5,drawY,drawY+22,drawY+17,drawY+18,drawY+18};
            g.fillPolygon(mainX,mainY,8);
            g.fillOval(drawX+30,drawY+4,8,14);
            g.fillOval(drawX+19,drawY+2,16,4);
            g.fillOval(drawX+19,drawY+16,16,4);
            g.setColor(Color.gray);
            g.fillOval(drawX,drawY,6,22);
            g.setColor(Color.black);
            g.drawOval(drawX,drawY,6,22);

            // Wheel
            g.setColor(Color.gray);
            g.fillOval(drawX+18,drawY+14,16,16);
            g.setColor(Color.black);
            g.drawOval(drawX+18,drawY+14,16,16);
            g.setColor(Color.yellow);
            g.fillOval(drawX+23,drawY+19,6,6);
            g.setColor(Color.black);
            g.drawOval(drawX+23,drawY+19,6,6);
        }
    }

    public void hurt(int d)
    {
        health-=d;
    }

    public void move(int charX, int charY, Pirate Bob, ArrayList<Wall> wall, ArrayList<enemyCannon> ebullet, int scrollX, int scrollY)
    {
        // Bob's x & y
        int distanceX = Math.abs(x - charX + 15);
        int distanceY = Math.abs(y - charY + 20);
        bob = Bob;

        // Applies the starting x and y
        firstX = x;
        firstY = y;

        // Stuff
        walls = wall;
        ebullets = ebullet;

        // Doesn't move

        // Shooter part
        if(count > 70){
            bulletCreator();
            count = 0;
        }
        else
            count++;

        box.translate(x-firstX, y-firstY);

        drawX = x - scrollX;
        drawY = y - scrollY;
    }

    public void bulletCreator()
    {
        c = Color.red;
        int bobX = x - bob.getX();
        int sinX = 0;
        if(right){
            sinX = 6;
            enemyCannon bull = new enemyCannon(sinX,x+20,y,150,50,2);
            ebullets.add(bull);
        }
        else{
            sinX = -6;
            enemyCannon bull = new enemyCannon(sinX,x,y,150,50,2);
            ebullets.add(bull);
        }
    }

    // The following collisions will return true if it collides with a wall
    public boolean topCollision(Wall wal){
        if((x<=wal.getX()+wal.getWidth()) && (x+w>=wal.getX()) && (y<=wal.getY()+wal.getHeight()) && (y>= wal.getY()+wal.getHeight()+vy) && vy<0){
            if(y<wal.getY()+wal.getHeight()){
                y = wal.getY()+wal.getHeight()+2;
            }
            return true;
        }
        else
            return false;
    }

    public boolean bottomCollision(Wall wal){
        if((x<=wal.getX()+wal.getWidth()) && (x+w>=wal.getX()) && (y+h>=wal.getY()) && (y+h <= wal.getY()+15) && (vy>=0)){
            if(y+h>wal.getY()){
                y = wal.getY()-h;
            }
            return true;
        }
        else
            return false;
    }

    public boolean rightCollision(Wall wal){
        if((y<=wal.getY()+wal.getHeight()-5) && (y+h>=wal.getY()+5) && (x+w>=wal.getX()) && (x+w<=wal.getX()+vx*2) && vx>0){
            x = wal.getX()-w;
            return true;
        }
        else
            return false;
    }

    public boolean leftCollision(Wall wal){
        if((y<=wal.getY()+wal.getHeight()-5) && (y+h>=wal.getY()+5) && (x<=wal.getX()+wal.getWidth()) && (x>=wal.getX()+wal.getWidth()+vx*2) && vx<0){
            x = wal.getX()+wal.getWidth();
            return true;
        }
        else
            return false;
    }

    public ArrayList<enemyCannon> geteBullets(){return ebullets;}

    public Rectangle getBox(){return box;}

    public int getX(){return x;}

    public int getY(){return y;}

    public int getWidth(){return w;}

    public boolean isFalling(){if(vx>0) return true; else return false;}

    public int getDamage(){return strength;}

    public int getH(){return health;}

    public int getScore() {return score;}

    public boolean Remove() {return remove;}

    public Pirate getBob() {return bob;}
}
