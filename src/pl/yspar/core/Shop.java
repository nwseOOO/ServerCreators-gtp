package pl.yspar.core;


import org.bukkit.configuration.file.*;


import pl.yspar.core.utils.ChatUtil;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

import org.bukkit.*;

public class Shop
{

    public static int CENA_WEDKA;
    public static int CENA_PAJECZYNA;
    public static int CENA_SNIEZKI;
    public static int CENA_OBSYDIAN;
    public static int CENA_LOD;


    
    static {
        Shop.CENA_WEDKA = 200;
        Shop.CENA_PAJECZYNA = 2000;
        Shop.CENA_SNIEZKI = 100;
        Shop.CENA_OBSYDIAN = 600;
        Shop.CENA_LOD = 1000;
    }
    
    public static void loadConfig() {
        try {
            CorePlugin.getPlugin().saveDefaultConfig();
            final FileConfiguration c = CorePlugin.getPlugin().getConfig();
            Field[] fields;
            for (int length = (fields = Shop.class.getFields()).length, i = 0; i < length; ++i) {
                final Field f = fields[i];
                if (c.isSet("shop." + f.getName().toLowerCase().replace("_", "."))) {
                    f.set(null, c.get("shop." + f.getName().toLowerCase().replace("_", ".")));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void saveConfig() {
        try {
            final FileConfiguration c = CorePlugin.getPlugin().getConfig();
            Field[] fields;
            for (int length = (fields = Shop.class.getFields()).length, i = 0; i < length; ++i) {
                final Field f = fields[i];
                c.set("shop." + f.getName().toLowerCase().replace("_", "."), f.get(null));
            }
            CorePlugin.getPlugin().saveConfig();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public static void reloadConfig() {
        CorePlugin.getPlugin().reloadConfig();
        loadConfig();
        saveConfig();
    }
}
