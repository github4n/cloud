package com.iot.shcs;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

/**
 * <p>
 * 测试生成代码
 * </p>
 *
 * @author laiguiming
 * @date 2018/07/05
 */
public class GeneratorServiceEntity {

    @Test
    public void generateCode() {
        //表名
        String table = "automation";

        //初始配置
        String packageName = "com.iot.shcs.ifttt";
        String dir = "F:\\tunk\\cloud\\business-service\\smart-home-service-project\\smart-home-service\\src\\main\\java";
        boolean serviceNameStartWithI = true; //user -> UserService, 设置成true: user -> IUserService
        generateByTables(serviceNameStartWithI,packageName, dir, table);
    }

    private void generateByTables(boolean serviceNameStartWithI, String packageName, String dir, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
        String dbUrl = "jdbc:mysql://192.168.5.11:3306/iot_db_control?characterEncoding=utf8";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername("leedarson")
                .setPassword("Root/123")
                .setDriverName("com.mysql.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(false)
                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames); //修改替换成你需要的表名，多个表名传数组
        config.setActiveRecord(false)
                .setAuthor("laiguiming")
                .setOutputDir(dir)
                .setFileOverride(true);
        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");
        }
        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setController("controller")
                                .setEntity("entity")
                ).execute();
    }
}
