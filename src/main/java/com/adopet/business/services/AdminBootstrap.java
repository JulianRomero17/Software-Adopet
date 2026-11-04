package com.adopet.business.services;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.adopet.business.models.Administrador;
import com.adopet.business.models.RolUsuario;
import com.adopet.business.models.Usuario;
import com.adopet.data.dao.UsuarioRepository;

@Component
public class AdminBootstrap implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminBootstrap.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean enabled;
    private final boolean resetExistingPassword;
    private final String nombre;
    private final String documento;
    private final String correo;
    private final String contrasena;

    public AdminBootstrap(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${adopet.admin.bootstrap.enabled:false}") boolean enabled,
            @Value("${adopet.admin.bootstrap.reset-existing-password:false}") boolean resetExistingPassword,
            @Value("${adopet.admin.bootstrap.nombre:}") String nombre,
            @Value("${adopet.admin.bootstrap.documento:}") String documento,
            @Value("${adopet.admin.bootstrap.correo:}") String correo,
            @Value("${adopet.admin.bootstrap.contrasena:}") String contrasena) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.enabled = enabled;
        this.resetExistingPassword = resetExistingPassword;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!enabled) {
            return;
        }

        validarConfiguracion();
        String correoNormalizado = correo.trim().toLowerCase(Locale.ROOT);
        Usuario existente = usuarioRepository.findByCorreo(correoNormalizado).orElse(null);
        if (existente != null) {
            if (existente.getRol() != RolUsuario.ADMINISTRADOR) {
                throw new IllegalStateException(
                        "El correo configurado para el administrador ya pertenece a una cuenta que no es administradora.");
            }
            if (resetExistingPassword) {
                existente.setContrasena(passwordEncoder.encode(contrasena));
                usuarioRepository.save(existente);
                logger.info("Se restableció la contraseña de la cuenta de administrador configurada.");
                return;
            }
            logger.info("La cuenta de administrador configurada ya existe; no se realizaron cambios.");
            return;
        }

        Administrador administrador = new Administrador();
        administrador.setNombre(nombre.trim());
        administrador.setDocumento(documento.trim());
        administrador.setCorreo(correoNormalizado);
        administrador.setContrasena(passwordEncoder.encode(contrasena));
        administrador.setRol(RolUsuario.ADMINISTRADOR);
        administrador.setActivo(true);
        usuarioRepository.save(administrador);
        logger.info("Se creó la cuenta de administrador inicial.");
    }

    private void validarConfiguracion() {
        if (nombre.isBlank() || documento.isBlank() || correo.isBlank() || contrasena.isBlank()) {
            throw new IllegalStateException(
                    "El bootstrap de administrador está activado, pero falta configurar nombre, documento, correo o contraseña.");
        }
        if (contrasena.length() < 12) {
            throw new IllegalStateException(
                    "La contraseña del administrador debe tener al menos 12 caracteres.");
        }
    }
}
