package lwjglgame;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;

/**
 * @author Iantra Solari
 * Main character class. Special rendering and updating. Kinda.
 */
public class Character extends GameObject{
	private Texture walkingTexture = Assets.walkingCharImg;
	private Texture standingTexture = Assets.standingCharImg;
	private float frameTime = 0;
	private boolean isJumping = true;
	private boolean isRunning = false;
	private int frame = 1;
	private int dir = 1;
	public Character(){
		super(Constants.WIDTH-145, Globals.screenHeight/2,  Assets.standingCharImg.getImageWidth()/6, Assets.standingCharImg.getImageWidth()/6, Assets.standingCharImg);

	}
	
	public void update(float _dt){
		if(y() < Globals.screenHeight*13/20-0.2f){
			setVY(vY()+_dt/40f);
		}
		if(isRunning()) setVX(vX()*2);
		super.update(_dt);
		if(isRunning()) setVX(vX()/2);
		while(y() > Globals.screenHeight*13/20){
			setVY(0);
			setY(y()-0.1f);
			isJumping = false;
		}
		frameTime += _dt;
		
		if(vX() != 0){
			setTexture(walkingTexture);
			if(frameTime >= 133){
				frame += 1;
				frameTime = 0;
				if(frame > 6){
					frame = 1;
				}
				if(dir == -1 && frame > 5){
					frame = 0;
				}
			}
			
			if(vX() > 0)
				dir = 1;
			if(vX() < 0)
				dir = -1;
		}else{
			setTexture(standingTexture);
			frame = (dir == 1) ? 1 : 0;
		}
		if(x() > Globals.background.getRightestChunk().x()){
			Globals.background.addBackgroundRight();
		}
		if(x() < Globals.background.getLeftestChunk().x()){
			Globals.background.addBackgroundLeft();
		}
	}
	
	public void render(){
		float xs = ((float)texture().getImageWidth()/(float)texture().getTextureWidth())/6;
		float ys = (float)texture().getImageHeight()/(float)texture().getTextureHeight();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glPushMatrix();
		GL11.glPopMatrix();
			texture().bind();
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(xs*(frame-dir),ys);
				GL11.glVertex2f(x()-width()/2,y()+height()/2);
				
				GL11.glTexCoord2f(xs*(frame-dir),0);
				GL11.glVertex2f(x()-width()/2,y()-height()/2);
				
				GL11.glTexCoord2f(xs*frame,0);
				GL11.glVertex2f(x()+width()/2,y()-height()/2);
				
				GL11.glTexCoord2f(xs*frame,ys);
				GL11.glVertex2f(x()+width()/2,y()+height()/2);
			GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public boolean isJumping(){
		return isJumping;
	}
	
	public void setJumping(boolean j){
		isJumping = j;
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
	public void setRunning(boolean r){
		isRunning = r;
	}
	
}
