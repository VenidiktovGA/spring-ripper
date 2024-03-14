package ru.venidiktov.spring.ripper.screensaver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Random;

@Component
public class ColorFrame extends JFrame {
    @Autowired
    private ColorForFrameImpl color;

    public ColorFrame() {
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
        getContentPane().setBackground(color.getColor());
        repaint();
    }

}


