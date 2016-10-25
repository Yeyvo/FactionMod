package fr.mff.facmod.model;

import java.util.HashMap;

import client.MCAClientLibrary.MCAModelRenderer;
import common.MCACommonLibrary.MCAVersionChecker;
import common.MCACommonLibrary.animation.AnimationHandler;
import common.MCACommonLibrary.math.Matrix4f;
import common.MCACommonLibrary.math.Quaternion;
import fr.mff.facmod.entity.EntityFactionGuardian;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelZygarde extends ModelBase {
public final int MCA_MIN_REQUESTED_VERSION = 5;
public HashMap<String, MCAModelRenderer> parts = new HashMap<String, MCAModelRenderer>();

MCAModelRenderer shape1;
MCAModelRenderer shape2;
MCAModelRenderer shape3;
MCAModelRenderer shape4;
MCAModelRenderer shape5;
MCAModelRenderer shape6;
MCAModelRenderer shape7;
MCAModelRenderer shape8;
MCAModelRenderer shape9;
MCAModelRenderer shape10;
MCAModelRenderer shape11;
MCAModelRenderer shape12;
MCAModelRenderer shape13;
MCAModelRenderer shape14;
MCAModelRenderer shape15;
MCAModelRenderer shape16;
MCAModelRenderer shape17;
MCAModelRenderer shape18;
MCAModelRenderer shape19;
MCAModelRenderer shape20;
MCAModelRenderer shape21;
MCAModelRenderer shape22;
MCAModelRenderer shape23;
MCAModelRenderer shape24;
MCAModelRenderer shape25;
MCAModelRenderer shape26;
MCAModelRenderer shape27;
MCAModelRenderer shape28;
MCAModelRenderer shape29;
MCAModelRenderer shape30;
MCAModelRenderer shape31;
MCAModelRenderer bas;

public ModelZygarde()
{
MCAVersionChecker.checkForLibraryVersion(getClass(), MCA_MIN_REQUESTED_VERSION);

textureWidth = 128;
textureHeight = 128;

shape1 = new MCAModelRenderer(this, "Shape1", 30, 86);
shape1.mirror = false;
shape1.addBox(-5.0F, -10.0F, -2.0F, 12, 10, 4);
shape1.setInitialRotationPoint(-1.0F, -3.0F, 7.0F);
shape1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
shape1.setTextureSize(128, 128);
parts.put(shape1.boxName, shape1);

shape2 = new MCAModelRenderer(this, "Shape1", 30, 72);
shape2.mirror = false;
shape2.addBox(-5.5F, -10.0F, -2.0F, 11, 10, 4);
shape2.setInitialRotationPoint(0.0F, -12.0F, 7.0F);
shape2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.25881904F, 0.0F, 0.0F, 0.9659258F)).transpose());
shape2.setTextureSize(128, 128);
parts.put(shape2.boxName, shape2);

shape3 = new MCAModelRenderer(this, "Shape1", 30, 100);
shape3.mirror = false;
shape3.addBox(-6.0F, -10.0F, -2.0F, 12, 10, 4);
shape3.setInitialRotationPoint(0.0F, 6.0F, 3.5F);
shape3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.17364818F, 0.0F, 0.0F, 0.9848077F)).transpose());
shape3.setTextureSize(128, 128);
parts.put(shape3.boxName, shape3);

shape4 = new MCAModelRenderer(this, "Shape1", 30, 114);
shape4.mirror = false;
shape4.addBox(-6.0F, -10.0F, -2.0F, 12, 10, 4);
shape4.setInitialRotationPoint(0.0F, 15.0F, 3.5F);
shape4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
shape4.setTextureSize(128, 128);
parts.put(shape4.boxName, shape4);

shape5 = new MCAModelRenderer(this, "Shape1", 30, 30);
shape5.mirror = false;
shape5.addBox(-6.0F, -10.0F, -2.0F, 12, 10, 4);
shape5.setInitialRotationPoint(0.0F, -15.5F, -10.5F);
shape5.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.70710677F, 0.0F, 0.0F, 0.70710677F)).transpose());
shape5.setTextureSize(128, 128);
parts.put(shape5.boxName, shape5);

