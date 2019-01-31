import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player
{
	private double x;
	private double y;
	private double vx;
	private double vy;
	
	//trajectory variables
	private boolean falling; //stores whether the player is free falling
	private double t_traj_start; //stores the time since the player started free falling
	
	private double width;
	private double height;
	private Color color;
	
	public Player(double x,double y,double width,double height,Color color)
	{
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = 0;
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
	public void updatePosition(double t) //t is the current time in seconds, from when the main animation timer started
	{
		x += 1;
	}
}