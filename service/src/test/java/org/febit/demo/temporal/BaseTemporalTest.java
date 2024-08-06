package org.febit.demo.temporal;

import org.febit.boot.FebitBoot;
import org.febit.boot.module.FebitModuleEnvironments;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("local-test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        BaseTemporalTest.TestApplication.class
})
@EmbeddedKafka
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTemporalTest {

    @SpringBootApplication
    @FebitModuleEnvironments(value = AppVersion.class)
    @FebitModuleEnvironments(value = FebitBoot.class, prefix = "febit-boot")
    public static class TestApplication {

        public static void main(String[] args) {
            SpringApplication.run(TestApplication.class, args);
        }
    }
}
