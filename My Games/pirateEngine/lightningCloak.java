import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class lightningCloak extends RainbowGun
{   
    private ArrayList<ZBullet> bullet;
    private int ammo;
    private String gunName;
    private Color c;

    public lightningCloak(ArrayList<ZBullet> bullets)
    {
        super(bullets);
        gunName = "Le Cloak that Shoots Lightning";
        bullet = new ArrayList<ZBullet>();
        bullet = bullets;
        ammo = 0;
    }

    public void bulletCreator(int x, int y, double mouseX, double mouseY, ArrayList<ZBullet> bullets)
    {
        bullet = bullets;
        for(int i = 0;i<5;i++)
        {
            double r = (Math.random());
            if(r <.5)
                c = Color.yellow;
            else
                c = new Color(168,219,206);
            double bobX = x - mouseX;
            double bobY = y - mouseY;
            int sinX = (int)(Math.random()*21-10);
            int sinY = (int)(Math.random()*21-10);
            ZBullet bull = new ZBullet(sinX,sinY,x,y,12,30,c,this,0,0);
            bullet.add(bull);
        }
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
