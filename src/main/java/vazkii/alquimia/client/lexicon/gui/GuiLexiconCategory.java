package vazkii.alquimia.client.lexicon.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.lexicon.LexiconCategory;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconRegistry;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonCategory;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonEntry;

public class GuiLexiconCategory extends GuiLexicon {

	static final int ENTRIES_PER_PAGE = 13;
	static final int ENTRIES_IN_FIRST_PAGE = 11;
	
	LexiconCategory category;
	LexiconTextRenderer text;
	
	List<GuiButton> dependentButtons;
	List<LexiconEntry> allEntries;
	int page, maxpages;
	
	public GuiLexiconCategory(LexiconCategory category) {
		this.category = category;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		text = new LexiconTextRenderer(this, fontRenderer, category.getDescription(), LEFT_PAGE_X, TOP_PADDING + 22, PAGE_WIDTH, TEXT_LINE_HEIGHT);
		
		allEntries = new ArrayList<>(category.getEntries());
		allEntries.addAll(category.getEntries());
		allEntries.addAll(category.getEntries());
		allEntries.addAll(category.getEntries());
		allEntries.addAll(category.getEntries());
		allEntries.addAll(category.getEntries());
		allEntries.addAll(category.getEntries());
		allEntries.addAll(category.getEntries());
		allEntries.addAll(category.getEntries());
		allEntries.addAll(category.getEntries());
		Collections.sort(allEntries);
		
		maxpages = 1;
		
		int count = allEntries.size();
		count -= ENTRIES_IN_FIRST_PAGE;
		if(count > 0)
			maxpages += (int) Math.ceil((float) count / (ENTRIES_PER_PAGE * 2));
		System.out.println(maxpages);
		
		dependentButtons = new ArrayList();
		buildEntryButtons();
	}
	
	@Override
	void drawForegroundElements(int mouseX, int mouseY, float partialTicks) {
		super.drawForegroundElements(mouseX, mouseY, partialTicks);
		
		if(page == 0) {
			drawCenteredStringNoShadow(category.getName(), LEFT_PAGE_X + PAGE_WIDTH / 2, TOP_PADDING, 0x333333);
			drawCenteredStringNoShadow(I18n.translateToLocal("alquimia.gui.lexicon.chapters"), RIGHT_PAGE_X + PAGE_WIDTH / 2, TOP_PADDING, 0x333333);

			drawSeparator(LEFT_PAGE_X, TOP_PADDING + 12);
			drawSeparator(RIGHT_PAGE_X, TOP_PADDING + 12);

			text.render(mouseX, mouseY);
			drawProgressBar(mouseX, mouseY, (e) -> e.getCategory() == category);	
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		text.click(mouseX, mouseY);
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		if(button instanceof GuiButtonCategory)
			displayLexiconGui(new GuiLexiconCategory(((GuiButtonCategory) button).getCategory()), true);
	}
	
	@Override
	void changePage(boolean left) {
		if(canSeePageButton(left)) {
			if(left)
				page--;
			else page++;
			
			buildEntryButtons();
		}
	}
	
	void buildEntryButtons() {
		buttonList.removeAll(dependentButtons);
		dependentButtons.clear();
		
		if(page == 0) {
			addEntryButtons(RIGHT_PAGE_X, TOP_PADDING + 20, 0, ENTRIES_IN_FIRST_PAGE);
			
			int i = 0;
			List<LexiconCategory> categories = new ArrayList(LexiconRegistry.INSTANCE.CATEGORIES.values());
			Collections.sort(categories);
			
			for(LexiconCategory ocategory : categories) {
				if(ocategory.getParentCategory() != category)
					continue;
				
				int x = LEFT_PAGE_X + 10 + (i % 4) * 24;
				int y = TOP_PADDING + PAGE_HEIGHT - 70;
				
				GuiButton button = new GuiButtonCategory(this, x, y, ocategory);
				buttonList.add(button);
				dependentButtons.add(button);
				
				i++;
			}
		} else {
			int start = getEntryCountStart();
			addEntryButtons(LEFT_PAGE_X, TOP_PADDING, start, ENTRIES_PER_PAGE);
			addEntryButtons(RIGHT_PAGE_X, TOP_PADDING, start + ENTRIES_IN_FIRST_PAGE, ENTRIES_PER_PAGE);
		}
	}
	
	int getEntryCountStart() {
		if(page == 0)
			return 0;
		
		int start = ENTRIES_IN_FIRST_PAGE;
		start += (ENTRIES_PER_PAGE * 2) * (page - 1);
		return start;
	}
	
	void addEntryButtons(int x, int y, int start, int count) {
		for(int i = 0; i < count && (i + start) < allEntries.size(); i++) {
			GuiButton button = new GuiButtonEntry(this, bookLeft + x, bookTop + y + i * 11, allEntries.get(start + i), start + i);
			buttonList.add(button);
			dependentButtons.add(button);
		}
	}
	
	@Override
	public boolean canSeePageButton(boolean left) {
		return left ? page > 0 : (page + 1) < maxpages; 
	}
	
}
