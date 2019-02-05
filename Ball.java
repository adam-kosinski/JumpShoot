import java.util.Optional;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball
{
	private double x; // (x,y) of center
	private double y;
	private double r; //radius
	private Color color;
	
	private boolean dangerous = false;
	private boolean held = false;
	private Optional<Player> holder;
	
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
		//if this ball is held, keep pace with the player holding it
		if(held)
		{
			x = holder.get().getX() + (holder.get().getWidth() / 2);
			y = holder.get().getY();
		}
	}
	
	public void pickup(Player p)
	{
		held = true;
		holder = Optional.of(p);
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
		return held;
	}
}