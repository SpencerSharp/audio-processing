package utils.global;

import java.nio.channels.*;
import java.io.*;
import java.nio.file.*;
import java.lang.*;
import java.util.*;
import org.mariuszgromada.math.mxparser.*;

import persistence.*;

public class GlobalFunctionFactory {
    public static final String GLOBAL_FUNCTION_FILE_PATH = 
        "/Users/spencersharp/Documents/Coding/Active/audio-processing/global/";

    public int id = -1;
    public String name;
    public String text;

    public Function function;

    public ArrayList<GlobalFunction> functions = null;

    public boolean needRefresh = true;

    public LockableFile functionFile;

    public GlobalFunctionFactory() {


    }

    public GlobalFunction get(String name) {

        this.name = name;
        this.load();
    }

    public GlobalFunction create(String name, String func) {

        this.name = name;
        this.text = func;

        this.reload();
    }

    private GlobalFunction(int id, String name, String func) {
        System.out.println("created " + func);
        this.id = id;
        this.name = name;
        this.text = func;
    }

    public String toString() {

        return this.text;
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

    public void refresh() {
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
            GlobalFunction doppel = null;
            for (GlobalFunction function : functions) {
                if(function.name.equals(name)) {
                    doppel = function;
                }
            }
            if (doppel != null) {

                id = doppel.id;
                text = doppel.text;
                if (doppel.function == null) {
                    loadFunction();
                    doppel.function = this.function;
                } else {
                    this.function = doppel.function;
                }
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

        System.out.println(functionFile);

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

        System.out.println(newFunctions);

        if (functions == null || newFunctions.size() > 0 || newFunctions.size() >= functions.size() || newFunctions.size() <= functions.size()) {
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
        String beforeParen = "(^|.*?\\W+?)" + escape(shortName + "(");
        String afterParen = escape(")") + ".*?";
        String keyword = "QWERTY";
        String replaceAllBeforeFunc = myText.replaceFirst(beforeParen,keyword);
        
        if (!replaceAllBeforeFunc.contains(keyword)) {
            return "";
        }
        replaceAllBeforeFunc = replaceAllBeforeFunc.replace(keyword, shortName + "(");
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
                System.out.println("Checking " + function.name);
                if (containsFunc(expandedText, function.name)) {
                    
                    isDone = false;
                    String itsParam = getParam(function.name);
                    String myCall = extractFunc(expandedText,function.name);
                    String myParam = getParam(myCall);
                    String newFunc = function.getRightSide().replace(itsParam, "("+myParam+")");
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