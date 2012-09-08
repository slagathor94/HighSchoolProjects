import java.applet.*;
import java.awt.*;
import java.util.ArrayList;

public class Enemy
{
    private int x,y,vx,vy,health,strength,score,w,h,firstX,firstY, wallStart, wallEnd, drawX, drawY;
    private Color c;
    private Pirate bob;
    private Rectangle box;
    private boolean remove, revealed, startFall;
    private ArrayList<Wall> walls;
    private ArrayList<enemyCannon> ebullets;

    public Enemy(int X, int Y)
    {
        // Used for easy moving of the rectangle
        firstY = Y;
        firstX = X;
        x = X;
        y = Y;

        // Width and height of enemy
        w = 60;
        h = 30;

        // May Implement score counter later
        score = 75;
        
        // Just stuff
        box = new Rectangle(x,y,w,h);
        bob = new Pirate();
        walls = new ArrayList<Wall>();
        ebullets = new ArrayList<enemyCannon>();
        
        // Speed Implementations
        vy = 0;
        vx = 3;
        
        // Health
        health = 200;
        
        // How much damage it will do every time it hits Pirate
        strength = 2;
        
        // Used to tell the applet to remove this enemy if it is dead
        remove = false;
        
        // This will make it so the enemy will not change directions when falling at the beginning of the game
        startFall = true;
    }

    public void draw(Graphics2D g2)
    {
        // Body
        c = Color.red;
        g2.setColor(c);
        g2.fillOval(drawX+14,drawY+7,32,15);
        g2.setColor(Color.black);
        g2.drawOval(drawX+14,drawY+7,32,15);

        // Eyes
        g2.setColor(Color.white);
        g2.fillRect(drawX+20,drawY+10,8,6);
        g2.fillRect(drawX+32,drawY+10,8,6);
        g2.setColor(Color.black);
        g2.fillRect(drawX+24,drawY+13,4,3);
        g2.fillRect(drawX+32,drawY+13,4,3);
        
        if(health < 200)
            drawHealth(g2);
    }

    public void hurt(int d)
    {
        health-=d;
        if(health<=0)
            remove=true;
    }

    public void move(int charX, int charY, Pirate Bob, ArrayList<Wall> walle,
    ArrayList<enemyCannon> ebullet, int scrollX, int scrollY)
    {
        // Applies inputed Pirate and Walls Array to be used throughout class
        bob = Bob;
        walls = walle;
        ebullets = ebullet;
        
        // Applies the starting x and y
        firstX = x;
        firstY = y;

        // AI
        
        // Makes it so it doesn't do anything if it isn't on the screen
        // May help make it not lag for large levels
        if(!revealed && drawX < 800 && drawY < 800){
            revealed = true;
        }

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
                    }
                }
            }
            // Changes direction if falling, allowing it to stay on the same platform
            if(vy == 0 && !startFall && (x <= wallStart+2 || x+w >= wallEnd-2))
                vx = -vx;
            //if(vy>0 && !startFall)
            //    vx=-vx;
            
            x+=vx;
        }

        y+=vy;

        box.translate(x-firstX, y-firstY);
        
        drawX = x - scrollX;
        drawY = y - scrollY;
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
        if((x<=wal.getX()+wal.getWidth()) && (x+w>=wal.getX()) && (y+h>=wal.getY())
        && (y+h <= wal.getY()+15) && (vy>=0)){
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
    
    public void drawHealth(Graphics g){
        g.setColor(Color.red);
        g.fillRect(drawX+5, drawY-10, (int)(50*(health/200.0)), 7);
        g.setColor(Color.black);
        g.drawRect(drawX+5, drawY-10, 50, 7);
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