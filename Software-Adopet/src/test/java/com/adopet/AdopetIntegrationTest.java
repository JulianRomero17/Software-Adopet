package com.adopet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.adopet.business.models.Administrador;
import com.adopet.business.models.RolUsuario;
import com.adopet.business.models.Usuario;
import com.adopet.data.dao.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class AdopetIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("adopet_test")
            .withUsername("adopet_test")
            .withPassword("adopet_test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @BeforeEach
    void crearAdministradorDePrueba() {
        usuarioRepository.deleteAll();

        Administrador administrador = new Administrador();
        administrador.setNombre("Administrador de Integración");
        administrador.setDocumento("ADMIN-TEST-001");
        administrador.setCorreo("admin.integration@adopet.test");
        administrador.setContrasena(passwordEncoder.encode("admin-password"));
        administrador.setRol(RolUsuario.ADMINISTRADOR);
        administrador.setActivo(true);
        usuarioRepository.save(administrador);
    }

    @Test
    void flujoCompletoDeAdopcion() throws Exception {
        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Adoptante Integración",
                                  "documento": "ADOPTANTE-TEST-001",
                                  "correo": "adoptante.integration@adopet.test",
                                  "telefono": "3000000000",
                                  "direccion": "Calle de Integración 1",
                                  "contrasena": "adoptante-password"
                                }
                                """))
                .andExpect(status().isCreated());

        String adoptanteToken = login("adoptante.integration@adopet.test", "adoptante-password");
        String adminToken = login("admin.integration@adopet.test", "admin-password");

        String mascotaResponse = mockMvc.perform(post("/api/mascotas")
                        .header("Authorization", bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Mascota Integración",
                                  "especie": "PERRO",
                                  "raza": "Criollo",
                                  "sexo": "MACHO",
                                  "edad": 3,
                                  "tamano": "MEDIANO",
                                  "descripcion": "Mascota para prueba de integración",
                                  "estadoSalud": "Salud estable",
                                  "imagenUrl": "https://example.com/integration.jpg"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("DISPONIBLE"))
                .andReturn().getResponse().getContentAsString();
        Long mascotaId = json(mascotaResponse).get("id").asLong();

        mockMvc.perform(get("/api/mascotas")
                        .header("Authorization", bearer(adoptanteToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mascota Integración"));

        String solicitudResponse = mockMvc.perform(post("/api/solicitudes")
                        .header("Authorization", bearer(adoptanteToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mascotaId\":" + mascotaId + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andReturn().getResponse().getContentAsString();
        Long solicitudId = json(solicitudResponse).get("id").asLong();

        mockMvc.perform(post("/api/solicitudes")
                        .header("Authorization", bearer(adoptanteToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mascotaId\":" + mascotaId + "}"))
                .andExpect(status().isConflict());

        mockMvc.perform(patch("/api/solicitudes/" + solicitudId + "/decision")
                        .header("Authorization", bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "decision": "APROBADA",
                                  "observaciones": "Aprobada en prueba de integración"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADA"));

        mockMvc.perform(get("/api/mascotas/" + mascotaId)
                        .header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ADOPTADA"));

        mockMvc.perform(get("/api/historial")
                        .header("Authorization", bearer(adoptanteToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("APROBADA"));
    }

    private String login(String correo, String contrasena) throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"" + correo
                                + "\",\"contrasena\":\"" + contrasena + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return json(response).get("token").asText();
    }

    private JsonNode json(String content) throws Exception {
        return objectMapper.readTree(content);
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}