package ru.venidiktov.spring.ripper.quoters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация Это улучшенная версия аннотации @Deprecated, с обязательным
 * указанием актуального заменяющего класса
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DeprecatedClass {
    Class newImpl();
}
