import java.awt.*;

public class MainMenu
{
    private Button[][] buttons;
    private int x, y, width, height, columnIndex, rowIndex, gameState, ty, tx;
    private int[] zstarsx,zstarsy,zstarsd;
    private Color c;

    public MainMenu(int xx, int yy, int w, int h){
        x = xx;
        y = yy;
        tx = 225;
        ty = 115;
        width = w;
        height = h;
        buttons = new Button[2][2];
        buttonAdder();

        zstarsx = new int[65];
        zstarsy = new int[65];
        zstarsd = new int[65];
        for(int i = 0;i<zstarsx.length;i++)
        {
            zstarsx[i] = (int)(Math.random()*260+tx+45);
            zstarsy[i] = (int)(Math.random()*90+ty+140);
            zstarsd[i] = (int)(Math.random()*3+2);
        }
    }

    public void buttonAdder(){
        Button button = new Button(300,400,50,50,1);
        buttons[0][0] = button;

        button = new Button(300,480,50,50,3);
        buttons[1][0] = button;

        //         button = new Button(300,560,50,50,1);
        //         buttons[2][0] = button;

        button = new Button(450,400,50,50,2);
        buttons[0][1] = button;

        button = new Button(450,480,50,50,4);
        buttons[1][1] = button;

        //         button = new Button(450,560,50,50,2);
        //         buttons[2][1] = button;
    }

    public void mover(boolean up, boolean down, boolean left, boolean right, boolean enter){
        if(down){
            rowIndex++;
            rowIndex %= buttons.length;
        }

        if(up){
            if(rowIndex != 0)
                rowIndex--;
            else
                rowIndex = 1;
        }

        if(left || right){
            if(columnIndex == 0)
                columnIndex = 1;
            else
                columnIndex = 0;
        }

        if(enter){
            gameState = buttons[rowIndex][columnIndex].getGameState();
        }
    }

    public void draw(Graphics g){
        c = new Color(56,47,20);

        // Base of Menu
        if(columnIndex == 0){
            int columns = 12;
            int rows = 25;
            int w = width/columns;
            int h = height/rows;
            for(int i = 0; i <= columns;i++){
                for(int k = 0; k <= rows; k++){
                    if(k%2 == 0){
                        g.setColor(c);
                        g.fillRect(i*w,k*h,w,h);
                        g.setColor(Color.black);
                        g.drawRect(i*w,k*h,w,h);
                    }
                    else{
                        g.setColor(c);
                        g.fillRect(i*w-w/2,k*h,w,h);
                        g.setColor(Color.black);
                        g.drawRect(i*w-w/2,k*h,w,h);
                    }
                }
            }

            //Draws the title
            drawPirateTitle(g);
        }
        else{
            // Base
            c = new Color(191,191,191);
            g.setColor(c);
            g.fillRect(0,0,width,height);
            g.setColor(Color.gray);
            g.drawRect(20,20,width-40,height-40);
            g.fillOval(3,3,14,14);
            g.fillOval(163,3,14,14);
            g.fillOval(323,3,14,14);
            g.fillOval(483,3,14,14);
            g.fillOval(643,3,14,14);

            g.fillOval(3,138,14,14);
            g.fillOval(3,273,14,14);
            g.fillOval(3,408,14,14);
            g.fillOval(3,543,14,14);

            g.fillOval(width-17,height-17,14,14);

            g.fillOval(width-17,3,14,14);
            g.fillOval(width-17,138,14,14);
            g.fillOval(width-17,273,14,14);
            g.fillOval(width-17,408,14,14);
            g.fillOval(width-17,543,14,14);

            g.fillOval(3,height-17,14,14);
            g.fillOval(163,height-17,14,14);
            g.fillOval(323,height-17,14,14);
            g.fillOval(483,height-17,14,14);
            g.fillOval(643,height-17,14,14);

            // Title
            drawZombieTitle(g);
        }

        // Text
        if(columnIndex == 0){
            g.setColor(Color.white);
            // Instructions
            g.drawString("A/LEFT = Move Left",40, 400);
            g.drawString("S/DOWN/CONTROL = Crouch",40, 420);
            g.drawString("D/RIGHT = Move Right",40, 440);
            g.drawString("W/UP = Jump",40, 460);
            g.drawString("SHIFT = Sprint",40, 480);
            g.drawString("C/K = Shoot Gun",40, 500);
            g.drawString("R = Reload Gun",40, 520);
            g.drawString("Lava WILL KILL YOU",40, 540);
            
            // Hints
            g.drawString("Some Platforms Move",570, 400);
            g.drawString("Dirt Platforms are Special",570, 420);
            g.drawString("You can jump through them AND",570, 440);
            g.drawString("Crouch on them to fall through",570, 460);
            g.drawString("Enemies throw you",570, 480);
            g.drawString("Your gun will auto reload",570, 500);
            g.drawString("You cannot shoot until ammo is full",570, 520);
            g.drawString("Get to the cave for the next level",570, 540);
        }
        else{
            g.setColor(Color.blue);
            // Instructions
            g.drawString("A = Move Left",40, 400);
            g.drawString("S = Move Down",40, 420);
            g.drawString("D = Move Right",40, 440);
            g.drawString("W = Move Up",40, 460);
            g.drawString("Q/E = Change Gun",40, 480);
            g.drawString("Click Mouse = Shoot",40, 500);
            
            // Hints
            g.drawString("Collect Colored Boxes",570, 400);
            g.drawString("They give ammo and health!",570, 420);
            g.drawString("KILL EVERYTHING",570, 440);
            g.drawString("Faster you kill",570, 460);
            g.drawString("More Points you get",570, 480);
            g.drawString("GET ALL POINTS!!!",570, 500);
        }
        g.drawString("Instructions:",40, 380);
        g.drawString("Hints:",570,380);
        g.drawString("Play", 390, 430);
        g.drawString("New Game", 370, 510);
        g.drawString("Pressing Escape in-game will bring up this menu", 260, 600);

        // Draws the buttons
        buttons[rowIndex][columnIndex].draw(g);

        // Arrow
        drawArrow(g);
        drawRestartArrow(g);
    }

