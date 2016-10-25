package fr.mff.facmod.entity.AI;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Predicate;

import fr.mff.facmod.entity.EntityFactionGuardian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;

public class EntityAIGuardian
  extends EntityAITarget
{
  private final Class targetClass;
  private final int targetChance;
  private EntityFactionGuardian golem;
  private final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
  private final Predicate<Entity> targetEntitySelector;
  private EntityLivingBase targetEntity;
  private static final String __OBFID = "CL_00001620";
  
  public EntityAIGuardian(EntityFactionGuardian p_i1663_1_, Class p_i1663_2_, int p_i1663_3_, boolean p_i1663_4_)
  {
    this(p_i1663_1_, p_i1663_2_, p_i1663_3_, p_i1663_4_, false);
    this.golem = p_i1663_1_;
	  System.out.println("test");

  }
  
  public EntityAIGuardian(EntityFactionGuardian p_i1664_1_, Class p_i1664_2_, int p_i1664_3_, boolean p_i1664_4_, boolean p_i1664_5_)
  {
    this(p_i1664_1_, p_i1664_2_, p_i1664_3_, p_i1664_4_, p_i1664_5_, (Predicate<Entity>)null);
    this.golem = p_i1664_1_;
	  System.out.println("test");

  }
  
  public EntityAIGuardian(EntityCreature p_i1665_1_, Class p_i1665_2_, int p_i1665_3_, boolean p_i1665_4_, boolean p_i1665_5_, Predicate<Entity> p_i1665_6_)
  {
    super(p_i1665_1_, p_i1665_4_, p_i1665_5_);
	  System.out.println("test");
    this.targetClass = p_i1665_2_;
    this.targetChance = p_i1665_3_;
    this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(p_i1665_1_);
    setMutexBits(1);
    this.targetEntitySelector = this.selectInventories;
    System.out.println(this.targetEntitySelector);
    System.out.println(this.selectInventories.apply(p_i1665_1_));
  }
  
  public Predicate<Entity> selectInventories = new Predicate<Entity>()
  {
      public boolean apply(Entity entity)
      {
    	  System.out.println("rrr");
         if ((entity instanceof EntityPlayer)) {
    		         EntityPlayer player = (EntityPlayer)entity;
    		         if ((!EntityAIGuardian.this.golem.checkWhitelist((EntityPlayer)entity)) && (!player.capabilities.isCreativeMode))
    	           return true;
    	      }
    		           return false;
      }   
  };
  
  public void startExecuting()
  {
    this.taskOwner.setAttackTarget(this.targetEntity);
    super.startExecuting();
  }
  
  public static class Sorter
    implements Comparator
  {
    private final Entity theEntity;
    
    public Sorter(Entity p_i1662_1_)
    {
      this.theEntity = p_i1662_1_;
    }
    
    public int compare(Entity p_compare_1_, Entity p_compare_2_)
    {
      double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
      double d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
      return d0 < d1 ? -1 : d0 > d1 ? 1 : 0;
    }
    
    public int compare(Object p_compare_1_, Object p_compare_2_)
    {
      return compare((Entity)p_compare_1_, (Entity)p_compare_2_);
    }
  }

  /*     */   public boolean shouldExecute()
  /*     */   {
  /*  71 */     if ((this.targetChance > 0) && (this.taskOwner.getRNG().nextInt(this.targetChance) != 0))
  /*     */     {
  /*  73 */       return false;
  /*     */     }
  /*  88 */     return true;
  /*     */   }
}
