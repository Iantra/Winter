package winter;

import org.newdawn.slick.opengl.Texture;

public class GUIObject extends GameObject{
	private String text = "what";
	private boolean clickable;
	private boolean shouldShow;
	private int index;
	private int[] reqs;
	private float tX, tY;
	public GUIObject(float x, float y, Texture t, int i, boolean c, int[] r){
		super(x, y, t);
		setTX(x+width()/2);
		setTY(y-8);
		index = i;
		clickable = c;
		reqs = r;
		setAlpha(1);
	}
	
	public void update(float _dt){
		super.update(_dt);
		shouldShow = true;
		if(Globals.items[index] < 1)
		shouldShow = canMake();
	}
	
	public boolean canMake(){
		for(int i = 0; i < reqs.length; i++){
			if(Globals.items[i] < reqs[i]){
				return false;
			}
		}
		return true;
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
	
	public boolean shouldShow(){
		return shouldShow;
	}
	
	public boolean clickable(){
		return clickable;
	}
	
	public void click(int[] a){
		for(int i = 0; i < a.length; i++){
			Globals.items[i] -= a[i];
		}
		Globals.items[index]++;
	}
}