    public void drawPirateTitle(Graphics g){
        int xLArray[] = new int[6];
        int yLArray[] = new int[6];
        xLArray[0] = tx;
        xLArray[1] = tx+20;
        xLArray[2] = tx+20;
        xLArray[3] = tx+60;
        xLArray[4] = tx+60;
        xLArray[5] = tx;
        yLArray[0] = ty;
        yLArray[1] = ty;
        yLArray[2] = ty+60;
        yLArray[3] = ty+60;
        yLArray[4] = ty+80;
        yLArray[5] = ty+80;
        g.setColor(Color.cyan);
        g.fillPolygon(xLArray, yLArray, 6);
        g.setColor(Color.black);
        g.drawPolygon(xLArray, yLArray, 6);

        int xAArray[] = new int[8];
        int yAArray[] = new int[8];
        xAArray[0] = tx+70;
        xAArray[1] = tx+110;
        xAArray[2] = tx+110;
        xAArray[3] = tx+100;
        xAArray[4] = tx+100;
        xAArray[5] = tx+100;
        xAArray[6] = tx+70;
        yAArray[0] = ty+50;
        yAArray[1] = ty+50;
        yAArray[2] = ty+80;
        yAArray[3] = ty+80;
        yAArray[4] = ty+70;
        yAArray[5] = ty+80;
        yAArray[6] = ty+80;
        g.setColor(Color.cyan);
        g.fillPolygon(xAArray, yAArray, 7);
        g.setColor(Color.black);
        g.drawPolygon(xAArray, yAArray, 7);

        g.setColor(c);
        g.fillRect(tx+80,ty+60,10,10);
        g.setColor(Color.black);
        g.drawRect(tx+80,ty+60,10,10);

        int xNArray[] = new int[8];
        int yNArray[] = new int[8];
        xNArray[0] = tx+120;
        xNArray[1] = tx+160;
        xNArray[2] = tx+160;
        xNArray[3] = tx+150;
        xNArray[4] = tx+150;
        xNArray[5] = tx+130;
        xNArray[6] = tx+130;
        xNArray[7] = tx+120;
        yNArray[0] = ty+50;
        yNArray[1] = ty+50;
        yNArray[2] = ty+80;
        yNArray[3] = ty+80;
        yNArray[4] = ty+70;
        yNArray[5] = ty+70;
        yNArray[6] = ty+80;
        yNArray[7] = ty+80;
        g.setColor(Color.cyan);
        g.fillPolygon(xNArray, yNArray, 8);
        g.setColor(Color.black);
        g.drawPolygon(xNArray, yNArray, 8);

        int xDArray[] = new int[6];
        int yDArray[] = new int[6];
        xDArray[0] = tx+190;
        xDArray[1] = tx+200;
        xDArray[2] = tx+200;
        xDArray[3] = tx+170;
        xDArray[4] = tx+170;
        xDArray[5] = tx+190;
        yDArray[0] = ty;
        yDArray[1] = ty;
        yDArray[2] = ty+80;
        yDArray[3] = ty+80;
        yDArray[4] = ty+50;
        yDArray[5] = ty+50;
        g.setColor(Color.cyan);
        g.fillPolygon(xDArray, yDArray, 6);
        g.setColor(Color.black);
        g.drawPolygon(xDArray, yDArray, 6);

        g.setColor(c);
        g.fillRect(tx+180,ty+60,10,10);
        g.setColor(Color.black);
        g.drawRect(tx+180,ty+60,10,10);

        c = new Color(193,161,79);
        int xHArray[] = new int[12];
        int yHArray[] = new int[12];
        xHArray[0] = tx+230;
        xHArray[1] = tx+250;
        xHArray[2] = tx+250;
        xHArray[3] = tx+270;
        xHArray[4] = tx+270;
        xHArray[5] = tx+290;
        xHArray[6] = tx+290;
        xHArray[7] = tx+270;
        xHArray[8] = tx+270;
        xHArray[9] = tx+250;
        xHArray[10] = tx+250;
        xHArray[11] = tx+230;
        yHArray[0] = ty+50;
        yHArray[1] = ty+50;
        yHArray[2] = ty+70;
        yHArray[3] = ty+70;
        yHArray[4] = ty+50;
        yHArray[5] = ty+50;
        yHArray[6] = ty+110;
        yHArray[7] = ty+110;
        yHArray[8] = ty+90;
        yHArray[9] = ty+90;
        yHArray[10] = ty+110;
        yHArray[11] = ty+110;
        g.setColor(c);
        g.fillPolygon(xHArray, yHArray, 12);
        g.setColor(Color.black);
        g.drawPolygon(xHArray, yHArray, 12);

        drawIsland(g);

        drawSpyGlass(g);
    }

