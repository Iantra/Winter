package lwjglgame;

import java.awt.event.WindowEvent;
import java.awt.image.SinglePixelPackedSampleModel;
import java.util.Iterator;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.openal.SoundStore;

/**
 * @author Iantra Solari
 * Main class of the game, initializes everything and executes all game functions.
 * KEY:
 * //Methods made by me and commented by me
 * //- Methods and comments from the internet
 */
public class Game {
	
	
	//Some variables to keep track of game speed.
	private long lastFrame, lastFPS;
	private int fps;
	
	public static void main(String[] args){
		new Game();		//Everything is initialized in the constructor, no need to put anything else in main method.
	}
	
	
	private Game(){
         
		//Create and initialize a new fullscreen window.
		specifiedCreate();
		init();
        initGL();
        getDelta();		//Initialize the delta time for fps calculations
        
        //While the window isn't closed, loop through the main game loop.
        while (!Display.isCloseRequested()) {
        	//Sync the display, that's all LWJGL and I have no idea how it works.
            Display.sync(120);
            
            //All my beautiful, hand-made update methods - Keys first, then game objects, then fps, and finally render everything.
            updateKeys();
            update(getDelta());
            updateFPS();
            render();
            
            //More LWJGL magic. Don't ask me how any of this s#*% works.
            Display.update();
        }
        
        terminate();
	}
	
     
    //- Use a specified version of OpenGL - namely version 3.2
    private void specifiedCreate() {
        PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
        	.withProfileCompatibility(true);
        	//NO forward compatibility. Forward compatibility breaks everything. It's the devil. The literal devil. Literally.
         
      //So much LWJGL magic. Whatever.
        try {
            Display.setDisplayMode(Display.getDesktopDisplayMode());
            Display.setFullscreen(true);
            Display.setTitle("No escape");
            Display.create(pixelFormat, contextAtrributes);
        } catch (LWJGLException e) {
            e.printStackTrace();
            terminate(-1);
        }
    }
    
    //My own little beautiful initializing method. It initializes all the needed assets and variables.
    private void init(){
    	Assets.load();
    	Globals.screenWidth = Display.getWidth()/3;
    	Globals.screenHeight = Display.getHeight()/3;
        Globals.load();
        lastFPS = getTime();
    }
    
    
    //And then there's this monster franken-method that I dug up from the darkest depths of online tutorials and forums. Also, openGL magic. SO much openGL magic.
    private void initGL() {
    	GL11.glEnable(GL11.GL_TEXTURE_2D);               
        
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);          
         
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Globals.screenWidth, Globals.screenHeight,0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
    
    //My rendering method. Some parts are taken from the internet, a lot of it is hand-made and perfect and beautiful no matter what you say.
    private void render() {
    	
        //- Clear The Screen And The Depth Buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        //Move entire world to camera position
        GL11.glTranslatef(Globals.camera.x(), Globals.camera.y(), 0);
        	
        //Set scene tone
        if(!Globals.paused){
        	
        	//Calculate tone from game time
        	float r, g, b;
        	r = .2f+((float)Math.sin((2*Math.PI)/Constants.SECONDS_IN_DAY*(Globals.gameTime-7*60*60))+1)*.4f;
        	g = .2f+((float)Math.sin((2*Math.PI)/Constants.SECONDS_IN_DAY*(Globals.gameTime-7*60*60))+1)*.3f;
        	b = .3f+((float)Math.pow(Math.sin((2*Math.PI)/Constants.SECONDS_IN_DAY*(Globals.gameTime-7*60*60)),3)+1)*.3f;
        	GL11.glColor3f(r, g, b);
        	
        }else{
        	GL11.glColor3f(.2f, .2f, .2f);
        }
        
        //Draw stuff
        Globals.background.render();          
        Globals.character.render();
        for(Snowflake s : Globals.snowflakes){
			s.render();
		}
        
        //Move scene back to default position
        GL11.glTranslatef(-Globals.camera.x(), -Globals.camera.y(), 0);
        
        //Draw vignette over scene
        Globals.vignette.render();
        
        //Draw FPS and time over everything else
        Assets.font.drawString(10, 10, "FPS: "+Globals.fps);
        Assets.font.drawString(10, 22, "Time: "+(int)(Globals.gameTime/60/60)+":"+(int)(Globals.gameTime/60)%60);
        //Assets.font.drawString(10, 34, "Red: "+(.2f+((float)Math.sin((2*Math.PI)/Constants.SECONDS_IN_DAY*(Globals.gameTime-7*60*60))+1)*.4f));
        //Assets.font.drawString(10, 46, "Blue: "+(.4f+((float)Math.sin((2*Math.PI)/Constants.SECONDS_IN_DAY*(Globals.gameTime-8*60*60))+1)*.3f));
        
        //Draw debug screen
        if(Globals.paused){
        	Assets.font.drawString(10, 34, "Snow strength: "+Globals.snowStrength);
        	Assets.font.drawString(10, 46, "Snowflake #: "+Globals.snowflakes.size());
        }
        //If there's a screen fade, draw it
        Globals.sFade.render();
    }
    
