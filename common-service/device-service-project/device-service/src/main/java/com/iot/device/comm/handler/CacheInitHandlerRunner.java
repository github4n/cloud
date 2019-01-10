//package com.iot.device.comm.handler;
//
//import com.baomidou.mybatisplus.plugins.Page;
//import com.iot.device.comm.cache.CacheKeyUtils;
//import com.iot.device.core.DeviceCacheCoreUtils;
//import com.iot.device.core.DeviceExtendsCacheCoreUtils;
//import com.iot.device.core.DeviceStateCacheCoreUtils;
//import com.iot.device.core.DeviceStatusCacheCoreUtils;
//import com.iot.device.core.DeviceTypeCacheCoreUtils;
//import com.iot.device.core.ProductCacheCoreUtils;
//import com.iot.device.core.service.DeviceExtendServiceCoreUtils;
//import com.iot.device.core.service.DeviceServiceCoreUtils;
//import com.iot.device.core.service.DeviceStateServiceCoreUtils;
//import com.iot.device.core.service.DeviceTypeServiceCoreUtils;
//import com.iot.device.core.service.ProductServiceCoreUtils;
//import com.iot.device.model.Device;
//import com.iot.device.model.DeviceExtend;
//import com.iot.device.model.DeviceState;
//import com.iot.device.model.DeviceStatus;
//import com.iot.device.model.DeviceType;
//import com.iot.device.model.Product;
//import com.iot.device.service.IDeviceExtendService;
//import com.iot.device.service.IDeviceService;
//import com.iot.device.service.IDeviceStateService;
//import com.iot.device.service.IDeviceStatusService;
//import com.iot.device.service.IDeviceTypeService;
//import com.iot.device.service.IProductService;
//import com.iot.redis.RedisCacheUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//
///**
// * @Author: xfz
// * @Descrpiton:
// * @Date: 9:45 2018/6/22
// * @Modify by:
// */
//@Component
//public class CacheInitHandlerRunner implements CommandLineRunner {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(CacheInitHandlerRunner.class);
//    private static final Integer DEFAULT_PAGE_NUM = 1;
//    private static final Integer DEFAULT_PAGE_SIZE = 50;
//    @Autowired
//    private IDeviceService deviceService;
//    @Autowired
//    private IDeviceExtendService deviceExtendService;
//    @Autowired
//    private IDeviceStatusService deviceStatusService;
//    @Autowired
//    private IDeviceStateService deviceStateService;
//    @Autowired
//    private IDeviceTypeService deviceTypeService;
//    @Autowired
//    private IProductService productService;
//
//    /**
//     * 初始化缓存
//     *
//     * @return
//     * @author lucky
//     * @date 2018/6/22 9:47
//     */
//    @Override
//    public void run(String... args) throws Exception {
////        removeCacheAll();
////        initCache();
//    }
//
//
//    public void initCache() {
//        if (!checkWhetherInitCache()) {
//            LOGGER.info("init--------cache----start");
//            initDevice();
//            initDeviceStatus();
//            initDeviceState();
//            initDeviceExtend();
////        initDeviceToVersion();
//            initDeviceType();
//            initProduct();
//            LOGGER.info("init--------cache----end");
//            RedisCacheUtil.valueSet(CacheKeyUtils.INIT_CACHE_KEY, "true");
//        }
//    }
//
//    public void removeCacheAll() {
//        LOGGER.info("remove--------cache----start");
//        DeviceCacheCoreUtils.removeCacheAll();
//        DeviceExtendsCacheCoreUtils.removeCacheAll();
//        DeviceStateCacheCoreUtils.removeCacheAll();
//        DeviceStatusCacheCoreUtils.removeCacheAll();
//        DeviceTypeCacheCoreUtils.removeCacheAll();
//        ProductCacheCoreUtils.removeCacheAll();
//        //清空缓存
//        RedisCacheUtil.delete(CacheKeyUtils.INIT_CACHE_KEY);
//        LOGGER.info("remove--------cache----end");
//    }
//
//    public boolean checkWhetherInitCache() {
//        String whetherCache = RedisCacheUtil.valueGet(CacheKeyUtils.INIT_CACHE_KEY);
//        if (StringUtils.isEmpty(whetherCache)) {
//            return false;
//        }
//        if (whetherCache.equals("true")) {
//            LOGGER.info("init-----already---cache----end");
//            return true;
//        }
//        return false;
//    }
//
//    public void initDevice() {
//        LOGGER.info("init--------initDevice----start");
//        Page<Device> page = new Page<>(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
//        page = deviceService.selectPage(page);
//        int totalPages = page.getPages();
//        if (page.getTotal() > 0) {
//            List<Device> dataList = page.getRecords();
//            //add cache
//            DeviceServiceCoreUtils.cacheDevices(dataList);
//        }
//        for (int i = 2; i <= totalPages; i++) {
//            page = new Page<>(i, DEFAULT_PAGE_SIZE);
//            page = deviceService.selectPage(page);
//            if (page.getTotal() > 0) {
//                List<Device> dataList = page.getRecords();
//                //add cache
//                DeviceServiceCoreUtils.cacheDevices(dataList);
//            }
//        }
//        LOGGER.info("init--------initDevice----end");
//    }
//
//    public void initDeviceStatus() {
//        LOGGER.info("init--------initDeviceStatus----start");
//        Page<DeviceStatus> page = new Page<>(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
//        page = deviceStatusService.selectPage(page);
//        int totalPages = page.getPages();
//        if (page.getTotal() > 0) {
//            List<DeviceStatus> dataList = page.getRecords();
//            //add cache
////            DeviceStatusServiceCoreUtils.cacheDeviceStatusList(dataList);
//        }
//        for (int i = 2; i <= totalPages; i++) {
//            page = new Page<>(i, DEFAULT_PAGE_SIZE);
//            page = deviceStatusService.selectPage(page);
//            if (page.getTotal() > 0) {
//                List<DeviceStatus> dataList = page.getRecords();
//                //add cache
////                DeviceStatusServiceCoreUtils.cacheDeviceStatusList(dataList);
//            }
//        }
//        LOGGER.info("init--------initDeviceStatus----end");
//    }
//
//    public void initDeviceState() {
//        LOGGER.info("init--------initDeviceState----start");
//        Page<DeviceState> page = new Page<>(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
//        page = deviceStateService.selectPage(page);
//        int totalPages = page.getPages();
//        if (page.getTotal() > 0) {
//            List<DeviceState> dataList = page.getRecords();
//            //add cache
//            DeviceStateServiceCoreUtils.cacheStateList(dataList);
//        }
//        for (int i = 2; i <= totalPages; i++) {
//            page = new Page<>(i, DEFAULT_PAGE_SIZE);
//            page = deviceStateService.selectPage(page);
//            if (page.getTotal() > 0) {
//                List<DeviceState> dataList = page.getRecords();
//                //add cache
//                DeviceStateServiceCoreUtils.cacheStateList(dataList);
//            }
//        }
//        LOGGER.info("init--------initDeviceState----end");
//    }
//
//    public void initDeviceExtend() {
//        LOGGER.info("init--------initDevice----start");
//        Page<DeviceExtend> page = new Page<>(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
//        page = deviceExtendService.selectPage(page);
//        int totalPages = page.getPages();
//        if (page.getTotal() > 0) {
//            List<DeviceExtend> dataList = page.getRecords();
//            //add cache
//            DeviceExtendsCacheCoreUtils.ca(dataList);
//        }
//        for (int i = 2; i <= totalPages; i++) {
//            page = new Page<>(i, DEFAULT_PAGE_SIZE);
//            page = deviceExtendService.selectPage(page);
//            if (page.getTotal() > 0) {
//                List<DeviceExtend> dataList = page.getRecords();
//                //add cache
//                DeviceExtendServiceCoreUtils.cacheDeviceExtendList(dataList);
//            }
//        }
//        LOGGER.info("init--------initDevice----end");
//    }
//
//
//
//    public void initDeviceType() {
//        LOGGER.info("init--------initDeviceType----start");
//        Page<DeviceType> page = new Page<>(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
//        page = deviceTypeService.selectPage(page);
//        int totalPages = page.getPages();
//        if (page.getTotal() > 0) {
//            List<DeviceType> dataList = page.getRecords();
//            //add cache
//            DeviceTypeServiceCoreUtils.cacheDeviceTypeList(dataList);
//        }
//        for (int i = 2; i <= totalPages; i++) {
//            page = new Page<>(i, DEFAULT_PAGE_SIZE);
//            page = deviceTypeService.selectPage(page);
//            if (page.getTotal() > 0) {
//                List<DeviceType> dataList = page.getRecords();
//                //add cache
//                DeviceTypeServiceCoreUtils.cacheDeviceTypeList(dataList);
//            }
//        }
//        LOGGER.info("init--------initDeviceType----end");
//    }
//
//    public void initProduct() {
//        LOGGER.info("init--------initProduct----start");
//        Page<Product> page = new Page<>(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
//        page = productService.selectPage(page);
//        int totalPages = page.getPages();
//        if (page.getTotal() > 0) {
//            List<Product> dataList = page.getRecords();
//            //add cache
//            ProductServiceCoreUtils.cacheProductList(dataList);
//        }
//        for (int i = 2; i <= totalPages; i++) {
//            page = new Page<>(i, DEFAULT_PAGE_SIZE);
//            page = productService.selectPage(page);
//            if (page.getTotal() > 0) {
//                List<Product> dataList = page.getRecords();
//                //add cache
//                ProductServiceCoreUtils.cacheProductList(dataList);
//            }
//        }
//        LOGGER.info("init--------initProduct----end");
//    }
//}
