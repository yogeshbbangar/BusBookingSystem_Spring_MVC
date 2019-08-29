package com.dev.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.dev.beans.User;

@Configuration
@ComponentScan(basePackageClasses=User.class)
public class UserConfig {

}
