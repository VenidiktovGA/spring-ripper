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
public class TerminatorQuoter implements Quoter {
    @Value("${message.terminator}")
    private String message;

    @InjectRandomInt(min = 2, max = 7)
    private int repeat;

    public TerminatorQuoter() {
        log.info("Фаза 1 до вызова методов BPP и init метода!"); // Тут мы не видим значений которые добавляются в BPP postProcessBeforeInitialization()
        log.info("Поле repeat = {} еще не инициализировано так как конструктор отрабатывает до метода postProcessBeforeInitialization", repeat);
    }

    public void setRepeat(int repeat) {
        log.info("Фаза 2, после вызова конструктора но до вызова методов BPP и init метода!");
        this.repeat = repeat;
        log.info("Поле repeat = {}, установили поле", repeat);
    }

    @PostConstruct
    public void init() {
        log.info("Фаза 4 после BPP postProcessBeforeInitialization но до postProcessAfterInitialization!");
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
