package ru.venidiktov.spring.ripper.quoters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

/**
 * В далеком 2003 году не было аннотаций, поэтому в файле context.properties поле repeat мы прописываем руками,
 * для этого для поля должен быть set метод!
 */
@Slf4j
public class PropertyFileApplicationContext extends GenericApplicationContext {
    public PropertyFileApplicationContext(String fileName) {
        // Принимает контекст для которого PropertiesBeanDefinitionReader будет регистрировать (создавать) BeanDefinition
        PropertiesBeanDefinitionReader propertiesBeanDefinitionReader = new PropertiesBeanDefinitionReader(this);
        int i = propertiesBeanDefinitionReader.loadBeanDefinitions(fileName); // Создаем все BeanDefinition из файла, и получаем количество созданных BeanDefinition
        log.info("PropertyFileApplicationContext нашел {} бинов", i);
        refresh(); // Последняя операция которую делает контекст когда в него закончили добавлять бины
    }

    public static void main(String[] args) {
        PropertyFileApplicationContext propertyFileApplicationContext = new PropertyFileApplicationContext("context.properties");
        propertyFileApplicationContext.getBean(Quoter.class).sayQuote();

    }
}
