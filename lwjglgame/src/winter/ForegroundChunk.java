package winter;

/**
 * @author Iantra Solari
 * Background chunk. Deals with the items inside it.
 */
public class ForegroundChunk extends GameObject{
	private final int index;
	private Collectible item;
	public ForegroundChunk(int x, int y, int i, int index){
		super(x, y, Assets.foregrounds[i]);
		if(Math.random()*5 > 4)
			item = new Collectible(x, Globals.screenHeight*15/20, (int)(Math.random()*2));
		else
			item = new Collectible(0, Globals.screenHeight+10, 1);
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
	
	public void render(){
		super.render();
		if(!item.wasCollected)
		item.render();
	}
	
	public void update(float _dt){
		super.update(_dt);
		if(!item.wasCollected)
		item.update(_dt);
	}
}
