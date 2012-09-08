import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class singularityGun extends RainbowGun
{   
    private ArrayList<ZBullet> bullet;
    private int ammo;
    private String gunName;
    private Color c;

    public singularityGun(ArrayList<ZBullet> bullets)
    {
        super(bullets);
        gunName = "Reverse Purpley Black-Holish Gun";
        bullet = new ArrayList<ZBullet>();
        bullet = bullets;
        ammo = 0;
    }

    public void bulletCreator(int x, int y, double mouseX, double mouseY, ArrayList<ZBullet> bullets)
    {
        bullet = bullets;
        for(int i = 0;i<150;i++)
        {
            double r = (Math.random());
            if(r <.33333)
                c = new Color(170,64,151);
            else if(r <.66666)
                c = new Color(186,97,191);
            else
                c = new Color(144,86,135);
            double bobX = x - mouseX;
            double bobY = y - mouseY;
            int sinX = (int)(Math.random()*15-8);
            int sinY = (int)(Math.random()*15-8);
            ZBullet bull = new ZBullet(sinX,sinY,(int)mouseX,(int)mouseY,30,10,c,this,0,0);
            bullet.add(bull);
        }
        ammo --;
    }

    public void addAmmo(int plus)
    {
        ammo+=plus;
    }

    public int getAmmo() {return ammo;}

    public ArrayList<ZBullet> getBullet(){return bullet;}

    public String getGun(){return gunName;}
}
