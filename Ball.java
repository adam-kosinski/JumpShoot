import java.util.Optional;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball
{
	private double x; // (x,y) of center
	private double y;
	private double r; //radius
	private Color color;
	
	private double xi; //initial x position (reset when velocity is reset)
	private double yi; //initial y position (reset when velocity is reset)
	private double vx;
	private double vy;
	
	private double time; //time of most recent updatePosition call
	private double ti_x; //time of most recent x velocity reset
	private double ti_y; //time of most recent y velocity reset
	
	private int x_collision; // -1 means wall to left, 0 means no collision, 1 means wall to right
	private int y_collision; // -1 means wall above, 0 means no collision, 1 means wall below
	
	private boolean dangerous = false;
	private Optional<Player> holder;
	
	private ArrayList<Wall> walls;
	private ArrayList<Ball> balls;
	
	public Ball(double x, double y, double r, Color color)
	{
		this.x = x;
		this.y = y;
		this.r = r;
		this.color = color;
		
		this.xi = x;
		this.yi = y;
		this.vx = 0;
		this.vy = 0;
		
		this.time = 0;
		this.ti_x = 0;
		this.ti_y = 0;
		
		this.x_collision = 0;
		this.y_collision = 0;
		
		this.holder = Optional.empty();
	}
	public void giveObjects(ArrayList<Wall> walls, ArrayList<Ball> balls)
	{
		this.walls = walls;
		this.balls = balls;
	}
	
	public void draw(GraphicsContext ctx)
	{
		ctx.setFill(color);
		ctx.fillOval(x-r, y-r, r*2, r*2);
	}
	
	public void updatePosition(double time, double ay)
	{
		this.time = time;
		
		//if this ball is held, keep pace with the player holding it
		if(holder.isPresent())
		{
			x = holder.get().getX() + (holder.get().getWidth() / 2);
			y = holder.get().getY();
			setXVelocity(0);
			setYVelocity(0);
			return;
		}
		
		//if not held, do normal physics
				
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
			if( (x+r > w.getX() && x-r < w.getX()+w.getWidth()) && (prev_y+r <= w.getY() && y+r >= w.getY()) ) //if in right x-range and collide vertically
			{
				foundYCollision = true;
				
				y = w.getY() - r;
				y_collision = 1;
				setYVelocity(0);
			}
			//all other collision tests are dependent on if this is a top-collision-only wall
			if(w.isTopCollisionOnly())
			{
				//test collision above
				if( (x+r > w.getX() && x-r < w.getX()+w.getWidth()) && (prev_y-r >= w.getY()+w.getHeight() && y-r <= w.getY()+w.getHeight()) ) //if in right x-range and collide vertically
				{
					foundYCollision = true;
					
					y = w.getY()+w.getHeight() + r;
					y_collision = -1;
					setYVelocity(0);
				}
				
				//x
				//test collision to right
				if( (y+r > w.getY() && y-r < w.getY()+w.getHeight()) && (prev_x+r <= w.getX() && x+r >= w.getX()) ) //if in right y-range and collide horizontally
				{
					foundXCollision = true;
					
					x = w.getX() - r;
					x_collision = 1;
					
					//do set velocity stuff without changing the stored velocity
					xi = x;
					ti_x = time;
				}
				//test collision to left
				if( (y+r > w.getY() && y-r < w.getY()+w.getHeight()) && (prev_x-r >= w.getX()+w.getWidth() && x-r <= w.getX()+w.getWidth()) ) //if in right y-range and collide horizontally
				{
					foundXCollision = true;
					
					x = w.getX()+w.getWidth() + r;
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
	
	public void pickup(Player p)
	{
		holder = Optional.of(p);
	}
	public void release()
	{
		holder = Optional.empty();
	}
	
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public double getRadius()
	{
		return r;
	}
	public boolean isDangerous()
	{
		return dangerous;
	}
	public boolean isHeld()
	{
		return holder.isPresent();
	}
}