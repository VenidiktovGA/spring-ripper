package ru.venidiktov.spring.ripper.quoters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class TerminatorQuoterTest {
    /**
     * org.springframework.beans.factory.UnsatisfiedDependencyException:
     * Error creating bean with name 'ru.venidiktov.spring.ripper.quoters.TerminatorQuoterTest':
     * Unsatisfied dependency expressed through field 'terminatorQuoter':
     * Bean named 'terminatorQuoter' is expected to be of type 'ru.venidiktov.spring.ripper.quoters.TerminatorQuoter'
     * but was actually of type 'jdk.proxy2.$Proxy47'
     * <p>
     * Можно проверить какой класс имеет бин - вот так context.getBean(Quoter.class).getClass()
     */
//    @Autowired
//    private TerminatorQuoter terminatorQuoter; // Вытаскиваем бин по классу это не правильно, надо по интерфейсу

    @Autowired
    private Quoter terminatorQuoter; // Вытаскиваем бин по интерфейсу правильно

    /**
     * Полезные методы для отладки
     * getBeanDefinitionNames() - имена всех бинов из контекста
     */
    @Autowired
    private ApplicationContext context;

    @Value("${message.terminator}")
    private String message;

    @Test
    void terminatorSay(CapturedOutput output) {
        terminatorQuoter.sayQuote();

        assertThat(output.toString()).contains(message);
    }

}