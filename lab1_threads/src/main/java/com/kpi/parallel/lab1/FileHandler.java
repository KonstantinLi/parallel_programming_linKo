package com.kpi.parallel.lab1;

import com.kpi.parallel.lab1.functions.ArithmeticFunction;
import com.kpi.parallel.lab1.functions.FunctionA;
import com.kpi.parallel.lab1.functions.FunctionB;
import com.kpi.parallel.lab1.functions.FunctionC;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Class-parser of <b>.json</b> files containing data for functions.
 * Performs a full-check of file's validation and finally return the {@link List}
 * of {@link ArithmeticFunction}-implemented classes.
 * @version 1.0-SNAPSHOT
 * @author Linenko Kostyantyn IO-01
 */
public class FileHandler {
    /**
     * Mapping of functions and appropriate data (names and types of variables).
     */
    private static final Map<String, Map<String, String>> FUNCTIONS_AND_VARIABLES =
            Map.of("F1", Map.of(
                        "A", "vector", "B", "vector", "C", "vector",
                            "MA", "matrix", "ME", "matrix"),
                    "F2", Map.of(
                            "MF", "matrix", "MG", "matrix", "MH", "matrix"),
                    "F3", Map.of(
                            "O", "vector", "P", "vector",
                            "MP", "matrix", "MS", "matrix")
            );

    /**
     * The list of resources.
     */
    private final List<File> resources = new ArrayList<>();

    /**
     * The lone constructor.
     * @param pathToFolder path to folder with resources.
     */
    public FileHandler(String pathToFolder) {
        findJSONFiles(pathToFolder);
    }

    /**
     * Fills out the list of <b>.json</b> files. It's a recursive method.
     * @param pathToFolder path to folder with resources.
     */
    private void findJSONFiles(String pathToFolder) {
        File folder = new File(pathToFolder);
        if (!folder.isDirectory()) {
            throw new RuntimeException();
        }
        File[] files = folder.listFiles();

        Arrays.stream(files).forEach(file -> {
            String path = file.getPath();
            if (file.isDirectory()) {
                findJSONFiles(path);
            } else if (path.endsWith(".json")) {
                resources.add(file);
            }
        });
    }

    /**
     * Returns created classes of functions existed in resource.
     * @param pathToFile path to folder with resources.
     * @return list of functions.
     */
    public List<ArithmeticFunction> getFunctions(String pathToFile) {
        List<ArithmeticFunction> functions = new ArrayList<>();

        File file = new File(pathToFile);
        if (!file.exists() || !file.getName().endsWith(".json")) {
            return null;
        }

        try {
            Reader reader = new FileReader(pathToFile);
            JSONArray data = (JSONArray) new JSONParser().parse(reader);
            reader.close();
            for (Object object : data) {
                JSONObject jsonObject = (JSONObject) object;

                if (validateJSONData(jsonObject)) {
                    String nameFunction = (String) jsonObject.get("function");
                    if (nameFunction.equals("F1")) {
                        functions.add(createF1(jsonObject));
                    } else if (nameFunction.equals("F2")) {
                        functions.add(createF2(jsonObject));
                    } else if (nameFunction.equals("F3")) {
                        functions.add(createF3(jsonObject));
                    }
                }
            }
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }

        return functions;
    }

    /**
     * Creates a {@link FunctionA} class.
     * There ought to be a timely validation before calling of the method.
     * @param jsonObject {@link JSONObject}
     * @return function.
     */
    private ArithmeticFunction createF1(JSONObject jsonObject) {
        int size = (int) ((long) jsonObject.get("size"));
        JSONArray data = (JSONArray) jsonObject.get("data");

        Map<String, Object> variables = Map.of(
                "A", new int[size],
                "B", new int[size],
                "C", new int[size],
                "MA", new int[size][size],
                "ME", new int[size][size]
        );

        for (Object object : data) {
            JSONObject variable = (JSONObject) object;
            String nameVariable = (String) variable.get("name");
            JSONArray valuesJSON = (JSONArray) variable.get("values");

            if (variable.get("type").equals("vector")) {
                copyArrayFromJSON(size, valuesJSON, (int[]) variables.get(nameVariable));
            } else {
                copyMatrixFromJSON(size, valuesJSON, (int[][]) variables.get(nameVariable));
            }
        }

        return new FunctionA(size,
                (int[]) variables.get("A"),
                (int[]) variables.get("B"),
                (int[]) variables.get("C"),
                (int[][]) variables.get("MA"),
                (int[][]) variables.get("ME"));
    }

