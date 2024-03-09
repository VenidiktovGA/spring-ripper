package ru.venidiktov.spring.ripper.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Маркерная аннотация
 * Метод аннотированный этой аннотацие должен запустится сам в тот момент когда контекст настроена и все Proxy настроены (BPP)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostProxy {
}
