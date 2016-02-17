
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
  
public class FullscreenExample {
  
    /** position of quad */
    float x = 400, y = 300;
    /** angle of quad rotation */
    float rotation = 0;
    
    float angle= 0;
  
    /** time at last frame */
    long lastFrame;
  
    /** frames per second */
    int fps;
    /** last fps time */
    long lastFPS;
     
    /** is VSync Enabled */
    boolean vsync;
  
    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
  
        initGL(); // init OpenGL
        getDelta(); // call once before loop to initialise lastFrame
        lastFPS = getTime(); // call before loop to initialise fps timer
  
        while (!Display.isCloseRequested()) {
            int delta = getDelta();
  
            update(delta);
            renderGL();
  
            Display.update();
            Display.sync(60); // cap fps to 60fps
        }
  
        Display.destroy();
    }
  
    public void update(int delta) {
        // rotate quad
        rotation += 0.15f * delta;
  
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) x -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) x += 0.35f * delta;
  
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) y -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) y += 0.35f * delta;
  
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_F) {
                    setDisplayMode(800, 600, !Display.isFullscreen());
                }
                else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
                    vsync = !vsync;
                    Display.setVSyncEnabled(vsync);
                }
            }
        }
         
        // keep quad on the screen
        if (x < 0) x = 0;
        if (x > 800) x = 800;
        if (y < 0) y = 0;
        if (y > 600) y = 600;
  
        updateFPS(); // update FPS Counter
    }
    
 
    /**
     * Set the display mode to be used 
     * 
     * @param width The width of the display required
     * @param height The height of the display required
     * @param fullscreen True if we want fullscreen mode
     */
    public void setDisplayMode(int width, int height, boolean fullscreen) {
 
        // return if requested DisplayMode is already set
                if ((Display.getDisplayMode().getWidth() == width) && 
            (Display.getDisplayMode().getHeight() == height) && 
            (Display.isFullscreen() == fullscreen)) {
            return;
        }
         
        try {
            DisplayMode targetDisplayMode = null;
             
            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;
                 
                for (int i=0;i<modes.length;i++) {
                    DisplayMode current = modes[i];
                     
                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }
 
                        // if we've found a match for bpp and frequence against the 
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
                            (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width,height);
            }
             
            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
                return;
            }
 
            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);
             
        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
        }
    }
     
    /** 
     * Calculate how many milliseconds have passed 
     * since last frame.
     * 
     * @return milliseconds passed since last frame 
     */
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
  
    /**
     * Calculate the FPS and set it in the title bar
     */
    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
  
    public void initGL() {
    	 GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
         // Clear the screen and depth buffer
    	 GL11.glMatrixMode(GL11.GL_PROJECTION);
    	 GL11.glLoadIdentity();
    	 GL11.glOrtho(0, 800, 600, 0, 800, -600);
    	 GL11.glMatrixMode(GL11.GL_MODELVIEW);

    }
  
    public void renderGL() {
        // Clear The Screen And The Depth Buffer
    	 GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
         // Clear the screen and depth buffer
      GL11.glMatrixMode(GL11.GL_PROJECTION);
      GL11.glLoadIdentity();
      GL11.glOrtho(0, 800, 600, 0, 800, -600);
      GL11.glMatrixMode(GL11.GL_MODELVIEW);

   
         
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         
          GL11.glTranslatef(400,300,0.0f);
         GL11.glRotatef(angle, 0.0f, 0.5f, 0.0f);
         GL11.glRotatef(50, 0.0f, 0.0f, 1.0f);
         GL11.glScalef(100,100,100);
         
         GL11.glBegin(GL11.GL_QUAD_STRIP);
          {
          //this Makes a box that has no top or bottom. 
          //Front - Orange
          //Right - White
          //Back - Blue
          //Left - Teal
          
          GL11.glColor4f(1.0f,0.5f,0.0f,1.0f); 
          GL11.glVertex3f(0.0f,1.0f,0.0f); //Front Bottom Left
          GL11.glVertex3f(0.0f,0.0f,0.0f); //Front Top Left
          GL11.glVertex3f(1.0f,1.0f,0.0f); //Front Bottom Right
          GL11.glVertex3f(1.0f,0.0f,0.0f); //Front Top Right
          
          GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
          GL11.glVertex3f(1.0f,1.0f,1.0f); //Right Bottom Back
          GL11.glVertex3f(1.0f,0.0f,1.0f); //Right Top Back


          GL11.glColor4f(0.0f,0.0f,1.0f,1.0f);
          GL11.glVertex3f(0.0f,1.0f,1.0f); //Back left Bottom
          GL11.glVertex3f(0.0f,0.0f,1.0f); //Back Left Top
          

          GL11.glColor4f(0.0f,1.0f,1.0f,1.0f);
          GL11.glVertex3f(0.0f,1.0f,0.0f); //Front Bottom Left
          GL11.glVertex3f(0.0f,0.0f,0.0f); //Front Top Left
          
          }
          GL11.glEnd(); 
         GL11.glPopMatrix();
         angle +=0.5f;
         System.out.println(angle);
    }
  
    public static void main(String[] argv) {
        FullscreenExample fullscreenExample = new FullscreenExample();
        fullscreenExample.start();
    }
}