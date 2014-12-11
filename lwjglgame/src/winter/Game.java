package winter;

import java.util.Iterator;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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
	private boolean wasMousePressed = false;
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
            Display.sync(60);
            
            //All my beautiful, hand-made update methods - Keys first, then game objects, then fps, and finally render everything.
            updateKeys();
            update(getDelta());
            updateTutorial();
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
        	float rr = .2f+((float)Math.sin((2*Math.PI)/Constants.SECONDS_IN_DAY*(Globals.gameTime-7*60*60))+1)*.4f;
        	float gr = .2f+((float)Math.sin((2*Math.PI)/Constants.SECONDS_IN_DAY*(Globals.gameTime-7*60*60))+1)*.3f;
        	float br = .3f+((float)Math.pow(Math.sin((2*Math.PI)/Constants.SECONDS_IN_DAY*(Globals.gameTime-7*60*60)),3)+1)*.3f;
        	Globals.r = rr + (Globals.r - rr)/1.4f;
        	Globals.g = gr + (Globals.g - gr)/1.4f;
        	Globals.b = br + (Globals.b - br)/1.4f;
        }else{
        	Globals.r = .2f+ (Globals.r - .2f)/1.4f;
        	Globals.g = .2f+ (Globals.g - .2f)/1.4f;
        	Globals.b = .2f+ (Globals.b - .2f)/1.4f;
        }
        
        GL11.glColor3f(Globals.r, Globals.g, Globals.b);
        
        
        
        
        //Draw stuff
        Globals.background.render();
        Globals.foreground.render();  
        for(GameObject f : Globals.fires){
        	f.setTexture(Assets.fires[(int)(Math.random()*3)]);
        	f.render();
        }
        Globals.character.render();
        for(Snowflake s : Globals.snowflakes){
			s.render();
		}
        
        //Move scene back to default position
        GL11.glTranslatef(-Globals.camera.x(), -Globals.camera.y(), 0);
        
        //Draw vignette and cold filter over scene
        Globals.vignette.render();
        Globals.coldFilter.render();
        
        //Call base GUI draw method, which then picks which parts of the GUI to render by itself
        Globals.gui.render();
        
        if(Globals.debug){
	        //Draw FPS and time over everything else
	        Assets.guiFont.drawString(10, 10, "FPS: "+Globals.fps);
	        Assets.guiFont.drawString(10, 22, "Time: "+(int)(Globals.gameTime/60/60)+":"+(int)(Globals.gameTime/60)%60);

	        //Draw debug screen
        	Assets.guiFont.drawString(10, 34, "Snow strength: "+Globals.snowStrength);
        	Assets.guiFont.drawString(10, 46, "Snowflake #: "+Globals.snowflakes.size());
        }
        //If there's a screen fade, draw it
        Globals.sFade.render();
        
        //Render text cues on top of everything, if done proceed to next step in tutorial.
        if(!Globals.to.isDone())
        	Globals.to.render();
        else if(Globals.tutStep == 0){
        	Globals.tutStep++;
        	Globals.sFade.fadeOut(0.05f);
        }
        
    }
    
    //Little method for updating tutorial stages.
    private void updateTutorial(){
    	if(Globals.tutStep == 1){
			for(int i : Globals.items){
				if(i > 0){
					Globals.tutStep++;
					Globals.paused = true;
					Globals.to = new TextObject(3, new String[]{"You picked up an item.", "Good job.", "You can look at it if you press 'i'."});
				}
			}
		}
    	
    	if(Globals.tutStep == 2){
			if(Globals.gameTime/60/60 > 19.5){
					Globals.tutStep++;
					Globals.paused = true;
					Globals.to = new TextObject(3, new String[]{"It's getting late.", "And cold.", "You can start a fire if you press 'f'."});
			}
		}
    	
    	if(Globals.gameOver){
    		Globals.paused = true;
    		if( Globals.to.isDone()){
    			System.exit(42);
    		}
    	}
    }
    
    
    //My method for checking for key events. Again, some stuff is from the internet and some stuff is hand-made and beautiful.
    private void updateKeys(){
    	//Mouse check in the key events because why not
    	if(!Mouse.isButtonDown(0) && wasMousePressed){
    		wasMousePressed = false;
    		Globals.gui.click();
    	}
    	if(Mouse.isButtonDown(0)){
    		wasMousePressed = true;
    	}
    	
    	
    	
    	//You can figure it all out. I trust you. You got this.
    	while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                	if(!Globals.paused){
                		Globals.character.setVX(-1);
                	}else if(Globals.snowStrength > 0){
                		Globals.gameTime -= 60*30;
                		Globals.updateSnowStrength();
                	}
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                	if(!Globals.paused){
                		Globals.character.setVX(1);
                	}else{
						Globals.gameTime += 60*30;
						Globals.updateSnowStrength();
                	}
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_W && !Globals.character.isJumping()) {
                	Globals.character.setVY(-7);
                	Globals.character.setJumping(true);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_LSHIFT){
                	Globals.character.setRunning(true);
                }
            }
            else {
                if (Keyboard.getEventKey() == Keyboard.KEY_A && Globals.character.vX() == -1) {
                	Globals.character.setVX(0);
                	Globals.camera.setVX(0);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_D && Globals.character.vX() == 1) {
                	Globals.character.setVX(0);
                	Globals.camera.setVX(0);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_LSHIFT){
                	Globals.character.setRunning(false);
                }
                
                if(Keyboard.getEventKey() == Keyboard.KEY_RBRACKET){
            		Globals.debug = !Globals.debug;
                }
                
                if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && !Globals.gameOver){
            		Globals.paused = !Globals.paused;
                }
                
                if(Keyboard.getEventKey() == Keyboard.KEY_Q && Globals.paused){
                	terminate();
                }
                
                if(Keyboard.getEventKey() == Keyboard.KEY_I){
                	if(Globals.guiState == 1)
                		Globals.guiState = 0;
                	else
                		Globals.guiState = 1;
                		
                }
                
                if(Keyboard.getEventKey() == Keyboard.KEY_F){
                	if(Globals.guiState == 2){
                		Globals.paused = false;
                		Globals.guiState = 0;
                	}else{
                		Globals.paused = true;
                		Globals.guiState = 2;
                	}
                		
                }
                
                if(Keyboard.getEventKey() == Keyboard.KEY_R){
                	if(Globals.guiState == 3){
                		Globals.paused = false;
                		Globals.guiState = 0;
                	}else{
                		Globals.paused = true;
                		Globals.guiState = 3;
                	}
                		
                }
            }
        }
    }
    
    //My own little update method. It's beautiful and perfect.
    private void update(int _dt) {
    	//Only update game if it isn't paused
    	if(!Globals.paused){
    		
    		//Update game time keeping variables along with environmental changes
    		updateGameTime(_dt);
            Globals.snowStrength = 3f*((float)Math.sin((2*Math.PI)/Constants.SECONDS_IN_DAY*(Globals.gameTime+60*60*6))+1);
            Globals.wind = -Globals.snowStrength/6f;
            if(Globals.character.warmth() >= 0.05)
            	if(Globals.character.warmth() <= 1)
            		Globals.character.setWarmth(Globals.character.warmth() - (Globals.snowStrength-3f)*_dt/100000f);
            	else
            		Globals.character.setWarmth(1);
            Globals.coldFilter.setAlpha(1-Globals.character.warmth());
            SoundStore.get().setCurrentMusicVolume((float)Math.pow(Globals.wind*2, 3f)/10f);
            Globals.updateSnowStrength();
    		 
    		//Move camera according to character position
    		if(Globals.character.vX() <= -1 && Globals.character.x()+Globals.camera.x() < Globals.screenWidth/2-64) Globals.camera.setVX(1);
 			else if(Globals.character.vX() >= 1 && Globals.character.x()+Globals.camera.x() > Globals.screenWidth/2+64) Globals.camera.setVX(-1);
 			else Globals.camera.setVX(0);
    		
	        //Update game objects
	    	Globals.sFade.update(_dt);
	    	
	    	if(Globals.character.isRunning()) Globals.camera.setVX(Globals.camera.vX()*(Globals.character.energy()+1));
	    	Globals.camera.update(_dt);
	    	if(Globals.character.isRunning()) Globals.camera.setVX(Globals.camera.vX()/(Globals.character.energy()+1));
	    	
	        Globals.character.update(_dt);
	        Globals.background.update(_dt);
	        Globals.foreground.update(_dt);
	        Globals.gui.update(_dt);
	        Iterator<Snowflake> iterator = Globals.snowflakes.iterator();
	       	while(iterator.hasNext()){
	       		Snowflake s = iterator.next();
	       		s.update(_dt);
	       		if(s.shouldRemove)
	       			iterator.remove();
	        }
    	}
    	Globals.to.update(_dt); //Update text cues even when paused;
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