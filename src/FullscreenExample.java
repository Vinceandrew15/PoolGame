
import org.jbox2d.common.IViewportTransform;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.AABB;  
import org.jbox2d.common.Vec2;  
import org.jbox2d.dynamics.Body;  
import org.jbox2d.dynamics.BodyDef;  
import org.jbox2d.dynamics.World;
import org.jbox2d.collision.shapes.PolygonShape; 
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

  
public class FullscreenExample {
	
	
	public static final int NUMBER_OF_BALLS = 16; // 0 indexed
	
	int mouse_x;
	int mouse_y;
	boolean leftButtonDown;
	
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
    
    Ball[] standardBall = new Ball[NUMBER_OF_BALLS];
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
    float distance_to_object(int x_1, int y_1,int x_2,int y_2){
        return (float)Math.sqrt((Math.pow(x_1-x_2,2))+(Math.pow(y_1-y_2,2)));

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
    	leftButtonDown = Mouse.isButtonDown(0);
    	
    	float point_y = standardBall[0].getY();
    	float point_x = standardBall[0].getX();
    	
    	cue_vector_x = (float)Math.cos(angle_radians);
    	cue_vector_y = (float)Math.sin(angle_radians);
    	
    	angle_radians = find_angle(point_x+cue_vector_x*10,point_y+cue_vector_y*10,mouse_x,mouse_y);
    	angle_degrees=(angle_radians*57.2957795f);
    	standardTable.setMouseAngle(angle_degrees+90);
    	
    	standardTable.setCueballLocation(point_x, point_y); 
    	
    	if(standardBall[0].getIsMoving())standardTable.setGameState(1);
    	if(!standardBall[0].getIsMoving())standardTable.setGameState(0);
    	
    	
    	
    	standardTable.setCueVectors(cue_vector_x, cue_vector_y);
    	
        if (leftButtonDown && !standardBall[0].getIsMoving())standardBall[0].addImpulse(cue_vector_x*power,cue_vector_y*power);

    	
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))standardBall[0].addImpulse(0f,0.3f);
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))standardBall[0].addImpulse(0f,-0.3f);
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))standardBall[0].addImpulse(-0.3f,0f);
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))standardBall[0].addImpulse(0.3f,0f);
        


        for (int i = 0; i <NUMBER_OF_BALLS; i++)  
        {  
            for (int j = i + 1; j < NUMBER_OF_BALLS; j++)  
            {  
                if (standardBall[i].isColliding(standardBall[j]))  
                {
                	standardBall[i].resolveCollision(standardBall[j]);
                }
            }
        }

        
         
     
         
        updateFPS(); // update FPS Counter
    }
     
    
	
    public void start() {
    	
    	  Vec2  gravity = new Vec2(0,0);
		    World world = new World(gravity);
		    BodyDef groundBodyDef = new BodyDef();
		    groundBodyDef.position.set(0,50);
		    Body groundBody = world.createBody(groundBodyDef);
		    PolygonShape groundBox = new PolygonShape();
		    groundBox.setAsBox(800, 0);
		    groundBody.createFixture(groundBox, 0);

		    // Dynamic Body
		    BodyDef bodyDef = new BodyDef();
		    bodyDef.type = BodyType.DYNAMIC;
		    bodyDef.position.set(0, 0);
		    Body body = world.createBody(bodyDef);
		    PolygonShape dynamicBox = new PolygonShape();
		    dynamicBox.setAsBox(12, 12);
		    FixtureDef fixtureDef = new FixtureDef();
		    fixtureDef.shape = dynamicBox;
		    fixtureDef.density = 1f;
		    fixtureDef.friction = 0.3f;
		    body.createFixture(fixtureDef);

		    // Setup world
		    float timeStep = 1.0f/60.0f;
		    int velocityIterations = 6;
		    int positionIterations = 2;
		    //body.setLinearVelocity(new Vec2(2500.0f, -2500.0f));
		    //body.setLinearDamping(1f);
		    Vec2 f = body.getWorldVector(new Vec2(0.0f, -30.0f));
		    Vec2 p = body.getWorldPoint(body.getLocalCenter().add(new Vec2(-.2f, 0f)));
		    body.applyForce(new Vec2(-200,-200),new Vec2(-200,200));
		    
    	
        try {
        Display.setDisplayMode(new DisplayMode(900,500));
        Display.create();
    } catch (LWJGLException e) {
        e.printStackTrace();
        System.exit(0);
    }
        
        lastFPS = getTime();
        
        //standardBall[0] = new Ball(400,200,0,12);
       //standardBall[1] = new Ball(100,210,2,12);
       // standardBall[0].addImpulse(-10,0);
        
        standardBall[0] = new Ball(200,300,0,12);
        standardBall[1] = new Ball(415,100,2,12);
        standardBall[0].addImpulse(10,-9.99f);
        
        standardBall[2] = new Ball(620,270,2,12);
        standardBall[3] = new Ball(640,290,2,12);
        standardBall[4] = new Ball(660,310,2,12);
        standardBall[5] = new Ball(680,330,2,12);
        
      
      
        standardBall[6] = new Ball(620,230,2,12);
        standardBall[7] = new Ball(640,210,2,12);
        standardBall[8] = new Ball(660,190,2,12);
        standardBall[9] = new Ball(680,170,2,12);
        
        standardBall[10] = new Ball(640,250,1,12);
        standardBall[11] = new Ball(660,230,2,12);
        standardBall[12] = new Ball(660,270,2,12);
        standardBall[13] = new Ball(680,290,2,12);
        standardBall[14] = new Ball(680,210,2,12);
        standardBall[15] = new Ball(680,250,2,12);

        
        
        standardTable = new Table(0,0,800,400,25,15);
        
        
        
        // init OpenGL
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 900, 0, 500, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        
        while (!Display.isCloseRequested()) {
        	int delta = getDelta();
        	world.step(timeStep, velocityIterations, positionIterations);
		     Vec2 position = body.getPosition();
		     float angle = body.getAngle();
		     //System.out.printf("%4.2f %4.2f %4.2f\n", position.x, position.y, angle);
        	
            // Clear the screen and depth buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);  
            
          
            
            standardTable.draw();
            
            for(int i=0; i<NUMBER_OF_BALLS; i++){
            	standardBall[i].draw();
                standardBall[i].update(delta);
            }
            standardBall[0].setX((position.x*20)+450);  
            standardBall[0].setY((position.y*20)+250);
            
           
            
            update(delta);
            
            
            Display.update();
            
            Display.sync(60);
            
        }
        Display.destroy();
        
        
    }
   
 
    
    
  
    
    
  
    public static void main(String[] argv) {
    	
 // Static Body
    			  
    			   
    	
        FullscreenExample quadExample = new FullscreenExample();
        quadExample.start();
    }
}