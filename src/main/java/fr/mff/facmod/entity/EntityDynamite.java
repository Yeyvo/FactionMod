package fr.mff.facmod.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityDynamite extends EntityThrowable {
	
	 private int explodefuse = 30;

	public EntityDynamite(World par1World) {

		super(par1World);
	}

	public EntityDynamite(World par1World, EntityLivingBase par2EntityLivingBase) {

		super(par1World, par2EntityLivingBase);
	}

	public EntityDynamite(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6);
	}

	protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
		if (par1MovingObjectPosition.entityHit != null) {
			byte b0 = 0;
			par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), b0);
		}
		for (int i = 0; i < 16; i++) {
			this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0.0D, 0.0D,
					0.0D, new int[0]);
		}
		this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.motionY = 0;

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
				this.explodefuse -= 1;
		
		if (this.explodefuse <= 0)
		{
			if (!this.worldObj.isRemote) {
				float f = 3.0F;
				this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, f, true);
				setDead();
			}
		}
	}

}
