package com.kpi.parallel.lab1;

import com.kpi.parallel.lab1.functions.ArithmeticFunction;

import java.io.File;
import java.util.List;

/**
 * The class with <u>main</u> method.
 * @version 1.0-SNAPSHOT
 * @author Linenko Kostyantyn IO-01
 */
public class Main {
    /** Path to folder with resources. */
    private static final String RESOURCE = "src/main/resources";

    /**
     * The main method.
     * @param args args
     */
    public static void main(String[] args) {

        FileHandler handler = new FileHandler(RESOURCE);
        List<File> resources = handler.getResources();
        for (File file : resources) {
            List<ArithmeticFunction> functions = handler.getFunctions(file.getPath());
            functions.forEach(function -> new Thread(((Runnable) function)).start());
        }
    }
}