shape6 = new MCAModelRenderer(this, "Shape1", 62, 114);
shape6.mirror = false;
shape6.addBox(-5.5F, -0.0F, -2.0F, 11, 10, 4);
shape6.setInitialRotationPoint(0.0F, -15.0F, -19.0F);
shape6.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.86602545F, 0.0F, 0.0F, 0.49999997F)).transpose());
shape6.setTextureSize(128, 128);
parts.put(shape6.boxName, shape6);

shape7 = new MCAModelRenderer(this, "Shape1", 62, 100);
shape7.mirror = false;
shape7.addBox(-5.0F, -10.0F, -2.0F, 10, 10, 4);
shape7.setInitialRotationPoint(0.0F, -20.0F, -26.0F);
shape7.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.81915206F, 0.0F, 0.0F, 0.57357645F)).transpose());
shape7.setTextureSize(128, 128);
parts.put(shape7.boxName, shape7);

shape8 = new MCAModelRenderer(this, "Shape2", 62, 87);
shape8.mirror = false;
shape8.addBox(-1.5F, -10.0F, -1.5F, 3, 10, 3);
shape8.setInitialRotationPoint(0.0F, -16.5F, -35.0F);
shape8.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.76604444F, 0.0F, 0.0F, 0.64278764F)).transpose());
shape8.setTextureSize(128, 128);
parts.put(shape8.boxName, shape8);

shape9 = new MCAModelRenderer(this, "Shape2", 62, 87);
shape9.mirror = false;
shape9.addBox(-1.5F, -9.0F, -1.5F, 3, 10, 3);
shape9.setInitialRotationPoint(4.0F, -16.5F, -35.0F);
shape9.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.7544065F, -0.1116189F, 0.13302222F, 0.63302225F)).transpose());
shape9.setTextureSize(128, 128);
parts.put(shape9.boxName, shape9);

shape10 = new MCAModelRenderer(this, "Shape3", 0, 0);
shape10.mirror = false;
shape10.addBox(-9.0F, -18.0F, -3.0F, 18, 18, 6);
shape10.setInitialRotationPoint(0.0F, 27.0F, 6.5F);
shape10.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.08715574F, 0.0F, 0.0F, 0.9961947F)).transpose());
shape10.setTextureSize(128, 128);
parts.put(shape10.boxName, shape10);

shape11 = new MCAModelRenderer(this, "Shape4", 92, 0);
shape11.mirror = false;
shape11.addBox(-14.0F, -0.0F, -2.0F, 14, 14, 4);
shape11.setInitialRotationPoint(0.0F, 34.0F, 7.0F);
shape11.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.85716075F, 0.38348323F, -0.13204387F, 0.31745934F)).transpose());
shape11.setTextureSize(128, 128);
parts.put(shape11.boxName, shape11);

shape12 = new MCAModelRenderer(this, "Shape1", 65, 0);
shape12.mirror = false;
shape12.addBox(-4.5F, -8.0F, -4.0F, 9, 8, 4);
shape12.setInitialRotationPoint(0.0F, 27.0F, 13.0F);
shape12.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.42261827F, 0.0F, 0.0F, 0.90630776F)).transpose());
shape12.setTextureSize(128, 128);
parts.put(shape12.boxName, shape12);

shape13 = new MCAModelRenderer(this, "Shape5", 0, 105);
shape13.mirror = false;
shape13.addBox(-2.5F, -10.0F, -1.0F, 5, 10, 1);
shape13.setInitialRotationPoint(0.0F, 32.0F, 2.0F);
shape13.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.17364818F, 0.0F, 0.0F, 0.9848077F)).transpose());
shape13.setTextureSize(128, 128);
parts.put(shape13.boxName, shape13);

shape14 = new MCAModelRenderer(this, "Shape5", 0, 117);
shape14.mirror = false;
shape14.addBox(-2.5F, -10.0F, -1.0F, 5, 10, 1);
shape14.setInitialRotationPoint(12.0F, 30.0F, 2.0F);
shape14.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.16043F, 0.06645229F, -0.37686962F, 0.9098437F)).transpose());
shape14.setTextureSize(128, 128);
parts.put(shape14.boxName, shape14);

shape15 = new MCAModelRenderer(this, "Shape5", 0, 94);
shape15.mirror = false;
shape15.addBox(-2.5F, -10.0F, -1.0F, 5, 10, 1);
shape15.setInitialRotationPoint(-12.0F, 30.0F, 2.0F);
shape15.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.16043F, -0.06645229F, 0.37686962F, 0.9098437F)).transpose());
shape15.setTextureSize(128, 128);
parts.put(shape15.boxName, shape15);

