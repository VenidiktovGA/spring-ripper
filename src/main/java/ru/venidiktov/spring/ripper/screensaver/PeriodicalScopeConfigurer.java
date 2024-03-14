package ru.venidiktov.spring.ripper.screensaver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.time.Duration;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Написали свой Scope
 * Надо контексту до его создания (то есть BFPP) сказать что теперь за Scope = "periodical" отвечает данный конфигуратор!
 * Регистрируем наш обработчик Scope в CustomScopeRegistryBeanFactoryPostProcessor
 */
@Slf4j
public class PeriodicalScopeConfigurer implements Scope {
    private Map<String, AbstractMap.SimpleEntry<LocalTime, Object>> cache = new HashMap<>();

    /**
     * В данном методе мы решаем отдавать новый бин или может закешированный,
     * в зависимости от того прошло 3 секунд или нет
     * objectFactory.getObject() - создает новый бин!
     */
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        log.info("Работает обработчик periodical Scope");
        var now = LocalTime.now();
        if (cache.containsKey(name)) {
            AbstractMap.SimpleEntry<LocalTime, Object> localTimeObjectPair = cache.get(name);
            var durationLifeBean = Duration.between(now, localTimeObjectPair.getKey()).getSeconds();
            if (Math.abs(durationLifeBean) > 3) {
                log.info("Бин {} устарел, кладем новый бин", name);
                cache.put(name, new AbstractMap.SimpleEntry(now, objectFactory.getObject()));
            }
        } else {
            log.info("Бина {} нет, кладем новый бин", name);
            cache.put(name, new AbstractMap.SimpleEntry(now, objectFactory.getObject()));
        }
        return cache.get(name).getValue();
    }

    @Override
    public Object remove(String name) {
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
