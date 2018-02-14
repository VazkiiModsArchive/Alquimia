package vazkii.alquimia.client.lexicon.gui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextComponentString;

public class LexiconTextRenderer {
	
	private static final int LINK_COLOR = 0x0000EE;
	private static final int LINK_COLOR_HOVER = 0x8800EE;
	
	private static final Map<String, String> MACROS = new HashMap() {{
		put("$(obf)", "$(k)");
		put("$(bold)", "$(l)");
		put("$(strike)", "$(m)");
		put("$(italic)", "$(o)");
		put("$(reset)", "$()");
		put("$(clear)", "$()");
		put("$(2br)", "$(br2)");
		put("$(p)", "$(br2)");
		
		put("/$", "$()");
		
		put("$(nocolor)", "$(0)");
		put("$(item)", "$(#05c)");
		put("$(thing)", "$(#490)");
	}};

	final GuiLexicon gui;
	final FontRenderer font;
	final String text;
	final int x, y, width;
	final int spaceWidth;
	final int lineHeight;
	final boolean defaultUnicode;
	
	int currX, currY, currLen, currColor, prevColor;
	String currCodes, currHref;
	List<Word> currCluster;
	
	List<Word> words;
	
	public LexiconTextRenderer(GuiLexicon gui, String text, int x, int y) {
		this(gui, text, x, y, GuiLexicon.PAGE_WIDTH, GuiLexicon.TEXT_LINE_HEIGHT);
	}
	
	public LexiconTextRenderer(GuiLexicon gui, String text, int x, int y, int width, int lineHeight) {
		this.gui = gui;
		this.font = gui.mc.fontRenderer;
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
		
		String actualText = text;
		if(actualText == null)
			actualText = "[ERROR]";
		
		for(String key : MACROS.keySet())
			actualText = actualText.replace(key, MACROS.get(key));
		
		actualText = actualText.replaceAll(" ", "\0 ").replaceAll("(\\$\\(.*?\\))", " $1 ");
		String[] tokens = actualText.split(" ");
		
		currX = x;
		currY = y;
		currLen = 0;
		currColor = 0;
		prevColor = 0;
		currCodes = "";
		currHref = "";
		currCluster = null;
		
		for(String s : tokens) {
			boolean space = s.contains("\0");
			
			s = buildCommand(s);
			if(s.isEmpty())
				continue;
			
			s = s.replaceAll("\0", "");
			
			int strWidth = font.getStringWidth(s) + (space ? spaceWidth : 0);
			currLen += strWidth;
			if(currLen > width) {
				currLen = strWidth;
				currX = x;
				currY += lineHeight;
			}
			
			Word word = new Word(font, currX, currY, strWidth, s, currColor, currCodes, currHref, currCluster);
			words.add(word);
			if(currCluster != null)
				currCluster.add(word);
			
			currX += strWidth;
		}
		
		font.setUnicodeFlag(defaultUnicode);
	}
	
	private String buildCommand(String s) {
		if(s.matches("^\\$\\((.*?)\\)$")) { // Special codes
			String cmd = s.substring(2, s.length() - 1);
			
			if(cmd.isEmpty()) { // Remove formatting
				currColor = 0;
				currCodes = "";
				currHref = "";
				currCluster = null;
			}
			
			else if(cmd.matches("br|br2")) { // Line break
				currLen = 0;
				currX = x;
				currY += (cmd.contains("2") ? lineHeight * 2 : lineHeight);
			}
			
			else if(cmd.startsWith("#") && (cmd.length() == 4 || cmd.length() == 6)) { // Hex colors
				String parse = cmd.substring(1);
				if(parse.length() == 3)
					parse = "" + parse.charAt(0) + parse.charAt(0) + parse.charAt(1) + parse.charAt(1) + parse.charAt(2) + parse.charAt(2);
				currColor = Integer.parseInt(parse, 16);
			}
			
			else if(cmd.matches("^[0123456789abcdef]$")) // Vanilla colors
				currColor = font.getColorCode(cmd.charAt(0));
			
			else if(cmd.matches("^[klmnor]$")) // Vanilla codes
				currCodes = "\u00A7" + cmd;
			
			else if(cmd.startsWith("l:")) { // Links
				String nextHref = cmd.substring(5);
				if(!nextHref.equals(currHref))
					currCluster = new LinkedList();
				
				currHref = cmd.substring(2);
				prevColor = currColor;
				currColor = LINK_COLOR;
			} 
			else if(cmd.equals("/l")) { // Link breaks
				currHref = "";
				currColor = prevColor;
				currCluster = null;
			}
			
			return "";
		}
		
		return s;
	}
	
	public void render(int mouseX, int mouseY) {
		font.setUnicodeFlag(true);
		words.forEach(word -> word.render(mouseX, mouseY));
		font.setUnicodeFlag(defaultUnicode);
	}
	
	public void click(int mouseX, int mouseY, int mouseButton) {
		words.forEach(word -> word.click(mouseX, mouseY, mouseButton));
	}
	
	class Word {
		
		final FontRenderer font;
		final int x, y, width, height;
		final String text;
		final int color;
		final String codes;
		final String href;
		final boolean hasHref;
		final List<Word> linkCluster;
		
		Word(FontRenderer font, int x, int y, int width, String text, int color, String codes, String href, List<Word> linkCluster) {
			this.font = font;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = 8;
			this.text = text;
			this.color = color;
			this.codes = codes;
			this.href = href;
			this.hasHref = (href != null && !href.isEmpty());
			this.linkCluster = linkCluster;
		}
		
		public void render(int mouseX, int mouseY) {
			String renderTarget = codes + text;
			int renderColor = color;
			if(isClusterHovered(mouseX, mouseY) && hasHref)
				renderColor = LINK_COLOR_HOVER;
			
//			if(hasHref) {
//				gui.drawRect(x, y, x + width, y + height, 0x33FF0000);
//				gui.drawRect(mouseX - 3 - gui.bookLeft, mouseY - 3 - gui.bookTop, mouseX + 3 - gui.bookLeft, mouseY + 3 - gui.bookTop, 0x33FF0000);
//				font.drawString("(" + mouseX + ", " + mouseY + ")", mouseX - 3 - gui.bookLeft, mouseY - 12 - gui.bookTop, 0xFF000000);
//			}
			
			font.drawString(renderTarget, x, y, renderColor);
		}
		
		public void click(int mouseX, int mouseY, int mouseButton) {
			System.out.println(mouseX + " " + mouseY);
			if(hasHref && mouseButton == 0 && isHovered(mouseX, mouseY))
				onClicked();
		}
		
		private void onClicked() {
			if(hasHref)
				gui.mc.player.sendMessage(new TextComponentString("Clicked link: " + text));
		}
		
		private boolean isHovered(int mouseX, int mouseY) {
			return gui.isMouseInRelativeRange(mouseX, mouseY, x, y, width, height);
		}
		
		private boolean isClusterHovered(int mouseX, int mouseY) {
			if(linkCluster == null)
				return false;
						
			for(Word w : linkCluster)
				if(w.isHovered(mouseX, mouseY))
					return true;
			
			return false;
		}
		
	}
	
}

