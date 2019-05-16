/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of ActiveViam Ltd. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */

package com.activeviam.training.cfg.source;

import com.qfs.msg.csv.ICSVParserConfiguration;
import com.qfs.msg.csv.ICSVTopic;
import com.qfs.msg.csv.filesystem.impl.FileSystemCSVTopicFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Source configuration loading data from the file system.
 *
 * @author ActiveViam
 */
@Configuration
public class LocalPathSourceConfig extends ASourceConfig<Path> {

    public static final String STORAGE_ROOT_PROPERTY = "source.data.root";

    @Override
    protected ICSVTopic<Path> createTopic(String topic, String file, ICSVParserConfiguration parserConfig) {
        return csvTopicFactory().createTopic(topic, getAbsolutePath(file), parserConfig);
    }

    @Override
    protected ICSVTopic<Path> createDirectoryTopic(String topic, String filePattern,
                                                   ICSVParserConfiguration parserConfig) {
    	String directoryToUse = env.getProperty(STORAGE_ROOT_PROPERTY);
        return csvTopicFactory().createDirectoryTopic(topic, directoryToUse, filePattern,
                parserConfig);
    }

    @Bean
    public FileSystemCSVTopicFactory csvTopicFactory() {
        return new FileSystemCSVTopicFactory(false);
    }

    @Bean
    public AutoCloseable watcherServiceStopper() {
        return csvTopicFactory().getWatcherService()::close;
    }

    protected String getAbsolutePath(String relativePath) {
        String root = env.getRequiredProperty(STORAGE_ROOT_PROPERTY);
        if (relativePath.isEmpty())
            return root;
        return Paths.get(root, relativePath).toString();
    }

}