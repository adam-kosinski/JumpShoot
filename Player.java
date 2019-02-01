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
	private double ti; //time of most recent velocity reset
	
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
		this.ti = 0;
		
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
	public void updatePosition(double time, double ay) //time is the current time in seconds, from when the main animation timer started
	{
		this.time = time;
		
		//define t to be time since most recent velocity reset
		double t = time - ti;
		
		//if velocity is in opposite direction to collision, no more collision
		if(x_collision == 1 && vx < 0 || x_collision == -1 && vx > 0)
		{
			x_collision = 0;
		}
		if(y_collision == 1 && vy < 0 || y_collision == -1 && vy > 0)
		{
			y_collision = 0;
		}
		
		//move player if no collisions preventing it
		if(x_collision == 0)
		{
			x = xi + vx*t;
		}
		if(y_collision != 1)
		{
			y = yi + vy*t + 0.5*ay*t*t;
		}
		
		//detect platform collision
		//x
		if(x > 400) //placeholder
		{
			x = 400;
			x_collision = 1;
			setVelocity(0,vy);
		}
		
		//y
		if(y > 400) //placeholder
		{
			y = 400;
			y_collision = 1;
			setVelocity(vx,0);
		}
		if(y < 100) //placeholder
		{
			y = 100;
			y_collision = -1;
			setVelocity(vx,0);
		}
	}
	
	public void setVelocity(double vx, double vy)//time is the current time in seconds, from when the main animation timer started
	{
		xi = x;
		yi = y;
		
		this.vx = vx;
		this.vy = vy;
		
		ti = time;
	}
	
	
}