package me.asgard.jlib.di.module;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import me.asgard.jlib.JLib;
import me.asgard.jlib.util.JUtil;

public class BaseModule extends AbstractModule {

    private JLib jLib;

    public BaseModule(JLib jLib) {
        this.jLib = jLib;
    }

    @Override
    protected void configure() {
        initObjectMapper();
        initYamlObjectMapper();
        bind(JLib.class).toInstance(this.jLib);
        requestStaticInjection(JUtil.class);
    }


    private void initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //反序列化时，属性不存在的兼容处理
        objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        bind(ObjectMapper.class).annotatedWith(Names.named("objectMapper")).toInstance(objectMapper);
    }

    private void initYamlObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        //反序列化时，属性不存在的兼容处理
        objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        bind(ObjectMapper.class).annotatedWith(Names.named("yamlObjectMapper")).toInstance(objectMapper);
    }
}
