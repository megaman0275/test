/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceship;

import java.io.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

import java.util.ArrayList;
import static spaceship.Missile.score;
import static spaceship.Star.lives;


public class Spaceship extends JFrame implements Runnable {
    boolean animateFirstTime = true;
    Image image;
    Graphics2D g;

    Image outerSpaceImage;
    
    Rocket rocket;
    
    Star star[] = new Star[Star.TotalStars];
    boolean gameOver;
    
    int timeCount; 
    
    sound ouchSound = null;
    
    static Spaceship frame;
    public static void main(String[] args) {
        frame = new Spaceship();
        frame.setSize(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public Spaceship() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button

// location of the cursor.
                    int xpos = e.getX();
                    int ypos = e.getY();

                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {

        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                
                if (gameOver)
                    return;
                
                if (e.VK_UP == e.getKeyCode()) {
                    rocket.increaseYSpeed(1);
                } else if (e.VK_DOWN == e.getKeyCode()) {
                    rocket.increaseYSpeed(-1);
                } else if (e.VK_LEFT == e.getKeyCode()) {
                    rocket.increaseXSpeed(-1);
                } else if (e.VK_RIGHT == e.getKeyCode()) {
                    rocket.increaseXSpeed(1);
                } else if (e.VK_INSERT == e.getKeyCode()) { 
                    ouchSound = new sound("WAVY.wav");
                } else if (e.VK_SPACE == e.getKeyCode()) { 
                    Missile.Fire(rocket);
                    Missile.Fire(rocket);
                    Missile.Fire(rocket);
                    Missile.Fire(rocket);
                    Missile.Fire(rocket);
                    Missile.Fire(rocket);
                    Missile.Fire(rocket);
                    Missile.Fire(rocket);
                    Missile.Fire(rocket);
                    Missile.Fire(rocket);
                    Missile.Fire(rocket);
                    Missile.Fire(rocket);
                    
                }
                repaint();
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }



////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || Window.xsize != getSize().width || Window.ysize != getSize().height) {
            Window.xsize = getSize().width;
            Window.ysize = getSize().height;
            image = createImage(Window.xsize, Window.ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
//fill background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, Window.xsize, Window.ysize);

        int x[] = {Window.getX(0), Window.getX(Window.getWidth2()), Window.getX(Window.getWidth2()), Window.getX(0), Window.getX(0)};
        int y[] = {Window.getY(0), Window.getY(0), Window.getY(Window.getHeight2()), Window.getY(Window.getHeight2()), Window.getY(0)};
//fill border
        g.setColor(Color.black);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }
            
        
              g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.PLAIN,40));
            g.drawString("Lives: " + Star.lives ,400,60);
            
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.PLAIN,50));
            g.drawString("Score: " + Missile.score ,20,60);                       
        
        
        g.drawImage(outerSpaceImage,Window.getX(0),Window.getY(0),
                Window.getWidth2(),Window.getHeight2(),this);
        
        
        Planet.Draw(g);
        
        Missile.Draw(g);
        Planet.Draw(g);
        rocket.draw(g,this);
        for (int i=0;i<star.length;i++) {
            star[i].draw(g,this);
        }
        if (gameOver)
        {
            g.setColor(Color.white);
            g.setFont(new Font("Arial",Font.PLAIN,50));
            g.drawString("Game Over", 60, 360);        
        }        
        Planet.Create(rocket); 
        gOld.drawImage(image, 0, 0, null);
    }
////////////////////////////////////////////////////////////////////////////
    public void drawCircle(int xpos,int ypos,double rot,double xscale,double yscale)
    {
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.setColor(Color.red);
        g.fillOval(-10,-10,20,20);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }

