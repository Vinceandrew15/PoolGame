import org.lwjgl.opengl.GL11;

public class Ball {
	
	float x;
	float y;
	double x_vector=1;
	double y_vector=1;
	int ball_id;
	double friction = 0.01;
	
	public Ball(int newX, int newY, int new_ball_id){
		x = newX;
		y = newY;
		ball_id = new_ball_id;
	}
	

	public void drawEllipse(int newX, int newY, float xradius, float yradius, float r, float g, float b, float a )
	{
		GL11.glColor4f(r,g,b,a);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
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
	
	public void draw(){
		drawEllipse((int)x,(int)y,12,12,0,0,0,0);
	}
	public void update(){
		x+=x_vector;
		y+=y_vector;
		if(x_vector>0)x_vector-=friction;
		if(x_vector<0)x_vector+=friction;
		if(y_vector>0)y_vector-=friction;
		if(y_vector<0)y_vector+=friction;
		
		
	}

}
