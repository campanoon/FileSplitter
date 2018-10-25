
import java.io.*;
import java.util.Arrays;
import java.util.List;

class ProcessFile {

    private BufferedReader br;
    private List<String> allowedExtensions = Arrays.asList(".txt",".csv",".java",".sql");

    ProcessFile(String fileName) {
        File file = new File(fileName);
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String obtainExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i);
        }
        return extension;
    }

    private String writeSplitFile(String splittedFileName, int numRows, String textLine) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(splittedFileName));
        for (int i = 0; i < numRows && textLine != null; i++) {
            writer.write(textLine + "\n");
            textLine = br.readLine();
        }
        writer.close();
        return textLine;
    }

    void splitFile(String fileName, int numRows) {
        String st;
        int numFiles = 0;

        try {
            String extensionFile = obtainExtension(fileName);
            if (br != null && allowedExtensions.contains(extensionFile)) {
                st = br.readLine();
                while (st != null) {
                    String newName = fileName.replace(extensionFile, "-v") + numFiles + extensionFile;
                    st = writeSplitFile(newName, numRows, st);
                    numFiles++;
                }
                br.close();
            }else {
                System.err.println("Error: Incompatible File");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length==2) {
            ProcessFile processFile = new ProcessFile(args[0]);
            processFile.splitFile(args[0], Integer.parseInt(args[1]));
        }else {
            System.err.println("Error: Please add the file name and the number of rows you want to arguments.");
        }
    }
}
