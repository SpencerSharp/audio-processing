package utils.global;

import java.nio.channels.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import org.mariuszgromada.math.mxparser.*;

public class GlobalFunction {
    protected static final String GLOBAL_FUNCTION_FILE_PATH = 
        "/Users/spencersharp/Documents/Coding/Active/audio-processing/global/functions";

    public int id = -1;
    public String name;
    public String text;

    public Function function;

    private static ArrayList<GlobalFunction> functions = null;

    private LockableFile functionFile = new LockableFile(GLOBAL_FUNCTION_FILE_PATH);

    public GlobalFunction(String name) {
        this.name = name;
        this.load();
    }

    public GlobalFunction(String name, String func) {
        this.name = name;
        this.text = func;

        this.reload();
    }

    private GlobalFunction(int id, String name, String func) {
        this.id = id;
        this.name = name;
        this.text = func;
    }

    public String getRightSide() {
        return text.substring(text.indexOf("=")+1,text.length()).trim();
    }

    public Function asFunction() {
        return function;
    }

    public boolean isValid() {
        System.out.println("eq of " + this.name + " is " + this.text);
        return !this.text.equals("error");
    }

    public static void refresh() {
        functions = null;
    }

    public void load() {
        if (functions == null) {
            System.out.println("LEGIT LOADING");
            functionFile.acquireLock();

            setupGlobalMap();

            if (id < functions.size()) {
                this.text = functions.get(id).text;
                loadFunction();
            } else {
                this.text = "error";
            }

            functionFile.releaseLock();
        } else {
            text = "error";
            for (GlobalFunction function : functions) {
                if(function.name.equals(name)) {
                    id = function.id;
                    text = function.text;
                }
            }
            loadFunction();
        }
    }

    public void reload() {
        functionFile.acquireLock();

        setupGlobalMap();
        updateGlobalMap();
        loadFunction();

        functionFile.releaseLock();
    }

    private void setupGlobalMap() {
        BufferedReader reader = functionFile.getReadBuffer();

        functions = new ArrayList<GlobalFunction>();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                String curName = st.nextToken();
                if (curName.equals(name)) {
                    id = functions.size();
                }
                functions.add(new GlobalFunction(functions.size(), curName, line));
            }
            reader.close();
        } catch (IOException e) {}

        if (id == -1) {
            id = functions.size();
        }
    }

    private void updateGlobalMap() {
        if (id < functions.size()) {
            functions.set(id, this);
        } else {
            functions.add(this);
        }

        functionFile.clear();

        PrintWriter writer = functionFile.getWriteBuffer();

        for (GlobalFunction function : functions) {
            writer.println(function.text);
        }

        writer.close();
    }

    private void loadFunction() {
        String expandedText = this.getRightSide();
        boolean isDone = false;
        while (!isDone) {
            isDone = true;
            for (GlobalFunction function : functions) {
                if (expandedText.contains(function.name)) {
                    // System.out.println("found |" + function.name + "|");
                    isDone = false;
                    expandedText = expandedText.replace(function.name, "(" + function.getRightSide() + ")");
                }
            }
            // System.out.println("|" + expandedText);
        }
        // System.out.println(""+functions.size() + " | " + expandedText);
        expandedText = name + " =" + expandedText;
        this.function = new Function(expandedText);
    }
}