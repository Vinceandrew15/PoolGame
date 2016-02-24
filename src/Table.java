import org.lwjgl.opengl.GL11;

public class Table {
	int playable_width;
	int playable_height;
	int x;
	int y;
	int border;
	int pocket_radius;
	float mouse_angle;

	int game_state;
	
	float cueball_x;
	float cueball_y;
	float cue_vector_x;
	float cue_vector_y;
	
	
	public Table(int newX, int newY, int newPlayableWidth,int newPlayableHeight,int newBorder, int newPocketRadius){
		playable_width = newPlayableWidth;
		playable_height = newPlayableHeight;
		x = newX;
		y = newY;
		border = newBorder;
		pocket_radius = newPocketRadius;
		
	}
	public void setMouseAngle(float newMouseAngle){
		mouse_angle = newMouseAngle;
	}
	public void setCueballLocation(float newX, float newY){
		cueball_x = newX;
		cueball_y = newY;
	}
	
	public void setCueVectors(float newX, float newY){
		cue_vector_x=newX;
		cue_vector_y=newY;
	}
	
	public void setGameState(int newGameState){
		game_state = newGameState;
	}
	
	
	public void drawEllipse(int newX, int newY, float xradius, float yradius, float r, float g, float b, float a )
	{
		GL11.glColor4f(r,g,b,a);
		GL11.glPushMatrix();
		GL11.glTranslatef(newX, newY, 0);
		GL11.glScalef(xradius, yradius, 1);

		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2f(0, 0);
		for(int i = 0; i <= 50; i++){ //NUM_PIZZA_SLICES decides how round the circle looks.
		    double angle = Math.PI * 2 * i / 50;
		    GL11.glVertex2f((float)Math.cos(angle), (float)Math.sin(angle));
		}
		GL11.glEnd();

		GL11.glPopMatrix();
	}
	
	public void drawRect(int newX, int newY, int width, int height, float r, float g, float b, float a){
		GL11.glColor4f(r,g,b,a);
		GL11.glPushMatrix();
		GL11.glTranslatef(newX, newY, 0);
		GL11.glBegin(GL11.GL_QUADS);
        	GL11.glVertex2f(0,0);
        	GL11.glVertex2f(width,0);
        	GL11.glVertex2f(width,height);
        	GL11.glVertex2f(0,height);
        GL11.glEnd();
        GL11.glPopMatrix();
        
    }
	
	public void rotateRect(float angle, int newX, int newY, int width, int height, float r, float g, float b, float a){
		
		GL11.glLoadIdentity();
		GL11.glPushMatrix();
		
		GL11.glTranslatef(newX, newY, 0);
		GL11.glRotatef(angle, 0f, 0, 1);
		GL11.glColor4f(r,g,b,a);
		GL11.glBegin(GL11.GL_QUADS);
        	GL11.glVertex2f(0,0);
        	GL11.glVertex2f(width,0);
        	GL11.glVertex2f(width,height);
        	GL11.glVertex2f(0,height);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
	
	public void draw(){
		  drawRect(border,border,playable_width+(border*2),playable_height+(border*2),0.5f,0.2f,0.1f,0.0f);
          drawRect(border*2,border*2,playable_width,playable_height,0f,1f,0.2f,0.0f);
          
          drawEllipse(border*2,border*2,pocket_radius,pocket_radius,0f,0f,0f,0.0f);
          drawEllipse((border*2)+(playable_width/2),border*2,pocket_radius,pocket_radius,0f,0f,0f,0.0f);
          drawEllipse((border*2)+playable_width,border*2,pocket_radius,pocket_radius,0f,0f,0f,0.0f);
          
          drawEllipse(border*2,(border*2)+playable_height,pocket_radius,pocket_radius,0f,0f,0f,0.0f);
          drawEllipse((border*2)+(playable_width/2),(border*2)+playable_height,pocket_radius,pocket_radius,0f,0f,0f,0.0f);
          drawEllipse((border*2)+playable_width,(border*2)+playable_height,pocket_radius,pocket_radius,0f,0f,0f,0.0f);
          
          if(game_state==0)rotateRect(mouse_angle,(int)cueball_x,(int)cueball_y,2,100,0.5f,0.2f,0.1f,0.0f);
	}
	
	
}