    //My method for checking for key events. Again, some stuff is from the internet and some stuff is hand-made and beautiful.
    private void updateKeys(){
    	//You can figure it all out. I trust you. You got this.
    	while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
                	if(!Globals.paused){
                		Globals.character.setVX(-1);
                	}else if(Globals.snowStrength > 0){
                		Globals.gameTime -= 60*30;
                		Globals.updateSnowStrength();
                	}
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
                	if(!Globals.paused){
                		Globals.character.setVX(1);
                	}else{
						Globals.gameTime += 60*30;
						Globals.updateSnowStrength();
                	}
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_UP && !Globals.character.isJumping()) {
                	Globals.character.setVY(-7);
                	Globals.character.setJumping(true);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_LSHIFT){
                	Globals.character.setRunning(true);
                }
            }
            else {
                if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Globals.character.vX() == -1) {
                	Globals.character.setVX(0);
                	Globals.camera.setVX(0);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Globals.character.vX() == 1) {
                	Globals.character.setVX(0);
                	Globals.camera.setVX(0);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_LSHIFT){
                	Globals.character.setRunning(false);
                }
                
                if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
            		Globals.paused = !Globals.paused;
                }
                if(Keyboard.getEventKey() == Keyboard.KEY_Q && Globals.paused){
                	terminate();
                }
            }
        }
    }
    
    //My own little update method. It's beautiful and perfect.
    private void update(int _dt) {
    	//For debugging, update the game time and snowflakes even if paused
    	updateGameTime(_dt);
        Globals.snowStrength = 3f*((float)Math.sin((2*Math.PI)/Constants.SECONDS_IN_DAY*(Globals.gameTime+60*60*6))+1);
        Globals.wind = Globals.snowStrength/6f;
        SoundStore.get().setCurrentMusicVolume((float)Math.pow(Globals.wind*2, 3f)/10f);
        Globals.updateSnowStrength();
    	//Only update game objects if the game isn't paused
    	if(!Globals.paused){
    		 
    		//Move camera according to character position
    		if(Globals.character.vX() == -1 && Globals.character.x()+Globals.camera.x() < Globals.screenWidth/2-64) Globals.camera.setVX(1);
 			else if(Globals.character.vX() == 1 && Globals.character.x()+Globals.camera.x() > Globals.screenWidth/2+64) Globals.camera.setVX(-1);
 			else Globals.camera.setVX(0);
    		
	        //Update stuff
	    	Globals.sFade.update(_dt);
	    	if(Globals.character.isRunning()) Globals.camera.setVX(Globals.camera.vX()*2);
	    	Globals.camera.update(_dt);
	    	if(Globals.character.isRunning()) Globals.camera.setVX(Globals.camera.vX()/2);
	        Globals.character.update(_dt);
	        Iterator<Snowflake> iterator = Globals.snowflakes.iterator();
	       	while(iterator.hasNext()){
	       		Snowflake s = iterator.next();
	       		s.update(_dt);
	       		if(s.shouldRemove)
	       			iterator.remove();
	        }
    	}
    }
    
    //A little fps function taken pretty much directly from the LWJGL timing tutorial
    private void updateFPS() {
        if (getTime() - lastFPS > 1000) {
        	Globals.fps = fps;
            fps = 0; //- reset the FPS counter
            lastFPS += 1000; //- add one second
        }
        fps++;
    }
    
    //A delta time function also taken from the LWJGL timing tutorial
    private int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;
             
        return delta;
    }
    
    //A function to get the current time. Can you guess where I got it? Yup. LWJGL timing tutorial.
    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    
    //A function to transfer irl time to game time. I actually made this myself. Isn't it pretty.
    private void updateGameTime(float _dt){
    	Globals.gameTime += (Constants.SECONDS_IN_DAY / Constants.DAY_LENGTH) * _dt/1000;
    	
    	//Confine the game time between 0 and 1 day
    	if(Globals.gameTime > Constants.SECONDS_IN_DAY){
    		Globals.gameTime = 0;
    	}
    	if(Globals.gameTime < 0){
    		Globals.gameTime = 0;
    	}
    }
    
    //A simple function to terminate the program
    private void terminate(){
    	terminate(0);
    }
    
    private void terminate(int err){
    	Display.destroy();
        System.exit(err);
    }
}