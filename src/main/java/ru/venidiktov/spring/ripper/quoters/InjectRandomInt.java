package ru.venidiktov.spring.ripper.quoters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Не забыть для своих аннотаций выбрать Retention = RUNTIME,
 * чтоб наша аннотация была видна в runtime и мы могли ее считать черерз reflection (Если надо конечно)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectRandomInt {
    int min();

    int max();
}
