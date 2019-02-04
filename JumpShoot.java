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
		players.add(new Player(100,200,50,50,Color.RED));
		players.get(0).bindKeys(KeyCode.E, KeyCode.S, KeyCode.F, KeyCode.D, KeyCode.Q);
		players.add(new Player(400,200,50,50,Color.GREEN));
		players.get(1).bindKeys(KeyCode.U, KeyCode.H, KeyCode.K, KeyCode.J, KeyCode.Y);
		
		walls.add(new Wall(20,400,200,30,Color.BROWN, true));
		walls.add(new Wall(300,500,200,30,Color.BROWN, false));
		walls.add(new Wall(0,570,600,30,Color.BROWN, false));
		
		balls.add(new Ball(300, 100, 50, Color.BLUE));
		
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
		
		private double ay = 750; // px/s^2
		
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
			
			//loop through players and update their positions and draw them
			for(Player p : players)
			{
				p.updatePosition(t, ay, walls);
				p.draw(ctx);
			}
			
			//loop through balls and draw them
			for(Ball b : balls)
			{
				b.updatePosition();
				b.draw(ctx);
			}
			
		}
	}
}