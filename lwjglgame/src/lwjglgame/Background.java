package lwjglgame;

import java.util.ArrayList;

/**
 * @author Iantra Solari
 * Background class, no real reason to not make it a simple GameObject except for visual separation.
 */

// CURRENT WORKING CLASS THINGY!!!! MAKE GETLOWESTINDEX AND THEN MAKE CHARACTER GENERATE TILES!
public class Background{
	ArrayList<BackgroundChunk> bgs = new ArrayList<BackgroundChunk>();
	public Background(){
		addBackground(0);
		addBackground(1);
		addBackground(-1);
	}
	
	public void addBackground(int index){
		bgs.add(new BackgroundChunk((int)(Constants.WIDTH*(index+.5f)), Constants.HEIGHT/2, (int)Math.round(Math.random()*0.6f), index));
	}
	
	
	
	public void addBackgroundRight(){
		addBackground(getRightestChunk().getIndex() + 1);
	}
	
	public BackgroundChunk getRightestChunk(){
		int highestindex = 0;
		int highestX = 0;
		for(BackgroundChunk b : bgs){
			if(b.x() > highestX){
				highestindex = bgs.indexOf(b);
				highestX = (int)bgs.get(highestindex).x();
			}
		}
		return bgs.get(highestindex);
	}
	
	
	
	public void addBackgroundLeft(){
		addBackground(getLeftestChunk().getIndex() - 1);
	}
	
	public BackgroundChunk getLeftestChunk(){
		int lowestindex = 0;
		int lowestX = 0;
		for(BackgroundChunk b : bgs){
			if(b.x() < lowestX){
				lowestindex = bgs.indexOf(b);
				lowestX = (int)bgs.get(lowestindex).x();
			}
		}
		return bgs.get(lowestindex);
	}
	
	
	
	public void render(){
		for(BackgroundChunk b : bgs){
			b.render();
		}
	}
	
	public void update(float _dt){
		for(BackgroundChunk b : bgs){
			b.update(_dt);
		}
	}
}
