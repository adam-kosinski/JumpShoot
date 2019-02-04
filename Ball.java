import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball
{
	private double x; // (x,y) of center
	private double y;
	private double r; //radius
	private Color color;
	
	private boolean dangerous = false;
	
	public Ball(double x, double y, double r, Color color)
	{
		this.x = x;
		this.y = y;
		this.r = r;
		this.color = color;
	}
	
	public void draw(GraphicsContext ctx)
	{
		ctx.setFill(color);
		ctx.fillOval(x-r, y-r, r, r);
	}
	
	public void updatePosition()
	{
		
	}
	
	public boolean isDangerous()
	{
		return dangerous;
	}
}