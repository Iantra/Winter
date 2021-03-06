package winter;

import java.awt.Font;
import java.io.IOException;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * @author Iantra Solari
 * Class to store all textures, fonts, and sounds.
 */
public class Assets {
	public static Texture background;
	public static Texture[] foregrounds;
	public static Texture[] items;
	public static Texture[] fires;
	public static Texture walkingCharImg;
	public static Texture standingCharImg;
	public static Texture vignette;
	public static Texture coldFilter;
	public static Texture emptyImg;
	public static Texture snowImg;
	public static Texture solid;
	public static TrueTypeFont guiFont;
	public static TrueTypeFont dispFont;
	public static Audio[] itemSounds;
	public static Audio wind;
	
	public static void load(){
		try{
			background = getTexture("bg.png");
			foregrounds = new Texture[]{getTexture("fg-1.png"), getTexture("fg-2.png")};
			items = new Texture[]{ getTexture("stick.png"), getTexture("rock.png"), getTexture("fire.png"), getTexture("axe.png")};
			fires = new Texture[]{ getTexture("fire1.png"), getTexture("fire2.png"), getTexture("fire3.png")};
			walkingCharImg = getTexture("char_walking.png");
			standingCharImg = getTexture("char_standing.png");
			vignette = getTexture("vignette.png");
			coldFilter = getTexture("coldFilter.png");
			emptyImg = getTexture("empty.png");
			snowImg = getTexture("snowflake.png");
			solid = getTexture("solid.png");
			
			Font awtFont = new Font("Times New Roman", Font.BOLD, 12);
	        guiFont = new TrueTypeFont(awtFont, false);
	        awtFont = new Font("Times New Roman", Font.BOLD, 24);
	        dispFont = new TrueTypeFont(awtFont, false);
	        itemSounds = new Audio[]{getAudio("Stick.wav"), getAudio("Rock.wav"), getAudio("Stick.wav"), getAudio("Stick.wav")};
	        wind = getAudio("Wind.wav");
	        
	        wind.playAsMusic(0.8f, 1.0f, true);
	        SoundStore.get().poll(0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static Texture getTexture(String name) throws IOException{
		return TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/textures/"+name));
	}
	
	private static Audio getAudio(String name) throws IOException{
		return AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/sounds/"+name));
	}
}
