package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Protocol {
    private static final ArrayList<String> STRING_ARRAY = new ArrayList<>();

    private static void buildStringArray() {
        STRING_ARRAY.add("Hello");
        STRING_ARRAY.add("Goodbye");
        STRING_ARRAY.add("Hi");
        STRING_ARRAY.add("Bye");
    }

    public static JSONObject createEchoResponse(String message) {
        JSONObject response = new JSONObject();
        response.put("type", "echo");
        response.put("body", message);
        return response;
    }

    public static JSONObject createReverseResponse(String message) {
        JSONObject response = new JSONObject();
        response.put("type", "reverse");
        StringBuilder reversedMessage = new StringBuilder(message);
        reversedMessage.reverse();
        response.put("body", reversedMessage);
        return response;
    }

    public static JSONObject createStringArrayResponse() {
        buildStringArray();
        JSONObject response = new JSONObject();
        JSONArray array = new JSONArray();
        response.put("type", "string array");
        for (String s: STRING_ARRAY) {
            array.put(s);
        }
        response.put("body", array);
        return response;
    }

    public static JSONObject createErrorResponse() {
        JSONObject response = new JSONObject();
        response.put("type", "error");
        response.put("body", "Unable to process your request at this time. Please try again.");
        return response;
    }

    public static JSONObject createEchoRequest(String message) {
        JSONObject request = new JSONObject();
        request.put("type", "echo");
        request.put("body", message);
        return request;
    }

    public static JSONObject createReverseRequest(String message) {
        JSONObject request = new JSONObject();
        request.put("type", "reverse");
        request.put("body", message);
        return request;
    }

    public static JSONObject createStringArrayRequest() {
        JSONObject request = new JSONObject();
        request.put("type", "string array");
        return request;
    }

    public static JSONObject createExitRequest() {
        JSONObject request = new JSONObject();
        request.put("type", "exit");
        return request;
    }
}
