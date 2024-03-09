package ru.venidiktov.spring.ripper.quoters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class TerminatorQuoterTest {
    @Autowired
    private TerminatorQuoter terminatorQuoter; // Вытаскиваем бин по классу это не правильно, надо по интерфейсу

    @Value("${message.terminator}")
    private String message;

    @Test
    void terminatorSay(CapturedOutput output) {
        terminatorQuoter.sayQuote();

        assertThat(output.toString()).contains(message);
    }

}