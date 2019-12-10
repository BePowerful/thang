package com.wcq.thang.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * @author wcq
 * @date 2019/11/8 10:57
 */
@Configuration
public class HttpConverterConfig {
    public HttpMessageConverter fastJsonHttpMessageConverter(){
        //1.定义一个converter转换消息的对象
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //2.添加fastJson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //3.在Converter中添加配置信息
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        //4.将converter赋值给HttpMessageConverter
        HttpMessageConverter<?> converter = fastJsonHttpMessageConverter;
        // 5.返回HttpMessageConverters对
        return (HttpMessageConverter) new HttpMessageConverters(converter);
    }
}
