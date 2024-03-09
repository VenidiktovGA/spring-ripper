package ru.venidiktov.spring.ripper.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Слушаем только ContextRefreshedEvent
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PostProxyInvokerContextListener implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * Возникает вопрос о том что инжектить в бины контекст Spring это плохая идея,
     * но тут это нормально так как мы инжектим бин Spring в бин Spring,
     * а вот если мы инжектим бин Spring в наш бин приложения это плохо (cappaling)
     */
    private final ConfigurableListableBeanFactory beanFactory; // Используем для возможности получить данные оригинального класса

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        var applicationContext = event.getApplicationContext();
        var beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) { // Помним что на этом этапе в контексте уже Proxy а не оригинальные классы!
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            String originalClassName = beanDefinition.getBeanClassName(); // В BeanDefinition есть оригинальное название класса
            if (originalClassName != null) { // У некоторых классов Spring может быть null, чтоб не было некрасивых exception
                try {
                    Class<?> originalClass = Class.forName(originalClassName);
                    Method[] methods = originalClass.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(PostProxy.class)) {
                            /**
                             * Просто вызвать метод вот так нельзя method.invoke(), так как
                             * мы его вызовем у оригинально класса, а мы сейчас имеет бин Proxy это два разных класса
                             */
                            Object bean = applicationContext.getBean(name);
                            // Вытаскиваем метод из Proxy, это будет тот же метод оригинального класса над которым стоит @PostProxy
                            Method currentMethod = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                            log.info("Фаза 6, все BPP отработали, произошло событие того что весь контекст построился");
                            currentMethod.invoke(bean);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