    public void drawIsland(Graphics g){ 
        g.setColor(c);
        g.fillOval(tx+300,ty+80,50,30);
        g.setColor(Color.cyan);
        g.drawOval(tx+300,ty+80,50,30);

        c = new Color(140,107,30);

        int xTree1[] = new int[4];
        int yTree1[] = new int[4];
        xTree1[0] = tx+312;
        xTree1[1] = tx+321;
        xTree1[2] = tx+322;
        xTree1[3] = tx+312;
        yTree1[0] = ty+86;
        yTree1[1] = ty+86;
        yTree1[2] = ty+95;
        yTree1[3] = ty+95;
        g.setColor(c);
        g.fillPolygon(xTree1, yTree1, 4);
        g.setColor(Color.black);
        g.drawPolygon(xTree1, yTree1, 4);

        xTree1[0] = tx+312;
        xTree1[1] = tx+321;
        xTree1[2] = tx+317;
        xTree1[3] = tx+310;
        yTree1[0] = ty+86;
        yTree1[1] = ty+86;
        yTree1[2] = ty+78;
        yTree1[3] = ty+78;
        g.setColor(c);
        g.fillPolygon(xTree1, yTree1, 4);
        g.setColor(Color.black);
        g.drawPolygon(xTree1, yTree1, 4);

        xTree1[0] = tx+310;
        xTree1[1] = tx+317;
        xTree1[2] = tx+314;
        xTree1[3] = tx+308;
        yTree1[0] = ty+78;
        yTree1[1] = ty+78;
        yTree1[2] = ty+70;
        yTree1[3] = ty+70;
        g.setColor(c);
        g.fillPolygon(xTree1, yTree1, 4);
        g.setColor(Color.black);
        g.drawPolygon(xTree1, yTree1, 4);

        xTree1[0] = tx+308;
        xTree1[1] = tx+314;
        xTree1[2] = tx+310;
        xTree1[3] = tx+305;
        yTree1[0] = ty+70;
        yTree1[1] = ty+70;
        yTree1[2] = ty+62;
        yTree1[3] = ty+62;
        g.setColor(c);
        g.fillPolygon(xTree1, yTree1, 4);
        g.setColor(Color.black);
        g.drawPolygon(xTree1, yTree1, 4);

        int xTree2[] = new int[4];
        int yTree2[] = new int[4];
        xTree2[0] = tx+338;
        xTree2[1] = tx+329;
        xTree2[2] = tx+328;
        xTree2[3] = tx+338;
        yTree2[0] = ty+86;
        yTree2[1] = ty+86;
        yTree2[2] = ty+95;
        yTree2[3] = ty+95;
        g.setColor(c);
        g.fillPolygon(xTree2, yTree2, 4);
        g.setColor(Color.black);
        g.drawPolygon(xTree2, yTree2, 4);

        xTree2[0] = tx+338;
        xTree2[1] = tx+329;
        xTree2[2] = tx+332;
        xTree2[3] = tx+340;
        yTree2[0] = ty+86;
        yTree2[1] = ty+86;
        yTree2[2] = ty+78;
        yTree2[3] = ty+78;
        g.setColor(c);
        g.fillPolygon(xTree2, yTree2, 4);
        g.setColor(Color.black);
        g.drawPolygon(xTree2, yTree2, 4);

        xTree2[0] = tx+332;
        xTree2[1] = tx+340;
        xTree2[2] = tx+342;
        xTree2[3] = tx+336;
        yTree2[0] = ty+78;
        yTree2[1] = ty+78;
        yTree2[2] = ty+70;
        yTree2[3] = ty+70;
        g.setColor(c);
        g.fillPolygon(xTree2, yTree2, 4);
        g.setColor(Color.black);
        g.drawPolygon(xTree2, yTree2, 4);

        xTree2[0] = tx+342;
        xTree2[1] = tx+336;
        xTree2[2] = tx+341;
        xTree2[3] = tx+346;
        yTree2[0] = ty+70;
        yTree2[1] = ty+70;
        yTree2[2] = ty+62;
        yTree2[3] = ty+62;
        g.setColor(c);
        g.fillPolygon(xTree2, yTree2, 4);
        g.setColor(Color.black);
        g.drawPolygon(xTree2, yTree2, 4);

        c = new Color(15,126,6);
        int xLeaf1[] = new int[4];
        int yLeaf1[] = new int[4];
        xLeaf1[0] = tx+297;
        xLeaf1[1] = tx+305;
        xLeaf1[2] = tx+299;
        xLeaf1[3] = tx+296;
        yLeaf1[0] = ty+62;
        yLeaf1[1] = ty+60;
        yLeaf1[2] = ty+66;
        yLeaf1[3] = ty+70;
        g.setColor(Color.green);
        g.fillPolygon(xLeaf1, yLeaf1, 4);
        g.setColor(c);
        g.drawPolygon(xLeaf1, yLeaf1, 4);
        
        xLeaf1[0] = tx+306;
        xLeaf1[1] = tx+305;
        xLeaf1[2] = tx+301;
        xLeaf1[3] = tx+300;
        yLeaf1[0] = ty+61;
        yLeaf1[1] = ty+68;
        yLeaf1[2] = ty+72;
        yLeaf1[3] = ty+66;
        g.setColor(Color.green);
        g.fillPolygon(xLeaf1, yLeaf1, 4);
        g.setColor(c);
        g.drawPolygon(xLeaf1, yLeaf1, 4);
        
        xLeaf1[0] = tx+307;
        xLeaf1[1] = tx+310;
        xLeaf1[2] = tx+309;
        xLeaf1[3] = tx+306;
        yLeaf1[0] = ty+61;
        yLeaf1[1] = ty+67;
        yLeaf1[2] = ty+72;
        yLeaf1[3] = ty+68;
        g.setColor(Color.green);
        g.fillPolygon(xLeaf1, yLeaf1, 4);
        g.setColor(c);
        g.drawPolygon(xLeaf1, yLeaf1, 4);
        
        xLeaf1[0] = tx+309;
        xLeaf1[1] = tx+316;
        xLeaf1[2] = tx+316;
        xLeaf1[3] = tx+312;
        yLeaf1[0] = ty+61;
        yLeaf1[1] = ty+66;
        yLeaf1[2] = ty+71;
        yLeaf1[3] = ty+68;
        g.setColor(Color.green);
        g.fillPolygon(xLeaf1, yLeaf1, 4);
        g.setColor(c);
        g.drawPolygon(xLeaf1, yLeaf1, 4);
        
        xLeaf1[0] = tx+310;
        xLeaf1[1] = tx+318;
        xLeaf1[2] = tx+319;
        xLeaf1[3] = tx+316;
        yLeaf1[0] = ty+60;
        yLeaf1[1] = ty+62;
        yLeaf1[2] = ty+69;
        yLeaf1[3] = ty+65;
        g.setColor(Color.green);
        g.fillPolygon(xLeaf1, yLeaf1, 4);
        g.setColor(c);
        g.drawPolygon(xLeaf1, yLeaf1, 4);
        
        // Tree 2 Leaves
        xLeaf1[0] = tx+297+35;
        xLeaf1[1] = tx+305+35;
        xLeaf1[2] = tx+299+35;
        xLeaf1[3] = tx+296+35;
        yLeaf1[0] = ty+62;
        yLeaf1[1] = ty+60;
        yLeaf1[2] = ty+66;
        yLeaf1[3] = ty+70;
        g.setColor(Color.green);
        g.fillPolygon(xLeaf1, yLeaf1, 4);
        g.setColor(c);
        g.drawPolygon(xLeaf1, yLeaf1, 4);
        
        xLeaf1[0] = tx+306+35;
        xLeaf1[1] = tx+305+35;
        xLeaf1[2] = tx+301+35;
        xLeaf1[3] = tx+300+35;
        yLeaf1[0] = ty+61;
        yLeaf1[1] = ty+68;
        yLeaf1[2] = ty+72;
        yLeaf1[3] = ty+66;
        g.setColor(Color.green);
        g.fillPolygon(xLeaf1, yLeaf1, 4);
        g.setColor(c);
        g.drawPolygon(xLeaf1, yLeaf1, 4);
        
        xLeaf1[0] = tx+307+35;
        xLeaf1[1] = tx+310+35;
        xLeaf1[2] = tx+309+35;
        xLeaf1[3] = tx+306+35;
        yLeaf1[0] = ty+61;
        yLeaf1[1] = ty+67;
        yLeaf1[2] = ty+72;
        yLeaf1[3] = ty+68;
        g.setColor(Color.green);
        g.fillPolygon(xLeaf1, yLeaf1, 4);
        g.setColor(c);
        g.drawPolygon(xLeaf1, yLeaf1, 4);
        
        xLeaf1[0] = tx+309+35;
        xLeaf1[1] = tx+316+35;
        xLeaf1[2] = tx+316+35;
        xLeaf1[3] = tx+312+35;
        yLeaf1[0] = ty+61;
        yLeaf1[1] = ty+66;
        yLeaf1[2] = ty+71;
        yLeaf1[3] = ty+68;
        g.setColor(Color.green);
        g.fillPolygon(xLeaf1, yLeaf1, 4);
        g.setColor(c);
        g.drawPolygon(xLeaf1, yLeaf1, 4);
        
        xLeaf1[0] = tx+310+35;
        xLeaf1[1] = tx+318+35;
        xLeaf1[2] = tx+319+35;
        xLeaf1[3] = tx+316+35;
        yLeaf1[0] = ty+60;
        yLeaf1[1] = ty+62;
        yLeaf1[2] = ty+69;
        yLeaf1[3] = ty+65;
        g.setColor(Color.green);
        g.fillPolygon(xLeaf1, yLeaf1, 4);
        g.setColor(c);
        g.drawPolygon(xLeaf1, yLeaf1, 4);
    }

