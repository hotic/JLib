package me.asgard.jlib;

import com.google.inject.Guice;
import com.google.inject.Inject;
import me.asgard.jlib.config.Config;
import me.asgard.jlib.di.module.BaseModule;
import me.asgard.jlib.util.JUtil;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.Entity;
import java.io.File;

public final class JLib extends JavaPlugin {

//    @Inject
//    private Annotation annotation;

    @Inject
    private Config config;

    @Override
    public void onEnable() {
        System.out.println(Entity.class);
        System.out.println(Entity.class.getResource(""));
        for (String s : System.getProperty("java.class.path").split(";")) {
            System.out.println(s);
        }
        Guice.createInjector(new BaseModule(this)).injectMembers(this);
        saveDefaultConfig();
        JUtil.ConfigInject(new File(getDataFolder(), "config.yml"), this.config);
//        System.out.println(this.config);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
