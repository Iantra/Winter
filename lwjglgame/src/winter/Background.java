package winter;

public class Background{
	private GameObject[] bg1;
	private GameObject[] bg2;
	
	public Background(){
		bg1 = new GameObject[]{
				new GameObject(Globals.screenWidth/2-Constants.WIDTH/2, Globals.screenHeight/2, Constants.WIDTH, Constants.HEIGHT, Assets.background),
				new GameObject(Globals.screenWidth/2+Constants.WIDTH/2, Globals.screenHeight/2, Constants.WIDTH, Constants.HEIGHT, Assets.background)};
	}
	
	public void update(float _dt){
		for(GameObject background : bg1){
			background.setVX(-Globals.camera.vX());
			if(Globals.character.isRunning())
				background.setVX(background.vX()*(Globals.character.energy()+1));
			background.update(_dt);
		}
	}
	
	public void render(){
		for(GameObject background : bg1){
			background.render();
		}
	}
}