    public void drawSpyGlass(Graphics g){
        c = new Color(162,112,32);

        g.setColor(c);
        g.fillOval(tx+11,ty+150,30,30);
        g.setColor(Color.yellow);
        g.drawOval(tx+11,ty+150,30,30);

        int xPart1[] = new int[4];
        int yPart1[] = new int[4];
        xPart1[0] = tx+25;
        xPart1[1] = tx+90;
        xPart1[2] = tx+90;
        xPart1[3] = tx+25;
        yPart1[0] = ty+150;
        yPart1[1] = ty+145;
        yPart1[2] = ty+185;
        yPart1[3] = ty+181;
        g.setColor(c);
        g.fillPolygon(xPart1, yPart1, 4);
        g.setColor(Color.black);
        g.drawLine(tx+20,ty+150,tx+90,ty+145);
        g.drawLine(tx+20,ty+180,tx+90,ty+185);

        g.setColor(c);
        g.fillOval(tx+85,ty+140,20,50);
        g.setColor(Color.yellow);
        g.drawOval(tx+85,ty+140,20,50);

        int xPart2[] = new int[4];
        int yPart2[] = new int[4];
        xPart2[0] = tx+95;
        xPart2[1] = tx+165;
        xPart2[2] = tx+165;
        xPart2[3] = tx+95;
        yPart2[0] = ty+140;
        yPart2[1] = ty+135;
        yPart2[2] = ty+195;
        yPart2[3] = ty+190;
        g.setColor(c);
        g.fillPolygon(xPart2, yPart2, 4);
        g.setColor(Color.black);
        g.drawLine(tx+95,ty+140,tx+165,ty+135);
        g.drawLine(tx+95,ty+190,tx+165,ty+195);

        g.setColor(c);
        g.fillOval(tx+155,ty+130,30,70);
        g.setColor(Color.yellow);
        g.drawOval(tx+155,ty+130,30,70);

        int xPart3[] = new int[4];
        int yPart3[] = new int[4];
        xPart3[0] = tx+170;
        xPart3[1] = tx+270;
        xPart3[2] = tx+270;
        xPart3[3] = tx+170;
        yPart3[0] = ty+130;
        yPart3[1] = ty+120;
        yPart3[2] = ty+210;
        yPart3[3] = ty+200;
        g.setColor(c);
        g.fillPolygon(xPart3, yPart3, 4);
        g.setColor(Color.black);
        g.drawLine(tx+170,ty+130,tx+270,ty+120);
        g.drawLine(tx+170,ty+200,tx+270,ty+210);

        g.setColor(Color.yellow);
        g.fillOval(tx+240,ty+120,70,90);
        g.setColor(Color.white);
        g.fillOval(tx+245,ty+125,60,80);
        g.setColor(Color.black);
        g.drawOval(tx+245,ty+125,60,80);
        g.drawOval(tx+240,ty+120,70,90);
    }

