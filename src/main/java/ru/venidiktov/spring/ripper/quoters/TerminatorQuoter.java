package ru.venidiktov.spring.ripper.quoters;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.venidiktov.spring.ripper.listener.PostProxy;
import ru.venidiktov.spring.ripper.profiling.Profiling;

/**
 * !Помним что для XmlBeanDefinitionReader когда мы пишем конфигурация в xml у бина для установки ему полей у этих полей должны быть
 * сеттеры, иначе для XmlBeanDefinitionReader это не свойство, XmlBeanDefinitionReader через reflection попытается вызывать set метод для поля
 * его нет и все упадет!
 */
@Slf4j
@Setter
@Component
@Profiling
/**
 * @DeprecatedClass(newImpl = T1000.class)
 * В DeprecationHandlerBeanFactoryPostProcessor у BeanDefinition для класса TerminatorQuoter меняем
 * поле beanClass на ru.venidiktov.spring.ripper.quoters.T1000 и поэтому вместо него будет создан бин типа T1000
 * !!! Поэтому метод sayQuote() будет вызван для T1000!
 */
@DeprecatedClass(newImpl = T1000.class)
public class TerminatorQuoter implements Quoter {
    @Value("${message.terminator}")
    private String message;

    @InjectRandomInt(min = 2, max = 7)
    private int repeat;

    public TerminatorQuoter() {
        log.info("Фаза 1, конструктор, до вызова методов BPP и init метода!"); // Тут мы не видим значений которые добавляются в BPP postProcessBeforeInitialization()
        log.info("Поле repeat = {} еще не инициализировано так как конструктор отрабатывает до метода postProcessBeforeInitialization", repeat);
    }

    public void setRepeat(int repeat) {
        log.info("Фаза 2, сеттер, после вызова конструктора но до вызова методов BPP и init метода!");
        this.repeat = repeat;
        log.info("Поле repeat = {}, установили поле", repeat);
    }

    @PostConstruct
    public void init() {
        log.info("Фаза 4, PostConstruct, после BPP postProcessBeforeInitialization но до postProcessAfterInitialization!");
        log.info("Поле repeat = {}, ну мы его и не трогали", repeat);
    }

    @Override
    @PostProxy
    public void sayQuote() {
        for (int i = 0; i < repeat; i++) {
            log.info("message = {}", message);
        }
    }
}
