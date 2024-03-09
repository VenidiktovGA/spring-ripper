package ru.venidiktov.spring.ripper.profiling;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация говорящая добавить в методы класса профилирование (время выполнения метода)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Profiling {
}
