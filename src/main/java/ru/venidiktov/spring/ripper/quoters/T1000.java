package ru.venidiktov.spring.ripper.quoters;

import lombok.extern.slf4j.Slf4j;
import ru.venidiktov.spring.ripper.listener.PostProxy;

@Slf4j
/**
 * @DeprecatedClass(newImpl = T1000.class)
 * В DeprecationHandlerBeanFactoryPostProcessor у BeanDefinition для класса TerminatorQuoter меняем
 * поле beanClass на ru.venidiktov.spring.ripper.quoters.T1000 и поэтому вместо него будет создан бин типа T1000
 * !!! Поэтому метод sayQuote() будет вызван для T1000!
 * !!! Если и этот класс T1000 пометить @Component то метод sayQuote() будет вызван два раза для T1000
 * (Один раз для оригинала второй раз для класса где есть аннотация @DeprecatedClass(newImpl = T1000.class))
 */
//@Component
public class T1000 extends TerminatorQuoter {
    @Override
    @PostProxy
    public void sayQuote() {
        log.info("Я ЖИДКИЙ!");
    }
}
