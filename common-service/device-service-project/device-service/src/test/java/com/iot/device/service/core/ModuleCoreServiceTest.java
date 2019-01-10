package com.iot.device.service.core;

import com.alibaba.fastjson.JSON;
import com.iot.device.BaseTest;
import com.iot.device.model.DeviceTypeToServiceModule;
import com.iot.device.model.ServiceModule;
import com.iot.device.model.ServiceModuleProperty;
import com.iot.device.model.enums.DevelopStatusEnum;
import com.iot.device.model.enums.PropertyStatusEnum;
import com.iot.device.service.IDeviceTypeToServiceModuleService;
import com.iot.device.service.IServiceModulePropertyService;
import com.iot.device.service.IServiceModuleService;
import com.iot.device.vo.req.AddOrUpdateServiceModuleReq;
import com.iot.saas.SaaSContextHolder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:01 2018/7/4
 * @Modify by:
 */
public class ModuleCoreServiceTest extends BaseTest {

    Long serviceModuleId = 147L;
    Long deviceTypeId = 54L;
    @Autowired
    private ModuleCoreService moduleCoreService;
    @Autowired
    private IServiceModuleService serviceModuleService;
    @Autowired
    private IDeviceTypeToServiceModuleService deviceTypeToServiceModuleService;
    @Autowired
    private IServiceModulePropertyService serviceModulePropertyService;

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void addOrUpdateModule() {
    }

    @Test
    public void updateActionInfo() {
    }

    @Test
    public void saveServiceModelTest() {

        ServiceModule serviceModule = new ServiceModule();
        serviceModule.setCode("schlage.lock.be469");
        serviceModule.setVersion("1.0.0");
        serviceModule.setName("门锁");
        serviceModule.setDescription("门锁");
        serviceModule.setDevelopStatus(DevelopStatusEnum.IDLE);
        serviceModule.setPropertyStatus(PropertyStatusEnum.MANDATORY);
        serviceModule.setCreateTime(new Date());

        serviceModuleService.insert(serviceModule);
        System.out.println("----147L----->" + serviceModule.getId());
    }

    @Test
    public void saveServiceModuleToProperty() {

        ServiceModuleProperty property = new ServiceModuleProperty();

        property.setServiceModuleId(serviceModuleId);
        property.setDevelopStatus(DevelopStatusEnum.IDLE);
        
        serviceModulePropertyService.insert(property);

    }

    @Test
    public void saveDeviceTypeToServiceModuleTest() {

        System.out.println("--------deviceTypeId  -------------54L");
        DeviceTypeToServiceModule target = new DeviceTypeToServiceModule();
        target.setDeviceTypeId(deviceTypeId);
        target.setServiceModuleId(serviceModuleId);
        target.setCreateTime(new Date());

        deviceTypeToServiceModuleService.insert(target);


        System.out.println("-----423---->" + target.getId());

    }


