import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player
{
	private double x;
	private double xi; //initial x position (reset when velocity is reset)
	private double y;
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
	
	public Player(double x,double y,double width,double height,Color color)
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
	}
	
	public void draw(GraphicsContext ctx)
	{
		ctx.setFill(color);
		ctx.fillRect(x,y,width,height);
	}
	
	//function to update player's position
	public void updatePosition(double time, double ay, ArrayList<Wall> walls) //time is the current time in seconds, from when the main animation timer started
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
		if(y_collision == 0)
		{
			y = yi + vy*t_y + 0.5*ay*t_y*t_y;
		}
		
		//detect wall collision
		//loop through walls
		for(Wall w : walls)
		{
			//x
			if(x > 400) //placeholder
			{
				x = 400;
				x_collision = 1;
				setXVelocity(0);
			}
			
			//y
			//test collision below
			if( (x+width > w.getX() && x < w.getX()+w.getWidth()) && (prev_y+height <= w.getY() && y+height >= w.getY()) ) //if on top of wall && if at height for collision
			{
				y = w.getY() - height;
				y_collision = 1;
				setYVelocity(0);
			}
			else
			{
				y_collision = 0; //this will break for multiple walls
			}
			
			
			if(y < 100) //placeholder
			{
				y = 100;
				y_collision = -1;
				setYVelocity(0);
			}
		}
	}
	
	public void setXVelocity(double vx)//time is the current time in seconds, from when the main animation timer started
	{
		xi = x;		
		this.vx = vx;
		
		ti_x = time;
	}
	
	public void setYVelocity(double vy)//time is the current time in seconds, from when the main animation timer started
	{
		yi = y;
		this.vy = vy;
		
		ti_y = time;
	}
	
	
}