    public void drawZombieTitle(Graphics g){
        int xZArray[] = new int[10];
        int yZArray[] = new int[10];
        xZArray[0] = tx;
        xZArray[1] = tx+70;
        xZArray[2] = tx+70;
        xZArray[3] = tx+20;
        xZArray[4] = tx+70;
        xZArray[5] = tx+70;
        xZArray[6] = tx;
        xZArray[7] = tx;
        xZArray[8] = tx+50;
        xZArray[9] = tx;
        yZArray[0] = ty;
        yZArray[1] = ty;
        yZArray[2] = ty+10;
        yZArray[3] = ty+60;
        yZArray[4] = ty+60;
        yZArray[5] = ty+70;
        yZArray[6] = ty+70;
        yZArray[7] = ty+60;
        yZArray[8] = ty+10;
        yZArray[9] = ty+10;
        g.setColor(Color.red);
        g.fillPolygon(xZArray, yZArray, 10);
        g.setColor(Color.black);
        g.drawPolygon(xZArray, yZArray, 10);

        g.setColor(Color.red);
        g.fillRect(tx+80,ty+30,40,40);
        g.setColor(Color.black);
        g.drawRect(tx+80,ty+30,40,40);
        drawZombie1(g);

        int xMArray[] = new int[10];
        int yMArray[] = new int[10];
        xMArray[0] = tx+130;
        xMArray[1] = tx+190;
        xMArray[2] = tx+190;
        xMArray[3] = tx+170;
        xMArray[4] = tx+170;
        xMArray[5] = tx+170;
        xMArray[6] = tx+150;
        xMArray[7] = tx+150;
        xMArray[8] = tx+150;
        xMArray[9] = tx+130;
        yMArray[0] = ty+30;
        yMArray[1] = ty+30;
        yMArray[2] = ty+70;
        yMArray[3] = ty+70;
        yMArray[4] = ty+50;
        yMArray[5] = ty+70;
        yMArray[6] = ty+70;
        yMArray[7] = ty+50;
        yMArray[8] = ty+70;
        yMArray[9] = ty+70;
        g.setColor(Color.red);
        g.fillPolygon(xMArray, yMArray, 10);
        g.setColor(Color.black);
        g.drawPolygon(xMArray, yMArray, 10);

        int xBArray[] = new int[6];
        int yBArray[] = new int[6];
        xBArray[0] = tx+200;
        xBArray[1] = tx+210;
        xBArray[2] = tx+210;
        xBArray[3] = tx+230;
        xBArray[4] = tx+230;
        xBArray[5] = tx+200;
        yBArray[0] = ty;
        yBArray[1] = ty;
        yBArray[2] = ty+40;
        yBArray[3] = ty+40;
        yBArray[4] = ty+70;
        yBArray[5] = ty+70;
        g.setColor(Color.red);
        g.fillPolygon(xBArray, yBArray, 6);
        g.setColor(Color.black);
        g.drawPolygon(xBArray, yBArray, 6);
        c = new Color(191,191,191);
        g.setColor(c);
        g.fillRect(tx+210,ty+50,10,10);
        g.setColor(Color.black);
        g.drawRect(tx+210,ty+50,10,10);

        int xEArray[] = new int[8];
        int yEArray[] = new int[8];
        xEArray[0] = tx+270;
        xEArray[1] = tx+300;
        xEArray[2] = tx+300;
        xEArray[3] = tx+280;
        xEArray[4] = tx+280;
        xEArray[5] = tx+300;
        xEArray[6] = tx+300;
        xEArray[7] = tx+270;
        yEArray[0] = ty+30;
        yEArray[1] = ty+30;
        yEArray[2] = ty+50;
        yEArray[3] = ty+50;
        yEArray[4] = ty+60;
        yEArray[5] = ty+60;
        yEArray[6] = ty+70;
        yEArray[7] = ty+70;
        g.setColor(Color.red);
        g.fillPolygon(xEArray, yEArray, 8);
        g.setColor(Color.black);
        g.drawPolygon(xEArray, yEArray, 8);
        c = new Color(191,191,191);
        g.setColor(c);
        g.fillRect(tx+280,ty+35,10,10);
        g.setColor(Color.black);
        g.drawRect(tx+280,ty+35,10,10);

        g.setColor(Color.red);
        g.fillRect(tx+240,ty+40,20,30);
        g.setColor(Color.black);
        g.drawRect(tx+240,ty+40,20,30);
        drawZombie2(g);

        int xSArray[] = new int[12];
        int ySArray[] = new int[12];
        xSArray[0] = tx+310;
        xSArray[1] = tx+350;
        xSArray[2] = tx+350;
        xSArray[3] = tx+320;
        xSArray[4] = tx+320;
        xSArray[5] = tx+350;
        xSArray[6] = tx+350;
        xSArray[7] = tx+310;
        xSArray[8] = tx+310;
        xSArray[9] = tx+340;
        xSArray[10] = tx+340;
        xSArray[11] = tx+310;
        ySArray[0] = ty+20;
        ySArray[1] = ty+20;
        ySArray[2] = ty+30;
        ySArray[3] = ty+30;
        ySArray[4] = ty+40;
        ySArray[5] = ty+40;
        ySArray[6] = ty+70;
        ySArray[7] = ty+70;
        ySArray[8] = ty+60;
        ySArray[9] = ty+60;
        ySArray[10] = ty+50;
        ySArray[11] = ty+50;
        g.setColor(Color.red);
        g.fillPolygon(xSArray, ySArray, 12);
        g.setColor(Color.black);
        g.drawPolygon(xSArray, ySArray, 12);

        g.setColor(Color.blue);
        g.fillRect(tx+150,ty+90,10,20);
        g.setColor(Color.black);
        g.drawRect(tx+150,ty+90,10,20);

        int xNArray[] = new int[8];
        int yNArray[] = new int[8];
        xNArray[0] = tx+170;
        xNArray[1] = tx+200;
        xNArray[2] = tx+200;
        xNArray[3] = tx+190;
        xNArray[4] = tx+190;
        xNArray[5] = tx+180;
        xNArray[6] = tx+180;
        xNArray[7] = tx+170;
        yNArray[0] = ty+90;
        yNArray[1] = ty+90;
        yNArray[2] = ty+110;
        yNArray[3] = ty+110;
        yNArray[4] = ty+100;
        yNArray[5] = ty+100;
        yNArray[6] = ty+110;
        yNArray[7] = ty+110;
        g.setColor(Color.blue);
        g.fillPolygon(xNArray, yNArray, 8);
        g.setColor(Color.black);
        g.drawPolygon(xNArray, yNArray, 8);

        // Space
        g.setColor(Color.gray);
        g.fillRect(tx+25,ty+120,300,130);
        g.setColor(Color.black);
        g.drawRect(tx+25,ty+120,300,130);
        g.fillRect(tx+40,ty+135,270,100);
        for(int i = 0; i<zstarsx.length;i++){
            if(Math.random()<.9)
                g.setColor(Color.white);
            else
                g.setColor(Color.yellow);
            g.fillOval(zstarsx[i],zstarsy[i],zstarsd[i],zstarsd[i]);
        }
    }

