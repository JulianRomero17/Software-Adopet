package com.adopet.business.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "adoptantes")
@Getter
@Setter
@NoArgsConstructor
public class Adoptante extends Usuario {
}