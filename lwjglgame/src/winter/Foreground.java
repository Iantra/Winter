package winter;

import java.util.ArrayList;

/**
 * @author Iantra Solari
 * Foreground class, manages foreground chunks and stuff.
 */

public class Foreground{
	ArrayList<ForegroundChunk> fgs = new ArrayList<ForegroundChunk>();
	public Foreground(){
		addForeground(0);
	}
	
	public void addForeground(int index){
		fgs.add(new ForegroundChunk((int)(Constants.WIDTH*(index+.5f)), Constants.HEIGHT/2, (int)Math.round(Math.random()*0.6f), index));
	}
	
	
	
	public void addForegroundRight(){
		addForeground(getRightestChunk().getIndex() + 1);
	}
	
	public ForegroundChunk getRightestChunk(){
		int highestindex = 0;
		int rindex = 0;
		for(ForegroundChunk f : fgs){
			if(f.getIndex() > highestindex){
				rindex = fgs.indexOf(f);
				highestindex = f.getIndex();
			}
		}
		return fgs.get(rindex);
	}
	
	
	
	public void addForegroundLeft(){
		addForeground(getLeftestChunk().getIndex() - 1);
	}
	
	public ForegroundChunk getLeftestChunk(){
		int lowestindex = 0;
		int rindex = 0;
		for(ForegroundChunk f : fgs){
			if(f.getIndex() < lowestindex){
				lowestindex = f.getIndex();
				rindex = fgs.indexOf(f);
			}
		}
		return fgs.get(rindex);
	}
	
	
	
	public void render(){
		for(ForegroundChunk f : fgs){
			f.render();
		}
	}
	
	public void update(float _dt){
		for(ForegroundChunk f : fgs){
			f.update(_dt);
		}
	}
}
