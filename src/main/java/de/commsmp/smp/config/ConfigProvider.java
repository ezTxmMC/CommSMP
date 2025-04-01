package de.commsmp.smp.config;

import de.eztxm.ezlib.config.reflect.JsonProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ConfigProvider {

    private Map<Class<?>, JsonProcessor<?>> processorMap = new HashMap<>();

    public ConfigProvider() {}

    public <T> void registerConfig(Class<T> clazz) {
        JsonProcessor<T> jsonProcessor = JsonProcessor.loadConfiguration(clazz);
        processorMap.put(clazz, jsonProcessor);
        injectProcessor(clazz, jsonProcessor);
        callOnLoad(jsonProcessor.getInstance(), clazz);
    }

    private <T> void injectProcessor(Class<T> clazz, JsonProcessor<T> processor) {
        try {
            Field field = clazz.getField("processor");
            field.setAccessible(true);
            field.set(processor.getInstance(), processor);
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void callOnLoad(Object object, Class<T> clazz) {
        try {
            Method method = clazz.getMethod("onLoad");
            method.setAccessible(true);
            method.invoke(object);
        } catch (NoSuchMethodException ignored) {
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig(Class<?> clazz) {
        processorMap.get(clazz).saveConfiguration();
    }

    public <T> JsonProcessor<T> getProcessor(Class<T> clazz) {
        return (JsonProcessor<T>) processorMap.get(clazz);
    }

}
