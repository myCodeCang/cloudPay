package com.thinkgem.jeesite.modules.tasks.entity;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;

import static java.util.stream.Collectors.joining;

/**
 * Created by yankai on 2017/6/18.
 */
public interface UserTaskBusiness {
    public static final String BUSINESS_METHOD_NAME = "doBusiness";

    /**
     * 需要调度的业务方法
     * @return
     */
    boolean doBusiness();

    default Logger getLogger() {
        Logger logger = Logger.getLogger(getClass());
        FileAppender appender = (FileAppender)Logger.getRootLogger().getAppender("RollingFile");

        String name = getClass().getName();
        String path = Arrays.stream(name.split("\\.")).collect(joining(File.separator));

        String url = SpringContextHolder.ROOT_PATH + "/WEB-INF/logs/" + File.separator + path + ".log";
        appender.setFile(url);
        appender.activateOptions();
        return logger;
    }
}
