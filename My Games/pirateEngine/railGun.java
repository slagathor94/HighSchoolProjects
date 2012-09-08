import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class railGun extends RainbowGun
{   
    private ArrayList<ZBullet> bullet;
    private int ammo;
    private String gunName;
    private Color c;

    public railGun(ArrayList<ZBullet> bullets)
    {
        super(bullets);
        gunName = "Blue LAZER OF DOOM";
        bullet = new ArrayList<ZBullet>();
        bullet = bullets;
        ammo = 0;
    }

    public void bulletCreator(int x, int y, double mouseX, double mouseY, ArrayList<ZBullet> bullets)
    {
        bullet = bullets;
        for(int i = 0;i<100;i++)
        {
            c = Color.blue;
            double bobX = x - mouseX;
            double bobY = y - mouseY;
            int sinX = (int)(-10*bobX/Math.sqrt(bobX*bobX+bobY*bobY));
            int sinY = (int)(-10*bobY/Math.sqrt(bobX*bobX+bobY*bobY));
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
            ZBullet bull = new ZBullet(sinX,sinY,x+sinX*i-firstX,y+sinY*i-firstY,1,100,c,this,-firstX,-firstY);
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
