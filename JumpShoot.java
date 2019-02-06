import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.KeyCode;

//animation stuff
import javafx.animation.AnimationTimer;
import javafx.util.Duration;

public class JumpShoot extends Application
{
	//variables to hold objects - players, bullets, surfaces
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	
	//references to GUI
	private Canvas canvas;
	private GraphicsContext ctx;
	
	//misc
	private double ay = 375; //gravitational acceleration, px/s^2
	
	@Override
	public void init()
	{
	}
	
	@Override
	public void start(Stage primary)
	{
		//load GUI
		
		canvas = new Canvas(600,600);
		ctx = canvas.getGraphicsContext2D();
		
		VBox vbox = new VBox(canvas);
		Scene s = new Scene(vbox,600,600); //sceneWidth and sceneHeight are state variables of this class
		primary.setScene(s);
		primary.show();
		
		//create objects
		players.add(new Player(100,200,25,25,Color.RED,ay));
		players.get(0).bindKeys(KeyCode.R, KeyCode.D, KeyCode.F, KeyCode.G, KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3);
		players.get(0).giveObjects(walls,balls);
		
		players.add(new Player(200,200,25,25,Color.GREEN,ay));
		players.get(1).bindKeys(KeyCode.UP, KeyCode.LEFT, KeyCode.DOWN, KeyCode.RIGHT, KeyCode.M, KeyCode.COMMA, KeyCode.PERIOD);
		players.get(1).giveObjects(walls,balls);
		
		walls.add(new Wall(20,450,200,15,Color.BROWN, false));
		walls.add(new Wall(300,500,200,15,Color.BROWN, true));
		
		walls.add(new Wall(0,570,600,30,Color.BROWN, false)); //floor
		walls.add(new Wall(-10,0,10,700,Color.BROWN, false)); //left wall
		walls.add(new Wall(600,0,10,700,Color.BROWN, false)); //right wall
		
		balls.add(new Ball(50, 100, 10, Color.BLUE,ay));
		
		for(Player p : players)
		{
			p.giveObjects(walls,balls);
		}
		for(Ball b : balls)
		{
			b.giveObjects(walls,balls);
		}
		
		//handle event listening
		s.setOnKeyPressed( e ->
		{
			KeyCode key = e.getCode();
			for(Player p : players)
			{
				p.handleKeyPress(key);
			}
		});
		s.setOnKeyReleased( e ->
		{
			KeyCode key = e.getCode();
			for(Player p : players)
			{
				p.handleKeyRelease(key);
			}
		});
		
		//start animation
		GameAnimation game_animation = new GameAnimation();
		game_animation.start();
	}
	
	@Override
	public void stop()
	{}
	
	public class GameAnimation extends AnimationTimer
	{
		
		//physics variables
		private double t_i = -1.0; //inital time in s; handle will define it if it sees that this is -1
		
		public GameAnimation()
		{
			super();
		}
		
		@Override
		public void handle(long now) //now is the current timestamp in "nanoseconds"
		{				
			//if we just started the animation, set the initial time
			if(t_i < 0)
			{
				t_i = (double) now / 10e8;
			}
			
			//get time in seconds
			double t = (double) now / 10e8 - t_i;
			
			//clear canvas
			ctx.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
			
			//loop through walls and draw them
			for(Wall w : walls)
			{
				w.draw(ctx);
			}
			
			//loop through balls and draw them - NOTE: doing ball position before players will cause a lag in position when carrying balls... which looks cool!!!
			for(Ball b : balls)
			{
				b.updatePosition(t);
				//only draw held balls now, b/c they should appear behind the players. Non-held balls will be drawn later, in front of the players
				if(b.isHeld())
				{
					b.draw(ctx);
				}
			}
			
			//loop through players and update their positions and draw them
			for(Player p : players)
			{
				p.updatePosition(t);
				p.draw(ctx);
			}
			
			//draw non-held balls
			for(Ball b : balls)
			{
				if(!b.isHeld())
				{
					b.draw(ctx);
				}
			}
			
		}
	}
}