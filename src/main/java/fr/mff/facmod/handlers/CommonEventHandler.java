package fr.mff.facmod.handlers;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import fr.mff.facmod.core.Homes;
import fr.mff.facmod.core.Lands;
import fr.mff.facmod.network.PacketHelper;

public class CommonEventHandler {

	@SubscribeEvent
	public void onEntityEnteringChunk(TickEvent.PlayerTickEvent event) {
		Lands.onPlayerTick(event);
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		Homes.onPlayerTick(event);
	}
	
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		Homes.onLivingHurt(event);
		Lands.onLivingHurt(event);
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		PacketHelper.updateClientFaction(event.player.getUniqueID());
		PacketHelper.updateClientRank(event.player.getUniqueID());
	}
	
	@SubscribeEvent
	public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		Lands.onPlayerLeave(event);
	}
	
	@SubscribeEvent
	public void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
		Lands.onPlayerBreakBlock(event);
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		Lands.onPlayerInteract(event);
	}
	
	@SubscribeEvent
	public void onPlayerPlaceBlock(PlaceEvent event) {
		Lands.onPlayerPlaceBlock(event);
	}
	
	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) {
		Lands.onBucketFill(event);
	}
	
	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event) {
		Lands.onAttackEntity(event);
	}
	
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		Lands.onLivingAttack(event);
	}
	
	@SubscribeEvent
	public void onExplosion(ExplosionEvent.Detonate event) {
		Lands.onExplosion(event);
	}
}
