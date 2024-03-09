package ru.venidiktov.spring.ripper.profiling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Надо быть внемательным по конвенции Spring те BPP которые заменяют оригинальный бин на Proxy
 * должны это делать в методе postProcessAfterInitialization а в методе postProcessBeforeInitialization
 * возвращать оригинальный бин, если они вернут Proxy в методе postProcessBeforeInitialization следующий BPP
 * в своем методе postProcessBeforeInitialization он не сможет определить оригинальный класс на который вернулся
 * Proxy класс, вед пришел Proxy, а в методе postProcessAfterInitialization уже можно вернуть Proxy.
 * Помнить что многие BPP в методе postProcessAfterInitialization могут накручивать Proxy на класс тем самым
 * у нас получится цепочка накрученныйх Proxy на класс (Чтоб узнать нужно ли накручивать Proxy на пришедший
 * бин обычно создают мапу и наполняют ее в методе postProcessBeforeInitialization где напротив имени бина стоит его изначальный оригинальный класс)
 */
@Slf4j
@Component
public class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {
    private Map<String, Class> profilingClass = new HashMap<>(); // Оригинальный класс для имени бина
    private ProfilingController profilingController = new ProfilingController();

    /**
     * Регистрируем MBean в MBeanServer, через экземпляр MBeanServer
     */
    public ProfilingHandlerBeanPostProcessor() throws Exception {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        // Регистрируем наш MBean ProfilingController в MBeanServer, в папке profiling с именем controller
        platformMBeanServer.registerMBean(profilingController, new ObjectName("profiling", "name", "controller"));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        var beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Profiling.class)) {
            profilingClass.put(beanName, beanClass); // Запоминаем оригинальный класс над которым стоит аннотация для имени бина
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        var beanClass = profilingClass.get(beanName);
        if (beanClass != null) {
            /**
             * Класс Proxy есть с 1999 года
             * Метод newProxyInstance() - создает объект из нового класса который он сам сгенерит на лету
             * Метод newProxyInstance() принимает: 1) ClassLoader при помощи которого класс загрузится в Heap,
             * 2) список интерфейсов которые должен имплементировать тот класс который сгенерируется на лету,
             * 3) invocationHandler - это код который инкапсулирует логику которая попадет во все методы класса который сгенерируется на лету
             */
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(),
                    (proxy, method, args) -> {
                        if (profilingController.isEnabled()) { // Добавляем доп логику профилирования если надо
                            log.info("Профилирование начато");
                            long before = System.nanoTime();
                            var retVal = method.invoke(bean, args);
                            long after = System.nanoTime();
                            log.info("Профилирование закончено, результат {}", after - before);
                            return retVal;
                        } else {
                            return method.invoke(bean, args);
                        }
                    });
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
