package com.tracker.presentation;


import com.tracker.business.RecibirArgumentos;

import java.io.File;
import java.nio.file.Path;

public class Main {

    // static Logger logger = new Logger(Logger.getLogger("Gestor"));
    static void main(String[] args) {
        //por ahora se envia la informacion desde la clase "RecibirArgumentos" hasta que implementemos el logger
        RecibirArgumentos.argumento(args);

    }
}