shape16 = new MCAModelRenderer(this, "Shape5", 0, 83);
shape16.mirror = false;
shape16.addBox(-2.5F, -10.0F, -1.0F, 5, 10, 1);
shape16.setInitialRotationPoint(-15.0F, 19.0F, 2.0F);
shape16.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.1116189F, -0.13302222F, 0.7544065F, 0.63302225F)).transpose());
shape16.setTextureSize(128, 128);
parts.put(shape16.boxName, shape16);

shape17 = new MCAModelRenderer(this, "Shape5", 0, 72);
shape17.mirror = false;
shape17.addBox(-2.5F, -10.0F, -1.0F, 5, 10, 1);
shape17.setInitialRotationPoint(15.0F, 19.0F, 2.0F);
shape17.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.1116189F, 0.13302222F, -0.7544065F, 0.63302225F)).transpose());
shape17.setTextureSize(128, 128);
parts.put(shape17.boxName, shape17);

shape18 = new MCAModelRenderer(this, "Shape5", 12, 72);
shape18.mirror = false;
shape18.addBox(-3.5F, -19.0F, -1.0F, 7, 19, 2);
shape18.setInitialRotationPoint(0.0F, 50.0F, -1.0F);
shape18.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.08715574F, 0.0F, 0.0F, 0.9961947F)).transpose());
shape18.setTextureSize(128, 128);
parts.put(shape18.boxName, shape18);

shape19 = new MCAModelRenderer(this, "Shape5", 12, 93);
shape19.mirror = false;
shape19.addBox(-3.5F, -15.0F, -1.0F, 7, 15, 2);
shape19.setInitialRotationPoint(22.0F, 40.0F, -1.0F);
shape19.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.0805214F, 0.03335306F, -0.38122723F, 0.9203639F)).transpose());
shape19.setTextureSize(128, 128);
parts.put(shape19.boxName, shape19);

shape20 = new MCAModelRenderer(this, "Shape5", 12, 55);
shape20.mirror = false;
shape20.addBox(-3.5F, -15.0F, -1.0F, 7, 15, 2);
shape20.setInitialRotationPoint(-22.0F, 40.0F, -1.0F);
shape20.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.0805214F, -0.03335306F, 0.38122723F, 0.9203639F)).transpose());
shape20.setTextureSize(128, 128);
parts.put(shape20.boxName, shape20);

shape21 = new MCAModelRenderer(this, "Shape5", 12, 38);
shape21.mirror = false;
shape21.addBox(-3.5F, -15.0F, -1.0F, 7, 15, 2);
shape21.setInitialRotationPoint(-28.0F, 16.5F, -1.0F);
shape21.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.05602263F, -0.06676517F, 0.7631294F, 0.64034164F)).transpose());
shape21.setTextureSize(128, 128);
parts.put(shape21.boxName, shape21);

shape22 = new MCAModelRenderer(this, "Shape5", 12, 111);
shape22.mirror = false;
shape22.addBox(-3.5F, -15.0F, -1.0F, 7, 15, 2);
shape22.setInitialRotationPoint(28.0F, 16.5F, -1.0F);
shape22.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.05602263F, 0.06676517F, -0.7631294F, 0.64034164F)).transpose());
shape22.setTextureSize(128, 128);
parts.put(shape22.boxName, shape22);

shape23 = new MCAModelRenderer(this, "Shape6", 56, 0);
shape23.mirror = false;
shape23.addBox(-1.5F, -6.0F, -0.5F, 3, 6, 1);
shape23.setInitialRotationPoint(0.0F, 55.0F, -1.0F);
shape23.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
shape23.setTextureSize(128, 128);
parts.put(shape23.boxName, shape23);

shape24 = new MCAModelRenderer(this, "Shape6", 56, 0);
shape24.mirror = false;
shape24.addBox(-1.5F, -6.0F, -0.5F, 3, 6, 1);
shape24.setInitialRotationPoint(33.0F, 16.0F, -1.0F);
shape24.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, -0.76604444F, 0.64278764F)).transpose());
shape24.setTextureSize(128, 128);
parts.put(shape24.boxName, shape24);

