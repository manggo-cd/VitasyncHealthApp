package persistence;

import model.VitaSyncData;
import org.json.JSONObject;

import java.io.*;

// CITATION: CPSC210 JSONSERIALIZATION REPOSITORY for the help

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    public JsonWriter(String destination) {
        this.destination = destination;
    }

    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    public void write(VitaSyncData vsData) {
        JSONObject json = vsData.toJson();
        saveToFile(json.toString(TAB));
    }

    public void close() {
        writer.close();
    }

    private void saveToFile(String json) {
        writer.print(json);
    }
}
