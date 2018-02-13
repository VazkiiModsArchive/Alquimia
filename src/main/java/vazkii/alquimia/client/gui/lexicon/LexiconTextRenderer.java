package vazkii.alquimia.client.gui.lexicon;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;

public class LexiconTextRenderer {

	final FontRenderer font;
	final String text;
	final int x, y, width;
	final int spaceWidth;
	final int lineHeight;
	final boolean defaultUnicode;
	
	List<Word> words;
	
	public LexiconTextRenderer(FontRenderer font, String text, int x, int y, int width, int lineHeight) {
		this.font = font;
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.spaceWidth = font.getStringWidth(" ");
		this.lineHeight = lineHeight;
		this.defaultUnicode = font.getUnicodeFlag();
		
		build();
	}
	
	private void build() {
		font.setUnicodeFlag(true);
		
		words = new LinkedList<>();
		String[] tokens = text.replaceAll("\n", " \n ").split(" ");
		
		int currX = x;
		int currY = y;
		int currLen = 0;
		
		int currColor = 0;
		String currCodes = "";
		String currHref = "";
		
		for(String s : tokens) {
			if(s.contains("\n")) { // Line feed
				currLen = 0;
				currX = x;
				currY += lineHeight;
				continue;
			}
			
			int strWidth = font.getStringWidth(s) + spaceWidth;
			currLen += strWidth;
			if(currLen > width) {
				currLen = strWidth;
				currX = x;
				currY += lineHeight;
			}
			
			Word word = new Word(font, currX, currY, strWidth, s, currColor, currCodes, currHref);
			words.add(word);
			currX += strWidth;
		}
		
		font.setUnicodeFlag(defaultUnicode);
	}
	
	public void render(int mouseX, int mouseY) {
		font.setUnicodeFlag(true);
		words.forEach(word -> word.render(mouseX, mouseY));
		font.setUnicodeFlag(defaultUnicode);
	}
	
	public void click(int mouseX, int mouseY) {
		words.forEach(word -> word.click(mouseX, mouseY));
	}
	
	class Word {
		
		final FontRenderer font;
		final int x, y, width, height;
		final String text;
		final int color;
		final String codes;
		final String href;
		final boolean hasHref;
		
		Word(FontRenderer font, int x, int y, int width, String text, int color, String codes, String href) {
			this.font = font;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = 10;
			this.text = text;
			this.color = color;
			this.codes = codes;
			this.href = href;
			this.hasHref = (href != null && !href.isEmpty());
		}
		
		public void render(int mouseX, int mouseY) {
			String renderTarget = codes + text;
			if(isHovered(mouseX, mouseY) && hasHref)
				renderTarget = TextFormatting.UNDERLINE + renderTarget;
			
			font.drawString(renderTarget, x, y, color);
		}
		
		public void click(int mouseX, int mouseY) {
			if(isHovered(mouseX, mouseY))	
				onClicked();
		}
		
		private void onClicked() {
			if(hasHref)
				System.out.println("Clicked " + text);
		}
		
		private boolean isHovered(int mouseX, int mouseY) {
			return mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height;
		}
		
	}
	
}