shape25 = new MCAModelRenderer(this, "Shape6", 56, 0);
shape25.mirror = false;
shape25.addBox(-1.5F, -6.0F, -0.5F, 3, 6, 1);
shape25.setInitialRotationPoint(-33.0F, 15.5F, -1.0F);
shape25.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.76604444F, 0.64278764F)).transpose());
shape25.setTextureSize(128, 128);
parts.put(shape25.boxName, shape25);

shape26 = new MCAModelRenderer(this, "Shape6", 57, 0);
shape26.mirror = false;
shape26.addBox(-1.5F, -6.0F, -0.5F, 3, 6, 1);
shape26.setInitialRotationPoint(26.0F, 44.0F, -1.0F);
shape26.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, -0.38268346F, 0.9238795F)).transpose());
shape26.setTextureSize(128, 128);
parts.put(shape26.boxName, shape26);

shape27 = new MCAModelRenderer(this, "Shape6", 56, 0);
shape27.mirror = false;
shape27.addBox(-1.5F, -6.0F, -0.5F, 3, 6, 1);
shape27.setInitialRotationPoint(-26.0F, 44.0F, -1.0F);
shape27.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.38268346F, 0.9238795F)).transpose());
shape27.setTextureSize(128, 128);
parts.put(shape27.boxName, shape27);

shape28 = new MCAModelRenderer(this, "Shape7", 0, 59);
shape28.mirror = false;
shape28.addBox(-1.5F, -12.0F, -1.0F, 3, 12, 1);
shape28.setInitialRotationPoint(19.0F, 9.0F, 3.0F);
shape28.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, -0.81915206F, 0.57357645F)).transpose());
shape28.setTextureSize(128, 128);
parts.put(shape28.boxName, shape28);

shape29 = new MCAModelRenderer(this, "Shape7", 0, 46);
shape29.mirror = false;
shape29.addBox(-1.5F, -12.0F, -1.0F, 3, 12, 1);
shape29.setInitialRotationPoint(-19.0F, 9.0F, 3.0F);
shape29.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.81915206F, 0.57357645F)).transpose());
shape29.setTextureSize(128, 128);
parts.put(shape29.boxName, shape29);

shape30 = new MCAModelRenderer(this, "Shape7", 0, 28);
shape30.mirror = false;
shape30.addBox(-1.5F, -8.0F, -1.0F, 3, 8, 1);
shape30.setInitialRotationPoint(12.0F, 5.0F, 3.0F);
shape30.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, -0.9063078F, 0.42261824F)).transpose());
shape30.setTextureSize(128, 128);
parts.put(shape30.boxName, shape30);

shape31 = new MCAModelRenderer(this, "Shape7", 0, 37);
shape31.mirror = false;
shape31.addBox(-1.5F, -8.0F, -1.0F, 3, 8, 1);
shape31.setInitialRotationPoint(-12.0F, 5.0F, 3.0F);
shape31.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.9063078F, 0.42261824F)).transpose());
shape31.setTextureSize(128, 128);
parts.put(shape31.boxName, shape31);

bas = new MCAModelRenderer(this, "Bas", 0, 0);
bas.mirror = false;
bas.addBox(0.0F, -2.0F, 0.0F, 10, 18, 2);
bas.setInitialRotationPoint(-5.0F, -21.0F, 0.0F);
bas.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.5948228F, 0.0F, 0.0F, 0.80385685F)).transpose());
bas.setTextureSize(128, 128);
parts.put(bas.boxName, bas);

}

@Override
public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) 
{
EntityFactionGuardian entity = (EntityFactionGuardian)par1Entity;


//Render every non-child part
shape1.render(par7);
shape2.render(par7);
shape3.render(par7);
shape4.render(par7);
shape5.render(par7);
shape6.render(par7);
shape7.render(par7);
shape8.render(par7);
shape9.render(par7);
shape10.render(par7);
shape11.render(par7);
shape12.render(par7);
shape13.render(par7);
shape14.render(par7);
shape15.render(par7);
shape16.render(par7);
shape17.render(par7);
shape18.render(par7);
shape19.render(par7);
shape20.render(par7);
shape21.render(par7);
shape22.render(par7);
shape23.render(par7);
shape24.render(par7);
shape25.render(par7);
shape26.render(par7);
shape27.render(par7);
shape28.render(par7);
shape29.render(par7);
shape30.render(par7);
shape31.render(par7);
bas.render(par7);
}
@Override
public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {}

public MCAModelRenderer getModelRendererFromName(String name)
{
return parts.get(name);
}
}