package ru.venidiktov.spring.ripper.screensaver;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * ХОРОШИЙ вариант, сделать метод абстрактный метод который будет возвращать Color, ну и сам класс ColorFrame так же будет абстрактный
 */
@Slf4j
public abstract class ColorFrameGood extends JFrame {

    public ColorFrameGood() {
        log.info("Создается новый объект ColorFrameGood -> {}", this);
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


