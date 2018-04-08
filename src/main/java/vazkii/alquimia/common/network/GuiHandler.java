package vazkii.alquimia.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import vazkii.alquimia.client.gui.GuiAutomaton;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.common.block.tile.TileAutomaton;
import vazkii.alquimia.common.container.ContainerAutomaton;
import vazkii.alquimia.common.lib.LibGuiIDs;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case LibGuiIDs.AUTOMATON: 
			return new ContainerAutomaton(player.inventory, (TileAutomaton) world.getTileEntity(new BlockPos(x, y, z)));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case LibGuiIDs.LEXICON: 
			return GuiLexicon.getCurrentGui();
		case LibGuiIDs.AUTOMATON:
			return new GuiAutomaton(player.inventory, (TileAutomaton) world.getTileEntity(new BlockPos(x, y, z)));
		}
		
		return null;
	}

}
