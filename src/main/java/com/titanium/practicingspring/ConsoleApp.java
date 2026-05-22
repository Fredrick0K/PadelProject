package com.titanium.practicingspring;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.titanium.practicingspring.model.Pista;
import com.titanium.practicingspring.model.Reserva;
import com.titanium.practicingspring.model.Usuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {

    // ---------------------------------------------------------------
    // CONSTANTES Y VARIABLES GLOBALES
    // ---------------------------------------------------------------

    private static String jwtToken = null;
    private static final String BASE_URL = "http://localhost:8080/api/";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Scanner scanner = new Scanner(System.in);

    // ---------------------------------------------------------------
    // PUNTO DE ENTRADA
    // ---------------------------------------------------------------

    public static void main(String[] args) {

        System.out.println("===============================================");
        System.out.println("    CLIENTE DE CONSOLA PARA API DE PADEL       ");
        System.out.println("===============================================");

        // Flujo de inicio de sesión o registro — no avanzamos hasta tener token
        while (jwtToken == null) {

            System.out.println("\n--- BIENVENIDO ---");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarse");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            String opStr = scanner.nextLine();

            if ("1".equals(opStr)) {
                login();
            } else if ("2".equals(opStr)) {
                register();
            } else if ("0".equals(opStr)) {
                System.out.println("¡Hasta pronto!");
                return;
            } else {
                System.out.println("Opción no válida.");
            }
        }

        // Menú principal — solo llegamos aquí si el login fue exitoso
        int opcion;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("¿Sobre qué entidad quieres trabajar?");
            System.out.println("1. Usuarios");
            System.out.println("2. Pistas");
            System.out.println("3. Reservas");
            System.out.println("0. Salir del programa");
            System.out.print("Elige una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    menuUsuarios();
                    break;
                case 2:
                    menuPistas();
                    break;
                case 3:
                    menuReservas();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);

        scanner.close();
    }

    // ---------------------------------------------------------------
    // AUTENTICACIÓN
    // ---------------------------------------------------------------

    private static void login() {

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        try {
            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("email", email);
            loginRequest.put("password", password);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    BASE_URL + "auth/login",
                    HttpMethod.POST,
                    new HttpEntity<>(loginRequest),
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                jwtToken = (String) response.getBody().get("token");
                System.out.println("¡Inicio de sesión exitoso!");
            }

        } catch (HttpStatusCodeException e) {

            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                System.out.println("Error: Usuario deshabilitado.");
            } else {
                System.out.println("Credenciales incorrectas. Inténtalo de nuevo.");
            }

        } catch (Exception e) {
            System.out.println("Error al iniciar sesión. Comprueba que la API está encendida.");
        }
    }

    private static void register() {

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setTelefono(telefono);
            nuevoUsuario.setContrasena(password);
            nuevoUsuario.setRol("USER");

            ResponseEntity<Usuario> response = restTemplate.postForEntity(
                    BASE_URL + "auth/register",
                    nuevoUsuario,
                    Usuario.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("¡Registro exitoso! Ahora puedes iniciar sesión.");
            }

        } catch (Exception e) {
            System.out.println("Error al registrar el usuario. Quizás el email ya existe.");
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------------
    // MENÚ USUARIOS
    // ---------------------------------------------------------------

    private static void menuUsuarios() {

        int opcion;

        do {
            System.out.println("\n--- GESTIÓN DE USUARIOS ---");
            System.out.println("1. Ver todos los usuarios");
            System.out.println("2. Ver usuario por ID");
            System.out.println("3. Actualizar usuario");
            System.out.println("4. Borrar usuario");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                opcion = -1;
            }

            try {

                if (opcion == 1) {

                    ResponseEntity<Usuario[]> res = restTemplate.exchange(
                            BASE_URL + "usuarios", HttpMethod.GET, getAuthRequest(), Usuario[].class);
                    for (Usuario u : res.getBody()) {
                        System.out.printf("[%d] %s - %s%n", u.getId(), u.getNombre(), u.getEmail());
                    }

                } else if (opcion == 2) {

                    System.out.print("ID del usuario: ");
                    String id = scanner.nextLine();

                    ResponseEntity<Usuario> res = restTemplate.exchange(
                            BASE_URL + "usuarios/" + id, HttpMethod.GET, getAuthRequest(), Usuario.class);
                    Usuario u = res.getBody();
                    System.out.println("Nombre: " + u.getNombre() + " | Rol: " + u.getRol());

                } else if (opcion == 3) {

                    System.out.print("ID del usuario a actualizar: ");
                    String id = scanner.nextLine();

                    System.out.print("Nuevo nombre: ");
                    String nombre = scanner.nextLine();

                    Usuario u = new Usuario();
                    u.setNombre(nombre);

                    HttpEntity<Usuario> req = new HttpEntity<>(u, getHeaders());
                    restTemplate.exchange(BASE_URL + "usuarios/" + id, HttpMethod.PUT, req, Usuario.class);
                    System.out.println("Actualizado con éxito.");

                } else if (opcion == 4) {

                    System.out.print("ID del usuario a borrar: ");
                    String id = scanner.nextLine();

                    restTemplate.exchange(BASE_URL + "usuarios/" + id, HttpMethod.DELETE, getAuthRequest(), Void.class);
                    System.out.println("Borrado con éxito.");
                }

            } catch (Exception e) {
                System.out.println("Operación fallida. (Asegúrate de que el ID existe o tienes permisos)");
            }

        } while (opcion != 0);
    }

    // ---------------------------------------------------------------
    // MENÚ PISTAS
    // ---------------------------------------------------------------

    private static void menuPistas() {

        int op;

        do {
            System.out.println("\n--- GESTIÓN DE PISTAS ---");
            System.out.println("1. Ver todas las pistas");
            System.out.println("2. Ver pista por ID");
            System.out.println("3. Añadir nueva pista");
            System.out.println("4. Actualizar pista");
            System.out.println("5. Borrar pista");
            System.out.println("6. Ver disponibilidad por fecha");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige: ");

            try {
                op = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                op = -1;
            }

            try {

                if (op == 1) {

                    ResponseEntity<Pista[]> res = restTemplate.exchange(
                            BASE_URL + "pistas", HttpMethod.GET, getAuthRequest(), Pista[].class);
                    for (Pista p : res.getBody()) {
                        System.out.printf("[%d] %s - %s - %.2f€%n", p.getId(), p.getNombre(), p.getEstado(),
                                p.getPrecioHora());
                    }

                } else if (op == 2) {

                    System.out.print("ID de la pista: ");
                    String id = scanner.nextLine();

                    ResponseEntity<Map<String, Object>> res = restTemplate.exchange(
                            BASE_URL + "pistas/" + id + "/detalle", HttpMethod.GET, getAuthRequest(),
                            new ParameterizedTypeReference<Map<String, Object>>() {
                            });

                    Map<String, Object> p = res.getBody();
                    System.out.printf("[%d] %s - %s%n", (Integer) p.get("id"), (String) p.get("nombre"),
                            (String) p.get("estado"));

                    java.util.List<java.util.Map<String, Object>> reservas = (List<Map<String, Object>>) p
                            .get("reservasActivas");
                    if (reservas != null && !reservas.isEmpty()) {
                        System.out.println("  Horas Ocupadas:");
                        for (java.util.Map<String, Object> rActiva : reservas) {
                            System.out.println("   - " + rActiva.get("horaInicio") + " a " + rActiva.get("horaFin")
                                    + " (por " + rActiva.get("nombreUsuario") + ")");
                        }
                    } else {
                        System.out.println("  No hay horas ocupadas.");
                    }

                    java.util.List<String> horasLibres = (java.util.List<String>) p.get("horasLibresHoy");
                    if (horasLibres != null && !horasLibres.isEmpty()) {
                        System.out.println("  Horas libres hoy:");
                        for (String hora : horasLibres) {
                            System.out.println("   - " + hora);
                        }
                    } else {
                        System.out.println("  No hay horas libres hoy.");
                    }

                } else if (op == 3) {

                    System.out.print("Nombre de la pista: ");
                    String nombre = scanner.nextLine();

                    Pista p = new Pista();
                    p.setNombre(nombre);
                    p.setEstado("Disponible");

                    HttpEntity<Pista> req = new HttpEntity<>(p, getHeaders());
                    restTemplate.exchange(BASE_URL + "pistas", HttpMethod.POST, req, Pista.class);
                    System.out.println("Pista creada.");

                } else if (op == 4) {

                    System.out.print("ID de la pista a actualizar: ");
                    String id = scanner.nextLine();

                    System.out.print("Nuevo nombre de la pista: ");
                    String nombre = scanner.nextLine();

                    Pista p = new Pista();
                    p.setNombre(nombre);

                    HttpEntity<Pista> req = new HttpEntity<>(p, getHeaders());
                    restTemplate.exchange(BASE_URL + "pistas/" + id, HttpMethod.PUT, req, Pista.class);
                    System.out.println("Pista actualizada.");

                } else if (op == 5) {

                    System.out.print("ID de la pista a borrar: ");
                    String id = scanner.nextLine();

                    restTemplate.exchange(BASE_URL + "pistas/" + id, HttpMethod.DELETE, getAuthRequest(), Void.class);
                    System.out.println("Pista borrada.");

                } else if (op == 6) {

                    System.out.print("ID de la pista: ");
                    String id = scanner.nextLine();

                    String fechaDef = java.time.LocalDate.now().toString();
                    System.out.print("Fecha (YYYY-MM-DD, por defecto " + fechaDef + "): ");
                    String fechaInput = scanner.nextLine();
                    if (fechaInput.isBlank()) {
                        fechaInput = fechaDef;
                    }

                    ResponseEntity<Map<String, Object>> dispRes = restTemplate.exchange(
                            BASE_URL + "pistas/" + id + "/disponibilidad?fecha=" + fechaInput,
                            HttpMethod.GET, getAuthRequest(),
                            new ParameterizedTypeReference<Map<String, Object>>() {
                            });
                    Map<String, Object> disp = dispRes.getBody();
                    if (disp != null) {
                        System.out.println("\n--- DISPONIBILIDAD ---");
                        System.out.println("Pista: " + disp.get("nombrePista") + " | Fecha: " + disp.get("fecha"));

                        List<Map<String, Object>> ocupadas = (List<Map<String, Object>>) disp.get("ocupadas");
                        if (ocupadas != null && !ocupadas.isEmpty()) {
                            System.out.println("Horas OCUPADAS:");
                            for (Map<String, Object> rActiva : ocupadas) {
                                System.out.println("  - " + rActiva.get("horaInicio") + " a " + rActiva.get("horaFin")
                                        + " (por " + rActiva.get("nombreUsuario") + ")");
                            }
                        } else {
                            System.out.println("No hay horas ocupadas en esta fecha.");
                        }

                        List<String> libres = (List<String>) disp.get("horasLibres");
                        if (libres != null && !libres.isEmpty()) {
                            System.out.println("Horas LIBRES:");
                            for (String hora : libres) {
                                System.out.println("  - " + hora);
                            }
                        } else {
                            System.out.println("No hay horas libres en esta fecha.");
                        }
                        System.out.println("------------------------\n");
                    }
                }

            } catch (HttpStatusCodeException e) {

                System.out.println(
                        "Error del servidor (" + e.getStatusCode().value() + "): " + e.getResponseBodyAsString());

            } catch (Exception e) {
                System.out.println("Operación fallida.");
            }

        } while (op != 0);
    }

    // ---------------------------------------------------------------
    // MENÚ RESERVAS
    // ---------------------------------------------------------------

    private static void menuReservas() {

        int op;

        do {
            System.out.println("\n--- GESTIÓN DE RESERVAS ---");
            System.out.println("1. Ver todas las reservas");
            System.out.println("2. Ver reserva por ID");
            System.out.println("3. Añadir nueva reserva");
            System.out.println("4. Actualizar reserva");
            System.out.println("5. Borrar reserva");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige: ");

            try {
                op = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                op = -1;
            }

            try {

                if (op == 1) {

                    ResponseEntity<Reserva[]> res = restTemplate.exchange(
                            BASE_URL + "reservas", HttpMethod.GET, getAuthRequest(), Reserva[].class);
                    for (Reserva r : res.getBody()) {
                        System.out.printf(
                                "[%d] Reserva (Usuario ID: %d, Pista ID: %d)%n" + r.getHoraInicio() + r.getHoraFin(),
                                r.getId(), r.getUsuario().getId(), r.getPista().getId());
                    }

                } else if (op == 2) {

                    System.out.print("ID de la reserva: ");
                    String id = scanner.nextLine();

                    ResponseEntity<Reserva> res = restTemplate.exchange(
                            BASE_URL + "reservas/" + id, HttpMethod.GET, getAuthRequest(), Reserva.class);
                    Reserva r = res.getBody();
                    System.out.printf("[%d] Usuario %d -> Pista %d%n",
                            r.getId(), r.getUsuario().getId(), r.getPista().getId());

                } else if (op == 3) {

                    System.out.print("ID del Usuario: ");
                    int uid = Integer.parseInt(scanner.nextLine());

                    System.out.print("ID de la Pista: ");
                    int pid = Integer.parseInt(scanner.nextLine());

                    // Pedir fecha y mostrar disponibilidad antes de los horarios
                    String fechaStr = java.time.LocalDate.now().toString();
                    System.out.print("Fecha (YYYY-MM-DD, por defecto " + fechaStr + "): ");
                    String fechaInput = scanner.nextLine();
                    if (fechaInput.isBlank()) {
                        fechaInput = fechaStr;
                    }
                    String fechaFinal = fechaInput;

                    try {
                        ResponseEntity<Map<String, Object>> dispRes = restTemplate.exchange(
                                BASE_URL + "pistas/" + pid + "/disponibilidad?fecha=" + fechaFinal,
                                HttpMethod.GET, getAuthRequest(),
                                new ParameterizedTypeReference<Map<String, Object>>() {
                                });
                        Map<String, Object> disp = dispRes.getBody();
                        if (disp != null) {
                            System.out.println("\n--- DISPONIBILIDAD DE LA PISTA ---");
                            System.out.println("Pista: " + disp.get("nombrePista") + " | Fecha: " + disp.get("fecha"));

                            List<Map<String, Object>> ocupadas = (List<Map<String, Object>>) disp.get("ocupadas");
                            if (ocupadas != null && !ocupadas.isEmpty()) {
                                System.out.println("Horas OCUPADAS:");
                                for (Map<String, Object> rActiva : ocupadas) {
                                    System.out.println("  - " + rActiva.get("horaInicio") + " a "
                                            + rActiva.get("horaFin") + " (por " + rActiva.get("nombreUsuario") + ")");
                                }
                            } else {
                                System.out.println("No hay horas ocupadas en esta fecha.");
                            }

                            List<String> libres = (List<String>) disp.get("horasLibres");
                            if (libres != null && !libres.isEmpty()) {
                                System.out.println("Horas LIBRES:");
                                for (String hora : libres) {
                                    System.out.println("  - " + hora);
                                }
                            } else {
                                System.out.println("No hay horas libres en esta fecha.");
                            }
                            System.out.println("----------------------------------\n");
                        }
                    } catch (Exception e) {
                        System.out.println("No se pudo obtener la disponibilidad de la pista.");
                    }

                    System.out.print("Hora de inicio (ej: " + fechaFinal + "T10:00): ");
                    java.time.LocalDateTime horaInicio = java.time.LocalDateTime.parse(scanner.nextLine());

                    System.out.print("Hora de fin    (ej: " + fechaFinal + "T11:30): ");
                    java.time.LocalDateTime horaFin = java.time.LocalDateTime.parse(scanner.nextLine());

                    Usuario u = new Usuario();
                    u.setId(uid);

                    Pista p = new Pista();
                    p.setId(pid);

                    Reserva r = new Reserva();
                    r.setUsuario(u);
                    r.setPista(p);
                    r.setHoraInicio(horaInicio);
                    r.setHoraFin(horaFin);

                    HttpEntity<Reserva> req = new HttpEntity<>(r, getHeaders());
                    restTemplate.exchange(BASE_URL + "reservas", HttpMethod.POST, req, Reserva.class);
                    System.out.println("Reserva creada.");

                } else if (op == 4) {

                    System.out.print("ID de la reserva a actualizar: ");
                    String id = scanner.nextLine();

                    System.out.print("Nuevo ID de Pista para esta reserva: ");
                    int pid = Integer.parseInt(scanner.nextLine());

                    Pista p = new Pista();
                    p.setId(pid);

                    Reserva r = new Reserva();
                    r.setPista(p);

                    HttpEntity<Reserva> req = new HttpEntity<>(r, getHeaders());
                    restTemplate.exchange(BASE_URL + "reservas/" + id, HttpMethod.PUT, req, Reserva.class);
                    System.out.println("Reserva actualizada.");

                } else if (op == 5) {

                    System.out.print("ID de la reserva a borrar: ");
                    String id = scanner.nextLine();

                    restTemplate.exchange(BASE_URL + "reservas/" + id, HttpMethod.DELETE, getAuthRequest(), Void.class);
                    System.out.println("Reserva borrada.");
                }

            } catch (HttpStatusCodeException e) {
                if (e.getStatusCode().value() == 409) {
                    System.out.println("Error: La pista ya está reservada en ese horario.");
                } else if (e.getStatusCode().value() == 403) {
                    System.out.println("Error: No tienes permiso. Asegúrate de haber iniciado sesión.");
                } else {
                    System.out.println(
                            "Error del servidor (" + e.getStatusCode().value() + "): " + e.getResponseBodyAsString());
                }
            } catch (Exception e) {
                System.out.println("Operación fallida.");
                e.printStackTrace();
            }

        } while (op != 0);
    }

    // ---------------------------------------------------------------
    // HELPERS DE AUTENTICACIÓN HTTP
    // ---------------------------------------------------------------

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        return headers;
    }

    private static HttpEntity<Void> getAuthRequest() {
        return new HttpEntity<>(getHeaders());
    }
}
