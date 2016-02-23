import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
  
public class FullscreenExample {
	
	int hello;
	
	 /** time at last frame */
    long lastFrame;
     
    /** frames per second */
    int fps;
    /** last fps time */
    long lastFPS;
    
    Ball eightball = new Ball(300,200,0,12);;
	
    
    
    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
             lastFPS += 1000;
          }
          fps++;
     }
    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;
      
        return delta;
    }
     
    /**
     * Get the accurate system time
     * 
     * @return The system time in milliseconds
     */
    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
           
    public void update(int delta) {
        // rotate quad

         
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))eightball.addImpulse(0f,0.3f);
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))eightball.addImpulse(0f,-0.3f);
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))eightball.addImpulse(-0.3f,0f);
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))eightball.addImpulse(0.3f,0f);


    
         
     
         
        updateFPS(); // update FPS Counter
    }
     
    
	
    public void start() {
    	
        try {
        Display.setDisplayMode(new DisplayMode(900,500));
        Display.create();
    } catch (LWJGLException e) {
        e.printStackTrace();
        System.exit(0);
    }
        
        lastFPS = getTime();
        
        
        Table standardTable = new Table(0,0,800,400,25,15);
        
        // init OpenGL
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 900, 0, 500, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        
        while (!Display.isCloseRequested()) {
        	int delta = getDelta();
        	
            // Clear the screen and depth buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);  
            
          
            
            standardTable.draw();
            
            eightball.draw();
            eightball.update(delta);
           
            
            update(delta);
            
            Display.update();
            Display.sync(60);
            
        }
        Display.destroy();
        
    }
   
 
    
    
  
    
    
  
    public static void main(String[] argv) {
        FullscreenExample quadExample = new FullscreenExample();
        quadExample.start();
    }
}