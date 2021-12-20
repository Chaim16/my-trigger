package cn.onedawn.triggertest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("cn.onedawn")
public class TriggerTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TriggerTestApplication.class, args);
    }

}
