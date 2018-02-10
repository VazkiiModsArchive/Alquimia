package vazkii.alquimia.common.network;

import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import vazkii.alquimia.client.gui.lexicon.GuiLexicon;
import vazkii.alquimia.common.lib.LibGuiIDs;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case LibGuiIDs.LEXICON: 
			return GuiLexicon.getCurrentGui();
		}
		return null;
	}

}
