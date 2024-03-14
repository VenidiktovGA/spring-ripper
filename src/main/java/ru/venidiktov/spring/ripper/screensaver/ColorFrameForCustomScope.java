package ru.venidiktov.spring.ripper.screensaver;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

@Slf4j
public abstract class ColorFrameForCustomScope extends JFrame {

    public ColorFrameForCustomScope() {
        log.info("Создается новый объект ColorFrameForCustomScope -> {}", this);
        setSize(200, 200);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Изменить позицию на случайную и цвет на заданный
     */
    public void showOnRandomPlace() {
        Random random = new Random();
        setLocation(random.nextInt(800), random.nextInt(300));
        getContentPane().setBackground(getColor());
        repaint();
    }

    protected abstract Color getColor();

}


