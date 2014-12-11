package winter;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * @author Iantra Solari
 * Screen fading filter class. Pretty complex and stuff (not really).
 */
public class ScreenFade extends GameObject {
	private float r, g, b;
	private int fadeType;
	private float fSpeed;
	boolean isVisible;
	public ScreenFade(float red, float green, float blue){
		super(0, 0, Assets.solid);
		r = red;
		g = green;
		b = blue;
		setAlpha(1f);
		isVisible = true;
		fadeType = 0;
	}
	
	public void fadeIn(float s){
		fadeType = 1;
		fSpeed = .005f*s;
	}
	public void fadeOut(float s){
		fadeType = 2;
		fSpeed = .005f*s;
	}
	public void update(float _dt){
		switch (fadeType){
		case 1: if(alpha() < 1)setAlpha(alpha() + fSpeed*_dt);
				break;
		case 2: if(alpha() > 0)setAlpha(alpha() - fSpeed*_dt *alpha());
				break;
		default: break;
		}
		if(alpha() <= 0){
			isVisible = false;
		}else{
			isVisible = true;
		}
	}
	public void setColor(int r, int g, int b){
		setColor(r/255f, g/255f, b/255f);
	}
	public void setColor(float red, float green, float blue){
		r = red;
		g = green;
		b = blue;
	}
	public void render(){
		if(isVisible){
			Assets.solid.bind();
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glColor4f(r, g, b, alpha());
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex2f(0,Globals.screenHeight);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex2f(0,0);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex2f(Globals.screenWidth,0);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex2f(Globals.screenWidth,Globals.screenHeight);
			GL11.glEnd();
		}
	}
}
