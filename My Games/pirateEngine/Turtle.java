import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Turtle extends Enemy
{
    private int x,y,vx,vy,health,strength,score,w,h,firstX,firstY,wallStart,
    wallEnd,drawX,drawY,count,animationCounter,mouthCounter;
    private Color c;
    private Pirate bob;
    private Rectangle box;
    private boolean remove, revealed, startFall, right;
    private ArrayList<Wall> walls;
    private ArrayList<enemyCannon> ebullets;

    public Turtle(int X, int Y)
    {
        super(X,Y);

        // Used for easy moving of the rectangle
        firstY = Y;
        firstX = X;
        x = X;
        y = Y;
        count = 0;

        // Animation Random
        mouthCounter = (int)(Math.random()*90);

        // Width and height of enemy
        w = 40;
        h = 30;

        // Health
        health = 250;

        // May Implement score counter later
        score = 125;

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
        if(animationCounter < 5)
            drawLegs1(g2);
        else if(animationCounter < 10)
            drawLegs2(g2);
        else if(animationCounter < 15)
            drawLegs1(g2);
        else
            drawLegs3(g2);

        drawBody(g2);

        if(health < 250)
            drawHealth(g2);

        if(animationCounter < 20)
            animationCounter ++;
        else 
            animationCounter = 0;

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

    public void move(int charX, int charY, Pirate Bob, ArrayList<Wall> wall, ArrayList<enemyCannon> ebullet,
    int scrollX, int scrollY)
    {
        // Bob's x & y
        int distanceX = x - charX + 20;
        int distanceY = Math.abs(y - charY + 15);
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
        if(count > 20){
            if(distanceX < 300 && distanceX > 0 && distanceY < 40 && !right){
                bulletCreator();
                count = 0;
            }
            if(distanceX > -300 && distanceX < 0 && distanceY < 40 && right){
                bulletCreator();
                count = 0;
            }
        }
        else
            count++;

        box.translate(x-firstX, y-firstY);

        drawX = x - scrollX;
        drawY = y - scrollY;
    }

    public void bulletCreator()
    {
        int sinX = 0;

        if(right){
            sinX = 10;
            enemyCannon bull = new enemyCannon(sinX,x+w/3*2,y,100,20,3);
            ebullets.add(bull);
        }
        else{
            sinX = -10;
            enemyCannon bull = new enemyCannon(sinX,x+w/4,y,100,20,3);
            ebullets.add(bull);
        }
    }

    public void drawBody(Graphics g2){
        if(!right){
            // Shell
            c = new Color(149,112,98);
            g2.setColor(c);
            g2.fillArc(drawX+12,drawY+8,28,24,0,180);
            c = new Color(56,47,20);
            g2.setColor(c);
            g2.drawArc(drawX+12,drawY+8,28,24,0,180);
            g2.fillRect(drawX+12,drawY+20,28,2);

            int[] spot1X = {drawX+12,drawX+16,drawX+18,drawX+17};
            int[] spot1Y = {drawY+19,drawY+14,drawY+18,drawY+20};
            g2.fillPolygon(spot1X,spot1Y,4);

            int[] spot2X = {drawX+18,drawX+26,drawX+24,drawX+20};
            int[] spot2Y = {drawY+12,drawY+8,drawY+14,drawY+16};
            g2.fillPolygon(spot2X,spot2Y,4);

            int[] spot3X = {drawX+21,drawX+24,drawX+26,drawX+20};
            int[] spot3Y = {drawY+16,drawY+15,drawY+20,drawY+20};
            g2.fillPolygon(spot3X,spot3Y,4);

            int[] spot4X = {drawX+26,drawX+28,drawX+35,drawX+32};
            int[] spot4Y = {drawY+16,drawY+9,drawY+12,drawY+16};
            g2.fillPolygon(spot4X,spot4Y,4);

            int[] spot5X = {drawX+28,drawX+29,drawX+34,drawX+36};
            int[] spot5Y = {drawY+20,drawY+18,drawY+16,drawY+20};
            g2.fillPolygon(spot5X,spot5Y,4);

            int[] spot6X = {drawX+36,drawX+37,drawX+40,drawX+38};
            int[] spot6Y = {drawY+17,drawY+14,drawY+19,drawY+20};
            g2.fillPolygon(spot6X,spot6Y,4);

            // Neck
            c = Color.green;
            g2.setColor(c);
            int[] neckX = {drawX+12,drawX+15,drawX+16,drawX+12};
            int[] neckY = {drawY+16,drawY+12,drawY+14,drawY+20};
            g2.fillPolygon(neckX,neckY,4);

            // Head
            g2.setColor(c);
            g2.fillOval(drawX,drawY,18,18);
            g2.setColor(Color.black);
            g2.drawOval(drawX,drawY,18,18);

            // Mouth
            if(mouthCounter < 45){
                c = new Color(134,219,220);
                g2.setColor(c);
                int[] mouthX = {drawX,drawX+8,drawX+3};
                int[] mouthY = {drawY+12,drawY+12,drawY+15};
                g2.fillPolygon(mouthX,mouthY,3);
                g2.setColor(Color.black);
                g2.drawLine(drawX+1,drawY+12,drawX+8,drawY+12);
                g2.drawLine(drawX+4,drawY+15,drawX+8,drawY+12);
            }
            else
                g2.drawLine(drawX+3,drawY+14,drawX+8,drawY+12);

            // Eyes
            g2.setColor(Color.white);
            g2.fillOval(drawX+3,drawY+4,6,6);
            g2.setColor(Color.black);
            g2.drawOval(drawX+3,drawY+4,6,6);
            g2.fillOval(drawX+3,drawY+5,4,4);
        }
        else{
            // Shell
            c = new Color(149,112,98);
            g2.setColor(c);
            g2.fillArc(drawX,drawY+8,28,24,0,180);
            c = new Color(56,47,20);
            g2.setColor(c);
            g2.drawArc(drawX,drawY+8,28,24,0,180);
            g2.fillRect(drawX,drawY+20,28,2);

            int[] spot1X = {drawX+28,drawX+24,drawX+22,drawX+23};
            int[] spot1Y = {drawY+19,drawY+14,drawY+18,drawY+20};
            g2.fillPolygon(spot1X,spot1Y,4);

            int[] spot2X = {drawX+22,drawX+14,drawX+16,drawX+20};
            int[] spot2Y = {drawY+12,drawY+8,drawY+14,drawY+16};
            g2.fillPolygon(spot2X,spot2Y,4);

            int[] spot3X = {drawX+19,drawX+16,drawX+14,drawX+20};
            int[] spot3Y = {drawY+16,drawY+15,drawY+20,drawY+20};
            g2.fillPolygon(spot3X,spot3Y,4);

            int[] spot4X = {drawX+14,drawX+12,drawX+5,drawX+8};
            int[] spot4Y = {drawY+16,drawY+9,drawY+12,drawY+16};
            g2.fillPolygon(spot4X,spot4Y,4);

            int[] spot5X = {drawX+12,drawX+11,drawX+6,drawX+4};
            int[] spot5Y = {drawY+20,drawY+18,drawY+16,drawY+20};
            g2.fillPolygon(spot5X,spot5Y,4);

            int[] spot6X = {drawX+4,drawX+3,drawX,drawX+2};
            int[] spot6Y = {drawY+17,drawY+14,drawY+19,drawY+20};
            g2.fillPolygon(spot6X,spot6Y,4);

            // Neck
            c = Color.green;
            g2.setColor(c);
            int[] neckX = {drawX+28,drawX+25,drawX+24,drawX+28};
            int[] neckY = {drawY+16,drawY+12,drawY+14,drawY+20};
            g2.fillPolygon(neckX,neckY,4);

            // Head
            g2.setColor(c);
            g2.fillOval(drawX+22,drawY,18,18);
            g2.setColor(Color.black);
            g2.drawOval(drawX+22,drawY,18,18);

            if(mouthCounter < 45){
                // Mouth
                c = new Color(134,219,220);
                g2.setColor(c);
                int[] mouthX = {drawX+40,drawX+32,drawX+38};
                int[] mouthY = {drawY+12,drawY+12,drawY+15};
                g2.fillPolygon(mouthX,mouthY,3);
                g2.setColor(Color.black);
                g2.drawLine(drawX+39,drawY+12,drawX+32,drawY+12);
                g2.drawLine(drawX+36,drawY+15,drawX+32,drawY+12);
            }
            else
                g2.drawLine(drawX+37,drawY+14,drawX+32,drawY+12);

            // Eyes
            g2.setColor(Color.white);
            g2.fillOval(drawX+31,drawY+4,6,6);
            g2.setColor(Color.black);
            g2.drawOval(drawX+31,drawY+4,6,6);
            g2.fillOval(drawX+33,drawY+5,4,4);
        }
    }

    public void drawLegs1(Graphics g){
        if(!right){
            int[] leg1X = {drawX+16,drawX+21,drawX+21,drawX+15};
            int[] leg1Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg1X,leg1Y,4);
            g.fillArc(drawX+15,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+16,drawY+22,drawX+15,drawY+28);
            g.drawArc(drawX+15,drawY+26,6,4,180,180);

            int[] leg2X = {drawX+19,drawX+24,drawX+26,drawX+20};
            int[] leg2Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg2X,leg2Y,4);
            g.fillArc(drawX+20,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+19,drawY+22,drawX+20,drawY+28);
            g.drawLine(drawX+24,drawY+22,drawX+26,drawY+28);
            g.drawArc(drawX+20,drawY+26,6,4,180,180);

            int[] leg3X = {drawX+35,drawX+40,drawX+40,drawX+34};
            int[] leg3Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg3X,leg3Y,4);
            g.fillArc(drawX+35,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+40,drawY+22,drawX+40,drawY+28);
            g.drawLine(drawX+35,drawY+22,drawX+34,drawY+28);
            g.drawArc(drawX+34,drawY+26,6,4,180,180);

            int[] leg4X = {drawX+30,drawX+35,drawX+35,drawX+29};
            int[] leg4Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg4X,leg4Y,4);
            g.fillArc(drawX+29,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+30,drawY+22,drawX+29,drawY+28);
            g.drawLine(drawX+35,drawY+22,drawX+35,drawY+28);
            g.drawArc(drawX+29,drawY+26,6,4,180,180);
        }
        else{
            int[] leg1X = {drawX+24,drawX+19,drawX+19,drawX+25};
            int[] leg1Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg1X,leg1Y,4);
            g.fillArc(drawX+19,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+24,drawY+22,drawX+25,drawY+28);
            g.drawArc(drawX+19,drawY+26,6,4,180,180);

            int[] leg2X = {drawX+21,drawX+16,drawX+14,drawX+20};
            int[] leg2Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg2X,leg2Y,4);
            g.fillArc(drawX+14,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+21,drawY+22,drawX+20,drawY+28);
            g.drawLine(drawX+16,drawY+22,drawX+14,drawY+28);
            g.drawArc(drawX+14,drawY+26,6,4,180,180);

            int[] leg3X = {drawX+5,drawX,drawX,drawX+6};
            int[] leg3Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg3X,leg3Y,4);
            g.fillArc(drawX,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX,drawY+22,drawX,drawY+28);
            g.drawLine(drawX+5,drawY+22,drawX+6,drawY+28);
            g.drawArc(drawX,drawY+26,6,4,180,180);

            int[] leg4X = {drawX+10,drawX+5,drawX+5,drawX+11};
            int[] leg4Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg4X,leg4Y,4);
            g.fillArc(drawX+5,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+10,drawY+22,drawX+11,drawY+28);
            g.drawLine(drawX+5,drawY+22,drawX+5,drawY+28);
            g.drawArc(drawX+5,drawY+26,6,4,180,180);
        }
    }

    public void drawLegs2(Graphics g){
        if(!right){
            int[] leg1X = {drawX+14,drawX+19,drawX+18,drawX+12};
            int[] leg1Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg1X,leg1Y,4);
            g.fillArc(drawX+12,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+14,drawY+22,drawX+12,drawY+28);
            g.drawArc(drawX+12,drawY+26,6,4,180,180);

            int[] leg2X = {drawX+19,drawX+24,drawX+26,drawX+20};
            int[] leg2Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg2X,leg2Y,4);
            g.fillArc(drawX+20,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+19,drawY+22,drawX+20,drawY+28);
            g.drawLine(drawX+24,drawY+22,drawX+26,drawY+28);
            g.drawArc(drawX+20,drawY+26,6,4,180,180);

            int[] leg3X = {drawX+35,drawX+40,drawX+40,drawX+34};
            int[] leg3Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg3X,leg3Y,4);
            g.fillArc(drawX+35,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+40,drawY+22,drawX+40,drawY+28);
            g.drawLine(drawX+35,drawY+22,drawX+34,drawY+28);
            g.drawArc(drawX+34,drawY+26,6,4,180,180);

            int[] leg4X = {drawX+28,drawX+33,drawX+32,drawX+26};
            int[] leg4Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg4X,leg4Y,4);
            g.fillArc(drawX+26,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+28,drawY+22,drawX+26,drawY+28);
            g.drawLine(drawX+33,drawY+22,drawX+32,drawY+28);
            g.drawArc(drawX+26,drawY+26,6,4,180,180);
        }
        else{
            int[] leg1X = {drawX+26,drawX+21,drawX+22,drawX+28};
            int[] leg1Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg1X,leg1Y,4);
            g.fillArc(drawX+21,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+26,drawY+22,drawX+28,drawY+28);
            g.drawArc(drawX+21,drawY+26,6,4,180,180);

            int[] leg2X = {drawX+21,drawX+16,drawX+14,drawX+20};
            int[] leg2Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg2X,leg2Y,4);
            g.fillArc(drawX+14,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+21,drawY+22,drawX+20,drawY+28);
            g.drawLine(drawX+16,drawY+22,drawX+14,drawY+28);
            g.drawArc(drawX+14,drawY+26,6,4,180,180);

            int[] leg3X = {drawX+5,drawX,drawX,drawX+6};
            int[] leg3Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg3X,leg3Y,4);
            g.fillArc(drawX,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX,drawY+22,drawX,drawY+28);
            g.drawLine(drawX+5,drawY+22,drawX+6,drawY+28);
            g.drawArc(drawX,drawY+26,6,4,180,180);

            int[] leg4X = {drawX+12,drawX+7,drawX+8,drawX+14};
            int[] leg4Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg4X,leg4Y,4);
            g.fillArc(drawX+8,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+7,drawY+22,drawX+8,drawY+28);
            g.drawLine(drawX+12,drawY+22,drawX+14,drawY+28);
            g.drawArc(drawX+8,drawY+26,6,4,180,180);
        }
    }

    public void drawLegs3(Graphics g){
        if(!right){
            int[] leg1X = {drawX+16,drawX+21,drawX+21,drawX+15};
            int[] leg1Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg1X,leg1Y,4);
            g.fillArc(drawX+15,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+16,drawY+22,drawX+15,drawY+28);
            g.drawArc(drawX+15,drawY+26,6,4,180,180);

            int[] leg2X = {drawX+15,drawX+20,drawX+20,drawX+14};
            int[] leg2Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg2X,leg2Y,4);
            g.fillArc(drawX+14,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+15,drawY+22,drawX+14,drawY+28);
            g.drawLine(drawX+20,drawY+22,drawX+20,drawY+28);
            g.drawArc(drawX+14,drawY+26,6,4,180,180);

            int[] leg3X = {drawX+28,drawX+34,drawX+36,drawX+30};
            int[] leg3Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg3X,leg3Y,4);
            g.fillArc(drawX+30,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+28,drawY+22,drawX+30,drawY+28);
            g.drawLine(drawX+34,drawY+22,drawX+36,drawY+28);
            g.drawArc(drawX+30,drawY+26,6,4,180,180);

            int[] leg4X = {drawX+30,drawX+35,drawX+35,drawX+29};
            int[] leg4Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg4X,leg4Y,4);
            g.fillArc(drawX+29,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+30,drawY+22,drawX+29,drawY+28);
            g.drawLine(drawX+35,drawY+22,drawX+35,drawY+28);
            g.drawArc(drawX+29,drawY+26,6,4,180,180);
        }
        else{
            int[] leg1X = {drawX+24,drawX+19,drawX+19,drawX+25};
            int[] leg1Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg1X,leg1Y,4);
            g.fillArc(drawX+19,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+24,drawY+22,drawX+25,drawY+28);
            g.drawArc(drawX+19,drawY+26,6,4,180,180);

            int[] leg2X = {drawX+25,drawX+20,drawX+20,drawX+26};
            int[] leg2Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg2X,leg2Y,4);
            g.fillArc(drawX+20,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+25,drawY+22,drawX+26,drawY+28);
            g.drawLine(drawX+20,drawY+22,drawX+20,drawY+28);
            g.drawArc(drawX+20,drawY+26,6,4,180,180);

            int[] leg3X = {drawX+12,drawX+6,drawX+4,drawX+10};
            int[] leg3Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg3X,leg3Y,4);
            g.fillArc(drawX+4,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+12,drawY+22,drawX+10,drawY+28);
            g.drawLine(drawX+6,drawY+22,drawX+4,drawY+28);
            g.drawArc(drawX+4,drawY+26,6,4,180,180);

            int[] leg4X = {drawX+10,drawX+5,drawX+5,drawX+11};
            int[] leg4Y = {drawY+22,drawY+22,drawY+28,drawY+28};
            g.setColor(Color.green);
            g.fillPolygon(leg4X,leg4Y,4);
            g.fillArc(drawX+5,drawY+26,6,4,180,180);
            g.setColor(Color.black);
            g.drawLine(drawX+10,drawY+22,drawX+11,drawY+28);
            g.drawLine(drawX+5,drawY+22,drawX+5,drawY+28);
            g.drawArc(drawX+5,drawY+26,6,4,180,180);
        }
    }

    public void drawHealth(Graphics g){
        g.setColor(Color.red);
        g.fillRect(drawX-5, drawY-10, (int)(50*(health/250.0)), 7);
        g.setColor(Color.black);
        g.drawRect(drawX-5, drawY-10, 50, 7);
    }

    // The following collisions will return true if it collides with a wall
    public boolean topCollision(Wall wal){
        if((x<=wal.getX()+wal.getWidth()) && (x+w>=wal.getX()) && (y<=wal.getY()+wal.getHeight())
        && (y>= wal.getY()+wal.getHeight()+vy) && vy<0){
            if(y<wal.getY()+wal.getHeight()){
                y = wal.getY()+wal.getHeight()+2;
            }
            return true;
        }
        else
            return false;
    }

    public boolean bottomCollision(Wall wal){
        if((x<=wal.getX()+wal.getWidth()) && (x+w>=wal.getX()) && (y+h>=wal.getY()) && (y+h <= wal.getY()+15)
        && (vy>=0)){
            if(y+h>wal.getY()){
                y = wal.getY()-h;
            }
            return true;
        }
        else
            return false;
    }

    public boolean rightCollision(Wall wal){
        if((y<=wal.getY()+wal.getHeight()-5) && (y+h>=wal.getY()+5) && (x+w>=wal.getX())
        && (x+w<=wal.getX()+vx*2) && vx>0){
            x = wal.getX()-w;
            return true;
        }
        else
            return false;
    }

    public boolean leftCollision(Wall wal){
        if((y<=wal.getY()+wal.getHeight()-5) && (y+h>=wal.getY()+5) && (x<=wal.getX()+wal.getWidth())
        && (x>=wal.getX()+wal.getWidth()+vx*2) && vx<0){
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
