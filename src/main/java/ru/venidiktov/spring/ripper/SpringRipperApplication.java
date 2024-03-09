package ru.venidiktov.spring.ripper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.venidiktov.spring.ripper.quoters.Quoter;

@SpringBootApplication
public class SpringRipperApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext run = SpringApplication.run(SpringRipperApplication.class, args);
        while (true) {
            Thread.sleep(100);
            run.getBean(Quoter.class).sayQuote();
        }
    }

}
