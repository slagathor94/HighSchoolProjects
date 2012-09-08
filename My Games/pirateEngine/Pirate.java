import java.applet.*;
import java.awt.*;
import java.util.ArrayList;

public class Pirate {
    private int x,y,w,h,vx,vy,health,life,firstX,firstY,maxSpeed,animationCounter
    ,drawX,drawY,screenX,screenY,startX,startY;
    private Color c;
    private Rectangle box;
    private boolean die, jumping, right, shift, d, a, crouch, jetpack, gunVisual, swordVisual, trampoline;
    private ArrayList<Wall> walls;
    private ArrayList<Sword> sword;

    public Pirate(){ 
        d = false;
        a = false;
        jetpack = true;
        gunVisual = true;
        swordVisual = false;
        animationCounter = 1;
        maxSpeed = 6;
        x = 60;
        y = 60;
        startX = 60;
        startY = 60;
        screenX = 40;
        screenY = 15;
        firstX = 375;
        firstY = 375;
        h = 40;
        w = 30;
        box = new Rectangle(x,y,w,h);
        sword = new ArrayList<Sword>();
        health = 300;
        life = 3;
        jumping = false;
        right = true;
    }

    public void move(boolean slowA, boolean slowD, boolean ae, boolean de, boolean jump, ArrayList<Wall> wallss,
    boolean shifte, boolean control, ArrayList<Enemy> enemies, int levelX, int levelY, ArrayList<enemyCannon> ebullets){
        a = ae;
        d = de;
        firstX = x;
        firstY = y;
        walls = wallss;
        shift = shifte;
        crouch = control;
        int lasty = y;
        int wallVX = 0;
        int wallVY = 0;

        if(!jumping){
            if(control)
                maxSpeed = 3;
            else if(shift)
                maxSpeed = 10;
            else
                maxSpeed = 5;
        }

        if(control)
            h = 35;
        else
            h = 40;

        // Moves Character's X
        if(slowA== false || slowD == false)
        {
            if(slowA== false){
                if(a == true){
                    if(vx>-maxSpeed)
                        vx--;
                    if(vx<-maxSpeed)
                        vx = -maxSpeed;
                    right = false;
                }
            }
            if(slowD == false){
                if(d == true){
                    if(vx<maxSpeed)
                        vx++;
                    if(vx>maxSpeed)
                        vx = maxSpeed;
                    right = true;
                }
            }
        }
        else {
            // Slows down Character X
            if(vx>0)
                vx--;
            else if(vx<0)
                vx++;
        }

        // Jump Mechanic
        if(!jetpack){
            if(jump && !jumping){
                vy = -14;
                jumping = true ;
            }
        }
        else if(jetpack && jump){
            vy-=2;
        }

        // Makes sure character is in box;
        y+=vy;
        if(y <= 0){
            y=55;
            vy=y-lasty;
        }
        else if(y > (levelX-h)){
            die = true;
        }
        x+=vx;

        if(vy<15)
            vy++;

        if(vy<-5 && (!jumping && !trampoline))
            vy = -5;

        // Collision Testing
        for(int i = 0; i<enemies.size();i++){
            if(enemies.get(i).getBox().intersects(box)){
                damage(enemies.get(i).getDamage());
                if(enemies.get(i) instanceof Cannon){}
                else{
                    vy-=2;
                    if(x >= enemies.get(i).getX() + .5*enemies.get(i).getWidth())
                        vx+=3;
                    else
                        vx-=3;
                }
            }
        }

        for(int i = 0; i<ebullets.size();i++){
            if(ebullets.get(i).getBox().intersects(box)){
                damage(ebullets.get(i).getDamage());
                if(ebullets.get(i).getVX() > 0)
                    x+=6;
                else
                    x-=6;
                ebullets.get(i).makeRemoved();
            }
        }

        if(walls.size()>0)
        {
            for(int i = 0;i<walls.size();i++)
            {
                if(walls.get(i) instanceof FakeWall){

                }
                else if(walls.get(i) instanceof Spikes){
                    if(walls.get(i).getWall().intersects(box))
                        damage(health);
                }
                else if(walls.get(i) instanceof Trampoline){
                    if(bottomCollision(walls.get(i))){
                        trampoline = true;
                        vy = -vy - 5;
                        wallVY = walls.get(i).getVY();
                        wallVX = walls.get(i).getVX();
                    }
                }
                else if(walls.get(i) instanceof Platform){
                    if(!control){
                        if(bottomCollision(walls.get(i))){
                            trampoline = false;
                            vy = 0;
                            wallVY = walls.get(i).getVY();
                            wallVX = walls.get(i).getVX();
                        }
                    }
                }
                else{
                    if(topCollision(walls.get(i))){
                        y++;
                        vy = 0;
                    }
                    if(bottomCollision(walls.get(i))){
                        trampoline = false;
                        vy = 0;
                        wallVY = walls.get(i).getVY();
                        wallVX = walls.get(i).getVX();
                    }
                    if(rightCollision(walls.get(i)) || leftCollision(walls.get(i))){
                        vx = 0;
                    }
                }
            }
        }

        y+= wallVY;
        x+= wallVX;

        if(x < 385){
            screenX = 0;
            drawX = x;
        }
        else if(x > levelX - 315){
            screenX = levelX - 700;
            drawX = x + 385 - (levelX - 315);
        }
        else{
            screenX = x - 385;
            drawX = 385;
        }

        if(y > levelY - 440){
            screenY = levelY - 800;
            drawY = y - (levelY - 440) + 360;
        }
        else if(y < 360){
            screenY = 0;
            drawY = y;
        }
        else{
            screenY = y - 360;
            drawY = 360;
        }

        box.translate(x-firstX, y-firstY);
        firstX = x-firstX;
    }

