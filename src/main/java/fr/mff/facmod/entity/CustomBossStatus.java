package fr.mff.facmod.entity;

import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomBossStatus
{
  public static float healthScale;
  public static int statusBarTime;
  public static String bossName;
  
  public static void setBossStatus(IBossDisplayData p_82824_0_, boolean p_82824_1_)
  {
    healthScale = p_82824_0_.getHealth() / p_82824_0_.getMaxHealth();
    statusBarTime = 10;
    if ((p_82824_0_ instanceof EntityFactionGuardian)) {
      bossName = ((EntityFactionGuardian)p_82824_0_).getGuardianNamesave();
    } else {
      bossName = p_82824_0_.getDisplayName().getFormattedText();
    }
    if ((p_82824_0_.getHealth() == 0.0F) || (p_82824_0_.getHealth() == 0.0F))
    {
      healthScale = 1.0F;
      statusBarTime = 0;
      bossName = null;
    }
  }
  
  public static void update()
  {
    if (statusBarTime-- <= 0) {
      bossName = null;
    }
  }
}
