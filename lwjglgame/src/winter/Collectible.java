package winter;

public class Collectible extends GameObject{
	public boolean wasCollected;
	public int index;
	public Collectible(int x, int y, int i){
		super(x, y, Assets.items[i]);
		index = i;
	}
	
	public void update(float _dt){
		super.update(_dt);
		if(isColliding(Globals.character)){
			Assets.itemSounds[index].playAsSoundEffect(1.0f, 1.0f, false);
			wasCollected = true;
			switch (index) {
			case 0:	Globals.sticks++;
					break;
			default:	break;
			}
		}
	}
}
