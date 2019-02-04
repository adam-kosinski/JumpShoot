import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wall
{
	private double x; //x,y of top left coord
	private double y;
	private double width;
	private double height;
	private Color color;
	
	private boolean top_collision_only; //if true, only stuff falling from above will interact with the wall
	
	public Wall(double x, double y, double width, double height, Color color, boolean top_collision_only)
	{
		this.x = x; //(x,y) of top left corner
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.top_collision_only = top_collision_only;
	}
	
	public void draw(GraphicsContext ctx)
	{
		ctx.setFill(color);
		ctx.fillRect(x,y,width,height);
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
	public boolean isTopCollisionOnly()
	{
		return top_collision_only;
	}
}