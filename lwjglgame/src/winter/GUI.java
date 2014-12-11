package winter;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GUI {
	private GUIObject[] inventory;
	private GUIObject[] fireCraft;
	private GUIObject[] weaponCraft;
	public GUI(){
		inventory = new GUIObject[]{
				new GUIObject(32, Globals.screenHeight-10, Assets.items[0], 0, false, new int[]{1}),
				new GUIObject(32+64, Globals.screenHeight-10, Assets.items[1], 1, false, new int[]{0}),
				new GUIObject(32+128, Globals.screenHeight-10, Assets.items[3], 3, false, new int[]{0,0,0,1})
				};
		fireCraft = new GUIObject[]{
				new GUIObject(Globals.screenWidth/2, Globals.screenHeight/2, Assets.items[2], 2, true, new int[]{4,2})
				};
		/*weaponCraft = new GUIObject[]{
				new GUIObject(32, Globals.screenHeight-10, Assets.items[0], 0, false, new int[]{1}),
				new GUIObject(32+64, Globals.screenHeight-10, Assets.items[1], 1, false, new int[]{0,1}),
				new GUIObject(32+128, Globals.screenHeight-10, Assets.items[2], 2, true, new int[]{1,1}),
				new GUIObject(32+192, Globals.screenHeight-10, Assets.items[3], 3, true, new int[]{1,1})
				};*/
	}
	
	public void render(){
		GL11.glPushMatrix();
		if(Globals.guiState >= 1)
		for(GUIObject g : inventory){
			if(g.shouldShow()){
				g.render();
				Assets.guiFont.drawString(g.tX(), g.tY(), g.getText());
			}else{
				GL11.glTranslatef(-40f, 0, 0);
			}
		}
		GL11.glPopMatrix();
		if(Globals.guiState == 2)
			for(GUIObject g : fireCraft){
					g.render();
		}
		
		GL11.glPushMatrix();
		/*if(Globals.guiState == 3)
			for(GUIObject g : weaponCraft){
				if(g.shouldShow()){
					g.render();
					Assets.guiFont.drawString(g.tX(), g.tY(), g.getText());
				}else{
					GL11.glTranslatef(-40f, 0, 0);
				}
			}*/
		GL11.glPopMatrix();
	}
	
	public void update(float _dt){
		for(int i = 0; i < inventory.length; i++){
			inventory[i].update(_dt);
			inventory[i].setText("x"+Globals.items[i]);
			//If this item is an axe, we don't need text, since you can only make 1 of each.
			if(i > 2)
				inventory[i].setText("");
		}
	}
	
	public void click(){
		System.out.println(new GameObject(Mouse.getX(), Globals.screenHeight - Mouse.getY(), 10, 10).y());
		for(int i = 0; i < fireCraft.length; i++){
			if(fireCraft[i].clickable() && fireCraft[i].canMake() && fireCraft[i].isColliding(new GameObject(Mouse.getX()/3, Globals.screenHeight - Mouse.getY()/3, 10, 10))){
				fireCraft[i].click(new int[]{4,2});
				Globals.fires.add(new GameObject(Globals.character.x(), Globals.character.y() + 32 + (float)Math.random()*3, Assets.items[2]));
				Globals.paused = false;
				Globals.guiState = 1;
			}
		}
	}
}
