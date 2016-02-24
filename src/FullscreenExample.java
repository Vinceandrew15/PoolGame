import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

  
public class FullscreenExample {
	
	int hello;
	int mouse_x;
	int mouse_y;
	
	float angle_radians;
	float angle_degrees;
	float cue_vector_x;
	float cue_vector_y;
	float power=25;
	
	 /** time at last frame */
    long lastFrame;
     
    /** frames per second */
    int fps;
    /** last fps time */
    long lastFPS;
    
    Ball eightball;
    Table standardTable;
    
    int game_state;
	
    
    
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
    
    public static float find_angle(float x_1, float y_1, float x_2, float y_2){
        float tan_1;
        float tan_2;

        tan_1=y_1-y_2;
        tan_2=x_1-x_2;

        return (float)Math.atan2(tan_1,tan_2);
    }

    
    public void update(int delta) {
       
    	mouse_x = Mouse.getX(); // will return the X coordinate on the Display.
    	mouse_y = Mouse.getY(); // will return the Y coordinate on the Display.
        
    	float point_y = eightball.getY();
    	float point_x = eightball.getX();
    	
    	angle_radians = find_angle(point_x,point_y,mouse_x,mouse_y);
    	angle_degrees=(angle_radians*57.2957795f);
    	standardTable.setMouseAngle(angle_degrees+90);
    	
    	standardTable.setCueballLocation(point_x, point_y); 
    	
    	if(eightball.getIsMoving())standardTable.setGameState(1);
    	if(!eightball.getIsMoving())standardTable.setGameState(0);
    	
    	cue_vector_x = (float)Math.cos(angle_radians);
    	cue_vector_y = (float)Math.sin(angle_radians);
    	
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !eightball.getIsMoving())eightball.addImpulse(cue_vector_x*power,cue_vector_y*power);

    	
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
        
        eightball = new Ball(300,200,0,12);
        standardTable = new Table(0,0,800,400,25,15);
        
        
        
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