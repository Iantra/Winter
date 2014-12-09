package winter;

import java.util.ArrayList;

/**
 * @author Iantra Solari
 * Class to store all global variables that aren't constants.
 */
public class Globals {
	//Score keeping
	public static int[] items;
	
	//Miscellaneous game info
	public static int screenWidth, screenHeight;
	public static int fps;
	public static float gameTime;
	public static float snowStrength;
	public static float wind;
	public static boolean paused;
	
	
	//Game objects
	public static GameObject camera;
	public static GameObject vignette;
	public static GameObject coldFilter;
	public static Background background;
	public static Character character;
	public static ArrayList<Snowflake> snowflakes;
	public static ScreenFade sFade;
	public static GUI gui;
	
	
	//Simple class that just initializes all the global variables.
	public static void load(){
		items = new int[]{0,2,0,0};
		
		fps = 120;
		paused = false;
		snowStrength = 5;
		wind = 0;
		gameTime = 60*60*6; //6 AM, in seconds
		
		camera = new GameObject(0,0);
		vignette = new GameObject(screenWidth/2, screenHeight/2, screenWidth, screenHeight, Assets.vignette);
		coldFilter = new GameObject(screenWidth/2, screenHeight/2, screenWidth, screenHeight, Assets.coldFilter);
		background = new Background();
		character = new Character();
		snowflakes = new ArrayList<Snowflake>();
		for(int i = 0; i < 100*Globals.snowStrength; i++){
			snowflakes.add(new Snowflake(Snowflake.getRandomX(), (float)(Math.random()*Globals.screenHeight)));
			snowflakes.get(i).setVY(.2f+(float)Math.random()/4);
		}
		sFade = new ScreenFade(0, 0, 0);
		sFade.fadeOut(.02f);
		
		
		
		gui = new GUI();
	}
	
	//Public snowflake initialization so I can re-initialize them when changing the amount of snow
	public static void updateSnowStrength(){
		float ss = 100*Globals.snowStrength;
		if(snowflakes.size() < ss){
			for(int i = snowflakes.size(); i < ss; i++){
				snowflakes.add(new Snowflake(Snowflake.getRandomX(), (float)(Math.random()*-Globals.screenHeight/3)));
				snowflakes.get(i).setVY(.2f+(float)Math.random()/4);
			}
		}/*else{
			System.out.println("removed snowflake");
			for(int i = (int)Math.floor(ss); i < snowflakes.size(); i++){
					snowflakes.remove(i);
					i--;
			}
			System.out.println(snowflakes.size()+", "+ss);
		}*/
	}
}
