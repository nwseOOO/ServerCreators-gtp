package pl.yspar.core.command;

import java.util.*;
import org.bukkit.command.*;
import org.bukkit.plugin.*;

import pl.yspar.core.utils.Reflection;

import org.bukkit.*;

public class CommandManager
{
    public static final HashMap<String, Command> commands;
    private static final Reflection.FieldAccessor<SimpleCommandMap> f;
    private static CommandMap cmdMap;
    
    static {
        commands = new HashMap<String, Command>();
        f = Reflection.getField(SimplePluginManager.class, "commandMap", SimpleCommandMap.class);
        CommandManager.cmdMap = (CommandMap)CommandManager.f.get(Bukkit.getServer().getPluginManager());
    }
    
    public static void register(final Command cmd) {
        if (CommandManager.cmdMap == null) {
            CommandManager.cmdMap = (CommandMap)CommandManager.f.get(Bukkit.getServer().getPluginManager());
        }
        CommandManager.cmdMap.register(cmd.getName(), (org.bukkit.command.Command)cmd);
        CommandManager.commands.put(cmd.getName(), cmd);
    }
}
