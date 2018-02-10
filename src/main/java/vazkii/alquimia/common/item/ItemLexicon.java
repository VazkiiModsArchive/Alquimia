package vazkii.alquimia.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaItem;
import vazkii.alquimia.common.lib.LibGuiIDs;
import vazkii.arl.item.ItemMod;

public class ItemLexicon extends ItemMod implements IAlquimiaItem {

	public ItemLexicon() {
		super("lexicon");
		setMaxStackSize(1);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.openGui(Alquimia.instance, LibGuiIDs.LEXICON, worldIn, 0, 0, 0);
		
		return succeed(playerIn, handIn);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}

}
