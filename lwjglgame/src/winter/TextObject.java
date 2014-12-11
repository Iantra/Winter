package winter;

import org.newdawn.slick.Color;
import org.newdawn.slick.util.FontUtils;

public class TextObject extends GameObject{
	private int currText;
	private String[] texts;
	private float progress = 0;
	private float totalTime;
	private boolean isDone;
	public TextObject( float tt, String[] t){
		super(Globals.screenWidth/2, Globals.screenHeight/2);
		totalTime = tt*1000;
		texts = t;
		currText = 0;
		isDone = false;
		setAlpha(0);
	}
	
	public TextObject(float tt, String t){
		this(tt, new String[]{t});
	}
	
	public void update(float _dt){
		if(!isDone){
			if(progress < .5f){
				setAlpha(alpha() + _dt/totalTime);
			}else{
				if(vY() == 0)
					setVY(2);
				setVY(vY() - 8*_dt/totalTime);
				setAlpha(alpha() - 2*_dt/totalTime);
			}
			
			super.update(_dt);
			progress += _dt/totalTime;
			if(progress >= 1){
				progress = 0;
				if(currText < texts.length-1){	
					setX(Globals.screenWidth/2);
					setY(Globals.screenHeight/2);
					setVY(0);
					setAlpha(0);
					currText++;
				}else{
					isDone = true;
					Globals.paused = false;
				}
			}
		}
	}
	
	public void render(){
		
		FontUtils.drawCenter(Assets.dispFont, texts[currText], (int)x(), (int)y(), 0, new Color(1, 1, 1, alpha()));
	}
	
	public boolean isDone(){
		return isDone;
	}
}
