package org.febit.demo.temporal;

import org.febit.boot.FebitBoot;
import org.febit.boot.module.FebitModuleEnvironments;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("local-test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        BaseMvcTest.TestApplication.class
})
public abstract class BaseMvcTest {

    @SpringBootApplication
    @FebitModuleEnvironments(value = AppVersion.class)
    @FebitModuleEnvironments(value = FebitBoot.class, prefix = "febit-boot")
    public static class TestApplication {

        public static void main(String[] args) {
            SpringApplication.run(TestApplication.class, args);
        }
    }
}
