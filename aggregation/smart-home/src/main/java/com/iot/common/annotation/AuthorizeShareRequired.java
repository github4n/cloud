package com.iot.common.annotation;

import com.iot.common.enums.PermissionEnum;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeShareRequired {
    PermissionEnum value();  //权限数据
}