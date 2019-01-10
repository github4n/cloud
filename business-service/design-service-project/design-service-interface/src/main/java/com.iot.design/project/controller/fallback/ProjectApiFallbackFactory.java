package com.iot.design.project.controller.fallback;

import com.iot.design.project.controller.ProjectApi;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/27
 */
@Component
public class ProjectApiFallbackFactory implements FallbackFactory<ProjectApi> {
    @Override
    public ProjectApi create(Throwable throwable) {
        return null;
    }
}
