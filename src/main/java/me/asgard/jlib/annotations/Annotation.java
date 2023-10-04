package me.asgard.jlib.annotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.inject.name.Named;
import me.asgard.jlib.JLib;
import me.asgard.jlib.util.FileUtils;
import me.asgard.jlib.util.ReflectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//@Singleton
public class Annotation {

    private ObjectMapper yamlObjectMapper;

    private JLib jLib;

    private Map<Plugin, List<Class>> pluginClassList = Maps.newHashMap();

    //@Inject
    public Annotation(@Named("yamlObjectMapper") ObjectMapper yamlObjectMapper, JLib jLib) {
        this.yamlObjectMapper = yamlObjectMapper;
        this.jLib = jLib;
        setupClasss();
        setupAllAnnotation();
    }

    private void setupClasss() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            pluginClassList.put(plugin, FileUtils.getClasses(plugin));
        }
    }

    private void setupAllAnnotation() {
        pluginClassList.forEach((k, v) -> {
            List<Class> classes = pluginClassList.get(k);
            if (CollectionUtils.isNotEmpty(classes)) {
                setupListeners(k, v);
//                setupConfigs(k, v);
            }
        });
    }

    private void setupListeners(Plugin plugin, List<Class> classList) {
        classList.forEach(c -> {
            JListener jListener = (JListener) c.getAnnotation(JListener.class);
            if (Objects.nonNull(jListener)) {
                // 实例化监听器
                Listener listener;
                try {
                    listener = plugin.getClass().equals(c) ? (Listener) plugin : (Listener) ReflectionUtils.instantiateObject(c);
                    System.out.println("注册监听成功: " + plugin.getName() + "," + c.getName());
                    // 注册监听
                    Bukkit.getPluginManager().registerEvents(listener, plugin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupConfigs(Plugin plugin, List<Class> classList) {
        classList.forEach(c -> {
            JConfig config = (JConfig) c.getAnnotation(JConfig.class);
            if (Objects.nonNull(config)) {
                try {
                    plugin.saveResource(config.configName(), false);
                    Map<String, Object> map = yamlObjectMapper.readValue(new File(jLib.getDataFolder(), config.configName()), Map.class);
                    System.out.println(map);
                    for (Field field : c.getFields()) {
                        ReflectionUtils.setValue(c, false, field.getName(), map.get(field.getName()));
                    }
                    System.out.println("注入配置成功: " + plugin.getName() + "," + c.getName());

                } catch (IOException | IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
