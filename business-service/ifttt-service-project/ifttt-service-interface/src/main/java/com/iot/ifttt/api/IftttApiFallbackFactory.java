package com.iot.ifttt.api;

import com.iot.ifttt.vo.AppletVo;
import com.iot.ifttt.vo.CheckAppletReq;
import com.iot.ifttt.vo.SetEnableReq;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 描述：联动接口熔断器
 * 创建人： LaiGuiMing
 * 创建时间： 2018/10/16 14:39
 */

@Component
public class IftttApiFallbackFactory implements FallbackFactory<IftttApi> {

    @Override
    public IftttApi create(Throwable cause) {
        return new IftttApi() {

            @Override
            public void check(CheckAppletReq req) {

            }

            @Override
            public Long save(AppletVo req) {
                return null;
            }

            @Override
            public void setEnable(SetEnableReq req) {

            }

            @Override
            public AppletVo get(Long id) {
                return null;
            }

            @Override
            public Boolean delete(Long id) {
                return false;
            }

            @Override
            public void countAstroClockJob() {
            }

            @Override
            public void delItem(Long itemId) {

            }
        };
    }
}
