package ecoduino.com.br.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class FirebaseClient {

    private static final String BASE_URL =
        "https://ecoduino-8163d-default-rtdb.firebaseio.com/";
    
    

    private static String authToken = null;

    public static void setAuthToken(String token) {
        authToken = token;
    }

    private static String buildUrl(String path) {
        String url = BASE_URL + path;
        if (authToken != null) {
            url += "?auth=" + authToken;
        }
        return url;
    }

    private static HttpURLConnection openConnection(String urlStr, String method) throws Exception {
        URI uri = URI.create(urlStr);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        return conn;
    }

    public static String get(String path) throws Exception {
        HttpURLConnection conn = openConnection(buildUrl(path), "GET");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }
}
