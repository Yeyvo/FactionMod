package fr.mff.facmod.util;

public enum EnumFluidLock
{
  STOPAT(1),  IGNORE(2);
  
  public final int mask;
  
  private EnumFluidLock(int mask)
  {
    this.mask = mask;
  }
  
  public static EnumFluidLock fromMask(int inputMask)
  {
    EnumFluidLock[] locks = { STOPAT, IGNORE };
    
    int safeMask = inputMask & 0x3;
    return locks[(safeMask - 1)];
  }
}
