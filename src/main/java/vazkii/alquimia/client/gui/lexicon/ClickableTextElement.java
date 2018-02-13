package vazkii.alquimia.client.gui.lexicon;

public class ClickableTextElement {

	public int posX, posY, width, height;
	public String text, href;
	
	public ClickableTextElement(int posX, int posY, int width, int height, String text, String href) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.text = text;
		this.href = href;
	}
	
	public void actUpon(int mouseX, int mouseY) {
		if(mouseX > posX && mouseY > posY && mouseX < posX + width && mouseY < posY + height)
			onClicked();
	}
	
	private void onClicked() {
		// TODO
	}
	
	
}
