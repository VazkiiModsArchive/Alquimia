package vazkii.alquimia.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.alquimia.client.lexicon.LexiconRegistry;
import vazkii.alquimia.common.handler.RitualHandler;
import vazkii.alquimia.common.ritual.ModRituals;
import vazkii.alquimia.common.ritual.Ritual;

public class EntityRitualLogic extends Entity {

	private static final String TAG_RITUAL_NAME = "ritual";
	private static final String TAG_RITUAL_TIME = "time";
	
    protected static final DataParameter<Integer> RITUAL_TIME = EntityDataManager.createKey(EntityRitualLogic.class, DataSerializers.VARINT);
    protected static final DataParameter<String> RITUAL_NAME = EntityDataManager.createKey(EntityRitualLogic.class, DataSerializers.STRING);
	
    private Ritual ritual;
    
	public EntityRitualLogic(World worldIn) {
		super(worldIn);
		setSize(0.1F, 0.1F);
	}
	
	public EntityRitualLogic(World worldIn, BlockPos pos, Ritual ritual) {
		super(worldIn);
		setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		dataManager.set(RITUAL_NAME, ritual.name.toString());
	}

	@Override
	protected void entityInit() {
        dataManager.register(RITUAL_TIME, 0);
        dataManager.register(RITUAL_NAME, "");
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		Ritual ritual = getRitual();
		if(ritual == null) {
			setDead();
			return;
		}
		
		int time = dataManager.get(RITUAL_TIME);
		if(ritual.tick(world, getPosition(), time, getEntityData()))
			setDead();
		else dataManager.set(RITUAL_TIME, time + 1);
	}
	
	protected void setRitual(String name) {
		dataManager.set(RITUAL_NAME, name);
		ritual = null;
	}
	
	public Ritual getRitual() {
		if(ritual == null)
			ritual = ModRituals.rituals.get(new ResourceLocation(dataManager.get(RITUAL_NAME)));
		
		return ritual;
	}
	
	public int getTime() {
		return dataManager.get(RITUAL_TIME);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		int time = compound.getInteger(TAG_RITUAL_TIME);
		String ritualName = compound.getString(TAG_RITUAL_NAME);
		dataManager.set(RITUAL_TIME, time);
		dataManager.set(RITUAL_NAME, ritualName);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		int time = dataManager.get(RITUAL_TIME);
		String ritualName = dataManager.get(RITUAL_NAME);
		compound.setInteger(TAG_RITUAL_TIME, time);
		setRitual(ritualName);
	}

}
