package lwjglgame;

import org.newdawn.slick.opengl.Texture;

public class BackgroundChunk extends GameObject{
	private final int index;
	public BackgroundChunk(int x, int y, int i, int index){
		super(x, y, Assets.backgrounds[i]);
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
}
