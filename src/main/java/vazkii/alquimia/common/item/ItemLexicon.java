package vazkii.alquimia.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.client.gui.GuiAdvancementsExt;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.AlquimiaSounds;
import vazkii.alquimia.common.base.IAlquimiaItem;
import vazkii.alquimia.common.lib.LibGuiIDs;
import vazkii.arl.item.ItemMod;

public class ItemLexicon extends ItemMod implements IAlquimiaItem {

	public ItemLexicon() {
		super("lexicon");
		setMaxStackSize(1);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
		
		addPropertyOverride(new ResourceLocation("open"), new IItemPropertyGetter() {
			
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
            	Minecraft mc = Minecraft.getMinecraft();
            	boolean valid = entityIn == mc.player 
            			&& (stack == mc.player.getHeldItemMainhand() || stack == mc.player.getHeldItemOffhand()) 
            			&& (mc.currentScreen instanceof GuiLexicon || mc.currentScreen instanceof GuiAdvancementsExt);
            	return valid ? 1 : 0;
            }
            
        });
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote)
			worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, AlquimiaSounds.book_open, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));
		playerIn.openGui(Alquimia.instance, LibGuiIDs.LEXICON, worldIn, 0, 0, 0);
		
		return succeed(playerIn, handIn);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}

}
