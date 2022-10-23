package pl.yspar.core;


import org.bukkit.plugin.java.*;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;


import pl.yspar.core.command.BroadcastCommand;
import pl.yspar.core.command.CcCommand;
import pl.yspar.core.command.CoinsCommand;
import pl.yspar.core.command.Command;
import pl.yspar.core.command.CommandManager;
import pl.yspar.core.command.DepositCommand;
import pl.yspar.core.command.GamemodeCommand;
import pl.yspar.core.command.GraczCommand;
import pl.yspar.core.command.IncognitoCommand;
import pl.yspar.core.command.ItemShopCommand;
import pl.yspar.core.command.KoszCommand;
import pl.yspar.core.command.PrestizCommand;
import pl.yspar.core.command.ProfileCommand;
import pl.yspar.core.command.ResetujRankingCommand;
import pl.yspar.core.command.SaveEqCommand;
import pl.yspar.core.command.SetSaveeqCommand;
import pl.yspar.core.command.SetSpawnCommand;
import pl.yspar.core.command.SpawnCommand;
import pl.yspar.core.command.VanishCommand;
import pl.yspar.core.command.sBCommand;
import pl.yspar.core.command.guild.CreateCommand;
import pl.yspar.core.command.guild.DeleteCommand;
import pl.yspar.core.command.guild.GuildHelpCommand;
import pl.yspar.core.command.guild.InfoCommand;
import pl.yspar.core.command.guild.InviteCommand;
import pl.yspar.core.command.guild.JoinCommand;
import pl.yspar.core.command.guild.KickCommand;
import pl.yspar.core.command.guild.LeaveCommand;
import pl.yspar.core.listener.AsyncPlayerChatListener;
import pl.yspar.core.listener.EntityDamageByEntityListener;
import pl.yspar.core.listener.GtpListener;
import pl.yspar.core.listener.InventoryClickListener;
import pl.yspar.core.listener.ItemConsumeListener;
import pl.yspar.core.listener.JoinQuitListener;
import pl.yspar.core.listener.PlayerAntyLogoutListener;
import pl.yspar.core.listener.PlayerCommandPreprocessListener;
import pl.yspar.core.listener.PlayerDeathListener;
import pl.yspar.core.listener.PlayerInteractListener;
import pl.yspar.core.listener.PlayerRespawnListener;
import pl.yspar.core.listener.SignListener;
import pl.yspar.core.manager.GuildManager;
import pl.yspar.core.manager.TagManager;
import pl.yspar.core.manager.UserManager;
import pl.yspar.core.sidebar.ScoreboardStack;
import pl.yspar.core.sidebar.SidebarRunnable;
import pl.yspar.core.store.MySQL;

import pl.yspar.core.tab.TablistManager;
import pl.yspar.core.task.ActionTask;
import pl.yspar.core.task.AntiLogoutRunnable;
import pl.yspar.core.task.LimitRunnable;
import pl.yspar.core.task.ParticleRunnable;
import pl.yspar.core.task.RefreshThread;
import pl.yspar.core.task.TablistRefreshTask;
import pl.yspar.core.task.TagRunnable;

import pl.yspar.core.utils.ChatUtil;
import pl.yspar.core.utils.Logger;
import pl.yspar.core.utils.Util;

import org.bukkit.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.Player;

import org.bukkit.event.*;


public class CorePlugin extends JavaPlugin {
    private static CorePlugin plugin;
    private static PluginManager pluginManager;
    
    public void onLoad() {
        CorePlugin.plugin = this;
    }
    
    
    public void onEnable() {
		TagManager.init();
	      Config.reloadConfig();
	      Shop.reloadConfig();
		MySQL.getInst().save();
		MySQL.getInst().load();
        this.registerListener();
        Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)this, (Runnable)new ParticleRunnable(), 11L, 11L);
		new ActionTask().runTaskTimerAsynchronously((Plugin)this, 40L, 20L);
        new TablistRefreshTask().runTaskTimerAsynchronously((Plugin)this, 30L, 300L);
		new RefreshThread().start();
		new LimitRunnable().start();
		AntiLogoutRunnable.start();
		new ScoreboardStack().start();
		new TagRunnable().start();
		new SidebarRunnable().start();
        registerCommand();

		Bukkit.getScheduler().runTaskLater((Plugin) this,
		(Runnable) new Runnable() {
			@Override
			public void run() {
				UserManager.registerPlayers();
			}
		}, 60L);

    }



    
    public void onDisable() {
        for (final Player p : Bukkit.getOnlinePlayers()) {
        	TablistManager.executeRemove(p);
        }
        Bukkit.getScheduler().cancelTasks((Plugin)this);
        Bukkit.savePlayers();
        for (final World w : Bukkit.getWorlds()) {
            w.save();
        }
        try {
            Thread.sleep(2000L);
        }
        catch (InterruptedException e2) {
            e2.printStackTrace();
        }
		UserManager.unregisterPlayers();
		MySQL.getInst().save();
    }
    
    public static CorePlugin getPlugin() {
        return CorePlugin.plugin;
    }
    

    public static void registerCommand(final Command command) {
        CommandManager.register(command);
    }
    
    public static void registerListener(final Plugin plugin, final Listener... listeners) {
        if (CorePlugin.pluginManager == null) {
            CorePlugin.pluginManager = Bukkit.getPluginManager();
        }
        for (final Listener listener : listeners) {
            CorePlugin.pluginManager.registerEvents(listener, plugin);
        }
    }
    
    public static void registerCommand() {
        registerCommand(new IncognitoCommand());
        registerCommand(new sBCommand());
        registerCommand(new CreateCommand());
        registerCommand(new DeleteCommand());
        registerCommand(new GuildHelpCommand());
        registerCommand(new InfoCommand());
        registerCommand(new InviteCommand());
        registerCommand(new JoinCommand());
        registerCommand(new CcCommand());
        registerCommand(new KickCommand());
        registerCommand(new KoszCommand());
        registerCommand(new GraczCommand());
        registerCommand(new LeaveCommand());
        registerCommand(new VanishCommand());
        registerCommand(new ItemShopCommand());
        registerCommand(new SetSpawnCommand());
        registerCommand(new SpawnCommand());
        registerCommand(new DepositCommand());
        registerCommand(new BroadcastCommand());
        registerCommand(new PrestizCommand());
        registerCommand(new SetSaveeqCommand());
        registerCommand(new SaveEqCommand());
        registerCommand(new GamemodeCommand());
        registerCommand(new CoinsCommand());
        registerCommand(new ProfileCommand());
        registerCommand(new ResetujRankingCommand());
    }
   
    
    
    public void registerListener() {
    	
        registerListener((Plugin)this, (Listener)new PlayerCommandPreprocessListener());
        registerListener((Plugin)this, (Listener)new JoinQuitListener());
        registerListener((Plugin)this, (Listener)new SignListener());
        registerListener((Plugin)this, (Listener)new GtpListener());
        registerListener((Plugin)this, (Listener)new AsyncPlayerChatListener());
        registerListener((Plugin)this, (Listener)new PlayerInteractListener());
        registerListener((Plugin)this, (Listener)new ItemConsumeListener());
        registerListener((Plugin)this, (Listener)new InventoryClickListener());
        registerListener((Plugin)this, (Listener)new PlayerRespawnListener());
        registerListener((Plugin)this, (Listener)new PlayerDeathListener());
        registerListener((Plugin)this, (Listener)new EntityDamageByEntityListener());
        registerListener((Plugin)this, (Listener)new PlayerAntyLogoutListener());
    }
}

