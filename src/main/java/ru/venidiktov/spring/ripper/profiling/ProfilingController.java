package ru.venidiktov.spring.ripper.profiling;

/**
 * Данный бин мы зарегистрируем как MBean в MBeanServer чтоб можно было управлять значение его полей через
 * JMX console (не связано со Spring)
 * На MBean построили Jboss.
 * Когда поднимается java процесс поднимается MBeanServer и все объекты которые зарегистрированные в нем доступны
 * через JMX console видны, видны их свойства их можно менять, видны их методы их можно запускать
 * <p>
 * Конвенция старая, чтобы класс попал в MBeanServer класс должен имплементировать интерфейс который должен называться
 * как класс но в конце у него должно быть MBean, в данном интерфейсе мы указываем те методы через к которым хотим
 * получить доступ в JMX console! Так же MBean надо зарегистрировать в MBeanServer можно через ManagenetnFactory,
 * необходимо не забывать что в интерфейсе должен быть и get метод!
 */
//    @Component // В каких то случаях его можно регистрировать как бин тогда для всего приложения будет 1 рубильник
public class ProfilingController implements ProfilingControllerMBean {
    private boolean enabled = true;

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
