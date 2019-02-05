import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;

public class Player
{
	private double x; //of top-left corner
	private double xi; //initial x position (reset when velocity is reset)
	private double y; //of top-left corner
	private double yi; //initial y position (reset when velocity is reset)
	private double vx;
	private double vy;
	
	private double time; //time of most recent updatePosition call
	private double ti_x; //time of most recent x velocity reset
	private double ti_y; //time of most recent y velocity reset
	
	private int x_collision; // -1 means wall to left, 0 means no collision, 1 means wall to right
	private int y_collision; // -1 means wall above, 0 means no collision, 1 means wall below
	
	private double width;
	private double height;
	private Color color;
	
	private KeyCode jumpKey;
	private KeyCode leftKey;
	private KeyCode rightKey;
	private KeyCode downKey;
	private KeyCode ballKey;
	
	private Optional<Ball> myBall;
	private ArrayList<Wall> walls; //will be equal to the main array of these
	private ArrayList<Ball> balls;
	
	public Player(double x,double y,double width,double height, Color color)
	{
		this.x = x;
		this.xi = x;
		this.y = y;
		this.yi = y;
		this.vx = 0;
		this.vy = 0;
		
		this.time = 0;
		this.ti_x = 0;
		this.ti_y = 0;
		
		this.x_collision = 0;
		this.y_collision = 0;
		
		this.width = width;
		this.height = height;
		this.color = color;
		
		this.myBall = Optional.empty();
	}
	
	public void handleKeyPress(KeyCode key)
	{
		//we assume that bindKeys() has been called already
		
		if(key == jumpKey && y_collision == 1) //can only jump if on a platform
		{
			setYVelocity(-500);
		}
		else if(key == leftKey)
		{
			setXVelocity(-150);
		}
		else if(key == rightKey)
		{
			setXVelocity(150);
		}
		else if(key == downKey)
		{
		}
		else if(key == ballKey)
		{
			//loop through available balls
			for(Ball b : balls)
			{
				
			}
			//if the center of this player is within the radius of the ball, we can pick it up
			//double dist = Math.hypot
		}
	}
	
	public void handleKeyRelease(KeyCode key)
	{
		if(key == leftKey || key == rightKey)
		{
			setXVelocity(0);
		}
	}
	
	public void bindKeys(KeyCode jumpKey, KeyCode leftKey, KeyCode rightKey, KeyCode downKey, KeyCode ballKey)
	{
		this.jumpKey = jumpKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.downKey = downKey;
		this.ballKey = ballKey;
	}
	
	public void giveObjects(ArrayList<Wall> walls, ArrayList<Ball> balls)
	{
		this.walls = walls;
		this.balls = balls;
	}
	
	public void draw(GraphicsContext ctx)
	{
		ctx.setFill(color);
		ctx.fillRect(x,y,width,height);
	}
	
	//function to update player's position
	public void updatePosition(double time, double ay) //time is the current time in seconds, from when the main animation timer started
	{
		this.time = time;
		
		//define t to be time since most recent velocity reset
		double t_x = time - ti_x;
		double t_y = time - ti_y;
		
		//if velocity is in opposite direction to collision, no more collision
		if(x_collision == 1 && vx < 0 || x_collision == -1 && vx > 0)
		{
			x_collision = 0;
		}
		if(y_collision == 1 && vy < 0 || y_collision == -1 && vy > 0)
		{
			y_collision = 0;
		}
		
		//store previous position
		double prev_x = x;
		double prev_y = y;
		
		//move player if no collisions preventing it
		if(x_collision == 0)
		{
			x = xi + vx*t_x;
		}
		if(y_collision != 1)
		{
			y = yi + vy*t_y + 0.5*ay*t_y*t_y;
		}
		
		//detect wall collision
		boolean foundXCollision = false;
		boolean foundYCollision = false;
		//loop through walls
		for(Wall w : walls)
		{
			//y
			//test collision below
			if( (x+width > w.getX() && x < w.getX()+w.getWidth()) && (prev_y+height <= w.getY() && y+height >= w.getY()) ) //if in right x-range and collide vertically
			{
				foundYCollision = true;
				
				y = w.getY() - height;
				y_collision = 1;
				setYVelocity(0);
			}
			//all other collision tests are dependent on if this is a top-collision-only wall
			if(w.isTopCollisionOnly())
			{
				//test collision above
				if( (x+width > w.getX() && x < w.getX()+w.getWidth()) && (prev_y >= w.getY()+w.getHeight() && y <= w.getY()+w.getHeight()) ) //if in right x-range and collide vertically
				{
					foundYCollision = true;
					
					y = w.getY()+w.getHeight();
					y_collision = -1;
					setYVelocity(0);
				}
				
				//x
				//test collision to right
				if( (y+height > w.getY() && y < w.getY()+w.getHeight()) && (prev_x+width <= w.getX() && x+width >= w.getX()) ) //if in right y-range and collide horizontally
				{
					foundXCollision = true;
					
					x = w.getX() - width;
					x_collision = 1;
					
					//do set velocity stuff without changing the stored velocity
					xi = x;
					ti_x = time;
				}
				//test collision to left
				if( (y+height > w.getY() && y < w.getY()+w.getHeight()) && (prev_x >= w.getX()+w.getWidth() && x <= w.getX()+w.getWidth()) ) //if in right y-range and collide horizontally
				{
					foundXCollision = true;
					
					x = w.getX()+w.getWidth();
					x_collision = -1;
					
					//do set velocity stuff without changing the stored velocity
					xi = x;
					ti_x = time;
				}
			}			
		} //finish looping through walls
		if(!foundXCollision) {x_collision = 0;}
		if(!foundYCollision) {y_collision = 0;}
	}
	
	public void setXVelocity(double vx)//time is the current time in seconds, from when the main animation timer started
	{
		if(x_collision == 1 && vx > 0){return;}
		if(x_collision == -1 && vx < 0){return;}
		
		xi = x;		
		this.vx = vx;
		
		ti_x = time;
	}
	
	public void setYVelocity(double vy)//time is the current time in seconds, from when the main animation timer started
	{
		if(y_collision == 1 && vy > 0){return;}
		if(y_collision == -1 && vy < 0){return;}
		
		yi = y;
		this.vy = vy;
		
		ti_y = time;
	}
	
	public void grabBall(Ball b)
	{
		myBall = Optional.of(b);
		b.pickup(this);
	}
	
	public void shootBall()
	{
		if(myBall.isEmpty()){return;}
	}
	
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public double getWidth()
	{
		return width;
	}
	public double getHeight()
	{
		return height;
	}
}