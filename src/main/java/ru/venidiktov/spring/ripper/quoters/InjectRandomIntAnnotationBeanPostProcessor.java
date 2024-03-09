package ru.venidiktov.spring.ripper.quoters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Random;

@Slf4j
@Component //Чтоб наш BeanPostProcessor работал добавляем его в контекст Spring
public class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        var fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            var injectRandomInt = field.getAnnotation(InjectRandomInt.class);
            if (injectRandomInt != null) {
                int min = injectRandomInt.min();
                int max = injectRandomInt.max();
                Random random = new Random();
                int i = min + random.nextInt(max - min);
                log.info("Фаза 3 после конструктора и set метода поля, до init метода");
                log.info("Поле repeat = {} тут мы его инициализируем", i);
                /**
                 * Устанавливаем значение в поле через ReflectionUtils чтоб самим не отлавливать
                 * ислючения возникающие при использовании стандартного подхода field.set()
                 */
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, i); // Для какого поля, для когого объекта, какое значение
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
