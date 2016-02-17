
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
    
    float angle_x= 0;
    float angle_y= 0;
    
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
  
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) angle_x -= 0.3f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) angle_x += 0.3f * delta;
  
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) angle_y -= 0.3f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) angle_y += 0.3f * delta;
  
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
      GL11.glClearColor(1f,1f,1f,1f);
      GL11.glMatrixMode(GL11.GL_PROJECTION);
      GL11.glLoadIdentity();
      GL11.glOrtho(0, 800, 600, 0, 800, -600);
      GL11.glMatrixMode(GL11.GL_MODELVIEW);
      //GL11.glEnable(GL11.GL_CULL_FACE);
      GL11.glEnable(GL11.GL_DEPTH_TEST);

   
         
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glGetError();
         
         GL11.glTranslatef(400,300,0.0f);
         GL11.glRotatef(angle_y,1.0f,0.0f,0f);
         GL11.glRotatef(angle_x,0f,1.0f,0f);
         //GL11.glRotatef(50, 0.0f, 0.0f, 1.0f);
         GL11.glScalef(100,100,100);
         
         
         
         GL11.glBegin(GL11.GL_QUAD_STRIP);
          {
        	  GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
              GL11.glLoadIdentity();
              //GL11.glTranslatef(angle_x, 0.0f, -6.0f);
           
              GL11.glBegin(GL11.GL_TRIANGLES);        // Drawing Using Triangles
              GL11.glColor3f(1.0f, 0.0f, 0.0f);     // Red
              GL11.glVertex3f(0.0f, 1.0f, 0.0f);    // Top Of Triangle (Front)
              GL11.glColor3f(0.0f, 1.0f, 0.0f);     // Green
              GL11.glVertex3f(-1.0f, -1.0f, 1.0f);  // Left Of Triangle (Front)
              GL11.glColor3f(0.0f, 0.0f, 1.0f);     // Blue
              GL11.glVertex3f(1.0f, -1.0f, 1.0f);   // Right Of Triangle (Front)
              GL11.glColor3f(1.0f, 0.0f, 0.0f);     // Red
              GL11.glVertex3f(0.0f, 1.0f, 0.0f);    // Top Of Triangle (Right)
              GL11.glColor3f(0.0f, 0.0f, 1.0f);     // Blue
              GL11.glVertex3f(1.0f, -1.0f, 1.0f);   // Left Of Triangle (Right)
              GL11.glColor3f(0.0f, 1.0f, 0.0f);     // Green
              GL11.glVertex3f(1.0f, -1.0f, -1.0f);  // Right Of Triangle (Right)
              GL11.glColor3f(1.0f, 0.0f, 0.0f);     // Red
              GL11.glVertex3f(0.0f, 1.0f, 0.0f);    // Top Of Triangle (Back)
              GL11.glColor3f(0.0f, 1.0f, 0.0f);     // Green
              GL11.glVertex3f(1.0f, -1.0f, -1.0f);  // Left Of Triangle (Back)
              GL11.glColor3f(0.0f, 0.0f, 1.0f);     // Blue
              GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Right Of Triangle (Back)
              GL11.glColor3f(1.0f, 0.0f, 0.0f);     // Red
              GL11.glVertex3f(0.0f, 1.0f, 0.0f);    // Top Of Triangle (Left)
              GL11.glColor3f(0.0f, 0.0f, 1.0f);     // Blue
              GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Left Of Triangle (Left)
              GL11.glColor3f(0.0f, 1.0f, 0.0f);     // Green
              GL11.glVertex3f(-1.0f, -1.0f, 1.0f);  // Right Of Triangle (Left)
              GL11.glEnd();                         // Finished Drawing The Triangle
              GL11.glLoadIdentity();
              //GL11.glTranslatef(1.5f, 0.0f, -6.0f);
              GL11.glBegin(GL11.GL_QUADS);            // Draw A Quad
              GL11.glColor3f(0.0f, 1.0f, 0.0f);     // Set The Color To Green
              GL11.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Right Of The Quad (Top)
              GL11.glVertex3f(-1.0f, 1.0f, -1.0f);  // Top Left Of The Quad (Top)
              GL11.glVertex3f(-1.0f, 1.0f, 1.0f);   // Bottom Left Of The Quad (Top)
              GL11.glVertex3f(1.0f, 1.0f, 1.0f);    // Bottom Right Of The Quad (Top)
       
              GL11.glColor3f(1.0f, 0.5f, 0.0f);     // Set The Color To Orange
              GL11.glVertex3f(1.0f, -1.0f, 1.0f);   // Top Right Of The Quad (Bottom)
              GL11.glVertex3f(-1.0f, -1.0f, 1.0f);  // Top Left Of The Quad (Bottom)
              GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Bottom)
              GL11.glVertex3f(1.0f, -1.0f, -1.0f);  // Bottom Right Of The Quad (Bottom)
       
              GL11.glColor3f(1.0f, 0.0f, 0.0f);     // Set The Color To Red
              GL11.glVertex3f(1.0f, 1.0f, 1.0f);    // Top Right Of The Quad (Front)
              GL11.glVertex3f(-1.0f, 1.0f, 1.0f);   // Top Left Of The Quad (Front)
              GL11.glVertex3f(-1.0f, -1.0f, 1.0f);  // Bottom Left Of The Quad (Front)
              GL11.glVertex3f(1.0f, -1.0f, 1.0f);   // Bottom Right Of The Quad (Front)
       
              GL11.glColor3f(1.0f, 1.0f, 0.0f);     // Set The Color To Yellow
              GL11.glVertex3f(1.0f, -1.0f, -1.0f);  // Bottom Left Of The Quad (Back)
              GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Back)
              GL11.glVertex3f(-1.0f, 1.0f, -1.0f);  // Top Right Of The Quad (Back)
              GL11.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Left Of The Quad (Back)
       
              GL11.glColor3f(0.0f, 0.0f, 1.0f);     // Set The Color To Blue
              GL11.glVertex3f(-1.0f, 1.0f, 1.0f);   // Top Right Of The Quad (Left)
              GL11.glVertex3f(-1.0f, 1.0f, -1.0f);  // Top Left Of The Quad (Left)
              GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Left)
              GL11.glVertex3f(-1.0f, -1.0f, 1.0f);  // Bottom Right Of The Quad (Left)
       
              GL11.glColor3f(1.0f, 0.0f, 1.0f);     // Set The Color To Violet
              GL11.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Right Of The Quad (Right)
              GL11.glVertex3f(1.0f, 1.0f, 1.0f);    // Top Left Of The Quad (Right)
              GL11.glVertex3f(1.0f, -1.0f, 1.0f);   // Bottom Left Of The Quad (Right)
              GL11.glVertex3f(1.0f, -1.0f, -1.0f);  // Bottom Right Of The Quad (Right)
          
          }
          GL11.glEnd(); 
         GL11.glPopMatrix();
         
         
    }
  
    public static void main(String[] argv) {
        FullscreenExample fullscreenExample = new FullscreenExample();
        fullscreenExample.start();
    }
}