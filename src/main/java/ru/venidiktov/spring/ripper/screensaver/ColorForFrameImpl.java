package ru.venidiktov.spring.ripper.screensaver;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Random;

@Slf4j
public class ColorForFrameImpl {

    private int id;

    private final Random random = new Random();

    public ColorForFrameImpl() {
        log.info("Создается новый объект ColorForFrameImpl -> {}", this);
        id = random.nextInt();
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        log.info("У объекта ColorForFrameImpl -> {} вызван метод getColor()", this);
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }
}
