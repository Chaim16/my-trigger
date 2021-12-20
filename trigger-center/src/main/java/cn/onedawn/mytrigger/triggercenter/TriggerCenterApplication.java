package cn.onedawn.mytrigger.triggercenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.onedawn.mytrigger")
public class TriggerCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TriggerCenterApplication.class, args);
    }

}
