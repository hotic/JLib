package me.asgard.jlib.util;

import com.google.inject.Singleton;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarFile;

@Singleton
public class FileUtils {
    /**
     * 获取插件所有类
     *
     * @return {@link List<Class>}
     */
    public static List<Class> getClasses(Class<?> obj) {
        List<Class> classes = new ArrayList<>();
        URL url = getCaller(obj).getProtectionDomain().getCodeSource().getLocation();
        try {
            File src;
            try {
                src = new File(url.toURI());
            } catch (URISyntaxException e) {
                src = new File(url.getPath());
            }
            new JarFile(src).stream().filter(entry -> entry.getName().endsWith(".class")).forEach(entry -> {
                String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                try {
                    classes.add(Class.forName(className, false, obj.getClassLoader()));
                } catch (Throwable ignored) {
                }
            });
        } catch (Throwable ignored) {
        }
        return classes;
    }

    /**
     * 获取插件所有类
     *
     * @param plugin 插件
     */
    public static List<Class> getClasses(Plugin plugin) {
        return getClasses(plugin, new String[0]);
    }

    /**
     * 获取插件所有类
     *
     * @param plugin 插件
     * @param ignore 忽略包名
     */
    public static List<Class> getClasses(Plugin plugin, String[] ignore) {
        List<Class> classes = new CopyOnWriteArrayList<>();
        URL url = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
        try {
            File src;
            try {
                src = new File(url.toURI());
            } catch (URISyntaxException e) {
                src = new File(url.getPath());
            }
            new JarFile(src).stream().filter(entry -> entry.getName().endsWith(".class")).forEach(entry -> {
                String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                try {
                    if (Arrays.stream(ignore).noneMatch(className::startsWith)) {
                        classes.add(Class.forName(className, false, plugin.getClass().getClassLoader()));
                    }
                } catch (Throwable ignored) {
                }
            });
        } catch (Throwable ignored) {
        }
        return classes;
    }

    // *********************************
    //
    //         Private Methods
    //
    // *********************************

    private static Class getCaller(Class<?> obj) {
        try {
            return Class.forName(Thread.currentThread().getStackTrace()[3].getClassName(), false, obj.getClassLoader());
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }
}
