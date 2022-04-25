package com.edgeburnmedia.playerfreeze;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class FrozenPlayerRunnable extends BukkitRunnable {
	private final PlayerFreeze plugin;
	public FrozenPlayerRunnable(PlayerFreeze plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		plugin.getServer().getOnlinePlayers().forEach(player -> {
			if (plugin.isFrozen(player)) {
				PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 40, 10);
				PotionEffect regeneration = new PotionEffect(PotionEffectType.REGENERATION, 40, 10);
				PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 10);
				player.addPotionEffect(blindness);
				player.addPotionEffect(regeneration);
				player.addPotionEffect(resistance);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§c§lYou are frozen!"));
			}
		});
	}
}
