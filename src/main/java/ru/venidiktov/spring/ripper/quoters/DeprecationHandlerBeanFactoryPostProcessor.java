package ru.venidiktov.spring.ripper.quoters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * Тут мы меняем класс в BeanFactory для бина у которого над классом стоит @DeprecatedClass
 * на тот класс который казан в параметре newImpl аннотации @DeprecatedClass
 * <p>
 * BeanFactoryPostProcessor работает когда контекст еще не начал создаваться, есть только BeanDefinition
 */
@Slf4j
@Component
public class DeprecationHandlerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beansName = beanFactory.getBeanDefinitionNames();
        for (String beanName : beansName) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String originalClassName = beanDefinition.getBeanClassName();
            if (originalClassName != null) { // У некоторых классов Spring может быть null, чтоб не было exception
                try {
                    Class<?> beanClass = Class.forName(originalClassName);
                    DeprecatedClass annotation = beanClass.getAnnotation(DeprecatedClass.class);
                    if (annotation != null) {
                        log.info("Фаза 0, работает BFPP, заменяем @Deprecated на @DeprecatedClass");
                        beanDefinition.setBeanClassName(annotation.newImpl().getName());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
