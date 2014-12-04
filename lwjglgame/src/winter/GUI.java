package winter;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GUI {
	private GUIObject[] guiItems;
	public GUI(){
		guiItems = new GUIObject[]{
				new GUIObject(32, Globals.screenHeight-10, Assets.items[0], 0, false, new int[]{1}),
				new GUIObject(32+64, Globals.screenHeight-10, Assets.items[1], 1, false, new int[]{0,1}),
				new GUIObject(32+128, Globals.screenHeight-10, Assets.items[2], 2, true, new int[]{1,1}),
				new GUIObject(32+192, Globals.screenHeight-10, Assets.items[3], 3, true, new int[]{1,1})
				};
	}
	
	public void render(){
		GL11.glPushMatrix();
		for(GUIObject g : guiItems){
			if(g.shouldShow()){
				g.render();
				Assets.font.drawString(g.tX(), g.tY(), g.getText());
			}else{
				GL11.glTranslatef(-40f, 0, 0);
			}
		}
		GL11.glPopMatrix();
	}
	
	public void update(float _dt){
		for(int i = 0; i < guiItems.length; i++){
			guiItems[i].update(_dt);
			guiItems[i].setText("x"+Globals.items[i]);
			//Check clicks, will move eventually
			
		}
	}
	
	public void click(){
		System.out.println(new GameObject(Mouse.getX(), Globals.screenHeight - Mouse.getY(), 10, 10).y());
		for(int i = 0; i < guiItems.length; i++){
			if(guiItems[i].clickable() && guiItems[i].canMake() && guiItems[i].isColliding(new GameObject(Mouse.getX()/3, Globals.screenHeight - Mouse.getY()/3, 10, 10))){
				
				switch(i){
				case 2: guiItems[i].click(new int[]{1,1});
					break;
				case 3: guiItems[i].click(new int[]{2,2});
					break;
				default: break;
				}
			}
		}
	}
}
