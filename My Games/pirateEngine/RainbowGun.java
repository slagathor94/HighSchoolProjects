import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class RainbowGun
{   
    private ArrayList<ZBullet> bullet;
    private String gunName;
    private int ammo,count;
    private Color c;

    public RainbowGun(ArrayList<ZBullet> bullets)
    {
        gunName = "Rainbow Machine Gun";
        bullet = new ArrayList<ZBullet>();
        bullet = bullets;
        count = 0;
        ammo = 1;
    }

    public void bulletCreator(int x, int y, double mouseX, double mouseY, ArrayList<ZBullet> bullets)
    {
        bullet = bullets;
        c = new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
        double bobX = x - mouseX;
        double bobY = y - mouseY;
        double sinX = -15.0*bobX/Math.sqrt(bobX*bobX+bobY*bobY);
        double sinY = -15.0*bobY/Math.sqrt(bobX*bobX+bobY*bobY);
        int firstX = 0;
        int firstY = 0;
        ZBullet bull = new ZBullet(sinX,sinY,x,y,150,6,c, this,-firstX,-firstY);
        bullet.add(bull);
        count = 0;
    }

    public void addAmmo(int plus)
    {
        ammo+=plus;
    }

    public int getAmmo() {return ammo;}

    public ArrayList<ZBullet> getBullet(){return bullet;}

    public String getGun(){return gunName;}
}
