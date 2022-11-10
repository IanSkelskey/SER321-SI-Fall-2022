package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import buffers.Message.Request;
import buffers.Message.Response;
import buffers.Message.Type;

import java.util.ArrayList;

public class Protocol {
    private static final ArrayList<String> STRING_ARRAY = new ArrayList<>();

    private static void buildStringArray() {
        STRING_ARRAY.add("Hello");
        STRING_ARRAY.add("Goodbye");
        STRING_ARRAY.add("Hi");
        STRING_ARRAY.add("Bye");
    }

    public static Response createEchoResponse(String message) {
        return Response.newBuilder()
                .setType(Type.ECHO)
                .setBody(message)
                .build();
    }

    public static Response createReverseResponse(String message) {
        StringBuilder reversedMessage = new StringBuilder(message);
        reversedMessage.reverse();
        return Response.newBuilder()
                .setType(Type.REVERSE)
                .setBody(reversedMessage.toString())
                .build();
    }

    public static Response createStringArrayResponse() {
        buildStringArray();
        String message = String.join("\n", STRING_ARRAY);
        return Response.newBuilder()
                .setType(Type.STRING_ARRAY)
                .setBody(message)
                .build();
    }

    public static Response createErrorResponse() {
        return Response.newBuilder()
                .setType(Type.ERROR)
                .setBody("Unable to process your request at this time. Please try again.")
                .build();
    }

    //
    public static Request createEchoRequest(String message) {
        return Request.newBuilder()
                .setType(Type.ECHO)
                .setBody(message)
                .build();
    }

    public static Request createReverseRequest(String message) {
        return Request.newBuilder()
                .setType(Type.REVERSE)
                .setBody(message)
                .build();
    }

    public static Request createStringArrayRequest() {
        return Request.newBuilder()
                .setType(Type.STRING_ARRAY)
                .build();
    }

    public static Request createExitRequest() {
        return Request.newBuilder()
                .setType(Type.EXIT)
                .build();
    }
}