    public void drawZombie1(Graphics g){
        // Body
        g.setColor(Color.orange);
        g.fillRect(tx+90,ty+40,20,20);
        g.setColor(Color.black);
        g.drawRect(tx+90,ty+40,20,20);

        // Eyes
        c = new Color(116,24,6);
        g.setColor(Color.white);
        g.fillRect(tx+93,ty+45,5,5);
        g.fillRect(tx+103,ty+45,5,5);
        g.setColor(Color.black);
        g.drawRect(tx+93,ty+45,5,5);
        g.drawRect(tx+103,ty+45,5,5);
    }

    public void drawZombie2(Graphics g){
        // Body
        c = new Color(12,137,51);
        g.setColor(c);
        g.fillRect(tx+240,ty+10,20,20);
        g.setColor(Color.black);
        g.drawRect(tx+240,ty+10,20,20);

        // Eyes
        g.setColor(Color.white);
        g.fillRect(tx+243,ty+15,5,5);
        g.fillRect(tx+253,ty+15,5,5);
        g.setColor(Color.black);
        g.drawRect(tx+243,ty+15,5,5);
        g.drawRect(tx+253,ty+15,5,5);
    }

    public void drawArrow(Graphics g){
        int xArray[] = new int[7];
        int yArray[] = new int[7];

        int xI = 300;
        int xI2 = 450;
        int yI = 400;
        int heightI = buttons[rowIndex][columnIndex].getHeight()/2;

        xArray[0] = xI+10;
        xArray[1] = xI+40;
        xArray[2] = xI+10;

        yArray[0] = yI+10;
        yArray[1] = yI+25;
        yArray[2] = yI+40;

        Polygon p = new Polygon(xArray,yArray,3);
        g.setColor(Color.red);
        g.fillPolygon(p);
        g.setColor(Color.black);
        g.drawPolygon(p);

        xArray[0] = xI2+10;
        xArray[1] = xI2+40;
        xArray[2] = xI2+10;

        yArray[0] = yI+10;
        yArray[1] = yI+25;
        yArray[2] = yI+40;

        p = new Polygon(xArray,yArray,3);
        g.setColor(Color.blue);
        g.fillPolygon(p);
        g.setColor(Color.black);
        g.drawPolygon(p);
    }