    public boolean topCollision(Wall wal){
        if((x<wal.getX()+wal.getWidth()) && (x+w>wal.getX()) && (y<=wal.getY()+wal.getHeight()) 
        && (y>= wal.getY()+wal.getHeight()+vy-2) && vy<0){
            if(y<wal.getY()+wal.getHeight()){
                y = wal.getY()+wal.getHeight()+1;
            }
            return true;
        }
        else
            return false;
    }

    public boolean bottomCollision(Wall wal){
        if((x < wal.getX()+wal.getWidth()) && (x+w > wal.getX()) && (y+h >= wal.getY()) && ((y+h <= wal.getY()+vy+5
                && jumping)  || (y+h <= wal.getY()+15 && jumping == false)) && (vy>0 || (vy>=0 && jumping == false))){
            if(y+h>wal.getY()){
                y = wal.getY()-h;
            }
            jumping = false;
            return true;
        }
        else
            return false;
    }

    public boolean rightCollision(Wall wal){
        if((y<=wal.getY()+wal.getHeight()-3) && (y+h>=wal.getY()+3) && (x+w>=wal.getX())
        && (x+w<=wal.getX()+vx+maxSpeed)){
            x = wal.getX()-w;
            return true;
        }
        else
            return false;
    }

    public boolean leftCollision(Wall wal){
        if((y<=wal.getY()+wal.getHeight()-3) && (y+h>=wal.getY()+3) && (x<=wal.getX()+wal.getWidth())
        && (x>=wal.getX()+wal.getWidth()+vx-maxSpeed)){
            x = wal.getX()+wal.getWidth();
            return true;
        }
        else
            return false;
    }

    public void draw(Graphics g2){
        if(!a && !d)
            animationCounter = 0;
        if(crouch){
            if(right){
                rightCrouch(g2);
                if(animationCounter == 0 || animationCounter == 1 || animationCounter == 2 
                || animationCounter == 7 || animationCounter == 8 || animationCounter == 9)
                    drawRight1(g2);
                else if(animationCounter == 3 || animationCounter == 4 || animationCounter == 5 || animationCounter == 6)
                    drawRight2(g2);
                else
                    drawRight3(g2);
            }   
            else if(!right){
                leftCrouch(g2);
                if(animationCounter == 0 || animationCounter == 1 || animationCounter == 2 
                || animationCounter == 7 || animationCounter == 8 || animationCounter == 9)
                    drawLeft1(g2);
                else if(animationCounter == 3 || animationCounter == 4 || animationCounter == 5 || animationCounter == 6)
                    drawLeft2(g2);
                else
                    drawLeft3(g2);
            }
        }
        else if(jumping) {
            if(right){
                drawRight(g2);
                rightJump(g2);
            }
            else{
                drawLeft(g2);
                leftJump(g2);
            }
        }
        else {
            if(right){
                drawRight(g2);
                if(animationCounter == 0 || animationCounter == 1 || animationCounter == 2 
                || animationCounter == 7 || animationCounter == 8 || animationCounter == 9)
                    legsapartRight(g2);
                else 
                    legstogetherRight(g2);
            }   
            else{
                drawLeft(g2);
                if(animationCounter == 0 || animationCounter == 1 || animationCounter == 2 
                || animationCounter == 7 || animationCounter == 8 || animationCounter == 9)
                    legsapartLeft(g2);
                else 
                    legstogetherLeft(g2);
            }

            if(!shift){
                if(right){
                    drawRight(g2);
                    if(animationCounter == 0 || animationCounter == 1 || animationCounter == 2 
                    || animationCounter == 7 || animationCounter == 8 || animationCounter == 9)
                        drawRight1(g2);
                    else if(animationCounter == 3 || animationCounter == 4 || animationCounter == 5 || animationCounter == 6)
                        drawRight2(g2);
                    else
                        drawRight3(g2);
                }   
                else if(!right){
                    drawLeft(g2);
                    if(animationCounter == 0 || animationCounter == 1 || animationCounter == 2 
                    || animationCounter == 7 || animationCounter == 8 || animationCounter == 9)
                        drawLeft1(g2);
                    else if(animationCounter == 3 || animationCounter == 4 || animationCounter == 5 || animationCounter == 6)
                        drawLeft2(g2);
                    else
                        drawLeft3(g2);
                }
            }
            else if(shift)
            {
                if(right){
                    drawRight(g2);
                    if(animationCounter == 0 || animationCounter == 1 || animationCounter == 2 
                    || animationCounter == 7 || animationCounter == 8 || animationCounter == 9)
                        drawSprintRight1(g2);
                    else if(animationCounter == 3 || animationCounter == 4 || animationCounter == 5 || animationCounter == 6)
                        drawSprintRight2(g2);
                    else
                        drawSprintRight3(g2);
                }   
                else if(!right){
                    drawLeft(g2);
                    if(animationCounter == 0 || animationCounter == 1 || animationCounter == 2 
                    || animationCounter == 7 || animationCounter == 8 || animationCounter == 9)
                        drawSprintLeft1(g2);
                    else if(animationCounter == 3 || animationCounter == 4 || animationCounter == 5 || animationCounter == 6)
                        drawSprintLeft2(g2);
                    else
                        drawSprintLeft3(g2);
                }
            }
        }
        if(animationCounter < 13)
            animationCounter ++;
        else 
            animationCounter = 0;
    }

