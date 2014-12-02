package lwjglgame;

public class Collectible extends GameObject{
	public boolean wasCollected;
	public Collectible(int x, int y, int i){
		super(x, y, Assets.stick);
	}
	
	public void update(float _dt){
		super.update(_dt);
		if(isColliding(Globals.character)){
			System.out.println("WOOT");
			Assets.wood.playAsSoundEffect(1.0f, 1.0f, false);
			wasCollected = true;
		}
	}
}
