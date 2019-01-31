import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

//animation stuff
import javafx.animation.AnimationTimer;
import javafx.util.Duration;

public class JumpShoot extends Application
{
	//variables to hold objects - players, bullets, surfaces
	private ArrayList<Player> players = new ArrayList<Player>();
	
	//misc. variables
	private int sceneWidth = 600;
	private int sceneHeight = 600;
	private boolean lost = false;
	
	//references to GUI
	private Canvas canvas;
	private GraphicsContext ctx;
	//private int fps = 50;
	
	@Override
	public void init()
	{
		players.add(new Player(100,200,50,50,Color.RED));
	}
	
	@Override
	public void start(Stage primary)
	{
		//load GUI
		
		canvas = new Canvas(600,600);
		ctx = canvas.getGraphicsContext2D();
		
		VBox vbox = new VBox(canvas);
		Scene s = new Scene(vbox,sceneWidth,sceneHeight); //sceneWidth and sceneHeight are state variables of this class
		primary.setScene(s);
		primary.show();
		
		//start animation
		GameAnimation game_animation = new GameAnimation();
		game_animation.start();
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
	
	public class GameAnimation extends AnimationTimer
	{
		//thing to animate
		private Player player;
		
		//physics variables
		private double t_i = -1.0; //inital time in s; handle will define it if it sees that this is -1
		
		private double ay; // px/s^2
		
		public GameAnimation() //2nd and 3rd args are initial velocities, px/s
		{
			super();
			ay = 400; // px/s^2
		}
		
		@Override
		public void handle(long now) //now is the current timestamp in "nanoseconds"
		{				
			if(t_i < 0)
			{
				t_i = (double) now / 10e8;
			}
			
			//get time in seconds
			double t = (double) now / 10e8 - t_i;
			
			//clear canvas
			ctx.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
			
			//loop through players and update their positions and draw them
			for(Player p : players)
			{
				p.updatePosition(t);
				p.draw(ctx);
			}
		}
	}
}