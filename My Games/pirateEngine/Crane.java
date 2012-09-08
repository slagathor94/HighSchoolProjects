import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Crane extends Enemy
{
    private int x,y,vx,vy,health,strength,score,w,h,firstX,firstY,
    wallStart,wallEnd,drawX,drawY,count,mouthCounter,animationCounter;
    private Color c;
    private Pirate bob;
    private Rectangle box;
    private boolean remove, revealed, startFall, right;
    private ArrayList<Wall> walls;
    private ArrayList<enemyCannon> ebullets;

    public Crane(int X, int Y)
    {
        super(X,Y);
        // Used for easy moving of the rectangle
        firstY = Y;
        firstX = X;
        x = X;
        y = Y;
        count = 0;

        // Width and height of enemy
        w = 30;
        h = 46;

        // Animation Random
        mouthCounter = (int)(Math.random()*90);

        // Health
        health = 180;

        // May Implement score counter later
        score = 100;

        // Just stuff
        box = new Rectangle(x,y,w,h);
        bob = new Pirate();
        walls = new ArrayList<Wall>();
        ebullets = new ArrayList<enemyCannon>();

        // Speed Implementations
        vy = 0;
        vx = 2;

        // How much damage it will do every time it hits Pirate
        strength = 1;

        // Used to tell the applet to remove this enemy if it is dead
        remove = false;

        // This will make it so the enemy will not change directions when falling at the beginning of the game
        startFall = true;
        right = true;
    }

    public void draw(Graphics2D g2)
    {
        // Body
        drawBody(g2);

        // Legs
        drawLegs1(g2);

        if(health < 180)
            drawHealth(g2);

            
        if(mouthCounter < 90)
            mouthCounter ++;
        else
            mouthCounter = 0;
    }

    public void hurt(int d)
    {
        health-=d;
        if(health<=0)
            remove=true;
    }

    public void move(int charX, int charY, Pirate Bob, ArrayList<Wall> wall, ArrayList<enemyCannon> ebullet, int scrollX, int scrollY)
    {
        // Bob's x & y
        int distanceX = Math.abs(x - charX + 15);
        int distanceY = Math.abs(y - charY + 23);
        bob = Bob;

        // Applies the starting x and y
        firstX = x;
        firstY = y;

        // Stuff
        walls = wall;
        ebullets = ebullet;

        // Makes it so it doesn't do anything if it isn't on the screen
        // May help make it not lag for large levels
        if(!revealed && drawX < 800 && drawY < 800){
            revealed = true;
        }

        // Moves the Zombie towards le Bob
        // Moving
        if(revealed && !remove){
            vy++;

            // Goes through the array of walls to check collisions
            if(walls.size()>0){
                for(int i = 0;i<walls.size();i++){
                    if(bottomCollision(walls.get(i))){
                        vy = 0;
                        startFall = false;
                        wallStart = walls.get(i).getX();
                        wallEnd = walls.get(i).getX() + walls.get(i).getWidth();
                    }
                    if(rightCollision(walls.get(i)) || leftCollision(walls.get(i))){
                        vx = -vx;
                        if(right)
                            right = false;
                        else
                            right = true;
                    }
                }
            }
            // Changes direction if falling, allowing it to stay on the same platform
            if(vy == 0 && !startFall && (x <= wallStart+2 || x+w >= wallEnd-2)){
                vx = -vx;
                if(right)
                    right = false;
                else
                    right = true;
            }

            x+=vx;
        }
        y+=vy;

        // Shooter part
        if(count > 30){
            if(distanceX < 300 && distanceY < 30)
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
        if(bobX < 0){
            sinX = 12;
            right = true;
        }
        else{
            sinX = -12;
            right = false;
        }
        enemyCannon bull = new enemyCannon(sinX,x,y,100,30,1);
        ebullets.add(bull);
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

    public void drawHealth(Graphics g){
        g.setColor(Color.red);
        g.fillRect(drawX-15, drawY-10, (int)(50*(health/180.0)), 7);
        g.setColor(Color.black);
        g.drawRect(drawX-15, drawY-10, 50, 7);
    }

    public void drawBody(Graphics g){
        if(!right){
            // Main Body
            g.setColor(Color.pink);
            int[] bodyX = {drawX+8,drawX+12,drawX+23,drawX+20,drawX+25,drawX+22,drawX+25,drawX+22,drawX+22,drawX+19,
                    drawX+26,drawX+30,drawX+30,drawX+28,drawX+27,drawX+26,drawX+24,drawX+23,drawX+22,drawX+18,drawX+22,
                    drawX+23,drawX+12,drawX+6,drawX+6,drawX+12,drawX+19,drawX+12,drawX+8};
            int[] bodyY = {drawY+2,drawY,drawY,drawY+2,drawY+1,drawY+6,drawY+4,drawY+8,drawY+12,drawY+22,drawY+22,
                    drawY+32,drawY+38,drawY+32,drawY+38,drawY+32,drawY+36,drawY+32,drawY+28,drawY+28,drawY+28,drawY+32,
                    drawY+32,drawY+30,drawY+24,drawY+22,drawY+12,drawY+12,drawY+9};
            g.fillPolygon(bodyX,bodyY,29);
            g.setColor(Color.black);
            g.drawPolygon(bodyX,bodyY,29);

            // Eyes
            g.setColor(Color.white);
            g.fillArc(drawX+8,drawY+2,6,7,270,180);
            g.setColor(Color.black);
            g.fillArc(drawX+8,drawY+3,4,5,270,180);

            // Mouth
            g.setColor(Color.orange);
            if(mouthCounter < 45){
                int[] mouthX = {drawX,drawX+8,drawX+8};
                int[] mouthY = {drawY+6,drawY+2,drawY+9};
                g.fillPolygon(mouthX,mouthY,3);
                g.setColor(Color.black);
                g.drawPolygon(mouthX,mouthY,3);
                g.drawLine(drawX,drawY+6,drawX+8,drawY+5);
            }
            else{
                int[] mouthX = {drawX,drawX+8,drawX+8,drawX,drawX+8};
                int[] mouthY = {drawY+1,drawY+2,drawY+9,drawY+10,drawY+5};
                g.fillPolygon(mouthX,mouthY,5);
                g.setColor(Color.black);
                g.drawPolygon(mouthX,mouthY,5);
            }
        }
        else{
            // Main Body
            g.setColor(Color.pink);
            int[] bodyX = {drawX+22,drawX+18,drawX+7,drawX+10,drawX+5,drawX+8,drawX+5,drawX+8,drawX+8,drawX+11,
                    drawX+4,drawX,drawX,drawX+2,drawX+3,drawX+4,drawX+6,drawX+7,drawX+8,drawX+12,drawX+8,
                    drawX+7,drawX+18,drawX+24,drawX+24,drawX+18,drawX+11,drawX+18,drawX+22};
            int[] bodyY = {drawY+2,drawY,drawY,drawY+2,drawY+1,drawY+6,drawY+4,drawY+8,drawY+12,drawY+22,drawY+22,
                    drawY+32,drawY+38,drawY+32,drawY+38,drawY+32,drawY+36,drawY+32,drawY+28,drawY+28,drawY+28,drawY+32,
                    drawY+32,drawY+30,drawY+24,drawY+22,drawY+12,drawY+12,drawY+9};
            g.fillPolygon(bodyX,bodyY,29);
            g.setColor(Color.black);
            g.drawPolygon(bodyX,bodyY,29);

            // Eyes
            g.setColor(Color.white);
            g.fillArc(drawX+14,drawY+2,6,7,270,180);
            g.setColor(Color.black);
            g.fillArc(drawX+16,drawY+3,4,5,270,180);

            // Mouth
            g.setColor(Color.orange);
            if(mouthCounter < 45){
                int[] mouthX = {drawX+30,drawX+22,drawX+22};
                int[] mouthY = {drawY+6,drawY+2,drawY+9};
                g.fillPolygon(mouthX,mouthY,3);
                g.setColor(Color.black);
                g.drawPolygon(mouthX,mouthY,3);
                g.drawLine(drawX+30,drawY+6,drawX+22,drawY+5);
            }
            else{
                int[] mouthX = {drawX+30,drawX+22,drawX+22,drawX+30,drawX+22};
                int[] mouthY = {drawY+1,drawY+2,drawY+9,drawY+10,drawY+5};
                g.fillPolygon(mouthX,mouthY,5);
                g.setColor(Color.black);
                g.drawPolygon(mouthX,mouthY,5);
            }
        }
    }

    public void drawLegs1(Graphics g){
        if(!right){
            g.setColor(Color.black);

            int[] leg1X = {drawX+16,drawX+16,drawX+10,drawX+14,drawX+14};
            int[] leg1Y = {drawY+32,drawY+46,drawY+46,drawY+44,drawY+32};
            g.fillPolygon(leg1X,leg1Y,5);

            int[] leg2X = {drawX+20,drawX+22,drawX+10,drawX+8,drawX+8,drawX+19,drawX+18};
            int[] leg2Y = {drawY+32,drawY+38,drawY+38,drawY+42,drawY+36,drawY+36,drawY+32};
            g.fillPolygon(leg2X,leg2Y,7);
        }
        else{
            g.setColor(Color.black);

            int[] leg1X = {drawX+14,drawX+14,drawX+20,drawX+16,drawX+16};
            int[] leg1Y = {drawY+32,drawY+46,drawY+46,drawY+44,drawY+32};
            g.fillPolygon(leg1X,leg1Y,5);

            int[] leg2X = {drawX+10,drawX+8,drawX+20,drawX+22,drawX+22,drawX+11,drawX+12};
            int[] leg2Y = {drawY+32,drawY+38,drawY+38,drawY+42,drawY+36,drawY+36,drawY+32};
            g.fillPolygon(leg2X,leg2Y,7);
        }
    }
    
    public void drawLegs2(Graphics g){
        
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
