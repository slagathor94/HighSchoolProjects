import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

public class PirateApplet extends Applet implements ActionListener, KeyListener, MouseListener, MouseMotionListener
{
    // Pirate Global Variables
    private Timer t;
    private Pirate bob;
    private Color c;
    private boolean a,d,slowA,slowD,jump,shift,control,slice,shoot,shootRelease,keyR;
    private int score,totalCount,changeX,gameState,levelY,levelX,scrollX,scrollY;
    private ArrayList<Wall> walls;
    private ArrayList<Enemy> enemies;
    private Gun revolver;
    private ArrayList<Bullet> bullets;
    private ArrayList<enemyCannon> ebullets;

    // Zombie Global Variables
    private ArrayList<BasicEnemy> zenemies;
    private ArrayList<ZBullet> zbullet;
    private ArrayList<enemyBullet> zebullet;
    private RainbowGun[] zgun;
    private Character zbob;
    private double zmouseX, zmouseY;
    private boolean za,zw,zs,zd,zslowA,zslowD,zslowW,zslowS,zmouse,zmouseP,zpowerup,ze2,zq;
    private int zscore,zpx,zpy,zP,zscount,zecount2,zecount3,zecount4,zecount5,ztotalCount,zcurrent;
    private Rectangle zpower;
    private int[] zstarsx,zstarsy,zstarsd;

    // Menu Global Variables
    private boolean menuUp, menuDown, menuLeft, menuRight, menuEnter;
    private ArrayList<MainMenu> menu;
    private AudioClip ac;

