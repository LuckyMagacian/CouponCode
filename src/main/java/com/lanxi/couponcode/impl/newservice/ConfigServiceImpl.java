package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.spi.config.ConstConfig;
import com.lanxi.couponcode.spi.config.StaticConfig;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 配置接口实现类
 *
 * @author yangyuanjian
 */
@Order (Ordered.HIGHEST_PRECEDENCE)
@Service ("configService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class ConfigServiceImpl extends StaticConfig implements ConfigService {
    private volatile Map<String, Properties> configs;

    public ConfigServiceImpl() {
        load();
    }

    @Override
    public String getValue(String fileName, String keyName) {
        // 若配置文件名称不是.properties结尾,补全
        if (fileName.endsWith(".properties")) {
            LogFactory.debug(this, "fileName:[" + fileName + "] end with .properties ! remove .properties");
            fileName = fileName.replace(".properties", "");
        }
        // 判断文件是否存在,若不存在报错
        Properties properties = configs.get(fileName);
        if (properties == null) {
            LogFactory.error(this, "file :[" + fileName + "] not found !");
            throw new RuntimeException("no such file :[" + fileName + "]");
        }
        // 获取配置的值
        String value = configs.get(fileName).getProperty(keyName);
        LogFactory.debug(this, "get value of [" + keyName + "] in [" + fileName + "] :" + value);
        return value;
    }

    @Override
    public String getValue(String keyName) {
        // 获取configs里所有的配置,映射为entry,通过keyName过滤,获取符合该配置名称的值
        Optional<Entry<Object, Object>> value = configs.entrySet().parallelStream()
                .flatMap(e -> e.getValue().entrySet().stream()).filter(e -> e.getKey().equals(keyName)).findAny();
        LogFactory.debug(this, "value of [" + keyName + "] in random file is [" + value + "]");
        return value.equals(Optional.empty()) ? null : value.get().getValue().toString();
    }

    @Override
    public synchronized void reload() {
        LogFactory.info(this, "reload configs !");
        load();
    }

    private synchronized void load() {
        // 获取class-path路径
        String classPath = ConfigServiceImpl.class.getClassLoader().getResource("").getPath();
        // 获取配置文件路径
        String configPath = ConstConfig.STATIC_CONFIG_PATH.replace("classpath:", classPath);
        LogFactory.debug(this, "开始加载[" + configPath + "]下的配置文件!-----------------------------------------------------");
        // 校验配置路径
        File file = new File(configPath);
        if (!file.exists()) {
            LogFactory.error(this, "no config foud in path : [" + configPath + "]");
            throw new RuntimeException("no properties !");
        }
        configs = new HashMap<>();
        // 获取配置文件列表
        File[] files = file.listFiles();
        LogFactory.debug(this,
                "load configs :" + Stream.of(files).parallel().map(e -> e.getName()).collect(Collectors.toList()));
        // 迭代配置路径下的文件
        Stream.of(files).forEach(e -> {
            // 判断是否是properteis文件
            if (!e.getName().endsWith(".properties"))
                return;
            InputStream in = null;
            try {
                in = new FileInputStream(e);
                InputStreamReader reader = new InputStreamReader(in, "utf-8");
                Properties props = new Properties();
                // 加载配置到properties
                props.load(reader);
                // 存入config
                configs.put(e.getName().replace(".properties", ""), props);
                LogFactory.debug(this, e.getName() + ":" + props);
                in.close();
                LogFactory.debug(this, "load file [" + e.getName() + "] success !");
            } catch (FileNotFoundException e1) {
                throw new RuntimeException("file : \"" + e.getAbsolutePath() + "\" not found !");
            } catch (IOException e1) {
                throw new RuntimeException(
                        "there's an exception occurred when read file : \"" + e.getAbsolutePath() + "\" !");
            } finally {
                try {
                    if (in != null)
                        in.close();
                } catch (IOException e1) {
                    throw new RuntimeException(
                            "there's an exception occurred when close file : \"" + e.getAbsolutePath() + "\" !");
                }
            }
        });
        LogFactory.debug(this, "[" + configPath + "]下的配置文件加载完成!-----------------------------------------------------");
    }

}