    public void drawLeft(Graphics g2){
        // Pirate Hat
        c = Color.red;
        g2.setColor(c);
        g2.fillOval(drawX+2, drawY, 24, 16);
        g2.setColor(Color.black);
        g2.drawOval(drawX+2, drawY, 24, 16);
        g2.setColor(c);
        int[] xA = new int[4];
        int[] yA = new int[4];
        xA[0] = drawX+27;
        xA[1] = drawX+27;
        xA[2] = drawX+29;
        xA[3] = drawX+29;
        yA[0] = drawY+8;
        yA[1] = drawY+6;
        yA[2] = drawY+4;
        yA[3] = drawY+6;
        g2.fillPolygon(xA, yA, 4);
        xA[0] = drawX+27;
        xA[1] = drawX+27;
        xA[2] = drawX+29;
        xA[3] = drawX+29;
        yA[0] = drawY+8;
        yA[1] = drawY+10;
        yA[2] = drawY+12;
        yA[3] = drawY+10;
        g2.fillPolygon(xA, yA, 4);

        // Face & Neck
        c = new Color(250,200,125);
        g2.setColor(c);
        g2.fillRect(drawX+2,drawY+8,24,14);
        g2.fillRect(drawX+12,drawY+22,2,2);
        g2.setColor(Color.black);
        g2.drawRect(drawX+2,drawY+8,24,14);

        c = new Color(156, 93, 82);
        g2.setColor(c);
        xA = new int[14];
        yA = new int[14];
        xA[0] = drawX+26;
        xA[1] = drawX+26;
        xA[2] = drawX+22;
        xA[3] = drawX+20;
        xA[4] = drawX+18;
        xA[5] = drawX+15;
        xA[6] = drawX+14;
        xA[7] = drawX+15;
        xA[8] = drawX+12;
        xA[9] = drawX+12;
        xA[10] = drawX+10;
        xA[11] = drawX+8;
        xA[12] = drawX+2;
        xA[13] = drawX+2;
        yA[0] = drawY+8;
        yA[1] = drawY+22;
        yA[2] = drawY+20;
        yA[3] = drawY+21;
        yA[4] = drawY+19;
        yA[5] = drawY+20;
        yA[6] = drawY+17;
        yA[7] = drawY+14;
        yA[8] = drawY+14;
        yA[9] = drawY+12;
        yA[10] = drawY+10;
        yA[11] = drawY+12;
        yA[12] = drawY+9;
        yA[13] = drawY+8;
        g2.fillPolygon(xA, yA, 14);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 14);

        // Eye
        g2.setColor(Color.white);
        g2.fillOval(drawX+5,drawY+12,6,6);
        g2.setColor(Color.blue);
        g2.fillOval(drawX+5,drawY+13,4,4);
        g2.setColor(Color.black);
        g2.drawOval(drawX+5,drawY+12,6,6);
        g2.drawLine(drawX+6, drawY+8, drawX+2, drawY+11);

        //Mouth
        g2.setColor(Color.black);
        g2.drawLine(drawX+2, drawY+20, drawX+8, drawY+20);