////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 0.04;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {

        gameOver = false;
        rocket = new Rocket();

        for (int i=0;i<star.length;i++) {
            star[i] = new Star();
        }
        
        Missile.getClear();
        
        Star.lives = 5;
        
        Missile.score = 0; 

    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {
        if (animateFirstTime) {
            animateFirstTime = false;
            if (Window.xsize != getSize().width || Window.ysize != getSize().height) {
                Window.xsize = getSize().width;
                Window.ysize = getSize().height;
            }
            outerSpaceImage = Toolkit.getDefaultToolkit().getImage("./outerSpace.jpg");
            reset();                   
        }
        if (gameOver)
            return;
        
        for (int i=0;i<star.length;i++)
            star[i].increaseXPos(-rocket.getXSpeed());
        rocket.increaseYPos();
        
        for (int i=0;i<star.length;i++)
        {
            if (star[i].collide(rocket))
                if(Star.lives==0)
                gameOver = true;
        }
        
        Missile.Move();
        
        for (int i=0;i<star.length;i++)
        {
            Missile.Collide(star[i], rocket.getFaceRight());
        }
        
        timeCount++;
    }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }

}

class Rocket {
//variables for rocket.
    private Image image = Toolkit.getDefaultToolkit().getImage("./rocket.GIF");
    private int xPos;
    private int yPos;
    private int xSpeed;
    private int ySpeed;
    private boolean faceRight;
    Rocket() {
//      rocketImage = Toolkit.getDefaultToolkit().getImage("./animRocket.GIF");
//init the location of the rocket to the center.
        xPos = Window.getWidth2()/2;
        yPos = Window.getHeight2()/2;
        xSpeed = 0;
        ySpeed = 0;
        faceRight = true;
    }
    public void increaseYPos() {
        yPos += ySpeed;
        
        if (yPos < 0) {
            yPos = 0;
            ySpeed = 0;
        } else if (yPos > Window.getHeight2()) {
            yPos = Window.getHeight2();
            ySpeed = 0;
        }
    }    

    public int getXPos() {
        return(xPos);
    }
    public int getYPos() {
        return(yPos);
    }    
    public int getXSpeed() {
        return(xSpeed);
    }
    public int getYSpeed() {
        return(ySpeed);
    }
    public boolean getFaceRight() {
        return(faceRight);
    }
    public void increaseXSpeed(int value) {
        xSpeed += value;
        if (xSpeed < 0)
            faceRight = false;
        else if (xSpeed > 0)
            faceRight = true;
    }
    public void increaseYSpeed(int value) {
        ySpeed += value;
    }    
    public void draw(Graphics2D g,Spaceship obj) {
        if (faceRight)
            drawRocket(g,obj,Window.getX(xPos),Window.getYNormal(yPos),0.0,2.0,2.0 );
        else
            drawRocket(g,obj,Window.getX(xPos),Window.getYNormal(yPos),0.0,-2.0,2.0 );
            
    }
////////////////////////////////////////////////////////////////////////////
    public void drawRocket(Graphics2D g,Spaceship obj,int xpos,int ypos,double rot,double xscale,
            double yscale) {
        int width = image.getWidth(obj);
        int height = image.getHeight(obj);
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.drawImage(image,-width/2,-height/2,
        width,height,obj);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }    
}

class Star {
    public static final int TotalStars = 10;
    
