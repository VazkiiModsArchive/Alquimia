package vazkii.alquimia.client.lexicon;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;

public abstract class LexiconPage {

	protected transient Minecraft mc;
	protected transient FontRenderer fontRenderer;
	protected transient GuiLexiconEntry parent;
	protected transient int pageNum;
	
	String type;
	
	public void onDisplayed(GuiLexiconEntry parent, int pageNum) { 
		mc = parent.mc;
		fontRenderer = mc.fontRenderer;
		this.parent = parent;
		this.pageNum = pageNum;
	}
	
	public void render(int mouseX, int mouseY, float pticks) { }
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) { }
	
	public static class LexiconPageAdapter implements JsonDeserializer<LexiconPage> {

		Gson rawGson = new Gson();
		
		@Override
		public LexiconPage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	        JsonObject obj = json.getAsJsonObject();
	        JsonPrimitive prim = (JsonPrimitive) obj.get("type");
	        String type = prim.getAsString();
	        Class<? extends LexiconPage> clazz = LexiconRegistry.INSTANCE.PAGE_TYPES.get(type);
	        if(clazz == null)
	        	return null;
	        
	        return rawGson.fromJson(json, clazz);
		}
		
	}
	
}
