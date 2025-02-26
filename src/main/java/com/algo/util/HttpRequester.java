package com.algo.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Map;

public class HttpRequester {
    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 30000;
    private static final String USER_AGENT_KEY = "User-Agent";
    //private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";
    //private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 Edg/129.0.0.0";
    private static final String USER_AGENT = "Apifox/1.0.0 (https://apifox.com)";
    public static String get(String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection conn = createConnection(url, HTTP_GET);
        conn.connect();
        return readDataString(conn);
    }

    public static String post(String address, String data, Map<String, String> headers) throws IOException {
        URL url = new URL(address);
        HttpURLConnection conn = createConnection(url, HTTP_POST);
        for (String key : headers.keySet()) {
            conn.setRequestProperty(key, headers.get(key));
        }
        conn.connect();

        try (OutputStream outputStream = conn.getOutputStream()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                writer.write(data);
                writer.flush();
            }
        }

        return readDataString(conn);
    }

    public static byte[] getBytes(String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection conn = createConnection(url, HTTP_GET);
        conn.connect();
        return readDateBytes(conn);
    }

    public static byte[] postBytes(String address, String data, Map<String, String> headers) throws IOException {
        URL url = new URL(address);
        HttpURLConnection conn = createConnection(url, HTTP_POST);
        for (String key : headers.keySet()) {
            conn.setRequestProperty(key, headers.get(key));
        }
        conn.connect();

        try (OutputStream outputStream = conn.getOutputStream()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                writer.write(data);
                writer.flush();
            }
        }

        return readDateBytes(conn);
    }

    private static HttpURLConnection createConnection(URL url, String method) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setRequestMethod(method);
        conn.setRequestProperty(USER_AGENT_KEY, USER_AGENT);
        if (method.equals(HTTP_POST)) {
            conn.setDoOutput(true);
        }
        return conn;
    }

    private static byte[] readDateBytes(HttpURLConnection conn) throws IOException {
        try (InputStream inputStream = conn.getInputStream()) {
            return inputStream.readAllBytes();
        }
    }

    private static String readDataString(HttpURLConnection conn) throws IOException {
        try (InputStream inputStream = conn.getInputStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                return builder.toString();
            }
        }
    }
}
