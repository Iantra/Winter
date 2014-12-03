package winter;

public class GUI {
	private GUIObject[] guiItems;
	public GUI(){
		guiItems = new GUIObject[]{
				new GUIObject(30, Globals.screenHeight-10, Assets.items[0])
				};
	}
	
	public void render(){
		for(GUIObject g : guiItems){
			g.render();
			Assets.font.drawString(g.tX(), g.tY(), g.getText());
		}
	}
	
	public void update(float _dt){
		guiItems[0].setText("x"+Globals.sticks);
	}
}
