package ru.venidiktov.spring.ripper.screensaver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

import java.awt.*;
import java.util.Random;

/**
 * В данном примере мы хотим получить окошко которое будет менять свой цвет и положение на экране.
 * Технически мы рассматриваем задачу того как обновлять Prototype бин в Singleton бин:
 * - НЕ ПРАВИЛЬНЫЙ вариант, инжектить в бин ColorFrame не Color а контекст Spring (ApplicationContext context) и из контекста доставать бин Color,
 * плохо так как мы создаем зависимость от всего контекста Spring для бина ColorFrame!
 * - ПОЛУЧШЕ, указать proxyMode = ScopedProxyMode.TARGET_CLASS, в таком случае каждый раз при обращении к классу ColorForFrameImpl,
 * да же в рамках одного метода мы получим новый бин Color, или proxyMode = ScopedProxyMode.INTERFACES так же создастся новый экземпляр класса
 * - ХОРОШИЙ вариант, сделать метод абстрактный метод который будет возвращать Color, ну и сам класс ColorFrame так же будет абстрактный
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "ru.venidiktov.spring.ripper.screensaver")
public class Config {
    /**
     * В java конфиге очень удобно создавать бины которые лежак в чижих библиотеках, так как
     * над ними мы не можем ставить стереотипные аннотации
     * <p>
     * Не получилось создать бин с proxyMode = ScopedProxyMode.TARGET_CLAS
     * Could not generate CGLIB subclass of class java.awt.Color: Common causes of this problem include using a final class or a non-visible class
     * <p>
     * Не получилось использовать бин с proxyMode = ScopedProxyMode.TARGET_CLAS в методе getContentPane().setBackground(color); класса ColorFrame
     * Bean named 'color' is expected to be of type 'java.awt.Color' but was actually of type 'jdk.proxy2.$Proxy17'
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Color color() {
        log.info("Обращение к определению  color");
        Random random = new Random();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    /**
     * Так как не получилось создать бин класса Color с proxyMode = ScopedProxyMode.INTERFACES, ScopedProxyMode.TARGET_CLAS
     * создал свой класс и интерфейс их можно использовать с proxyMode = ScopedProxyMode.INTERFACES, ScopedProxyMode.TARGET_CLAS
     * получается что в обоих случаях создается новый объект класса!
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ColorForFrameImpl colorForFrameImpl() {
        log.info("Обращение к определению  colorForFrameImpl");
        return new ColorForFrameImpl();
    }

    @Bean
    public ColorFrameGood colorFrameGood() {
        log.info("Обращение к определению  colorFrameGood");
        return new ColorFrameGood() {
            @Override
            protected Color getColor() {
                log.info("Запрашиваем бин ColorFrameGood");
                return color(); // Обращаемся к определению бина, так как он Prototype каждый раз получаем новый бин!
            }
        };
    }

    /**
     * Если у нас тоит задача обновлять цвет окна каждые 3 секунды, в не зависимости сколько раз в этот промежуток запросили бин
     * (иначе при каждом запросе будет меняться бин), мы решим задачу через создание своего собственного Scope,
     * наш Scope сам решает конда создать новый бин
     */
    @Bean
    @Scope(value = "periodical")
    public Color colorForCustomScope() {
        log.info("Обращение к определению  colorForCustomScope");
        Random random = new Random();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    @Bean
    public ColorFrameForCustomScope colorFrameForCustomScope() {
        log.info("Обращение к определению  colorFrameForCustomScope");
        return new ColorFrameForCustomScope() {
            @Override
            protected Color getColor() {
                log.info("Запрашиваем бин colorForCustomScope");
                return colorForCustomScope(); // Обращаемся к определению бина, его созданием и обновлением управляет наш Scope!
            }
        };
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        while (true) {
            // Внедряем prototype бин через Proxy, даже в рамках одного метода два обращения к бину создадут два бина
//            context.getBean(ColorFrame.class).showOnRandomPlace(); // Для проверки подхода с proxyMode = ScopedProxyMode.TARGET_CLASS

            // Внедряем prototype бин через вызов определения бина в методе переопределенном от абстрактного класса
//            context.getBean(ColorFrameGood.class).showOnRandomPlace(); // Для проверки подхода с абстрактным методом и классом

            // Внедряем prototype бин через свой Scope, наш Scope сам решает когда пора создать новый бин класса
            context.getBean(ColorFrameForCustomScope.class).showOnRandomPlace(); // Для проверки подхода со своим Scope
            Thread.sleep(1000);
        }
    }
}