        //Shirt
        g2.fillRect(drawX+8, drawY+26, 10, 2);
        g2.fillRect(drawX+8, drawY+30, 10, 2);
        g2.setColor(Color.white);
        g2.fillRect(drawX+8, drawY+24, 10, 2);
        g2.fillRect(drawX+8, drawY+28, 10, 2);
    }

    public void drawRight(Graphics g2){
        // Pirate Hat
        c = Color.red;
        g2.setColor(c);
        g2.fillOval(drawX+4, drawY, 24, 16);
        g2.setColor(Color.black);
        g2.drawOval(drawX+4, drawY, 24, 16);
        g2.setColor(c);
        int[] xA = new int[4];
        int[] yA = new int[4];
        xA[0] = drawX+4;
        xA[1] = drawX+4;
        xA[2] = drawX+2;
        xA[3] = drawX+2;
        yA[0] = drawY+8;
        yA[1] = drawY+6;
        yA[2] = drawY+4;
        yA[3] = drawY+6;
        g2.fillPolygon(xA, yA, 4);
        xA[0] = drawX+4;
        xA[1] = drawX+4;
        xA[2] = drawX+2;
        xA[3] = drawX+2;
        yA[0] = drawY+8;
        yA[1] = drawY+10;
        yA[2] = drawY+12;
        yA[3] = drawY+10;
        g2.fillPolygon(xA, yA, 4);

        // Face & Neck
        c = new Color(250,200,125);
        g2.setColor(c);
        g2.fillRect(drawX+4,drawY+8,24,14);
        g2.fillRect(drawX+14,drawY+22,2,2);
        g2.setColor(Color.black);
        g2.drawRect(drawX+4,drawY+8,24,14);

        //Hair
        c = new Color(156, 93, 82);
        g2.setColor(c);
        xA = new int[14];
        yA = new int[14];
        xA[0] = drawX+4;
        xA[1] = drawX+4;
        xA[2] = drawX+8;
        xA[3] = drawX+10;
        xA[4] = drawX+12;
        xA[5] = drawX+15;
        xA[6] = drawX+16;
        xA[7] = drawX+15;
        xA[8] = drawX+18;
        xA[9] = drawX+18;
        xA[10] = drawX+20;
        xA[11] = drawX+22;
        xA[12] = drawX+28;
        xA[13] = drawX+28;
        yA[0] = drawY+8;
        yA[1] = drawY+22;
        yA[2] = drawY+20;
        yA[3] = drawY+21;
        yA[4] = drawY+19;
        yA[5] = drawY+20;
        yA[6] = drawY+17;
        yA[7] = drawY+14;
        yA[8] = drawY+14;
        yA[9] = drawY+12;
        yA[10] = drawY+10;
        yA[11] = drawY+12;
        yA[12] = drawY+9;
        yA[13] = drawY+8;
        g2.fillPolygon(xA, yA, 14);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 14);

        // Eye patch
        g2.setColor(Color.black);
        g2.fillOval(drawX+20,drawY+12,6,6);
        g2.drawLine(drawX+24, drawY+14, drawX+28, drawY+11);
        g2.drawLine(drawX+22, drawY+14, drawX+12, drawY+8);

        //Mouth
        g2.setColor(Color.black);
        g2.drawLine(drawX+22, drawY+20, drawX+28, drawY+20);

        //Shirt
        g2.fillRect(drawX+10, drawY+26, 10, 2);
        g2.fillRect(drawX+10, drawY+30, 10, 2);
        g2.setColor(Color.white);
        g2.fillRect(drawX+10, drawY+24, 10, 2);
        g2.fillRect(drawX+10, drawY+28, 10, 2);
    }

    public void legsapartLeft(Graphics g2){
        //Shorts
        c = new Color(156, 93, 82);
        g2.setColor(c);
        int[] xA = new int[7];
        int[] yA = new int[7];
        xA[0] = drawX+8;
        xA[1] = drawX+6;
        xA[2] = drawX+12;
        xA[3] = drawX+13;
        xA[4] = drawX+14;
        xA[5] = drawX+20;
        xA[6] = drawX+18;
        yA[0] = drawY+32;
        yA[1] = drawY+38;
        yA[2] = drawY+38;
        yA[3] = drawY+34;
        yA[4] = drawY+38;
        yA[5] = drawY+38;
        yA[6] = drawY+32;
        g2.fillPolygon(xA, yA, 7);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 7);

        //Shoes
        g2.setColor(Color.black);
        g2.fillRect(drawX+6, drawY+38, 6, 2);
        g2.fillRect(drawX+14, drawY+38, 6, 2);
    }

    public void legstogetherLeft(Graphics g2){
        //Shorts
        c = new Color(156, 93, 82);
        g2.setColor(c);
        int[] xA = new int[7];
        int[] yA = new int[7];
        xA[0] = drawX+8;
        xA[1] = drawX+8;
        xA[2] = drawX+13;
        xA[3] = drawX+13;
        xA[4] = drawX+13;
        xA[5] = drawX+18;
        xA[6] = drawX+18;
        yA[0] = drawY+32;
        yA[1] = drawY+38;
        yA[2] = drawY+38;
        yA[3] = drawY+34;
        yA[4] = drawY+38;
        yA[5] = drawY+38;
        yA[6] = drawY+32;
        g2.fillPolygon(xA, yA, 7);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 7);

        //Shoes
        g2.setColor(Color.black);
        g2.fillRect(drawX+8, drawY+38, 6, 2);
        g2.fillRect(drawX+13, drawY+38, 6, 2);
    }

    public void legsapartRight(Graphics g2){
        //Shorts
        c = new Color(156, 93, 82);
        g2.setColor(c);
        int[] xA = new int[7];
        int[] yA = new int[7];
        xA[0] = drawX+10;
        xA[1] = drawX+8;
        xA[2] = drawX+14;
        xA[3] = drawX+15;
        xA[4] = drawX+16;
        xA[5] = drawX+22;
        xA[6] = drawX+20;
        yA[0] = drawY+32;
        yA[1] = drawY+38;
        yA[2] = drawY+38;
        yA[3] = drawY+34;
        yA[4] = drawY+38;
        yA[5] = drawY+38;
        yA[6] = drawY+32;
        g2.fillPolygon(xA, yA, 7);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 7);

        //Shoes
        g2.setColor(Color.black);
        g2.fillRect(drawX+8, drawY+38, 6, 2);
        g2.fillRect(drawX+16, drawY+38, 6, 2);
    }

    public void legstogetherRight(Graphics g2){
        //Shorts
        c = new Color(156, 93, 82);
        g2.setColor(c);
        int[] xA = new int[7];
        int[] yA = new int[7];
        xA[0] = drawX+10;
        xA[1] = drawX+10;
        xA[2] = drawX+15;
        xA[3] = drawX+15;
        xA[4] = drawX+15;
        xA[5] = drawX+20;
        xA[6] = drawX+20;
        yA[0] = drawY+32;
        yA[1] = drawY+38;
        yA[2] = drawY+38;
        yA[3] = drawY+34;
        yA[4] = drawY+38;
        yA[5] = drawY+38;
        yA[6] = drawY+32;
        g2.fillPolygon(xA, yA, 7);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 7);

        //Shoes
        g2.setColor(Color.black);
        g2.fillRect(drawX+10, drawY+38, 6, 2);
        g2.fillRect(drawX+15, drawY+38, 6, 2);
    }

    public void drawLeft1(Graphics g2){ 
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[4];
        int[] yA = new int[4];
        xA[0] = drawX+11;
        xA[1] = drawX+11;
        xA[2] = drawX+15;
        xA[3] = drawX+15;
        yA[0] = drawY+25;
        yA[1] = drawY+33;
        yA[2] = drawY+33;
        yA[3] = drawY+25;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,4);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,4);
    }

    public void drawLeft2(Graphics g2){
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[4];
        int[] yA = new int[4];
        xA[0] = drawX+15;
        xA[1] = drawX+13;
        xA[2] = drawX+9;
        xA[3] = drawX+11;
        yA[0] = drawY+26;
        yA[1] = drawY+33;
        yA[2] = drawY+32;
        yA[3] = drawY+25;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,4);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,4);
    }

    public void drawLeft3(Graphics g2){
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[4];
        int[] yA = new int[4];
        xA[0] = drawX+11;
        xA[1] = drawX+13;
        xA[2] = drawX+17;
        xA[3] = drawX+15;
        yA[0] = drawY+26;
        yA[1] = drawY+33;
        yA[2] = drawY+32;
        yA[3] = drawY+25;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,4);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,4);
    }

    public void drawRight1(Graphics g2){
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[4];
        int[] yA = new int[4];
        xA[0] = drawX+13;
        xA[1] = drawX+13;
        xA[2] = drawX+17;
        xA[3] = drawX+17;
        yA[0] = drawY+25;
        yA[1] = drawY+33;
        yA[2] = drawY+33;
        yA[3] = drawY+25;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,4);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,4);
    }

    public void drawRight2(Graphics g2){
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[4];
        int[] yA = new int[4];
        xA[0] = drawX+13;
        xA[1] = drawX+15;
        xA[2] = drawX+19;
        xA[3] = drawX+17;
        yA[0] = drawY+26;
        yA[1] = drawY+33;
        yA[2] = drawY+32;
        yA[3] = drawY+25;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,4);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,4);
    }

    public void drawRight3(Graphics g2){
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[4];
        int[] yA = new int[4];
        xA[0] = drawX+17;
        xA[1] = drawX+15;
        xA[2] = drawX+11;
        xA[3] = drawX+13;
        yA[0] = drawY+26;
        yA[1] = drawY+33;
        yA[2] = drawY+32;
        yA[3] = drawY+25;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,4);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,4);
    }

    public void drawSprintRight1(Graphics g2){
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[6];
        int[] yA = new int[6];
        xA[0] = drawX+13;
        xA[1] = drawX+12;
        xA[2] = drawX+18;
        xA[3] = drawX+19;
        xA[4] = drawX+16;
        xA[5] = drawX+17;
        yA[0] = drawY+26;
        yA[1] = drawY+30;
        yA[2] = drawY+32;
        yA[3] = drawY+29;
        yA[4] = drawY+28;
        yA[5] = drawY+26;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,6);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,6);
    }

    public void drawSprintRight2(Graphics g2){
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[6];
        int[] yA = new int[6];
        xA[0] = drawX+13;
        xA[1] = drawX+14;
        xA[2] = drawX+20;
        xA[3] = drawX+21;
        xA[4] = drawX+18;
        xA[5] = drawX+17;
        yA[0] = drawY+26;
        yA[1] = drawY+30;
        yA[2] = drawY+32;
        yA[3] = drawY+29;
        yA[4] = drawY+28;
        yA[5] = drawY+25;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,6);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,6);
    }

    public void drawSprintRight3(Graphics g2){
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[6];
        int[] yA = new int[6];
        xA[0] = drawX+13;
        xA[1] = drawX+10;
        xA[2] = drawX+15;
        xA[3] = drawX+18;
        xA[4] = drawX+15;
        xA[5] = drawX+17;
        yA[0] = drawY+26;
        yA[1] = drawY+29;
        yA[2] = drawY+33;
        yA[3] = drawY+31;
        yA[4] = drawY+29;
        yA[5] = drawY+27;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,6);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,6);
    }

    public void drawSprintLeft1(Graphics g2){
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[6];
        int[] yA = new int[6];
        xA[0] = drawX+15;
        xA[1] = drawX+16;
        xA[2] = drawX+10;
        xA[3] = drawX+9;
        xA[4] = drawX+12;
        xA[5] = drawX+11;
        yA[0] = drawY+26;
        yA[1] = drawY+30;
        yA[2] = drawY+32;
        yA[3] = drawY+29;
        yA[4] = drawY+28;
        yA[5] = drawY+26;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,6);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,6);
    }

    public void drawSprintLeft2(Graphics g2){
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[6];
        int[] yA = new int[6];
        xA[0] = drawX+15;
        xA[1] = drawX+14;
        xA[2] = drawX+8;
        xA[3] = drawX+7;
        xA[4] = drawX+10;
        xA[5] = drawX+11;
        yA[0] = drawY+26;
        yA[1] = drawY+30;
        yA[2] = drawY+32;
        yA[3] = drawY+29;
        yA[4] = drawY+28;
        yA[5] = drawY+25;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,6);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,6);
    }

    public void drawSprintLeft3(Graphics g2){
        // Arms
        c = new Color(250,200,125);
        int[] xA = new int[6];
        int[] yA = new int[6];
        xA[0] = drawX+15;
        xA[1] = drawX+18;
        xA[2] = drawX+13;
        xA[3] = drawX+10;
        xA[4] = drawX+13;
        xA[5] = drawX+11;
        yA[0] = drawY+26;
        yA[1] = drawY+29;
        yA[2] = drawY+33;
        yA[3] = drawY+31;
        yA[4] = drawY+29;
        yA[5] = drawY+27;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,6);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,6);
    }

    public void rightJump(Graphics g2){
        //Shorts
        c = new Color(156, 93, 82);
        g2.setColor(c);
        int[] xA = new int[11];
        int[] yA = new int[11];
        xA[0] = drawX+10;
        xA[1] = drawX+11;
        xA[2] = drawX+8;
        xA[3] = drawX+14;
        xA[4] = drawX+16;
        xA[5] = drawX+15;
        xA[6] = drawX+16;
        xA[7] = drawX+14;
        xA[8] = drawX+20;
        xA[9] = drawX+21;
        xA[10] = drawX+20;
        yA[0] = drawY+32;
        yA[1] = drawY+35;
        yA[2] = drawY+38;
        yA[3] = drawY+38;
        yA[4] = drawY+35;
        yA[5] = drawY+34;
        yA[6] = drawY+35;
        yA[7] = drawY+38;
        yA[8] = drawY+38;
        yA[9] = drawY+35;
        yA[10] = drawY+32;
        g2.fillPolygon(xA, yA, 11);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 11);

        //Shoes
        g2.setColor(Color.black);
        g2.fillRect(drawX+8, drawY+38, 6, 2);
        g2.fillRect(drawX+14, drawY+38, 6, 2);

        // Arms
        c = new Color(250,200,125);
        xA = new int[4];
        yA = new int[4];
        xA[0] = drawX+13;
        xA[1] = drawX+15;
        xA[2] = drawX+19;
        xA[3] = drawX+17;
        yA[0] = drawY+26;
        yA[1] = drawY+19;
        yA[2] = drawY+19;
        yA[3] = drawY+26;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,4);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,4);
    }

    public void leftJump(Graphics g2){
        //Shorts
        c = new Color(156, 93, 82);
        g2.setColor(c);
        int[] xA = new int[11];
        int[] yA = new int[11];
        xA[0] = drawX+18;
        xA[1] = drawX+17;
        xA[2] = drawX+20;
        xA[3] = drawX+14;
        xA[4] = drawX+12;
        xA[5] = drawX+13;
        xA[6] = drawX+12;
        xA[7] = drawX+14;
        xA[8] = drawX+8;
        xA[9] = drawX+7;
        xA[10] = drawX+8;
        yA[0] = drawY+32;
        yA[1] = drawY+35;
        yA[2] = drawY+38;
        yA[3] = drawY+38;
        yA[4] = drawY+35;
        yA[5] = drawY+34;
        yA[6] = drawY+35;
        yA[7] = drawY+38;
        yA[8] = drawY+38;
        yA[9] = drawY+35;
        yA[10] = drawY+32;
        g2.fillPolygon(xA, yA, 11);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 11);

        //Shoes
        g2.setColor(Color.black);
        g2.fillRect(drawX+8, drawY+38, 6, 2);
        g2.fillRect(drawX+14, drawY+38, 6, 2);

        // Arms
        c = new Color(250,200,125);
        xA = new int[4];
        yA = new int[4];
        xA[0] = drawX+15;
        xA[1] = drawX+13;
        xA[2] = drawX+9;
        xA[3] = drawX+11;
        yA[0] = drawY+26;
        yA[1] = drawY+19;
        yA[2] = drawY+19;
        yA[3] = drawY+26;
        g2.setColor(c);
        g2.fillPolygon(xA,yA,4);
        g2.setColor(Color.black);
        g2.drawPolygon(xA,yA,4);
    }

    public void rightCrouch(Graphics g2){
        // Pirate Hat
        c = Color.red;
        g2.setColor(c);
        g2.fillOval(drawX+4, drawY, 24, 16);
        g2.setColor(Color.black);
        g2.drawOval(drawX+4, drawY, 24, 16);
        g2.setColor(c);
        int[] xA = new int[4];
        int[] yA = new int[4];
        xA[0] = drawX+4;
        xA[1] = drawX+4;
        xA[2] = drawX+2;
        xA[3] = drawX+2;
        yA[0] = drawY+8;
        yA[1] = drawY+6;
        yA[2] = drawY+4;
        yA[3] = drawY+6;
        g2.fillPolygon(xA, yA, 4);
        xA[0] = drawX+4;
        xA[1] = drawX+4;
        xA[2] = drawX+2;
        xA[3] = drawX+2;
        yA[0] = drawY+8;
        yA[1] = drawY+10;
        yA[2] = drawY+12;
        yA[3] = drawY+10;
        g2.fillPolygon(xA, yA, 4);

        // Face & Neck
        c = new Color(250,200,125);
        g2.setColor(c);
        g2.fillRect(drawX+4,drawY+8,24,14);
        g2.fillRect(drawX+14,drawY+22,2,2);
        g2.setColor(Color.black);
        g2.drawRect(drawX+4,drawY+8,24,14);

        //Hair
        c = new Color(156, 93, 82);
        g2.setColor(c);
        xA = new int[14];
        yA = new int[14];
        xA[0] = drawX+4;
        xA[1] = drawX+4;
        xA[2] = drawX+8;
        xA[3] = drawX+10;
        xA[4] = drawX+12;
        xA[5] = drawX+15;
        xA[6] = drawX+16;
        xA[7] = drawX+15;
        xA[8] = drawX+18;
        xA[9] = drawX+18;
        xA[10] = drawX+20;
        xA[11] = drawX+22;
        xA[12] = drawX+28;
        xA[13] = drawX+28;
        yA[0] = drawY+8;
        yA[1] = drawY+22;
        yA[2] = drawY+20;
        yA[3] = drawY+21;
        yA[4] = drawY+19;
        yA[5] = drawY+20;
        yA[6] = drawY+17;
        yA[7] = drawY+14;
        yA[8] = drawY+14;
        yA[9] = drawY+12;
        yA[10] = drawY+10;
        yA[11] = drawY+12;
        yA[12] = drawY+9;
        yA[13] = drawY+8;
        g2.fillPolygon(xA, yA, 14);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 14);

        // Eye patch
        g2.setColor(Color.black);
        g2.fillOval(drawX+20,drawY+12,6,6);
        g2.drawLine(drawX+24, drawY+14, drawX+28, drawY+11);
        g2.drawLine(drawX+22, drawY+14, drawX+12, drawY+8);

        //Mouth
        g2.setColor(Color.black);
        g2.drawLine(drawX+22, drawY+20, drawX+28, drawY+20);

        // Shirt
        g2.fillRect(drawX+8,drawY+22,12,2);
        g2.fillRect(drawX+8,drawY+26,12,2);
        g2.setColor(Color.white);
        g2.fillRect(drawX+8, drawY+24, 12, 2);
        g2.fillRect(drawX+8, drawY+28, 12, 2);
        g2.setColor(Color.black);
        g2.drawLine(drawX+8, drawY+22, drawX+8, drawY+30);
        g2.drawLine(drawX+20, drawY+22, drawX+20, drawY+30);

        // Shorts
        c = new Color(156, 93, 82);
        g2.setColor(c);
        xA = new int[7];
        yA = new int[7];
        xA[0] = drawX+8;
        xA[1] = drawX+8;
        xA[2] = drawX+14;
        xA[3] = drawX+14;
        xA[4] = drawX+14;
        xA[5] = drawX+20;
        xA[6] = drawX+20;
        yA[0] = drawY+30;
        yA[1] = drawY+33;
        yA[2] = drawY+33;
        yA[3] = drawY+31;
        yA[4] = drawY+33;
        yA[5] = drawY+33;
        yA[6] = drawY+30;
        g2.fillPolygon(xA, yA, 7);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 7);

        // Shoes
        g2.fillRect(drawX+8, drawY+33, 12, 2);
    }

    public void leftCrouch(Graphics g2){
        // Pirate Hat
        c = Color.red;
        g2.setColor(c);
        g2.fillOval(drawX+2, drawY, 24, 16);
        g2.setColor(Color.black);
        g2.drawOval(drawX+2, drawY, 24, 16);
        g2.setColor(c);
        int[] xA = new int[4];
        int[] yA = new int[4];
        xA[0] = drawX+27;
        xA[1] = drawX+27;
        xA[2] = drawX+29;
        xA[3] = drawX+29;
        yA[0] = drawY+8;
        yA[1] = drawY+6;
        yA[2] = drawY+4;
        yA[3] = drawY+6;
        g2.fillPolygon(xA, yA, 4);
        xA[0] = drawX+27;
        xA[1] = drawX+27;
        xA[2] = drawX+29;
        xA[3] = drawX+29;
        yA[0] = drawY+8;
        yA[1] = drawY+10;
        yA[2] = drawY+12;
        yA[3] = drawY+10;
        g2.fillPolygon(xA, yA, 4);

        // Face & Neck
        c = new Color(250,200,125);
        g2.setColor(c);
        g2.fillRect(drawX+2,drawY+8,24,14);
        g2.fillRect(drawX+12,drawY+22,2,2);
        g2.setColor(Color.black);
        g2.drawRect(drawX+2,drawY+8,24,14);

        c = new Color(156, 93, 82);
        g2.setColor(c);
        xA = new int[14];
        yA = new int[14];
        xA[0] = drawX+26;
        xA[1] = drawX+26;
        xA[2] = drawX+22;
        xA[3] = drawX+20;
        xA[4] = drawX+18;
        xA[5] = drawX+15;
        xA[6] = drawX+14;
        xA[7] = drawX+15;
        xA[8] = drawX+12;
        xA[9] = drawX+12;
        xA[10] = drawX+10;
        xA[11] = drawX+8;
        xA[12] = drawX+2;
        xA[13] = drawX+2;
        yA[0] = drawY+8;
        yA[1] = drawY+22;
        yA[2] = drawY+20;
        yA[3] = drawY+21;
        yA[4] = drawY+19;
        yA[5] = drawY+20;
        yA[6] = drawY+17;
        yA[7] = drawY+14;
        yA[8] = drawY+14;
        yA[9] = drawY+12;
        yA[10] = drawY+10;
        yA[11] = drawY+12;
        yA[12] = drawY+9;
        yA[13] = drawY+8;
        g2.fillPolygon(xA, yA, 14);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 14);

        // Eye
        g2.setColor(Color.white);
        g2.fillOval(drawX+5,drawY+12,6,6);
        g2.setColor(Color.blue);
        g2.fillOval(drawX+5,drawY+13,4,4);
        g2.setColor(Color.black);
        g2.drawOval(drawX+5,drawY+12,6,6);
        g2.drawLine(drawX+6, drawY+8, drawX+2, drawY+11);

        //Mouth
        g2.setColor(Color.black);
        g2.drawLine(drawX+2, drawY+20, drawX+8, drawY+20);

        // Shirt
        g2.fillRect(drawX+8,drawY+22,12,2);
        g2.fillRect(drawX+8,drawY+26,12,2);
        g2.setColor(Color.white);
        g2.fillRect(drawX+8, drawY+24, 12, 2);
        g2.fillRect(drawX+8, drawY+28, 12, 2);
        g2.setColor(Color.black);
        g2.drawLine(drawX+8, drawY+22, drawX+8, drawY+30);
        g2.drawLine(drawX+20, drawY+22, drawX+20, drawY+30);

        // Shorts
        c = new Color(156, 93, 82);
        g2.setColor(c);
        xA = new int[7];
        yA = new int[7];
        xA[0] = drawX+8;
        xA[1] = drawX+8;
        xA[2] = drawX+14;
        xA[3] = drawX+14;
        xA[4] = drawX+14;
        xA[5] = drawX+20;
        xA[6] = drawX+20;
        yA[0] = drawY+30;
        yA[1] = drawY+33;
        yA[2] = drawY+33;
        yA[3] = drawY+31;
        yA[4] = drawY+33;
        yA[5] = drawY+33;
        yA[6] = drawY+30;
        g2.fillPolygon(xA, yA, 7);
        g2.setColor(Color.black);
        g2.drawPolygon(xA, yA, 7);

        // Shoes
        g2.fillRect(drawX+8, drawY+33, 12, 2);
    }

    public void drawGun(Graphics g){
        if(gunVisual = true){
            int[] gunX = new int[10];
            int[] gunY = new int[10];

            gunX[0] = drawX+22;
            gunX[1] = drawX+31;
            gunX[2] = drawX+31;
            gunX[3] = drawX+33;
            gunX[4] = drawX+33;
            gunX[5] = drawX+31;
            gunX[6] = drawX+31;
            gunX[7] = drawX+26;
            gunX[8] = drawX+26;
            gunX[9] = drawX+22;
            gunY[0] = drawY+22;
            gunY[1] = drawY+22;
            gunY[2] = drawY+23;
            gunY[3] = drawY+23;
            gunY[4] = drawY+24;
            gunY[5] = drawY+24;
            gunY[6] = drawY+25;
            gunY[7] = drawY+25;
            gunY[8] = drawY+28;
            gunY[9] = drawY+28;

            g.setColor(Color.orange);
            g.fillPolygon(gunX,gunY,10);
            g.setColor(Color.black);
            g.drawPolygon(gunX,gunY,10);
        }
    }

    public void drawSword(Graphics g){

    }

    public void death(Graphics g2){
        g2.setColor(Color.red);
        g2.drawString("You dead son, Press Enter to Reload the Game and Try Again", 250,375);
    }

    public void damage(int d) {
        health-=d;
        if(health<=0){
            life--;
            if(life==0){
                die = true;
                health = 0;
            }
            else{
                health = 300;
                x = startX;
                y = startY;
            }
        }
        else if(health>300){
            health = health%300;
            life++;
        }
    }

    public void changeStart(int changeX, int changeY){x = changeX;y = changeY;}
    public void gunTrue(){gunVisual = true; swordVisual = false;}
    public void swordTrue(){gunVisual = false; swordVisual = true;}
    public int getScrollX(){return screenX;}
    public int getScrollY(){return screenY;}
    public void screenSlide(int bobX){x+=bobX; box.translate(bobX,0);}
    public boolean isRight() {return right;}
    public boolean getDead() {return die;}
    public int getLife() {return life;}
    public int getfirstX() {return firstX;}
    public int getX() {return x;}
    public int getY() {return y;}
    public int getVX() {return vx;}
    public int getVY() {return vy;}
    public int getCX() {return x+15;}
    public int getCY() {return y+20;}
    public int getH() {return health;}
    public Rectangle getBox() {return box;}
}