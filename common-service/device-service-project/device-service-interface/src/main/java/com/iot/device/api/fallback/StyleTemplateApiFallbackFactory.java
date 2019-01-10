package com.iot.device.api.fallback;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.StyleTemplateApi;
import com.iot.device.vo.req.StyleTemplateReq;
import com.iot.device.vo.rsp.StyleTemplateResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StyleTemplateApiFallbackFactory implements FallbackFactory<StyleTemplateApi> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StyleTemplateApiFallbackFactory.class);


    @Override
    public StyleTemplateApi create(Throwable throwable) {
        return new StyleTemplateApi() {
            @Override
            public Long saveOrUpdate(StyleTemplateReq styleTemplateReq) {
                return null;
            }

            @Override
            public void delete(ArrayList<Long> ids) {

            }

            @Override
            public PageInfo<StyleTemplateResp> list(StyleTemplateReq styleTemplateReq) {
                return null;
            }


            @Override
            public List<StyleTemplateResp> listByDeviceTypeId(Long deviceTypeId) {
                return null;
            }

            @Override
            public List<StyleTemplateResp> listByModuleStyleId(Long moduleStyleId) {
                return null;
            }

            @Override
            public List<StyleTemplateResp> listByProductId(Long productId) {
                return null;
            }

            @Override
            public StyleTemplateResp infoById(Long id) {
                return null;
            }
        };
    }
}
