package ru.venidiktov.spring.ripper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringRipperApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(SpringRipperApplication.class, args);

        /**
         * Для просмотра работы профилирования
         */
//        ConfigurableApplicationContext run = SpringApplication.run(SpringRipperApplication.class, args);
//        while (true) {
//            Thread.sleep(100);
//            run.getBean(Quoter.class).sayQuote();
//        }
    }

}
