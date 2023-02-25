package com.edgeburnmedia.playerfreeze;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class PlayerFreeze extends JavaPlugin implements Listener {
	private ArrayList<UUID> frozenPlayers = new ArrayList<>();

	public ArrayList<UUID> getFrozenPlayers() {
		return frozenPlayers;
	}

	public ArrayList<Player> getFrozenPlayersAsPlayers() {
		ArrayList<Player> players = new ArrayList<>();
		for (UUID uuid : frozenPlayers) {
			players.add(Bukkit.getPlayer(uuid));
		}
		return players;
	}

	public void freezePlayer(Player player) {
		final boolean isExempt = player.hasPermission("playerfreeze.exempt");
		if (!isExempt) {
			player.sendMessage("§cYou are now frozen!");
			frozenPlayers.add(player.getUniqueId());
		} else {
			player.sendMessage("You were not frozen because you have the playerfreeze.exempt permission.");
		}
	}

	public void unfreezePlayer(Player player) {
		if (isFrozen(player)) {
			frozenPlayers.remove(player.getUniqueId());
			player.sendMessage("§aYou are no longer frozen!");
		}
	}

	public void freezePlayer(String playerName) {
		Player player = getServer().getPlayer(playerName);
		if (player != null) {
			freezePlayer(player);
		}
	}

	public void unfreezePlayer(String playerName) {
		Player player = getServer().getPlayer(playerName);
		if (player != null) {
			unfreezePlayer(player);
		}
	}

	public boolean isFrozen(Player player) {
		return frozenPlayers.contains(player.getUniqueId());
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		FreezeCommand freezeCommand = new FreezeCommand(this);
		getCommand("freeze").setExecutor(freezeCommand);
		getCommand("unfreeze").setExecutor(freezeCommand);
		getCommand("freezeall").setExecutor(freezeCommand);
		getCommand("unfreezeall").setExecutor(freezeCommand);
		new FrozenPlayerRunnable(this).runTaskTimer(this, 0, 1);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (isFrozen(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (isFrozen(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (isFrozen(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (isFrozen(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player player) {
			if (isFrozen(player)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player player) {
			if (isFrozen(player)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		unfreezePlayer(e.getPlayer());
	}

	public void freezeAllPlayers() {
		getServer().getOnlinePlayers().forEach(this::freezePlayer);
	}

	public void unfreezeAllPlayers() {
		getServer().getOnlinePlayers().forEach(this::unfreezePlayer);
	}
}
