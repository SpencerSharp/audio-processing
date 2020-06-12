package utils.global;

import java.nio.channels.*;
import java.io.*;
import java.nio.file.*;
import java.lang.*;
import java.util.*;
import org.mariuszgromada.math.mxparser.*;

import persistence.*;

public class GlobalFunction {
    public static final String GLOBAL_FUNCTION_FILE_PATH = 
        "/Users/spencersharp/Documents/Coding/Active/audio-processing/global/";

    public int id = -1;
    public String name;
    public String text;

    public Function function;

    private static ArrayList<GlobalFunction> functions = null;

    private static boolean needRefresh = true;

    public static LockableFile functionFile;

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
        needRefresh = true;
        if (functionFile == null) {
            File parentDir = new File(PersistentInfo.getPath());
            Path parentPath = parentDir.toPath();
            Path channelPath = parentPath.resolve(""+PersistentObject.channel);
            File channelFile = channelPath.toFile();

            if (!channelFile.exists()) {
                channelFile.mkdir();
            }

            Path myFilePath = channelPath.resolve("functions");

            functionFile = new LockableFile(myFilePath.toAbsolutePath().toString());
        }
    }

    public void load() {
        if (needRefresh) {
            System.out.println("LEGIT LOADING " + functionFile.file.getName());
            functionFile.acquireLock();

            setupGlobalMap();

            if (id < functions.size()) {
                this.text = functions.get(id).text;
                loadFunction();
            } else {
                this.text = "error";
            }

            needRefresh = false;

            functionFile.releaseLock();
        } else {
            text = "error";
            for (GlobalFunction function : functions) {
                if(function.name.equals(name)) {
                    id = function.id;
                    text = function.text;
                    if (function.function != null) {
                        this.function = function.function;
                    }
                }
            }
            if (false) {
                loadFunction();
            }
        }
    }

    public void reload() {
        functionFile.acquireLock();

        setupGlobalMap();
        updateGlobalMap();
        loadFunction();

        needRefresh = false;

        functionFile.releaseLock();
    }

    private void setupGlobalMap() {
        BufferedReader reader = functionFile.getReadBuffer();

        ArrayList<GlobalFunction> newFunctions = new ArrayList<GlobalFunction>();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (st.hasMoreTokens()) {
                    String curName = st.nextToken();
                    if (curName.equals(name)) {
                        id = newFunctions.size();
                    }
                    newFunctions.add(new GlobalFunction(newFunctions.size(), curName, line));
                }
            }
            reader.close();
        } catch (IOException e) {}

        if (id == -1) {
            id = newFunctions.size();
        }

        if (functions == null || newFunctions.size() >= functions.size() || newFunctions.size() <= functions.size()) {
            functions = newFunctions;
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

    private String getParam(String fText) {
        String noName = fText.replaceFirst(".*?\\(","(");
        if (noName.contains(")")) {
            return noName.substring(1,getOuterMatchingParen(noName));
        }
        return "";
    }

    private String escape(String s) {
        return "\\Q"+s+"\\E";
    }

    private int getOuterMatchingParen(String s) {
        boolean reachedFirst = false;
        int depth = 0;
        int ind = 0;
        while (ind < s.length() && (!reachedFirst || depth != 0)) {
            if (s.charAt(ind) == '(') {
                reachedFirst = true;
                depth++;
            } else if (s.charAt(ind) == ')') {
                depth--;
            }
            ind++;
        }
        return ind - 1;
    }

    private String extractFunc(String myText, String otherName) {
        String shortName = otherName.replaceFirst("\\(.*?\\)","");
        String oldParam = getParam(otherName);
        String beforeParen = ".*?\\W+?" + escape(shortName + "(");
        String afterParen = escape(")") + ".*?";
        String replaceAllBeforeFunc = myText.replaceFirst(beforeParen,shortName + "(");
        if (replaceAllBeforeFunc.equals(myText)) {
            return "";
        }
        return replaceAllBeforeFunc.substring(0,getOuterMatchingParen(replaceAllBeforeFunc)+1);
    }

    private boolean containsFunc(String myText, String otherName) {
        String res = extractFunc(myText, otherName);
        // System.out.println("RES ! " + res + " ? " + myText);
        return otherName.equals(myText) || !(res.equals(""));
    }

    private void loadFunction() {
        String expandedText = this.getRightSide();
        boolean isDone = false;
        while (!isDone) {
            isDone = true;
            for (GlobalFunction function : functions) {
                if (containsFunc(expandedText, function.name)) {
                    // System.out.println("----" + function.name + "|" + expandedText);
                    isDone = false;
                    String itsParam = getParam(function.name);
                    String myCall = extractFunc(expandedText,function.name);
                    String myParam = getParam(myCall);
                    String newFunc = function.getRightSide().replace(itsParam, myParam);
                    expandedText = expandedText.replace(myCall, "(" + newFunc + ")");
                }
                // System.out.println("~~~~" + function.name + "|" + expandedText);
            }
        }
        expandedText = name + " =" + expandedText;
        System.out.println(expandedText);
        this.function = new Function(expandedText);
    }
}