package winter;

import org.newdawn.slick.opengl.Texture;

public class GUIObject extends GameObject{
	private String text = "what";
	private float tX, tY;
	public GUIObject(float x, float y, Texture t){
		super(x, y, t);
		setTX(x+width()/2);
		setTY(y-8);
	}
	
	public void setText(String t){
		text = t;
	}
	
	public String getText(){
		return text;
	}
	
	public void setTX(float t){
		tX = t;
	}
	
	public void setTY(float t){
		tY = t;
	}
	
	public float tX(){
		return tX;
	}
	
	public float tY(){
		return tY;
	}
}