    private Image image = Toolkit.getDefaultToolkit().getImage("./starAnim.GIF");
    private  int xPos;
    private  int yPos;
    public static int lives = 5; 
    Star() {
        xPos = (int)(Math.random()*Window.getWidth2());
        yPos = (int)(Math.random()*Window.getHeight2());      
    }
    public int getXPos() {
        return(xPos);
    }  
    public int getYPos() {
        return(yPos);
    }  
    public void setXPos(int num){
        xPos = num; 
    }
    public void setYPos(int num){
        yPos = num; 
    }
    public void increaseXPos(int value) {
        xPos += value;
        if (xPos < -50) {
            xPos = Window.getWidth2() + 50;
            yPos = (int)(Math.random()*Window.getHeight2());      
        } else if (xPos > Window.getWidth2() + 50) {
            xPos = -50;
            yPos = (int)(Math.random()*Window.getHeight2());      
        }
    }
    
    
    public boolean collide(Rocket rocket) {
        if ( xPos < rocket.getXPos() + 30 && xPos > rocket.getXPos() - 30 &&
             yPos < rocket.getYPos() + 20 && yPos > rocket.getYPos() - 20)
        {
            lives--;
            return (true);
        }
        return (false);
    }
    
    
    public void draw(Graphics2D g,Spaceship obj) {
                drawStar(g,obj,Window.getX(xPos),
                Window.getYNormal(yPos),0.0,1.0,1.0 );
    }
////////////////////////////////////////////////////////////////////////////
    public void drawStar(Graphics2D g,Spaceship obj,int xpos,int ypos,double rot,double xscale,
            double yscale) {
        int width = image.getWidth(obj);
        int height = image.getHeight(obj);
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.drawImage(image,-width/2,-height/2,
        width,height,obj);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }    
}
class Planet{
    private static ArrayList<Planet> planets = new ArrayList<Planet>();
   private int xPos;
   private int yPos;
   private int score;
   private Image image = Toolkit.getDefaultToolkit().getImage("./giphy.GIF");
   public static void getClear() {
        planets.clear(); 
    } 
    public static void Create(Rocket rocket){
       Planet obj = new Planet();
       planets.add(obj);
   }
   public static void Move(Rocket rocket){
       for (int i=0;i<planets.size();i++)
           planets.get(i).increaseXPos(rocket.getXSpeed());
   }
   public static void Collide(Star star, boolean faceRight){
       for (int i=0;i<planets.size();i++)
       {
           planets.get(i).collide(star,faceRight);
       }
   }
   public static void Draw(Graphics2D g){
       for (int i=0;i<planets.size();i++)
           planets.get(i).draw(g);
   }
/////////////////////////////////////////////////////////////////   
   Planet(){
        xPos = (int)(Math.random()*Window.getWidth2());
        yPos = (int)(Math.random()*Window.getHeight2());    
   }
   private void collide(Star star,boolean faceRight){
        if (yPos >= star.getYPos()-20 && xPos >= star.getXPos()-30
            && xPos <= star.getXPos()+30 && yPos <= star.getYPos()+20)
        {
            planets.remove(this); 
            star.setYPos((int)(Math.random()*Window.getHeight2()));
            if(faceRight)
            {
            star.setXPos(Window.getWidth2()+20);
            }
            else
            {
             star.setXPos(+20);
            }
            score++; 
        }          
   }
    public void increaseXPos(int value) {
        xPos += value;
        if (xPos < -50) {
            xPos = Window.getWidth2() + 50;
            yPos = (int)(Math.random()*Window.getHeight2());      
        } else if (xPos > Window.getWidth2() + 50) {
            xPos = -50;
            yPos = (int)(Math.random()*Window.getHeight2());      
        }
    }
   private void draw(Graphics2D g){
       drawPlanet(g,Window.getX(xPos),Window.getYNormal(yPos),1,1);
   }
    private void drawPlanet(Graphics2D g,int xpos,int ypos,double xscale,double yscale)
    {
        g.translate(xpos,ypos);
        g.scale( xscale , yscale );
   
        g.setColor(Color.blue);
        g.fillOval(-2,-5,50,50);
     
        g.scale( 1.0/xscale,1.0/yscale );
        g.translate(-xpos,-ypos);
    }         
   

}
class Missile{
   private static ArrayList<Missile> missiles = new ArrayList<Missile>();
   private int xPos;
   private int yPos;
   private int speed;
   public static int score;
   int randomSpeed = (int)(Math.random()*100/2); 
   