    public void init(){
        resize(800,700);
        int[] pixels = new int[16 * 16];
        Image image = Toolkit.getDefaultToolkit().createImage(
                new MemoryImageSource(16, 16, pixels, 0, 16));
        Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                image, new Point((int)zmouseX,(int)zmouseY), "invisibleCursor");
        setCursor(transparentCursor);
        zstarsx = new int[150];
        zstarsy = new int[150];
        zstarsd = new int[150];
        for(int i = 0;i<zstarsx.length;i++)
        {
            zstarsx[i] = (int)(Math.random()*800);
            zstarsy[i] = (int)(Math.random()*600+100);
            zstarsd[i] = (int)(Math.random()*3+2);
        }
        // Menu Initiation
        menu = new ArrayList<MainMenu>();
        gameState = 0;
        zpower = new Rectangle(0,0,0,0);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this); 
        t = new Timer(23, this);
        ac = getAudioClip(getCodeBase(), "Title.wav");
        ac.loop();
        t.start();       
        start();
    }

    public void start(){
        //Thread t = new Thread(this);
        //t.start();

        // Zombie Initiation
        if(gameState == 0 || gameState == 2 || gameState == 4){
            zgun = new RainbowGun[6];
            zbullet = new ArrayList<ZBullet>();
            zgun[0] = new RainbowGun(zbullet);
            zgun[1] = new Flames(zbullet);
            zgun[2] = new singularityGun(zbullet);
            zgun[3] = new lightningCloak(zbullet);
            zgun[4] = new railGun(zbullet);
            zgun[5] = new blackHole(zbullet);
            zbob = new Character();
            zenemies = new ArrayList<BasicEnemy>();
            zebullet = new ArrayList<enemyBullet>();
            zslowA = true;
            zslowD = true;
            zslowS = true;
            zslowW = true;
            ztotalCount = 0;
            zscore = 0;
            zcurrent = 0;
            zscount = 100;
            zP = 0;
            ztotalCount = 0;
            zecount2 = 0;
            zecount3 = 0;
            zecount4 = 0;
            zecount5 = 0;
            zcurrent = 0;
        }

        // Pirate Initiation
        if(gameState == 0 || gameState == 1 || gameState == 3){
            shootRelease = true;
            changeX = 0;
            revolver = new Gun(bullets);
            ebullets = new ArrayList<enemyCannon>();
            bullets = new ArrayList<Bullet>();
            enemies = new ArrayList<Enemy>();
            walls = new ArrayList<Wall>();
            levelTwo();
            bob = new Pirate();
            bob.changeStart(240,60);
            slowA=true;
            slowD=true;
            jump = false;
            totalCount=0;
            score = 0;
            totalCount = 0;
            levelX = 3560;
            levelY = 1930;
            scrollX = bob.getScrollX();
            scrollY = bob.getScrollY();
        }

        if(gameState == 3)
            gameState = 1;
        else if(gameState == 4)
            gameState = 2;
    }

    public void actionPerformed(ActionEvent e){
        if(gameState == 1){
            // Check gun
            revolver.checkGun(keyR);

            // Moves walls
            for(int i = 0;i<walls.size();i++)
                walls.get(i).move(scrollX,scrollY);

            // Moves enemies
            for(int i = 0;i<enemies.size();i++){
                enemies.get(i).move(bob.getCX(),bob.getCY(),bob,walls,ebullets,scrollX,scrollY);
                ebullets = enemies.get(i).geteBullets();
                if(enemies.get(i).Remove()){
                    score += enemies.get(i).getScore();
                    enemies.remove(i);
                }
            }

            // Creates and moves bullets
            if(shoot){
                if(bob.isRight())
                    revolver.bulletCreator(bob.getX()+30,bob.getCY(),bob.isRight(),bullets);
                else
                    revolver.bulletCreator(bob.getX()-16,bob.getCY(),bob.isRight(),bullets);
                shoot = false;
            }
            bullets = revolver.getBullet();
            if(bullets.size()>0){
                for(int i = 0;i<bullets.size();i++){
                    bullets.get(i).move(enemies,walls,scrollX,scrollY);
                    if(bullets.get(i).Remove())
                        bullets.remove(i);
                }
            }

            // Enemy Bullets
            if(ebullets.size() > 0){
                for(int i = 0; i < ebullets.size(); i++){
                    ebullets.get(i).move(walls, scrollX,scrollY);
                    if(ebullets.get(i).Remove())
                        ebullets.remove(i);
                }
            }

            // Moves Character
            bob.move(slowA,slowD,a,d,jump,walls,shift,control,enemies,levelX,levelY, ebullets);
            scrollX = bob.getScrollX();
            scrollY = bob.getScrollY();

            // Slides Screen
            //             for(int i = 0;i<walls.size();i++)
            //                 walls.get(i).screenSlide(changeX);
            //             for(int i = 0;i<enemies.size();i++)
            //                 enemies.get(i).screenSlide(changeX);
            //             if(bullets.size()>0){
            //                 for(int i = 0;i<bullets.size();i++){
            //                     bullets.get(i).screenSlide(changeX);
            //                 }
            //             }

            // Gun Counter
            revolver.counterPlus();

            // Checks if Game Over
            if(bob.getDead() == true){
                repaint();
                t.stop();
            }

            totalCount++;
        }

        if(gameState == 2){
            // Changes gun
            changeGun();

            // Moves Enemies
            if(zenemies.size()>0)
            {
                boolean hit = false;
                for(int i = 0; i<zenemies.size();i++)
                {
                    zenemies.get(i).move(zbob.getX(),zbob.getY(),zbullet, zbob,zebullet);
                    zbob = zenemies.get(i).getC();
                    zbullet = zenemies.get(i).getB();
                    zebullet = zenemies.get(i).getEB();
                    for(int k = 0; k<zbullet.size();k++){
                        if(zbullet.get(k).Remove() == true){
                            zbullet.remove(k);
                            hit = true;
                        }
                    }
                    if(zenemies.get(i).Remove() == true)
                    {
                        int random = (int)(Math.random()*100);
                        if(random <= 10 && zpowerup == false)
                        {
                            int prand = (int)(Math.random()*6);
                            powerUp(zenemies.get(i).getX(),zenemies.get(i).getY(),prand);
                            zpowerup = true;
                        }
                        zscore+=zenemies.get(i).getScore();
                        zenemies.remove(i);
                    }
                }
                if(hit){
                    AudioClip az = getAudioClip(getCodeBase(), "hit.au");
                    az.play();
                }
            }

            // Moves Enemy Bullets
            for(int i = 0; i<zebullet.size();i++)
                zebullet.get(i).move();

            // Checks if Character is on the zpower up
            if(zbob.getBox().intersects(zpower))
            {
                zpower = new Rectangle(0,0,0,0);
                zpowerup = false;
                if(zP == 0)
                    zgun[1].addAmmo(400);
                else if(zP == 3)
                    zgun[4].addAmmo(20);
                else if(zP == 2)
                    zgun[3].addAmmo(400);
                else if(zP == 1)
                    zgun[2].addAmmo(15);
                else if(zP == 4)
                    zbob.damage(-50);
                else if(zP == 5)
                    zgun[5].addAmmo(5);
            }

            // Spawn le enemies
            spawnEnemies();

            // Count for Special Gun Reloads
            zscount++;

            // Checks if Game Over
            if(zbob.getDead() == true)
            {
                t.stop();
                repaint();

            }

            // Moves Character
            zbob.move(this,zslowA,zslowD,za,zd,zslowS,zslowW,zw,zs,zebullet);

            // Moves Bullet and removes in count is up
            for(int i = 0; i<zgun[zcurrent].getBullet().size();i++)
            {
                zbullet.get(i).move();
                if(zbullet.get(i).Remove() == true)
                    zbullet.remove(i);
            }
            // Creates Bullets
            if(zmouseP == true && zgun[zcurrent].getAmmo()>0)
            {
                if(zscount > 50 && zgun[zcurrent] == zgun[2])
                {
                    zgun[zcurrent].bulletCreator(zbob.getCX(),zbob.getCY(),zmouseX,zmouseY,zbullet);
                    zbullet=zgun[zcurrent].getBullet();
                    zscount = 0;
                }
                else if(zscount > 10 && zgun[zcurrent] == zgun[4])
                {
                    zgun[zcurrent].bulletCreator(zbob.getCX(),zbob.getCY(),zmouseX,zmouseY,zbullet);
                    zbullet=zgun[zcurrent].getBullet();
                    zscount = 0;
                }
                else if(zgun[zcurrent] == zgun[5] && zscount > 100)
                {
                    zgun[zcurrent].bulletCreator(zbob.getCX(),zbob.getCY(),zmouseX,zmouseY,zbullet);
                    zbullet=zgun[zcurrent].getBullet();
                    getAudioClip(getCodeBase(),"hole.au").play();
                    zscount = 0;
                }
                else if(zgun[zcurrent] == zgun[1])
                {
                    zgun[zcurrent].bulletCreator(zbob.getCX(),zbob.getCY(),zmouseX,zmouseY,zbullet);
                    zbullet=zgun[zcurrent].getBullet();
                }
                else if(zgun[zcurrent] == zgun[3])
                {
                    zgun[zcurrent].bulletCreator(zbob.getCX(),zbob.getCY(),zmouseX,zmouseY,zbullet);
                    zbullet=zgun[zcurrent].getBullet();
                }
                else if(zgun[zcurrent] == zgun[0])
                {
                    zgun[zcurrent].bulletCreator(zbob.getCX(),zbob.getCY(),zmouseX,zmouseY,zbullet);
                    zbullet=zgun[zcurrent].getBullet();
                }
            }
            else if(zmouseP == true)
                zcurrent = 0;
        }

        if(gameState == 0){
            // Creates menus
            if(menu.size() == 0){
                MainMenu leMenu = new MainMenu(0, 0, getWidth(), getHeight());
                menu.add(leMenu);
            }

            // Moves between different buttons
            if(menu.size()>0)
                menu.get(menu.size()-1).mover(menuUp, menuDown, menuLeft, menuRight, menuEnter);

            // Checks for different menu gamestates
            if(menu.size()>0 && menu.get(menu.size()-1).getGameState() != 0){
                gameState = menu.get(menu.size()-1).getGameState();
                ac.stop();
                if(gameState == 2 || gameState == 4){
                    ac = getAudioClip(getCodeBase(), "Cantina.au");
                    ac.loop();
                }
                else if(gameState == 1 || gameState == 3){
                    ac = getAudioClip(getCodeBase(), "PirateBack.wav");
                    ac.loop();
                }
            }

            // Resets the key presses
            menuUp = false;
            menuDown = false;
            menuLeft = false;
            menuRight = false;
            menuEnter = false;
        }
        else
        // Removes menus
        if(menu.size() > 0)
            menu.remove(0);

        if(gameState == 3 || gameState == 4)
            start();

        //Repaints
        repaint();
    }

    @SuppressWarnings("deprecation")
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D)g;

        // Zombie Game Drawing
        if(gameState == 2){
            String Score = ""+zscore;

            // Background
            c = Color.black;
            g2.setColor(c);
            g2.fillRect(0,0,getWidth(),getHeight());
            g2.setColor(Color.white);
            for(int i = 0;i<zstarsx.length;i++)
                g2.fillRect(zstarsx[i],zstarsy[i],zstarsd[i],zstarsd[i]);

            // Bullet Draw
            for(int i = 0; i<zbullet.size();i++)
                zbullet.get(i).draw(g2);
            for(int i = 0; i<zebullet.size();i++)
                zebullet.get(i).draw(g2);

            // Powerup Drawing
            if(zpowerup == true && zP == 0 )
            {
                g2.setColor(Color.red);
                g2.fillRect(zpx,zpy,15,15);
            }
            else if(zpowerup == true && zP == 3)
            {
                c = Color.blue;
                g2.setColor(c);
                g2.fillRect(zpx,zpy,15,15);
            }
            else if(zpowerup == true && zP == 1)
            {
                c = new Color(170,64,151);
                g2.setColor(c);
                g2.fillRect(zpx,zpy,15,15);
            }
            else if(zpowerup == true && zP == 2)
            {
                c = Color.yellow;
                g2.setColor(c);
                g2.fillRect(zpx,zpy,15,15);
            }
            else if(zpowerup == true && zP == 4)
            {
                g2.setColor(Color.red);
                g2.fillRect(zpx,zpy,15,15);
                g2.setColor(Color.white);
                g2.fillRect(zpx+1,zpy+5,13,5);
                g2.fillRect(zpx+5,zpy+1,5,13);
            }
            else if(zpowerup == true && zP == 5)
            {
                g2.setColor(Color.white);
                g2.fillRect(zpx,zpy,15,15);
                g2.setColor(Color.black);
                g2.fillOval(zpx,zpy,14,14);
            }

            // Draw enemies
            for(int i = 0; i<zenemies.size();i++)
                zenemies.get(i).draw(g2);

            // Draw Bob
            if(zbob.getDead() == false)
                zbob.draw(g2);
            else
                zbob.death(g2);

            // Bob'zs Hat
            if(zgun[zcurrent] == zgun[0])
                c = new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
            else if(zgun[zcurrent] == zgun[1])
                c = Color.red;
            else if(zgun[zcurrent] == zgun[2])
                c = new Color(170,64,151);
            else if(zgun[zcurrent] == zgun[3])
                c = Color.yellow;
            else if(zgun[zcurrent] == zgun[4])
                c = Color.blue;
            else if(zgun[zcurrent] == zgun[5])
                c = Color.gray;
            g2.setColor(c);
            g2.fillRect(zbob.getX(),zbob.getY(),30,10);
            g2.setColor(Color.white);
            g2.fillRect(zbob.getX()+10,zbob.getY()-5,10,10);
            g2.setColor(Color.black);
            g2.drawRect(zbob.getX()+10,zbob.getY()-5,10,10);

            // UI
            g2.setColor(Color.orange);
            g2.fillRect(0,0,getWidth(),50);
            g2.setColor(Color.black);
            g2.drawString("Score:",660,23);
            g2.drawString(Score,700,23);
            g2.drawString("Gun:",360,23);
            g2.drawString(zgun[zcurrent].getGun(),400,23);
            g2.drawString("Health:",10,23);
            g2.drawString("x"+zbob.getLife(),260,23);
            g2.setColor(Color.red);
            g2.fillRect(55,14,zbob.getH()*2,10);
            g2.setColor(Color.gray);
            g2.drawRect(55,14,200,10);
            g2.fillRect(0,30,getWidth(),20);

            // Gun UI
            g2.setColor(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
            g2.fillRect(0,30,getWidth()/6,13);
            g2.setColor(Color.red);
            g2.fillRect(getWidth()/6,30,getWidth()/6,13);
            g2.setColor(new Color(170,64,151));
            g2.fillRect(2*getWidth()/6,30,getWidth()/6,13);
            g2.setColor(Color.yellow);
            g2.fillRect(3*getWidth()/6,30,getWidth()/6,13);
            g2.setColor(Color.blue);
            g2.fillRect(4*getWidth()/6,30,getWidth()/6,13);
            g2.setColor(Color.black);
            g2.fillRect(5*getWidth()/6,30,getWidth()/6,13);
            for(int i = 0;i<zgun.length;i++)
            {
                if(zgun[zcurrent] == zgun[i])
                {
                    g2.setColor(Color.orange);
                    if(zgun[i] == zgun[2])
                    {
                        if(zscount<50)
                            g2.fillRect(i*getWidth()/6,43,(int)(getWidth()/6*(zscount/50.0)),7);
                        else
                            g2.fillRect(i*getWidth()/6,43,getWidth()/6,7);
                    }
                    else if(zgun[i] == zgun[4])
                    {
                        if(zscount<10)
                            g2.fillRect(i*getWidth()/6,43,(int)(getWidth()/6*(zscount/10.0)),7);
                        else
                            g2.fillRect(i*getWidth()/6,43,getWidth()/6,7);
                    }
                    else if(zgun[i] == zgun[5])
                    {
                        if(zscount<100)
                            g2.fillRect(i*getWidth()/6,43,(int)(getWidth()/6*(zscount/100.0)),7);
                        else
                            g2.fillRect(i*getWidth()/6,43,getWidth()/6,7);
                    }
                    else
                        g2.fillRect(i*getWidth()/6,43,getWidth()/6,7);
                }
                g2.setColor(Color.white);
                g2.drawRect(i*getWidth()/6,30,getWidth()/6,13);
                g2.drawRect(i*getWidth()/6,43,getWidth()/6,7);
                if(zgun[i] != zgun[3])
                    g2.setColor(Color.white);
                else
                    g2.setColor(Color.black);
                if(zgun[i] != zgun[0])
                {
                    if(zgun[i].getAmmo()>0)
                        g2.drawString(""+zgun[i].getAmmo(),i*getWidth()/6+60,42);
                    else
                        g2.drawString(""+0,i*getWidth()/6+60,42);
                }
            }

            // Beginning Game Instructions
            g2.setColor(Color.yellow);
            if(ztotalCount <500)
                g2.drawString("GOOD LUCK!",350,400);

            // Mouse Exited
            g2.setColor(Color.red);
            if(zmouse == false)
            {
                g2.drawString("Your zmouse is outside the screen.", 275,375);
                g2.drawString("The game is paused, enter again to start!",250,425);
            }
        }

        // Pirate Game Drawing
        if(gameState == 1){
            Font font;
            String[] fonts = getToolkit().getFontList();
            //font = new Font(fonts[2],Font.BOLD,14);
            //g2.setFont(font);
            String Score = ""+score;

            // Background
            c = new Color(134,219,220);
            g2.setColor(c);
            g2.fillRect(0,0,getWidth(),getHeight());
            g2.setColor(Color.white);

            // Draws Bullets
            if(bullets.size() > 0){
                for(int i = 0;i<bullets.size();i++)
                    bullets.get(i).draw(g2);
            }

            // Draws Enemy Bullets
            if(ebullets.size() > 0){
                for(int i = 0;i<ebullets.size();i++)
                    ebullets.get(i).draw(g2);
            }

            // Draws Walls
            for(int i = 0;i<walls.size();i++)
                walls.get(i).draw(g2);

            // Draws Enemies
            for(int i = 0;i<enemies.size();i++)
                enemies.get(i).draw(g2);

            // Draw Bob
            if(bob.getDead() == false)
                bob.draw(g2);
            else
                bob.death(g2);

            // UI
            g2.setColor(Color.orange);
            g2.fillRect(0,0,getWidth(),35);
            g2.setColor(Color.black);
            g2.drawString("Score:",660,23);
            g2.drawString(Score,700,23);
            g2.drawString("Revolver Ammo:",360,23);
            for(int i = 0;i<6;i++){
                if(i+1<=revolver.getAmmo())
                    g2.setColor(Color.yellow);
                else
                    g2.setColor(Color.gray);
                int[] xA = new int[5];
                int[] yA = new int[5];
                xA[0] = 455+20*i+16;
                xA[1] = 455+20*i+12;
                xA[2] = 455+20*i+4;
                xA[3] = 455+20*i+4;
                xA[4] = 455+20*i+12;
                yA[0] = 18;
                yA[1] = 14;
                yA[2] = 14;
                yA[3] = 22;
                yA[4] = 22;
                g2.fillPolygon(xA,yA,5);
                g2.setColor(Color.black);
                g2.drawPolygon(xA,yA,5);
            }
            g2.setColor(Color.black);
            g2.drawString("Health:",10,23);
            g2.drawString("x"+bob.getLife(),260,23);
            g2.setColor(Color.red);
            g2.fillRect(55,14,(int)(bob.getH()*.66),10);
            g2.setColor(Color.gray);
            g2.drawRect(55,14,200,10);
            //      g2.fillRect(0,30,getWidth(),20);

            // Beginning Game Instructions
            g2.setColor(Color.yellow);
        }

        // Draws menu
        if(gameState == 0){
            for(int i = 0; i < menu.size(); i++){
                menu.get(i).draw(g);
            }
        }

        // Cursor Crosshairs
        g2.setColor(Color.red);
        g2.fillRect((int)zmouseX-9,(int)zmouseY+2,8,2);
        g2.fillRect((int)zmouseX+7,(int)zmouseY+2,8,2);
        g2.fillRect((int)zmouseX+2,(int)zmouseY+7,2,8);
        g2.fillRect((int)zmouseX+2,(int)zmouseY-9,2,8);
    }

    public void keyTyped(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Pirate Keys
        if(gameState == 1){
            if(keyCode == KeyEvent.VK_R)
                keyR = true;
            if(keyCode == 65 || keyCode == KeyEvent.VK_LEFT){
                a= true;
                slowA = false;
            }
            if(keyCode == 68|| keyCode == KeyEvent.VK_RIGHT){
                d = true;
                slowD = false;
            }
            if(keyCode == 87 || keyCode == KeyEvent.VK_UP){
                jump = true;
            }
            if(keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_J)
                slice = true;
            if(keyCode == KeyEvent.VK_SHIFT)
                shift = true;
            if(keyCode == KeyEvent.VK_DOWN || keyCode == 83 || keyCode == KeyEvent.VK_CONTROL){
                control = true;
            }
        }

        // Zombie Keys
        else if(gameState == 2){
            if(keyCode == 65)
            {
                za= true;
                zslowA = false;
            }
            if(keyCode == 68)
            {
                zd = true;
                zslowD = false;
            }
            if(keyCode == 83)
            {
                zs = true;
                zslowS = false;
            }
            if(keyCode == 87)
            {
                zw = true;
                zslowW = false;
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Pirate Keys
        if(gameState == 1){
            if(keyCode == KeyEvent.VK_R)
                keyR = true;
            if(keyCode == 65 || keyCode == KeyEvent.VK_LEFT){
                a= true;
                slowA = false;
            }
            if(keyCode == 68|| keyCode == KeyEvent.VK_RIGHT){
                d = true;
                slowD = false;
            }
            if(keyCode == 87 || keyCode == KeyEvent.VK_UP){
                jump = true;
            }
            if(keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_J)
                slice = true;
            if((keyCode == KeyEvent.VK_K || keyCode == KeyEvent.VK_C) && shootRelease){
                shoot = true;
                shootRelease = false;
            }
            if(keyCode == KeyEvent.VK_SHIFT)
                shift = true;
            if(keyCode == KeyEvent.VK_DOWN || keyCode == 83 || keyCode == KeyEvent.VK_CONTROL){
                control = true;
            }
        }

        // Zombie Keys
        else if(gameState == 2){
            if(keyCode == 65)
            {
                za= true;
                zslowA = false;
            }
            if(keyCode == 68)
            {
                zd = true;
                zslowD = false;
            }
            if(keyCode == 83)
            {
                zs = true;
                zslowS = false;
            }
            if(keyCode == 87)
            {
                zw = true;
                zslowW = false;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Pirate Button Presses
        if(gameState == 1){
            if(keyCode == KeyEvent.VK_R)
                keyR = false;
            if(keyCode == 65 || keyCode == KeyEvent.VK_LEFT) {
                a= false;
                slowA = true;
            }
            if(keyCode == 68|| keyCode == KeyEvent.VK_RIGHT) {
                d = false;
                slowD = true;
            }
            if(keyCode == 87 || keyCode == KeyEvent.VK_UP) // keyCode for w
                jump = false;
            if(keyCode == KeyEvent.VK_SHIFT)
                shift = false;
            if(keyCode == KeyEvent.VK_DOWN || keyCode == 83 || keyCode == KeyEvent.VK_CONTROL){
                control = false;
            }
            if(keyCode == KeyEvent.VK_K || keyCode == KeyEvent.VK_C)
                shootRelease = true;
            if(keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_J)
                slice = false;
            if(keyCode == KeyEvent.VK_ENTER && bob.getDead() == true){
                start();
                t.start();
            }
        }

        // Zombie Game Keys
        else if(gameState == 2){
            if(keyCode == 65)
            {
                za= false;
                zslowA = true;
            }
            if(keyCode == 68)
            {
                zd = false;
                zslowD = true;
            }
            if(keyCode == 83)
            {
                zs = false;
                zslowS = true;
            }
            if(keyCode == 87)
            {
                zw = false;
                zslowW = true;
            }
            if(keyCode == 81)
                zq = true;
            if(keyCode == 69)
                ze2 = true;
            if(keyCode == KeyEvent.VK_ENTER && zbob.getDead() == true){
                start();
                t.start();
            }
        }

        // Menu Button Presses
        else if(gameState == 0){
            if(keyCode == 87 || keyCode == KeyEvent.VK_UP)
                menuUp = true;
            if(keyCode == KeyEvent.VK_DOWN || keyCode == 83){
                menuDown = true;
            }
            if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)
                menuLeft = true;
            if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)
                menuRight = true;
            if(keyCode == KeyEvent.VK_ENTER)
                menuEnter = true;
            if(keyCode == KeyEvent.VK_ESCAPE)
                gameState = 0;
        }
        if(keyCode == KeyEvent.VK_ESCAPE)
            gameState = 0;
    }

    //Pirate Classes
    public void levelOne(){
        // Border
        Wall walle = new Wall(0,1800,3660,30);
        walls.add(walle);
        walle = new Wall(0,0,3630,30);
        walls.add(walle);
        walle = new Wall(0,0,30,1830);
        walls.add(walle);
        walle = new Wall(3630,0,30,1830);
        walls.add(walle);

        // First Grid Top
        walle = new Wall(30,120,90,480);
        walls.add(walle);
        walle = new Wall(180,30,630,570);
        walls.add(walle);
        walle = new Wall(30,870,480,30);
        walls.add(walle);
        walle = new Wall(660,750,30,270);
        walls.add(walle);
        walle = new Wall(960,600,330,30);
        walls.add(walle);

        walle = new Platform(120,585,60,15,0,0,0);
        walls.add(walle);
        walle = new Platform(870,660,90,15,0,0,0);
        walls.add(walle);
        walle = new Platform(870,600,90,15,0,0,0);
        walls.add(walle);

        walle = new MovingWall(60,720,90,30,3,0,300);
        walls.add(walle);
        walle = new MovingWall(1050,500,120,30,3,-3,90);
        walls.add(walle);

        Enemy eneme = new Turtle(50,820);
        enemies.add(eneme);

        // First Grid Bottom
        walle = new Wall(180,990,480,30);
        walls.add(walle);
        walle = new Wall(30,1170,300,30);
        walls.add(walle);
        walle = new Wall(390,1170,30,540);
        walls.add(walle);
        walle = new Wall(150,1380,150,30);
        walls.add(walle);
        walle = new Wall(150,1530,90,30);
        walls.add(walle);
        walle = new Wall(180,1680,240,30);
        walls.add(walle);
        walle = new Wall(870,990,330,30);
        walls.add(walle);
        walle = new Wall(630,1200,330,600);
        walls.add(walle);

        walle = new Platform(660,1065,180,15,0,0,0);
        walls.add(walle);
        walle = new Platform(60,1080,90,15,0,0,0);
        walls.add(walle);
        walle = new Platform(360,1110,240,15,0,0,0);
        walls.add(walle);
        walle = new Platform(330,1170,60,15,0,0,0);
        walls.add(walle);
        walle = new Platform(330,1260,60,15,0,0,0);
        walls.add(walle);
        walle = new Platform(330,1350,60,15,0,0,0);
        walls.add(walle);
        walle = new Platform(30,1470,90,15,0,0,0);
        walls.add(walle);
        walle = new Platform(270,1605,90,15,0,0,0);
        walls.add(walle);
        walle = new Platform(90,1725,90,15,0,0,0);
        walls.add(walle);
        walle = new Platform(960,1200,60,15,0,0,0);
        walls.add(walle);
        walle = new Platform(960,1290,60,15,0,0,0);
        walls.add(walle);
        walle = new Platform(960,1380,60,15,0,0,0);
        walls.add(walle);
        walle = new Platform(960,1470,60,15,0,0,0);
        walls.add(walle);
        walle = new Platform(960,1560,60,15,0,0,0);
        walls.add(walle);
        walle = new Platform(960,1650,60,15,0,0,0);
        walls.add(walle);
        walle = new Platform(960,1740,60,15,0,0,0);
        walls.add(walle);

        eneme = new Crane(200,950);
        enemies.add(eneme);
        eneme = new BlueCrab(180,1350);
        enemies.add(eneme);
        eneme = new Crane(240,1640);
        enemies.add(eneme);
        eneme = new Turtle(450,1080);
        enemies.add(eneme);
        eneme = new Turtle(1470,930);
        enemies.add(eneme);
        eneme = new Turtle(900,930);
        enemies.add(eneme);
        eneme = new Crane(1020,930);
        enemies.add(eneme);

        // 2nd Grid Top
        walle = new Wall(1470,90,360,30);
        walls.add(walle);
        walle = new Wall(1470,180,360,30);
        walls.add(walle);

        walle = new FakeWall(1800,120,30,60);
        walls.add(walle);

        eneme = new BlueCrab(1500,150);
        enemies.add(eneme);

        // 2nd Grid Bottom
        walle = new Wall(2070,1500,330,600);
        walls.add(walle);

        walle = new Platform(2100,1470,150,15,0,-5,169);
        walls.add(walle);

        walle = new MovingWall(1080,1200,90,30,2,0,135);
        walls.add(walle);
        walle = new MovingWall(1260,1290,90,30,3,2,90);
        walls.add(walle);
        walle = new MovingWall(1320,1470,90,30,-2,2,40);
        walls.add(walle);
        walle = new MovingWall(1290,1680,90,30,5,0,102);
        walls.add(walle);
        walle = new MovingWall(1920,1410,90,30,0,2,102);
        walls.add(walle);

        walle = new Spikes(2400,1770,330,30);
        walls.add(walle);

        eneme = new BlueCrab(1470,1740);
        enemies.add(eneme);
        eneme = new BlueCrab(1620,1740);
        enemies.add(eneme);
        eneme = new BlueCrab(1200,1740);
        enemies.add(eneme);
        eneme = new BlueCrab(1800,1740);
        enemies.add(eneme);
        eneme = new BlueCrab(1080,1740);
        enemies.add(eneme);
        eneme = new BlueCrab(900,1740);
        enemies.add(eneme);
        eneme = new Enemy(810,1740);
        enemies.add(eneme);
        eneme = new Crane(2100,1440);
        enemies.add(eneme);

        // 3rd Grid Top
        walle = new Trampoline(2430,860,150,15);
        walls.add(walle);

        walle = new Wall(2730,30,900,270);
        walls.add(walle);
        walle = new Wall(2730,300,270,30);
        walls.add(walle);
        walle = new Wall(2730,330,240,30);
        walls.add(walle);
        walle = new Wall(2730,360,210,30);
        walls.add(walle);
        walle = new Wall(2730,390,180,30);
        walls.add(walle);
        walle = new Wall(2730,420,150,30);
        walls.add(walle);
        walle = new Wall(2730,450,120,30);
        walls.add(walle);
        walle = new Wall(2730,480,90,30);
        walls.add(walle);
        walle = new Wall(2730,510,60,30);
        walls.add(walle);
        walle = new Wall(2730,540,30,30);
        walls.add(walle);
        walle = new Wall(2730,660,840,30);
        walls.add(walle);
        walle = new Wall(2730,690,60,1200);
        walls.add(walle);
        walle = new Wall(2880,630,360,30);
        walls.add(walle);
        walle = new Wall(2940,600,300,30);
        walls.add(walle);
        walle = new Wall(3000,570,240,30);
        walls.add(walle);
        walle = new Wall(3060,540,180,30);
        walls.add(walle);
        walle = new Wall(3120,510,120,30);
        walls.add(walle);
        walle = new Wall(3180,480,60,30);
        walls.add(walle);
        walle = new Wall(3480,480,90,210);
        walls.add(walle);
        walle = new Wall(2850,870,810,30);
        walls.add(walle);
        walle = new Wall(2850,810,90,60);
        walls.add(walle);
        walle = new Wall(3540,810,90,60);
        walls.add(walle);

        walle = new Spikes(3240,630,240,30);
        walls.add(walle);

        eneme = new Crane(2790,600);
        enemies.add(eneme);
        eneme = new Crane(3180,810);
        enemies.add(eneme);
        eneme = new Enemy(3330,810);
        enemies.add(eneme);
        eneme = new Turtle(3030,810);
        enemies.add(eneme);
        eneme = new Cannon(2895,780,true);
        enemies.add(eneme);

        // 3rd Grid Bottom
        walle = new Trampoline(2790,1185,150,15);
        walls.add(walle);
        walle = new Trampoline(2790,1725,360,15);
        walls.add(walle);
        walle = new Trampoline(2970,1315,120,15);
        walls.add(walle);
        walle = new Trampoline(3000,1485,120,15);
        walls.add(walle);
        walle = new Trampoline(3300,1485,120,15);
        walls.add(walle);

        walle = new Platform(3000,1065,210,15,0,0,0);
        walls.add(walle);
        walle = new Platform(2790,1545,210,15,0,0,0);
        walls.add(walle);
        walle = new Platform(3120,1545,180,15,0,0,0);
        walls.add(walle);
        walle = new Platform(3420,1545,210,15,0,0,0);
        walls.add(walle);

        walle = new Spikes(2790,1770,840,30);
        walls.add(walle);

        eneme = new Turtle(3060,1020);
        enemies.add(eneme);
        eneme = new Turtle(2820,1515);
        enemies.add(eneme);
        eneme = new Turtle(3150,1515);
        enemies.add(eneme);
        eneme = new Turtle(3450,1515);
        enemies.add(eneme);
    }

    public void levelTwo(){
        walls = new ArrayList<Wall>();
        enemies = new ArrayList<Enemy>();

        // Border
        Wall walle = new Wall(0,1800,3660,30);
        walls.add(walle);
        walle = new Wall(0,0,3630,30);
        walls.add(walle);
        walle = new Wall(0,0,30,1830);
        walls.add(walle);
        walle = new Wall(3630,0,30,1830);
        walls.add(walle);

        walle = new Wall(150,990,30,570);
        walls.add(walle);   
        walle = new Wall(150,1200,240,30);
        walls.add(walle);
        walle = new Wall(150,1530,240,30);
        walls.add(walle);
        walle = new Wall(480,1710,30,690);
        walls.add(walle);
        walle = new Wall(270,1380,240,30);
        walls.add(walle);
        walle = new Wall(270,1680,240,30);
        walls.add(walle);
        walle = new Wall(720,1110,510,30);
        walls.add(walle);
        walle = new Wall(720,1410,510,30);
        walls.add(walle);
        walle = new Platform(600,1740,120,15,0,-3,100);
        walls.add(walle);
        walle = new Platform(600,1080,120,15,0,-3,180);
        walls.add(walle);

        walle =  new Wall(90,0,90,870);
        walls.add(walle);
        walle = new Wall(120,840,30,60);
        walls.add(walle);
        walle = new Wall(330,0,90,870);
        walls.add(walle);
        walle = new Wall(360,840,30,60);
        walls.add(walle);
        walle = new Wall(720,600,90,30);
        walls.add(walle);
        walle = new Wall(780,570,120,30);
        walls.add(walle);
        walle = new Wall(870,540,120,30);
        walls.add(walle);
        walle = new Wall(960,810,900,30);
        walls.add(walle);
        walle = new Platform(1080,165,120,15,0,0,0);
        walls.add(walle);
        walle = new Platform(1080,285,120,15,0,0,0);
        walls.add(walle);
        walle = new Platform(1080,405,120,15,0,0,0);
        walls.add(walle);
        walle = new Wall(1260,120,120,30);
        walls.add(walle);
        walle = new Wall(1290,210,60,30);
        walls.add(walle);
        walle = new Wall(1290,300,60,30);
        walls.add(walle);
        walle = new Wall(1560,330,210,30);
        walls.add(walle);
        walle = new Wall(1770,210,90,30);
        walls.add(walle);
        walle = new Wall(1830,120,30,120);
        walls.add(walle);
        walle = new Wall(1830,120,30,120);
        walls.add(walle);
        walle = new Wall(1230,510,840,30);
        walls.add(walle);
        walle = new Wall(2040,120,30,420);
        walls.add(walle);
        walle = new Wall(2040,120,150,30);
        walls.add(walle);
        walle = new Wall(2160,480,210,30);
        walls.add(walle);
        walle = new Wall(1680,600,30,30);
        walls.add(walle);
        walle = new Wall(1950,600,210,30);
        walls.add(walle);
        walle = new Wall(1680,870,180,30);
        walls.add(walle);
        walle = new Wall(2160,870,30,30);
        walls.add(walle);
        walle = new Platform(1710,315,60,15,0,0,0);
        walls.add(walle);
        walle = new Platform(2070,480,90,15,0,0,0);
        walls.add(walle);
        walle = new MovingWall(1650,690,30,120,10,0,54);

        Enemy eneme = new Enemy(810,1080);
        enemies.add(eneme);
        eneme= new Enemy(930,1080);
        enemies.add(eneme);
        Turtle turt = new Turtle(1050,1080);
        enemies.add(turt);
        eneme = new BlueCrab(810,1380);
        enemies.add(eneme);
        eneme= new BlueCrab(1050,1380);
        enemies.add(eneme);


    }
    public void addStairs(int startX, int startY, int width, int height, int plusX, int plusY, int count){
        for(int i = 0;i<count;i++){
            Wall walle = new Wall(startX+plusX*i,startY+plusY*i,width,height-plusY*i);
            walls.add(walle);
        }
    }

    // Zombie Methods
    public void powerUp(int x, int y, int p)
    {
        zpx = x;
        zpy = y;
        zpower = new Rectangle(x,y,15,15);
        zP = p;
    }

    public void spawnEnemies()
    {
        zomSpawn();
        shootSpawn();
        suicSpawn();
        strongSpawn();
        ztotalCount++;
        zecount2++;
        zecount3++;
        zecount4++;
        zecount5++;
    }

    public void zomSpawn()
    {
        int r = (int)(Math.random()*4);
        if(ztotalCount<2000 && zecount2 == 50)
        {
            if(r == 0)
            {
                Zombie bill = new Zombie((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 1)
            {
                Zombie bill = new Zombie((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 2)
            {
                Zombie bill = new Zombie(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 3)
            {
                Zombie bill = new Zombie(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount2 = 0;
            }
        }
        else if(ztotalCount>=2000 && zecount2 >= 40)
        {
            if(r == 0)
            {
                Zombie bill = new Zombie((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 1)
            {
                Zombie bill = new Zombie((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 2)
            {
                Zombie bill = new Zombie(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 3)
            {
                Zombie bill = new Zombie(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount2 = 0;
            }
        }
        else if(ztotalCount>=4000 && zecount2 >=25)
        {
            if(r == 0)
            {
                Zombie bill = new Zombie((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 1)
            {
                Zombie bill = new Zombie((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 2)
            {
                Zombie bill = new Zombie(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 3)
            {
                Zombie bill = new Zombie(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount2 = 0;
            }
        }
        else if(ztotalCount>=6000 && zecount2 >=15)
        {
            if(r == 0)
            {
                Zombie bill = new Zombie((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 1)
            {
                Zombie bill = new Zombie((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 2)
            {
                Zombie bill = new Zombie(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount2 = 0;
            }
            else if(r == 3)
            {
                Zombie bill = new Zombie(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount2 = 0;
            }
        }
    }

    public void shootSpawn()
    {
        int r = (int)(Math.random()*4);
        if(ztotalCount<2000 && zecount3 >=500)
        {
            if(r == 0)
            {
                zomShoot bill = new zomShoot((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 1)
            {
                zomShoot bill = new zomShoot((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 2)
            {
                zomShoot bill = new zomShoot(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 3)
            {
                zomShoot bill = new zomShoot(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount3 = 0;
            }
        }
        else if(ztotalCount>=2000 && zecount3 >=300)
        {
            if(r == 0)
            {
                zomShoot bill = new zomShoot((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 1)
            {
                zomShoot bill = new zomShoot((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 2)
            {
                zomShoot bill = new zomShoot(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 3)
            {
                zomShoot bill = new zomShoot(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount3 = 0;
            }
        }
        else if(ztotalCount>=4000 && zecount3 >=120)
        {
            if(r == 0)
            {
                zomShoot bill = new zomShoot((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 1)
            {
                zomShoot bill = new zomShoot((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 2)
            {
                zomShoot bill = new zomShoot(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 3)
            {
                zomShoot bill = new zomShoot(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount3 = 0;
            }
        }
        else if(ztotalCount>=6000 && zecount3 >=80)
        {
            if(r == 0)
            {
                zomShoot bill = new zomShoot((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 1)
            {
                zomShoot bill = new zomShoot((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 2)
            {
                zomShoot bill = new zomShoot(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount3 = 0;
            }
            else if(r == 3)
            {
                zomShoot bill = new zomShoot(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount3 = 0;
            }
        }
    }

    public void suicSpawn()
    {
        int r = (int)(Math.random()*4);
        if(ztotalCount<2000 && zecount4 >= 300)
        {
            if(r == 0)
            {
                BasicEnemy bill = new BasicEnemy((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 1)
            {
                BasicEnemy bill = new BasicEnemy((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 2)
            {
                BasicEnemy bill = new BasicEnemy(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 3)
            {
                BasicEnemy bill = new BasicEnemy(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount4 = 0;
            }
        }
        else if(ztotalCount>=2000  && ztotalCount<=4000 && zecount4 >=200)
        {
            if(r == 0)
            {
                BasicEnemy bill = new BasicEnemy((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 1)
            {
                BasicEnemy bill = new BasicEnemy((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 2)
            {
                BasicEnemy bill = new BasicEnemy(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 3)
            {
                BasicEnemy bill = new BasicEnemy(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount4 = 0;
            }
        }
        else if(ztotalCount>=4000 && zecount4 >= 80)
        {
            if(r == 0)
            {
                BasicEnemy bill = new BasicEnemy((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 1)
            {
                BasicEnemy bill = new BasicEnemy((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 2)
            {
                BasicEnemy bill = new BasicEnemy(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 3)
            {
                BasicEnemy bill = new BasicEnemy(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount4 = 0;
            }
        }
        else if(ztotalCount>=6000 && zecount4 >= 60)
        {
            if(r == 0)
            {
                BasicEnemy bill = new BasicEnemy((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 1)
            {
                BasicEnemy bill = new BasicEnemy((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 2)
            {
                BasicEnemy bill = new BasicEnemy(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount4 = 0;
            }
            else if(r == 3)
            {
                BasicEnemy bill = new BasicEnemy(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount4 = 0;
            }
        }
    }

    public void strongSpawn()
    {
        int r = (int)(Math.random()*4);
        if(ztotalCount<2000 && zecount5 >= 500)
        {
            if(r == 0)
            {
                strongZom bill = new strongZom((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 1)
            {
                strongZom bill = new strongZom((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 2)
            {
                strongZom bill = new strongZom(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 3)
            {
                strongZom bill = new strongZom(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount5 = 0;
            }
        }
        else if(ztotalCount>=2000 && zecount5 >= 300)
        {
            if(r == 0)
            {
                strongZom bill = new strongZom((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 1)
            {
                strongZom bill = new strongZom((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 2)
            {
                strongZom bill = new strongZom(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 3)
            {
                strongZom bill = new strongZom(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount5 = 0;
            }
        }
        else if(ztotalCount>=4000 && zecount5 >= 150)
        {
            if(r == 0)
            {
                strongZom bill = new strongZom((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 1)
            {
                strongZom bill = new strongZom((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 2)
            {
                strongZom bill = new strongZom(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 3)
            {
                strongZom bill = new strongZom(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount5 = 0;
            }
        }
        else if(ztotalCount>=6000 && zecount5 >= 120)
        {
            if(r == 0)
            {
                strongZom bill = new strongZom((int)(Math.random()*700),30);
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 1)
            {
                strongZom bill = new strongZom((int)(Math.random()*700),705);
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 2)
            {
                strongZom bill = new strongZom(-10,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount5 = 0;
            }
            else if(r == 3)
            {
                strongZom bill = new strongZom(805,(int)(Math.random()*700));
                zenemies.add(bill);
                zecount5 = 0;
            }
        }
    }

    public void changeGun()
    {
        if(ze2 == true)
        {
            if(zcurrent != zgun.length-1)
                zcurrent++;
            else
                zcurrent = 0;
            while(zgun[zcurrent].getAmmo() == 0)
            {
                if(zcurrent != zgun.length-1)
                    zcurrent++;
                else
                    zcurrent = 0;
            }
            ze2 = false;
        }
        if(zq == true)
        {
            if(zcurrent!=0)
                zcurrent--;
            else
                zcurrent = zgun.length-1;  
            while(zgun[zcurrent].getAmmo() == 0)
            {
                if(zcurrent!=0)
                    zcurrent--;
                else
                    zcurrent = zgun.length-1;
            }
            zq = false;
        }
    }

    public void mouseDragged(MouseEvent e){zmouseX = e.getX(); zmouseY = e.getY();
        if(zbob.getDead() == true)repaint();}

    public void mouseMoved(MouseEvent e) {zmouseX = e.getX(); zmouseY = e.getY(); 
        if(zbob.getDead() == true)repaint();}

    public void mouseClicked(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) { zmouseP = false;}

    public void mousePressed(MouseEvent e) { zmouseP = true;}

    public void mouseEntered(MouseEvent e) { if(zbob.getDead() == false)t.start(); zmouse = true;}

    public void mouseExited(MouseEvent e) { t.stop(); zmouse = false; repaint();}

    // For Windows Double buffering
    public void run()
    { 
        if(isWindows()){
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            while (true)
            {
                repaint();
                try
                {   // Stop thread for 20 milliseconds
                    Thread.sleep (20);  //throws an InterruptedException, so has be be in a try...
                }
                catch (InterruptedException ex){}
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            } 
        }
    }

    public void update (Graphics g)
    {
        if(isWindows()){
            if (dbImage == null)
            {
                dbImage = createImage (this.getSize().width, this.getSize().height);
                dbg = dbImage.getGraphics ();

            }
            // clear screen in background
            dbg.setColor (getBackground ());
            dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

            // draw elements in background
            dbg.setColor (getForeground());
            paint (dbg);

            // draw image on the screen
            g.drawImage (dbImage, 0, 0, this);
        }
    }

    private Image dbImage;
    private Graphics dbg;

    private String OS = null;

    public String getOsName(){
        if(OS == null)
            OS = System.getProperty("os.name");
        return OS;
    }

    public boolean isWindows(){
        return getOsName().startsWith("Windows");
    }
}