    public void drawRestartArrow(Graphics g){
        int xArray[] = new int[7];
        int yArray[] = new int[7];

        int xI = 307;
        int xI2 = 457;
        int yI = 480;
        int heightI = buttons[rowIndex][columnIndex].getHeight()/2;

        xArray[0] = xI+10;
        xArray[1] = xI+40;
        xArray[2] = xI+10;

        yArray[0] = yI+10;
        yArray[1] = yI+25;
        yArray[2] = yI+40;

        Polygon p = new Polygon(xArray,yArray,3);
        g.setColor(Color.red);
        g.fillPolygon(p);
        g.setColor(Color.black);
        g.drawPolygon(p);

        xArray[0] = xI2+10;
        xArray[1] = xI2+40;
        xArray[2] = xI2+10;

        yArray[0] = yI+10;
        yArray[1] = yI+25;
        yArray[2] = yI+40;

        p = new Polygon(xArray,yArray,3);
        g.setColor(Color.blue);
        g.fillPolygon(p);
        g.setColor(Color.black);
        g.drawPolygon(p);

        g.setColor(Color.red);
        g.fillRect(xI-2,yI+10,7,30);
        g.setColor(Color.blue);
        g.fillRect(xI2-2,yI+10,7,30);
        g.setColor(Color.black);
        g.drawRect(xI-2,yI+10,7,30);
        g.drawRect(xI2-2,yI+10,7,30);
    }

    public int getGameState(){return gameState;}
}
