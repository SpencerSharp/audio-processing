// package utils.global;

// import java.nio.channels.*;
// import java.io.*;
// import java.lang.*;
// import java.util.*;

// public class GlobalFunction {
//     protected static final String STORAGE_FILE_PATH;

//     private ArrayList<GlobalObject> objects;

//     private LockableFile functionFile = new LockableFile(STORAGE_FILE_PATH);

//     public GlobalObject() {
//         functionFile.acquireLock();

//         setupGlobalMap();
//         updateGlobalMap();

//         functionFile.releaseLock();
//     }

//     private void setupGlobalMap() {
//         BufferedReader reader = functionFile.getReadBuffer();

//         functions = new ArrayList<GlobalObject>();

//         String line;
//         try {
//             while ((line = reader.readLine()) != null) {
//                 StringTokenizer st = new StringTokenizer(line);
//                 String curName = st.nextToken();
//                 if (curName.equals(fName)) {
//                     id = functions.size();
//                 }
//                 functions.add(new GlobalObject(functions.size(), line));
//             }

//             reader.close();
//         } catch (IOException e) {}
//     }

//     private void updateGlobalMap() {
//         functions.set(id, this);

//         functionFile.clear();

//         PrintWriter writer = functionFile.getWriteBuffer();

//         for (GlobalFunction function : functions) {
//             writer.println(function.text);
//         }

//         writer.close();
//     }

//     private void expandText() {
//         expandedText = text;
//         boolean isDone = false;
//         while (!isDone) {
//             isDone = true;
//             for (GlobalFunction function : functions) {
//                 if (expandedText.contains(function.name)) {
//                     isDone = false;
//                     expandedText = expandedText.replaceAll(function.name, function.getRightSide());
//                 }
//             }
//         }
//     }
// }