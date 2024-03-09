package ru.venidiktov.spring.ripper.profiling;

public interface ProfilingControllerMBean {
    void setEnabled(boolean enabled);

    boolean isEnabled(); // Без этого метода в JDK Mission Control не возможно изменить значение!
}
