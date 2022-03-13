package com.zs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//RetentionPolicy.RUNTIME:注解保留在程序运行期间,此时可以通过反射获得定义在某个类上的所有注解.
//ElementType.METHOD：说明该注解只能被声明在一个类的方法前。
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtApiIdempotent {
}