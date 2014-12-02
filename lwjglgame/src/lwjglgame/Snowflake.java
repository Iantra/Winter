package lwjglgame;

/**
 * @author Iantra Solari
 * Snowflake class. Special update method and an additional method to get a random X position.
 */
public class Snowflake extends GameObject{
	public boolean shouldRemove;
	public Snowflake(float x, float y) {
		super(x, y, Assets.snowImg);
	}
	
	public void update(double _dt){
		setVX(Globals.wind+(((float)Math.random()-.5f)/2));
		super.update(_dt);
		
		if(y() > Globals.screenHeight){
			resetY();
		}
		if(x()+Globals.camera.x() > Globals.screenWidth){
			setX(-Globals.camera.x());
		}
		if(x()+Globals.camera.x() < 0){
			setX(-Globals.camera.x() + Globals.screenWidth);
		}
		
	}
	
	private void resetY(){
		float ss = 100*Globals.snowStrength;
		if(Globals.snowflakes.size() > ss){
			shouldRemove = true;
		}
		setX(getRandomX());
		setY(-10);
	}
	
	public static float getRandomX(){
		return -Globals.camera.x() + (float)(Math.random()*Globals.screenWidth);
	}

}
