package org.febit.demo.temporal;

import org.febit.boot.FebitBoot;
import org.febit.boot.module.FebitModuleEnvironments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = {
        org.febit.demo.temporal.config.SpringdocConfig.class
})
@FebitModuleEnvironments(value = AppVersion.class)
@FebitModuleEnvironments(value = FebitBoot.class, prefix = "febit-boot")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
