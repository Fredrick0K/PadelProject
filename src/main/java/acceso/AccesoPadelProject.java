/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package acceso;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Pilar
 */
public class AccesoPadelProject {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private static String token = "";
    private static int idUsuarioActual = -1;

    public static void setToken(String t) {
        token = t;
    }

    public static void setIdUsuario(int id) {
        idUsuarioActual = id;
    }

    public static int getIdUsuario() {
        return idUsuarioActual;
    }

    public void respuestaServidor(HttpClient httpClient, String endPoint) {
        try {
            HttpRequest peticion = HttpRequest.newBuilder().uri(URI.create(BASE_URL + endPoint)).GET().build();
            HttpResponse<String> respuesta = httpClient.send(peticion, BodyHandlers.ofString());
            if (respuesta.statusCode() == 200) {
                Object json = mapper.readValue(respuesta.body(), Object.class);

                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));

            } else {
                System.out.println("Error al obtener datos: " + respuesta.statusCode());
                System.out.println(respuesta.body());
            }
        } catch (Exception e) {
            System.out.println("Error al ejecutar");
            e.printStackTrace();
        }
    }

    // Para obtener un solo objeto
    public <T> T obtenerObjeto(String endPoint, Class<T> clase) {
        try {
            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endPoint))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            HttpResponse<String> respuesta = httpClient.send(peticion, BodyHandlers.ofString());

            if (respuesta.statusCode() == 200) {
                return mapper.readValue(respuesta.body(), clase);
            } else {
                System.out.println("Error: " + respuesta.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Para obtener una lista de objetos
    public <T> List<T> obtenerLista(String endPoint, Class<T> clase) {
        try {
            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endPoint))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            HttpResponse<String> respuesta = httpClient.send(peticion, BodyHandlers.ofString());

            if (respuesta.statusCode() == 200) {
                return mapper.readValue(respuesta.body(),
                        mapper.getTypeFactory().constructCollectionType(List.class, clase));
            } else {
                System.out.println("Error: " + respuesta.statusCode());
                return Collections.emptyList();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean eliminar(String endPoint) {
        try {
            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endPoint))
                    .header("Authorization", "Bearer " + token)
                    .DELETE()
                    .build();

            HttpResponse<String> respuesta = httpClient.send(peticion, BodyHandlers.ofString());

            return respuesta.statusCode() == 200 || respuesta.statusCode() == 204;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    // POST para crear y modificar

    public boolean crear(String endPoint, Object objeto) {
        try {
            String json = mapper.writeValueAsString(objeto);

            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endPoint))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> respuesta = httpClient.send(peticion, BodyHandlers.ofString());

            return respuesta.statusCode() == 200 || respuesta.statusCode() == 201;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }

    }

    public Map<String, Object> login(String email, String contrasena) {
        try {
            // El servidor espera "email" y "password" (no "contrasena")
            Map<String, String> credenciales = new HashMap<>();
            credenciales.put("email", email);
            credenciales.put("password", contrasena);

            String json = mapper.writeValueAsString(credenciales);

            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> respuesta = httpClient.send(peticion, BodyHandlers.ofString());

            if (respuesta.statusCode() == 200) {
                return mapper.readValue(respuesta.body(), Map.class);
            }
            return null;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
