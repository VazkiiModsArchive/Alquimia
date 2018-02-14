package vazkii.alquimia.client.lexicon;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public abstract class LexiconPage {

	String type;
	
	public abstract void render(GuiLexicon lexicon, int mouseX, int mouseY);

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
