package winter;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;

/**
 * @author Iantra Solari
 * Main GameObject class, every object that is part of the game derives from this.
 * Not an interface because most of the methods remain unchanged between objects.
 */
public class GameObject {
	private float width, height;
	private float x, y;
	private float rotation;
	private Texture texture;
	private float vX = 0, vY = 0;
	
	
	public GameObject(float x, float y){
		this.x = x;
		this.y = y;
		width = 5;
		height = 5;
		rotation = 0f;
		texture = Assets.emptyImg;
	}
	
	public GameObject(float x, float y, float w, float h){
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		rotation = 0f;
		texture = Assets.emptyImg;
	}
	
	public GameObject(float x, float y, Texture t){
		this.x = x;
		this.y = y;
		rotation = 0f;
		texture = t;
		width = texture.getImageWidth();
		height = texture.getImageHeight();
	}
	
	public GameObject(float x, float y, float w, float h, Texture t){
		this.x = x;
		this.y = y;
		rotation = 0f;
		texture = t;
		width = w;
		height = h;
	}
	
	public void update(double _dt){
		x += (double)vX*_dt/5000*Constants.WIDTH;
		y += (double)vY*_dt/5000*Constants.WIDTH;
	}
	
	public void render(){
		float xs = (float)texture.getImageWidth()/(float)texture.getTextureWidth();
		float ys = (float)texture.getImageHeight()/(float)texture.getTextureHeight();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 0);
			GL11.glRotatef(rotation, 0f, 0f, 1f);
			GL11.glTranslatef(-x, -y, 0);
			texture.bind();
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0,ys);
				GL11.glVertex2f(x-width/2,y+height/2);
				
				GL11.glTexCoord2f(0,0);
				GL11.glVertex2f(x-width/2,y-height/2);
				
				GL11.glTexCoord2f(xs,0);
				GL11.glVertex2f(x+width/2,y-height/2);
				
				GL11.glTexCoord2f(xs,ys);
				GL11.glVertex2f(x+width/2,y+height/2);
			GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public boolean isColliding(GameObject go){
		if(	x()-width()/2 < go.x()+go.width()/2 	&&
			x()+width()/2 > go.x()-go.width()/2		&&
			y()-height()/2 < go.y()+go.height()/2	&&
			y()+height()/2 > go.y()-go.height()/2)
			return true;
		else {
			return false;
		}
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public void setVX(float v){
		vX = v;
	}
	
	public void setVY(float v){
		vY = v;
	}
	
	public void setRotation(float r){
		rotation = r;
	}
	
	public void setTexture(Texture t){
		texture = t;
	}
	
	public void setTextureAndDimension(Texture t){
		texture = t;
		width = t.getImageWidth();
		height = t.getImageHeight();
	}
	
	public float width(){
		return width;
	}
	
	public float height(){
		return height;
	}
	
	public float x(){
		return x;
	}
	
	public float y(){
		return y;
	}
	
	public float vX(){
		return vX;
	}
	
	public float vY(){
		return vY;
	}

	public float rotation(){
		return rotation;
	}
	
	public Texture texture(){
		return texture;
	}
}