    /**
     * Creates a {@link FunctionB} class.
     * There ought to be a timely validation before calling of the method.
     * @param jsonObject {@link JSONObject}
     * @return function.
     */
    private ArithmeticFunction createF2(JSONObject jsonObject) {
        int size = (int) ((long) jsonObject.get("size"));
        JSONArray data = (JSONArray) jsonObject.get("data");

        Map<String, int[][]> variables = Map.of(
                "MF", new int[size][size],
                "MG", new int[size][size],
                "MH", new int[size][size]
        );

        for (Object object : data) {
            JSONObject variable = (JSONObject) object;
            String nameVariable = (String) variable.get("name");
            JSONArray valuesJSON = (JSONArray) variable.get("values");

            copyMatrixFromJSON(size, valuesJSON, variables.get(nameVariable));
        }

        return new FunctionB(size,
                variables.get("MF"),
                variables.get("MG"),
                variables.get("MH"));
    }

    /**
     * Creates a {@link FunctionC} class.
     * There ought to be a timely validation before calling of the method.
     * @param jsonObject {@link JSONObject}
     * @return function.
     */
    private ArithmeticFunction createF3(JSONObject jsonObject) {
        int size = (int) ((long) jsonObject.get("size"));
        JSONArray data = (JSONArray) jsonObject.get("data");

        Map<String, Object> variables = Map.of(
                "O", new int[size],
                "P", new int[size],
                "MP", new int[size][size],
                "MS", new int[size][size]
        );

        for (Object object : data) {
            JSONObject variable = (JSONObject) object;
            String nameVariable = (String) variable.get("name");
            JSONArray valuesJSON = (JSONArray) variable.get("values");

            if (variable.get("type").equals("vector")) {
                copyArrayFromJSON(size, valuesJSON, (int[]) variables.get(nameVariable));
            } else {
                copyMatrixFromJSON(size, valuesJSON, (int[][]) variables.get(nameVariable));
            }
        }

        return new FunctionC(size,
                (int[]) variables.get("O"),
                (int[]) variables.get("P"),
                (int[][]) variables.get("MP"),
                (int[][]) variables.get("MS"));
    }

    /**
     * Copies a {@link JSONArray} object to array.
     * @param size size of array.
     * @param source source.
     * @param target target.
     */
    private void copyArrayFromJSON(int size, JSONArray source, int[] target) {
        int[] values = source
                .stream()
                .mapToInt(obj -> (int) ((long) obj))
                .toArray();

        for (int i = 0; i < size; i++) {
            target[i] = values[i];
        }
    }

    /**
     * Copies a {@link JSONArray} object to matrix.
     * @param size size of array.
     * @param source source.
     * @param target target.
     */
    private void copyMatrixFromJSON(int size, JSONArray source, int[][] target) {
        int[][] values = new int[size][size];
        for (int i = 0; i < size; i++) {
            values[i] = ((JSONArray) source.get(i))
                    .stream()
                    .mapToInt(obj -> (int) ((long) obj))
                    .toArray();
        }

        for (int i = 0; i < size; i++) {
            System.arraycopy(values[i], 0, target[i], 0, size);
        }
    }

    /**
     * Validates a {@link JSONObject} on its internal structure.
     * @param jsonObject data
     * @return true or false
     */
    private boolean validateJSONData(JSONObject jsonObject) {
        String nameFunction = (String) jsonObject.get("function");
        Map<String, String> variables = FUNCTIONS_AND_VARIABLES.get(nameFunction);
        JSONArray data = (JSONArray) jsonObject.get("data");

        if (data.size() != variables.size() || !data.stream().allMatch(object -> {
            JSONObject jsonVariable = (JSONObject) object;
            String nameVariable = (String) jsonVariable.get("name");
            String type = (String) jsonVariable.get("type");

            return variables.get(nameVariable).equals(type);
        })) {
            return false;
        }

        long size = (long) jsonObject.get("size");

        for (Object object : data) {
            JSONObject jsonVariable = (JSONObject) object;
            String type = (String) jsonVariable.get("type");
            JSONArray values = (JSONArray) jsonVariable.get("values");

            if (type.equals("vector") && values.size() != size) {
                return false;

            } else if (type.equals("matrix") && !values.stream().allMatch(rowObject -> {
                JSONArray row = (JSONArray) rowObject;
                return row.size() == size;
            })) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the {@link List} of resources.
     * @return list.
     */
    public List<File> getResources() {
        return resources;
    }
}
