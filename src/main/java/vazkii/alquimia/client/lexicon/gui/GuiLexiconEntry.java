package vazkii.alquimia.client.lexicon.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import vazkii.alquimia.client.base.PersistentData;
import vazkii.alquimia.client.base.PersistentData.DataHolder.Bookmark;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconPage;

public class GuiLexiconEntry extends GuiLexicon {

	LexiconEntry entry;
	LexiconPage leftPage, rightPage;

	public GuiLexiconEntry(LexiconEntry entry) {
		this(entry, 0);
	}

	public GuiLexiconEntry(LexiconEntry entry, int page) {
		this.entry = entry;
		this.page = page; 
	}

	@Override
	public void initGui() {
		super.initGui();

		maxpages = (int) Math.ceil((float) entry.getPages().length / 2);
		setupPages();

		String key = entry.getResource().toString();
		if(!PersistentData.data.viewedEntries.contains(key)) {
			PersistentData.data.viewedEntries.add(key);
			PersistentData.save();
		}
	}

	@Override
	void drawForegroundElements(int mouseX, int mouseY, float partialTicks) {
		drawPage(leftPage, LEFT_PAGE_X, TOP_PADDING, mouseX, mouseY, partialTicks);
		drawPage(rightPage, RIGHT_PAGE_X, TOP_PADDING, mouseX, mouseY, partialTicks);

		if(rightPage == null)
			drawPageFiller();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		clickPage(leftPage, LEFT_PAGE_X, TOP_PADDING, mouseX, mouseY, mouseButton);
		clickPage(rightPage, RIGHT_PAGE_X, TOP_PADDING, mouseX, mouseY, mouseButton);
	}

	void drawPage(LexiconPage page, int x, int y, int mouseX, int mouseY, float pticks) {
		if(page == null)
			return;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		page.render(mouseX - x, mouseY - y, pticks);
		GlStateManager.popMatrix();
	}

	void clickPage(LexiconPage page, int x, int y, int mouseX, int mouseY, int mouseButton) {
		if(page != null)
			page.mouseClicked(mouseX - x, mouseY - y, mouseButton);
	}

	@Override
	void onPageChanged() {
		setupPages();
		needsBookmarkUpdate = true;
	}

	void setupPages() {
		LexiconPage[] pages = entry.getPages();
		int leftNum = page * 2;
		int rightNum = (page * 2) + 1;

		leftPage  = leftNum  < pages.length ? pages[leftNum]  : null;
		rightPage = rightNum < pages.length ? pages[rightNum] : null;

		if(leftPage != null)
			leftPage.onDisplayed(this);
		if(rightPage != null)
			rightPage.onDisplayed(this);
	}

	public LexiconEntry getEntry() {
		return entry;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof GuiLexiconEntry && ((GuiLexiconEntry) obj).entry == entry && ((GuiLexiconEntry) obj).page == page);
	}

	@Override
	boolean canBeOpened() {
		return !entry.isLocked() && !equals(Minecraft.getMinecraft().currentScreen);
	}

	@Override
	boolean shouldAddAddBookmarkButton() {
		return !isBookmarkedAlready();
	}
	
	boolean isBookmarkedAlready() {
		String entryKey = entry.getResource().toString();
		for(Bookmark bookmark : PersistentData.data.bookmarks)
			if(bookmark.entry.equals(entryKey) && bookmark.page == page)
				return true;
		
		return false;
	}

	@Override
	void bookmarkThis() {
		String entryKey = entry.getResource().toString();
		PersistentData.data.bookmarks.add(new Bookmark(entryKey, page));
		PersistentData.save();
		needsBookmarkUpdate = true;
	}

}