    @Test
    public void updateServiceModuleInfo() {
        SaaSContextHolder.setCurrentTenantId(2L);
        SaaSContextHolder.setCurrentUserId(1318L);
        String jsonStr = "{\"productId\":1090210617,\"serviceModuleList\":[{\"serviceModuleId\":207,\"parentServiceModuleId\":null,\"actionList\":[{\"actionInfo\":{\"id\":691,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"set_onoff\",\"code\":\"set_onoff\",\"tags\":\"set_onoff\",\"apiLevel\":0,\"developStatus\":1,\"propertyStatus\":1,\"reqParamType\":1,\"returnType\":1,\"params\":null,\"returnDesc\":\"成功，超时\",\"returns\":null,\"testCase\":\"set OnOff，0 = Off, 1 = On\",\"description\":\"set OnOff，0 = Off, 1 = On\",\"iftttType\":0},\"paramPropertyList\":[{\"id\":2897,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"OnOff\",\"code\":\"OnOff\",\"tags\":\"OnOff\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":0,\"paramType\":2,\"minValue\":null,\"maxValue\":null,\"allowedValues\":null,\"testCase\":\"0 = Off，1 = On\",\"description\":\"0 = Off，1 = On\",\"iftttType\":0,\"propertyType\":0,\"updateBy\":null,\"createBy\":null}],\"returnPropertyList\":[{\"id\":2897,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"OnOff\",\"code\":\"OnOff\",\"tags\":\"OnOff\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":0,\"paramType\":2,\"minValue\":null,\"maxValue\":null,\"allowedValues\":null,\"testCase\":\"0 = Off，1 = On\",\"description\":\"0 = Off，1 = On\",\"iftttType\":0,\"propertyType\":0,\"updateBy\":null,\"createBy\":null}]}],\"eventList\":[{\"eventInfo\":{\"id\":653,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"倒计时执行事件\",\"code\":\"CountDownExecEvent\",\"tags\":\"CountDownExecEvent\",\"apiLevel\":0,\"developStatus\":1,\"propertyStatus\":1,\"reqParamType\":null,\"returnType\":null,\"params\":null,\"returnDesc\":null,\"returns\":null,\"testCase\":\"0\",\"description\":\"倒计时执行事件\",\"iftttType\":0},\"propertyList\":null}],\"propertyList\":[{\"id\":2897,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"OnOff\",\"code\":\"OnOff\",\"tags\":\"OnOff\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":0,\"paramType\":2,\"minValue\":null,\"maxValue\":null,\"allowedValues\":null,\"testCase\":\"0 = Off，1 = On\",\"description\":\"0 = Off，1 = On\",\"iftttType\":null,\"propertyType\":null,\"updateBy\":null,\"createBy\":null}],\"tenantId\":null,\"createBy\":null},{\"serviceModuleId\":270,\"parentServiceModuleId\":null,\"actionList\":null,\"eventList\":[{\"eventInfo\":{\"id\":629,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"电量上报事件\",\"code\":\"UpdateEnergyEvent\",\"tags\":\"UpdateEnergyEvent\",\"apiLevel\":0,\"developStatus\":1,\"propertyStatus\":1,\"reqParamType\":null,\"returnType\":null,\"params\":null,\"returnDesc\":null,\"returns\":null,\"testCase\":\"0\",\"description\":\"周期地电量上报事件\",\"iftttType\":0},\"propertyList\":[{\"id\":3063,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"周期电量\",\"code\":\"PeriodEnergy\",\"tags\":\"PeriodEnergy\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":2,\"paramType\":0,\"minValue\":\"0\",\"maxValue\":\"999999\",\"allowedValues\":null,\"testCase\":\"0\",\"description\":\"周期电量，用于电量报表\",\"iftttType\":0,\"propertyType\":0,\"updateBy\":null,\"createBy\":null}]}],\"propertyList\":[{\"id\":3062,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"SingleEnergy\",\"code\":\"SingleEnergy\",\"tags\":\"Energy\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":2,\"paramType\":0,\"minValue\":\"0\",\"maxValue\":\"999999\",\"allowedValues\":null,\"testCase\":\"0\",\"description\":\"单次电量\",\"iftttType\":null,\"propertyType\":null,\"updateBy\":null,\"createBy\":null}],\"tenantId\":null,\"createBy\":null},{\"serviceModuleId\":271,\"parentServiceModuleId\":null,\"actionList\":null,\"eventList\":[{\"eventInfo\":{\"id\":630,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"运行时间上报事件\",\"code\":\"UpdateRuntimeEvent\",\"tags\":\"UpdateRuntimeEvent\",\"apiLevel\":0,\"developStatus\":1,\"propertyStatus\":1,\"reqParamType\":null,\"returnType\":null,\"params\":null,\"returnDesc\":null,\"returns\":null,\"testCase\":\"0\",\"description\":\"周期地运行时间上报事件\",\"iftttType\":0},\"propertyList\":[{\"id\":3067,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"周期运行时间\",\"code\":\"PeriodRuntime\",\"tags\":\"PeriodRuntime\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":2,\"paramType\":0,\"minValue\":\"0\",\"maxValue\":\"999999\",\"allowedValues\":null,\"testCase\":\"0\",\"description\":\"周期运行时间，用于报表\",\"iftttType\":0,\"propertyType\":0,\"updateBy\":null,\"createBy\":null}]}],\"propertyList\":[{\"id\":3066,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"单次运行时间\",\"code\":\"SingleRuntime\",\"tags\":\"SingleRuntime\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":2,\"paramType\":0,\"minValue\":\"0\",\"maxValue\":\"999999\",\"allowedValues\":null,\"testCase\":\"0\",\"description\":\"单次On-Off的运行时间\",\"iftttType\":null,\"propertyType\":null,\"updateBy\":null,\"createBy\":null}],\"tenantId\":null,\"createBy\":null},{\"serviceModuleId\":272,\"parentServiceModuleId\":null,\"actionList\":null,\"eventList\":null,\"propertyList\":[{\"id\":3088,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"倒计时时间\",\"code\":\"CountDown\",\"tags\":\"CountDown\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":0,\"paramType\":0,\"minValue\":\"0\",\"maxValue\":\"43200\",\"allowedValues\":null,\"testCase\":\"0\",\"description\":\"倒计时时间，不超过12H，单位S\",\"iftttType\":null,\"propertyType\":null,\"updateBy\":null,\"createBy\":null}],\"tenantId\":null,\"createBy\":null},{\"serviceModuleId\":273,\"parentServiceModuleId\":null,\"actionList\":null,\"eventList\":null,\"propertyList\":[{\"id\":3107,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"用户模式使能\",\"code\":\"UserModeEnable\",\"tags\":\"UserModeEnable\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":0,\"paramType\":2,\"minValue\":null,\"maxValue\":null,\"allowedValues\":null,\"testCase\":\"0\",\"description\":\"用户模式使能——勿删\",\"iftttType\":null,\"propertyType\":null,\"updateBy\":null,\"createBy\":null}],\"tenantId\":null,\"createBy\":null},{\"serviceModuleId\":321,\"parentServiceModuleId\":null,\"actionList\":null,\"eventList\":null,\"propertyList\":[{\"id\":3063,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"周期电量\",\"code\":\"PeriodEnergy\",\"tags\":\"PeriodEnergy\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":2,\"paramType\":0,\"minValue\":\"0\",\"maxValue\":\"999999\",\"allowedValues\":null,\"testCase\":\"0\",\"description\":\"周期电量，用于电量报表\",\"iftttType\":null,\"propertyType\":null,\"updateBy\":null,\"createBy\":null}],\"tenantId\":null,\"createBy\":null},{\"serviceModuleId\":322,\"parentServiceModuleId\":null,\"actionList\":null,\"eventList\":null,\"propertyList\":[{\"id\":3067,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"周期运行时间\",\"code\":\"PeriodRuntime\",\"tags\":\"PeriodRuntime\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":2,\"paramType\":0,\"minValue\":\"0\",\"maxValue\":\"999999\",\"allowedValues\":null,\"testCase\":\"0\",\"description\":\"周期运行时间，用于报表\",\"iftttType\":null,\"propertyType\":null,\"updateBy\":null,\"createBy\":null}],\"tenantId\":null,\"createBy\":null},{\"serviceModuleId\":323,\"parentServiceModuleId\":null,\"actionList\":null,\"eventList\":null,\"propertyList\":[{\"id\":3090,\"parentId\":null,\"tenantId\":-1,\"serviceModuleId\":0,\"version\":\"V1.0\",\"name\":\"倒计时使能\",\"code\":\"CountDownEnable\",\"tags\":\"CountDownEnable\",\"apiLevel\":0,\"developStatus\":1,\"reqParamType\":1,\"returnType\":1,\"propertyStatus\":1,\"rwStatus\":0,\"paramType\":2,\"minValue\":null,\"maxValue\":null,\"allowedValues\":null,\"testCase\":\"0\",\"description\":\"倒计时使能\",\"iftttType\":null,\"propertyType\":null,\"updateBy\":null,\"createBy\":null}],\"tenantId\":null,\"createBy\":null}]}";
        AddOrUpdateServiceModuleReq serviceModuleReq = JSON.parseObject(jsonStr, AddOrUpdateServiceModuleReq.class);

        moduleCoreService.updateServiceModuleInfo(serviceModuleReq);
    }
}