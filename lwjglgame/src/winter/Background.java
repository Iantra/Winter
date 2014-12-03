package winter;

import java.util.ArrayList;

/**
 * @author Iantra Solari
 * Background class, manages background chunks and stuff.
 */

public class Background{
	ArrayList<BackgroundChunk> bgs = new ArrayList<BackgroundChunk>();
	public Background(){
		addBackground(0);
	}
	
	public void addBackground(int index){
		bgs.add(new BackgroundChunk((int)(Constants.WIDTH*(index+.5f)), Constants.HEIGHT/2, (int)Math.round(Math.random()*0.6f), index));
	}
	
	
	
	public void addBackgroundRight(){
		addBackground(getRightestChunk().getIndex() + 1);
	}
	
	public BackgroundChunk getRightestChunk(){
		int highestindex = 0;
		int rindex = 0;
		for(BackgroundChunk b : bgs){
			if(b.getIndex() > highestindex){
				rindex = bgs.indexOf(b);
				highestindex = b.getIndex();
			}
		}
		return bgs.get(rindex);
	}
	
	
	
	public void addBackgroundLeft(){
		addBackground(getLeftestChunk().getIndex() - 1);
	}
	
	public BackgroundChunk getLeftestChunk(){
		int lowestindex = 0;
		int rindex = 0;
		for(BackgroundChunk b : bgs){
			if(b.getIndex() < lowestindex){
				lowestindex = b.getIndex();
				rindex = bgs.indexOf(b);
			}
		}
		return bgs.get(rindex);
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
