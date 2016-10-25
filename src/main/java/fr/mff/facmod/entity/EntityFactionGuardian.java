package fr.mff.facmod.entity;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.core.Faction;
import fr.mff.facmod.core.GuardianHelper;
import fr.mff.facmod.entity.AI.EntityAIGuardian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFactionGuardian
  extends EntityTameable
  implements IAnimals,IInventory, IBossDisplayData
{
  public ItemStack[] content;
  private String name;
  private String namesave;
  private int attackCooldown;
  private int attackTimer;
  private boolean isUseable;
  private int level;
  private int subLevel;
  private int requiredXP;
  public String ownerUUID;
  private double life;
  private double speed;
  private double damages;
  private int textureid;
  private int xpmodifier;
  public final double HEALTH_UP = 100.0D;
  public final double SPEED_UP = 0.02D;
  public final double DAMAGE_UP = 1.0D;
  public final double HEALTH_BASE = 100.0D;
  public final double SPEED_BASE = 0.25D;
  public final double DAMAGE_BASE = 1.0D;
  public final int TEXTURE_DEFAULT = 0;
  
  public EntityFactionGuardian(World world)
  {
    super(world);
    setSize(1.4F, 3F);
    this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
    this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
    this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
    this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
    this.targetTasks.addTask(8, new EntityAIGuardian(this, EntityPlayer.class, 0, true));
    this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityFactionGuardian.class, 15.0F));
    this.tasks.addTask(9, new EntityAILookIdle(this));
    
    this.name = "Faction Guardian";
    this.requiredXP = 10;
    this.content = new ItemStack[7];
    this.isUseable = true;
    this.textureid = 0;
    this.xpmodifier = 1;
    this.ignoreFrustumCheck = true;
    this.damages = 1.0D;
  }
  
  public void addInformations(ItemStack[] content, int level, int subLevel, String player, Faction fac)
  {
    this.content = content;
    this.level = level;
    this.subLevel = subLevel;
    this.ownerUUID = player;
    this.name = fac.getName();
    setHealth(100.0F);
    setRequiredXP(level);
  }
  
  @SideOnly(Side.CLIENT)
  public void setClientInfos(int level, int subLevel)
  {
    this.level = level;
    this.subLevel = subLevel;
    setRequiredXP(level);
  }
  
  public void sync()
  {
   /* PacketGolem packet = new PacketGolem();
    packet.addInformations(this.level, this.subLevel, getEntityId());
    FactionMod.proxy.packetPipeline.sendToAll(packet);*/
    this.damages = (1.0D + 1.0D * this.level);
    this.speed = (0.25D + 0.02D * this.level);
    this.life = (100.0D + 100.0D * this.level);
  }
  
  public void onLivingUpdate()
  {
    super.onLivingUpdate();
   
    if (this.attackTimer > 0) {
      this.attackTimer -= 1;
    }
    if ((!this.worldObj.isRemote) && (getHealth() < this.life) && (this.rand.nextInt(20) == 0) && (this.isUseable)) {
      setHealth(getHealth() + 1.0F);
    }
  }
  
  public void onDeath(DamageSource damage)
  {
    this.isUseable = false;

  }
  
  public boolean attackEntityAsMob(Entity entity)
  {
    if (((entity instanceof EntityPlayer)) && (checkWhitelist((EntityPlayer)entity))) {
      return false;
    }
    if (this.attackTimer == 0) {
      this.attackTimer = this.attackCooldown;
    }
    this.worldObj.setEntityState(this, (byte)4);
    boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 
      (float)(this.damages + this.rand.nextInt(5)));
    if (flag) {
      entity.motionY += 0.4000000059604645D;
    }
    playSound("mob.irongolem.throw", 1.0F, 1.0F);
    return flag;
  }
  
  public boolean checkWhitelist(EntityPlayer entity)
  {
	  Faction faction = Faction.Registry.getPlayerFaction(entity.getUniqueID());
	  if ((this.ownerUUID != null) && (entity.getUniqueID().toString().equals(this.ownerUUID))) {
      return true;
    }
    if (faction != null) {
		GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(entity.getName());
		if(profile != null) {
			Faction pFaction = Faction.Registry.getPlayerFaction(profile.getId());
			if(pFaction != null && pFaction == faction) {	
      return true;
			}
		}
    }   
    return false;
  }

  
  
  
  public boolean interact(EntityPlayer player)
  {
    sync();
    if ((player.getHeldItem() != null) && (GuardianHelper.checkXPStuff(player.getHeldItem().getItem())))
    {
      if (this.level < 80)
      {
        consumeXP(player);
      }
      return true;
    }
    if ((!this.worldObj.isRemote) && (checkWhitelist(player)))
    {
      player.openGui(FactionMod.INSTANCE, 7, this.worldObj, getEntityId(), 0, 0);
      return true;
    }
    return super.interact(player);
  }
  
  protected void collideWithEntity(Entity entity)
  {
    if (((entity instanceof IMob)) && (getRNG().nextInt(20) == 0)) {
      setAttackTarget((EntityLivingBase)entity);
    }
    super.collideWithEntity(entity);
  }
  
  protected void applyEntityAttributes()
  {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
  }
  
  protected boolean isAIEnabled()
  {
    return true;
  }
  
  protected boolean canDespawn()
  {
    return false;
  }
 
  
  public int getSizeInventory()
  {
    return this.content.length;
  }
  
  public ItemStack getStackInSlot(int slot)
  {
    return this.content[slot];
  }
  
  public ItemStack decrStackSize(int slotIndex, int amount)
  {
    if (this.content[slotIndex] != null)
    {
      if (this.content[slotIndex].stackSize <= amount)
      {
        ItemStack itemstack = this.content[slotIndex];
        this.content[slotIndex] = null;
        markDirty();
        return itemstack;
      }
      ItemStack itemstack = this.content[slotIndex].splitStack(amount);
      if (this.content[slotIndex].stackSize == 0) {
        this.content[slotIndex] = null;
      }
      markDirty();
      return itemstack;
    }
    return null;
  }
  
  public ItemStack getStackInSlotOnClosing(int slotIndex)
  {
    if (this.content[slotIndex] != null)
    {
      ItemStack itemstack = this.content[slotIndex];
      this.content[slotIndex] = null;
      return itemstack;
    }
    return null;
  }
  
  public void setInventorySlotContents(int slotIndex, ItemStack stack)
  {
    this.content[slotIndex] = stack;
    if ((stack != null) && (stack.stackSize > getInventoryStackLimit())) {
      stack.stackSize = getInventoryStackLimit();
    }
    markDirty();
  }
  
  public String getInventoryName()
  {
    return "Faction Guardian";
  }
  
  public boolean hasCustomInventoryName()
  {
    return false;
  }
  
  public int getInventoryStackLimit()
  {
    return 1;
  }
  
  public void markDirty() {}
  
  public boolean isUseableByPlayer(EntityPlayer player)
  {
    if (!this.isUseable) {
      return false;
    }
    return player.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64.0D;
  }
  
  public void openInventory() {}
  
  public void closeInventory() {}
  
  public boolean isItemValidForSlot(int slot, ItemStack stack)
  {
    return true;
  }
  
  public EntityAgeable createChild(EntityAgeable p_90011_1_)
  {
    return null;
  }
  
  public boolean isTamed()
  {
    return true;
  }
  
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    NBTTagList nbttaglist = new NBTTagList();
    for (int i = 0; i < this.content.length; i++) {
      if (this.content[i] != null)
      {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setByte("Slot", (byte)i);
        this.content[i].writeToNBT(nbttagcompound1);
        nbttaglist.appendTag(nbttagcompound1);
      }
    }
    compound.setTag("Items", nbttaglist);
    compound.setInteger("Levels", this.level);
    compound.setInteger("SubLevels", this.subLevel);
    compound.setString("player", this.ownerUUID);
    compound.setBoolean("isUseable", this.isUseable);
  }
  
  public void readFromNBT(NBTTagCompound compound)
  {
    super.readFromNBT(compound);
    
    NBTTagList nbttaglist = compound.getTagList("Items", 10);
    this.content = new ItemStack[getSizeInventory()];
    for (int i = 0; i < nbttaglist.tagCount(); i++)
    {
      NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
      int j = nbttagcompound1.getByte("Slot") & 0xFF;
      if ((j >= 0) && (j < this.content.length)) {
        this.content[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
      }
    }
    this.level = compound.getInteger("Levels");
    this.subLevel = compound.getInteger("SubLevels");
    this.ownerUUID = compound.getString("player");
    setRequiredXP(this.level);
  }
  
  public int getLevel()
  {
    return this.level;
  }
  
  @SideOnly(Side.CLIENT)
  public int getLife()
  {
    return (int)this.life;
  }
  
  @SideOnly(Side.CLIENT)
  public int getSpeed()
  {
    return (int)(this.speed * 10.0D);
  }
  
  @SideOnly(Side.CLIENT)
  public int getDamages()
  {
    return (int)this.damages;
  }
  
  @SideOnly(Side.CLIENT)
  public String getGuardianNamesave()
  {
    return this.namesave;
  }
  @SideOnly(Side.CLIENT)
  public String getGuardianName()
  {
    return this.name;
  }
  public void setGuardianName(String name)
  {
    this.namesave = name;
  }
  
  @SideOnly(Side.CLIENT)
  public float getScaledHealth()
  {
    return getHealth() / getMaxHealth();
  }
  
  @SideOnly(Side.CLIENT)
  public float getRequiredXP()
  {
    return this.requiredXP;
  }
  
  @SideOnly(Side.CLIENT)
  public int getTextureId()
  {
    return this.textureid;
  }
  
  @SideOnly(Side.CLIENT)
  public int getAttackTimer()
  {
    return this.attackTimer;
  }
  
  @SideOnly(Side.CLIENT)
  public int getSubLevel()
  {
    return this.subLevel;
  }
  
  @SideOnly(Side.CLIENT)
  public void handleHealthUpdate(byte byt)
  {
    if ((byt == 4) && (this.attackTimer == 0))
    {
      this.attackTimer = 10;
    }
  }
  
  public void setOwner(EntityPlayer player)
  {
    this.ownerUUID = player.getUniqueID().toString();
  }
  
  public void setRequiredXP(int level)
  {
    this.requiredXP = (1 * level * level + 10);
  }
  
  public void addXp(int xp)
  {
    this.subLevel += xp * this.xpmodifier;
    if (this.subLevel >= this.requiredXP)
    {
      this.level += 1;
      this.subLevel -= this.requiredXP;
      setRequiredXP(this.level);
      onLevelUp();
      addXp(0);
    }
  }
  
  public void onLevelUp()
  {
    this.speed += 0.02D;
    this.damages += 1.0D;
    this.life += 100.0D;
    applyLevelModifiers();
  }
  
  public void applyLevelModifiers()
  {
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.life);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.speed);
  }
  
  private void consumeXP(EntityPlayer player)
  {
    ItemStack stack = player.getHeldItem();
    addXp(GuardianHelper.getXpFromItem(stack.getItem()));
    stack.stackSize -= 1;
    if (stack.stackSize <= 0) {
      stack = null;
    }
  }

@Override
public ItemStack removeStackFromSlot(int index) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void openInventory(EntityPlayer player) {
	// TODO Auto-generated method stub
	
}

@Override
public void closeInventory(EntityPlayer player) {
	// TODO Auto-generated method stub
	
}

@Override
public int getField(int id) {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public void setField(int id, int value) {
	// TODO Auto-generated method stub
	
}

@Override
public int getFieldCount() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public void clear() {
	// TODO Auto-generated method stub
	
}

}