   public static void Fire(Rocket rocket){
       int randomSpeed = (int)(Math.random()*50+1); 
       Missile obj = new Missile();
       obj.xPos = rocket.getXPos();
       obj.yPos = rocket.getYPos();
       if (rocket.getFaceRight())
           obj.speed = randomSpeed;
       else
           obj.speed = -randomSpeed;
       missiles.add(obj);
   }
       public static void getClear() {
        missiles.clear(); 
    }  
   public static void Move(){
       for (int i=0;i<missiles.size();i++)
           missiles.get(i).move();
   }
   public static void Collide(Star star, boolean faceRight){
       for (int i=0;i<missiles.size();i++)
       {
           missiles.get(i).collide(star,faceRight);
       }
   }
   public static void Draw(Graphics2D g){
       for (int i=0;i<missiles.size();i++)
           missiles.get(i).draw(g);
   }
/////////////////////////////////////////////////////////////////   
   Missile(){
       
   }
   private void collide(Star star,boolean faceRight){
        if (yPos >= star.getYPos()-20 && xPos >= star.getXPos()-30
            && xPos <= star.getXPos()+30 && yPos <= star.getYPos()+20)
        {
            missiles.remove(this); 
            star.setYPos((int)(Math.random()*Window.getHeight2()));
            if(faceRight)
            {
            star.setXPos(Window.getWidth2()+20);
            }
            else
            {
             star.setXPos(+20);
            }
            score++; 
        }          
   }
   private void move(){
        xPos += speed;
        if (xPos < 0)
            missiles.remove(this);
        if (xPos > Window.getWidth2())
            missiles.remove(this);
   }
   private void draw(Graphics2D g){
       drawMissile(g,Window.getX(xPos),Window.getYNormal(yPos),1,1);
   }
    private void drawMissile(Graphics2D g,int xpos,int ypos,double xscale,double yscale)
    {
        g.translate(xpos,ypos);
        g.scale( xscale , yscale );
   
        g.setColor(Color.green);
        g.fillOval(-2,-5,7,7);
     
        g.scale( 1.0/xscale,1.0/yscale );
        g.translate(-xpos,-ypos);
    }         
   

}

class Window {
    private static final int XBORDER = 20;
    
//    private static final int YBORDER = 20;
    
    private static final int TOP_BORDER = 40;
    private static final int BOTTOM_BORDER = 20;
    
    private static final int YTITLE = 30;
    private static final int WINDOW_BORDER = 8;
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + 600;
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 600;
    static int xsize = -1;
    static int ysize = -1;


/////////////////////////////////////////////////////////////////////////
    public static int getX(int x) {
        return (x + XBORDER + WINDOW_BORDER);
    }

    public static int getY(int y) {
//        return (y + YBORDER + YTITLE );
        return (y + TOP_BORDER + YTITLE );
        
    }

    public static int getYNormal(int y) {
//          return (-y + YBORDER + YTITLE + getHeight2());
      return (-y + TOP_BORDER + YTITLE + getHeight2());
        
    }
    
    public static int getWidth2() {
        return (xsize - 2 * (XBORDER + WINDOW_BORDER));
    }

    public static int getHeight2() {
//        return (ysize - 2 * YBORDER - WINDOW_BORDER - YTITLE);
        return (ysize - (BOTTOM_BORDER + TOP_BORDER) - WINDOW_BORDER - YTITLE);
    }    
    

}

class sound implements Runnable {
    Thread myThread;
    File soundFile;
    public boolean donePlaying = false;
    sound(String _name)
    {
        soundFile = new File(_name);
        myThread = new Thread(this);
        myThread.start();
    }
    public void run()
    {
        try {
        AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
        AudioFormat format = ais.getFormat();
    //    System.out.println("Format: " + format);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine source = (SourceDataLine) AudioSystem.getLine(info);
        source.open(format);
        source.start();
        int read = 0;
        byte[] audioData = new byte[16384];
        while (read > -1){
            read = ais.read(audioData,0,audioData.length);
            if (read >= 0) {
                source.write(audioData,0,read);
            }
        }
        donePlaying = true;

        source.drain();
        source.close();
        }
        catch (Exception exc) {
            System.out.println("error: " + exc.getMessage());
            exc.printStackTrace();
        }
    }

}