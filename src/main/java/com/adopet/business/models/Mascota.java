package com.adopet.business.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mascotas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String especie;

    private String raza;

    private String sexo;

    private Integer edad;

    private String tamano;

    @Column(length = 1000)
    private String descripcion;

    private String estadoSalud;

    private String imagenUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMascota estado;
}