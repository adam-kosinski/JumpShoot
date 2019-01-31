import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;

//animation stuff
import javafx.animation.AnimationTimer;
import javafx.util.Duration;

//external classes
import Player;

public class JumpShoot extends Application
{
	private ArrayList<Player> players = new ArrayList<Player>();
	private int sceneWidth = 600;
	private int sceneHeight = 600;
	private boolean lost = false;
	private 
	//private int fps = 50;
	
	@Override
	public void init()
	{
		players.add(new Player(0,200,50,50,Color.RED));
	}
	
	@Override
	public void start(Stage primary)
	{
		Group group = new Group();
		for(int i=0; i<players.size(); i++){
			group.getChildren().addAll(players.get(i));
		}
		lost = false;
		
		Scene s = new Scene(group,sceneWidth,sceneHeight); //sceneWidth and sceneHeight are state variables of this class
		primary.setScene(s);
		primary.show();
	}
	
	@Override
	public void stop()
	{}
	
	public void startGame()
	{
		
	}
	
	public void endGame()
	{
		
	}
	
	public class Player extends Rectangle
	{
		
		//variables to hold previous position of the mouse during drags; needed so drags aren't auto-centered on the mouse
		private double mouseX;
		private double mouseY;
		
		//variables to hold change in mouse position / "velocity"
		private double mouseDX = 0; //change in mouseX from last mouse event to this mouse event
		private double mouseDY = 0;
		private double v_scalar = 20; //used to scale mouseDX or mouseDY to get initial velocities when creating a FallAnimation object (see startFall())
		private double max_initial_speed = 400; //needed b/c lag can cause mouseDX or mouseDY to be insanely huge
		
		//variable to hold transition reference
		private FallAnimation animation;
		
		public Player(double x, double y, double width, double height, Color color)
		{
			super(x,y,width,height); //call constructor for Rectangle
			setFill(color);
			setStroke(Color.BLACK);
			
			//event handling
			
			setOnMouseReleased( e ->
			{
				if(lost)
				{
					return;
				}
				//create and start the animation
				animation = new FallAnimation(this, 0, -300);
				animation.start();
			});
			
		}
		
		public void startFall()
		{
			//get initial velocities
			double sqrt_scalar = Math.sqrt(max_initial_speed);
			
			double vx_i = mouseDX*v_scalar;
			if(Math.abs(vx_i) > max_initial_speed)
			{
				vx_i = vx_i<0? -max_initial_speed : max_initial_speed;
			}
			double sign_y = mouseDY >= 0 ? 1 : -1;
			System.out.printf("mouseDY*v_scalar: %f\n",mouseDY*v_scalar);
			double vy_i = sign_y * sqrt_scalar * Math.sqrt(Math.abs(mouseDY)*v_scalar);
			if(Math.abs(vy_i) > max_initial_speed)
			{
				vy_i = vy_i<0? -max_initial_speed : max_initial_speed;
			}
			
			System.out.printf("vx_i: %f, vy_i:%f\n",vx_i,vy_i);
			
			//create and start the animation
			animation = new FallAnimation(this, vx_i, vy_i);
			animation.start();
		}
		
		public void stopFall()
		{
			if(animation != null)
			{
				animation.stop();
			}
		}
		
		public class FallAnimation extends AnimationTimer
		{
			//thing to animate
			private Player player;
			
			//physics variables
			private double t_i = -1.0; //inital time in s; handle will define it if it sees that this is -1
			//private double t_last_frame;
			private double x_i; //initial x position
			private double y_i; //initial y position
			private double vx_i; // px/s
			private double vy_i; // px/s
			private double ay; // px/s^2
			
			public FallAnimation(Player player, double vx_i, double vy_i) //2nd and 3rd args are initial velocities, px/s
			{
				super();
				
				this.player = player;
				
				x_i = player.getX();
				y_i = player.getY();
				this.vx_i = vx_i;
				this.vy_i = vy_i;
				ay = 400;
			}
			
			@Override
			public void handle(long now) //now is the current timestamp in "nanoseconds"
			{				
				if(lost)
				{
					return;
				}
				
				if(t_i < 0)
				{
					t_i = (double) now / 10e8;
					//t_last_frame = 0;
				}
				
				//get time in seconds
				double time = (double) now / 10e8 - t_i;
				
				//handle fps
				//if((time-t_last_frame) * fps >= 1)
				//{					
					//handle x motion
					double dx = vx_i*time;
					player.setX(x_i + dx);

					//handle y motion
					double dy = vy_i*time + 0.5*ay*time*time;
					player.setY(y_i + dy);
					
					//reset last frame time so the fps check works
					//t_last_frame = time;
				//}
				
				if(y_i + dy >= sceneHeight)
				{
					lost = true;
					endGame();
				}
			}
		}
		
	}
}