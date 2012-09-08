import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Flames extends RainbowGun
{ 
    private String gunName;
    private Color c;
    private int ammo;
    private ArrayList<ZBullet> bullet;

    public Flames(ArrayList<ZBullet> bullets)
    {  
        super(bullets);
        gunName = "Fire Flames of Redness";
        bullet = new ArrayList<ZBullet>();
        bullet = bullets;
        ammo = 0;
    }

    public void bulletCreator(int x, int y, double mouseX, double mouseY, ArrayList<ZBullet> bullets)
    {
        bullet = bullets;
        int crand = (int)(Math.random()*2);
        if(crand == 0)
            c = Color.red;
        else if(crand == 1)
            c = Color.orange;
        double bobX = x - mouseX;
        double bobY = y - mouseY;
        int sinX = (int)(-15*bobX/Math.sqrt(bobX*bobX+bobY*bobY));
        int sinY = (int)(-15*bobY/Math.sqrt(bobX*bobX+bobY*bobY));
        int rand = (int)((Math.random()*8+1)-4);
        int firstX = 0;
        int firstY = 0;
        if(sinX != 0 && sinY !=0)
        {
            if(bobX/sinX > bobY/sinY)
            {
                firstX = (int)(bobX)%sinX;
                firstY = (int)bobY-((int)bobX/sinX)*sinY;
            }
            else
            {
                firstX = (int)bobX-((int)bobY/sinY)*sinX;
                firstY = (int)(bobY)%sinY;
            }
        }
        else
        {
            if(sinX == 0)
                firstX = (int)bobX;
            if(sinY == 0)
                firstY = (int)bobY;
        }
        ZBullet bull = new ZBullet(sinX+rand,sinY-rand,x-rand,y+rand,20,10,c,this,firstX,firstY);
        bullet.add(bull);
        rand = (int)((Math.random()*8+1)-4);
        ZBullet bulls = new ZBullet(sinX+rand,sinY-rand,x-rand,y+rand,20,10,c,this,firstX,firstY);
        bullet.add(bulls);
        ammo--;
    }

    public void addAmmo(int plus)
    {
        ammo+=plus;
    }

    public int getAmmo() {return ammo;}

    public ArrayList<ZBullet> getBullet(){return bullet;}

    public String getGun(){return gunName;}
}
