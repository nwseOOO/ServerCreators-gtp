package pl.yspar.core.task;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import pl.yspar.core.basic.User;
import pl.yspar.core.utils.ChatUtil;
import pl.yspar.core.utils.TitleAPI;
import pl.yspar.core.utils.Util;



public class ActionTask extends BukkitRunnable {

    public void run() {
  
        for (final Player p : Bukkit.getOnlinePlayers()) {
            User u = User.get(p);
            if (u.isVanish()) {
            	ChatUtil.sendActionBar(p, Util.fixColor("&8-» &7Vanish: &aWlaczony &8«-"));
            	continue;
            }
        }	
    }
    

}