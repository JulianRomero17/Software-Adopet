import dogOne from '../../images/PerrosAdopcion/PerroA1.jpg'
import dogTwo from '../../images/PerrosAdopcion/PerroA2.jpg'
import dogThree from '../../images/PerrosAdopcion/PerroA3.jpg'
import dogFour from '../../images/PerrosAdopcion/PerroA4.jpg'
import dogFive from '../../images/PerrosAdopcion/PerroA5.jpg'
import dogSix from '../../images/PerrosAdopcion/PerroA6.jpg'
import dogSeven from '../../images/PerrosAdopcion/PerroA7.jpg'
import catOne from '../../images/GatosAdopcion/GatoA1.jpg'
import catTwo from '../../images/GatosAdopcion/GatoA2.jpg'
import catThree from '../../images/GatosAdopcion/GatoA3.jpg'
import catFour from '../../images/GatosAdopcion/GatoA4.jpeg'
import catFive from '../../images/GatosAdopcion/GatoA5.jpg'
import catSix from '../../images/GatosAdopcion/GatoA6.jpg'

const provisionalPets = [
  { id: 1, nombre: 'Bruno', especie: 'PERRO', raza: 'Mestizo', sexo: 'MACHO', edad: 0, edadTexto: '2 meses', tamano: 'PEQUENO', esterilizacion: 'No aún por edad', vacunacion: 'Pendiente de completar', descripcion: 'Cachorro cariñoso. Requiere valoración veterinaria y esquema de vacunación.', estado: 'DISPONIBLE', imagenUrl: dogOne },
  { id: 2, nombre: 'Luna', especie: 'PERRO', raza: 'Maltés mestizo', sexo: 'HEMBRA', edad: 2, edadTexto: '2 años', tamano: 'PEQUENO', esterilizacion: 'Pendiente de confirmar', vacunacion: 'Pendiente de confirmar', descripcion: 'Pequeña, atenta y sociable. Ideal para una familia comprometida.', estado: 'DISPONIBLE', imagenUrl: dogTwo },
  { id: 3, nombre: 'Toby', especie: 'PERRO', raza: 'Mestizo de pelo rizado', sexo: 'MACHO', edad: 0, edadTexto: '3 meses', tamano: 'PEQUENO', esterilizacion: 'No aún por edad', vacunacion: 'Pendiente de completar', descripcion: 'Cachorro juguetón que necesita cuidados, paciencia y socialización.', estado: 'DISPONIBLE', imagenUrl: dogThree },
  { id: 7, nombre: 'Rocky', especie: 'PERRO', raza: 'Mestizo tipo pitbull', sexo: 'MACHO', edad: 4, edadTexto: '4 años', tamano: 'GRANDE', esterilizacion: 'Pendiente de confirmar', vacunacion: 'Pendiente de confirmar', descripcion: 'Perro adulto, fuerte y noble. Requiere una familia responsable.', estado: 'DISPONIBLE', imagenUrl: dogFour },
  { id: 8, nombre: 'Maya', especie: 'PERRO', raza: 'Pomerania mestiza', sexo: 'HEMBRA', edad: 0, edadTexto: '4 meses', tamano: 'PEQUENO', esterilizacion: 'No aún por edad', vacunacion: 'Pendiente de completar', descripcion: 'Cachorra pequeña, curiosa y llena de energía.', estado: 'DISPONIBLE', imagenUrl: dogFive },
  { id: 9, nombre: 'Max', especie: 'PERRO', raza: 'Mestizo tipo terrier', sexo: 'MACHO', edad: 6, edadTexto: '6 años', tamano: 'MEDIANO', esterilizacion: 'Pendiente de confirmar', vacunacion: 'Pendiente de confirmar', descripcion: 'Adulto tranquilo y cariñoso con las personas.', estado: 'DISPONIBLE', imagenUrl: dogSix },
  { id: 10, nombre: 'Kira', especie: 'PERRO', raza: 'Mestiza', sexo: 'HEMBRA', edad: 0, edadTexto: '4 meses', tamano: 'PEQUENO', esterilizacion: 'No aún por edad', vacunacion: 'Pendiente de completar', descripcion: 'Cachorra dulce y compañera. Necesita continuar su calendario veterinario.', estado: 'DISPONIBLE', imagenUrl: dogSeven },
  { id: 4, nombre: 'Michi', especie: 'GATO', raza: 'Doméstico de pelo corto', sexo: 'MACHO', edad: 2, edadTexto: '2 años', tamano: 'PEQUENO', esterilizacion: 'Pendiente de confirmar', vacunacion: 'Pendiente de confirmar', descripcion: 'Gato curioso, tierno y amante de las caricias.', estado: 'DISPONIBLE', imagenUrl: catOne },
  { id: 5, nombre: 'Simba', especie: 'GATO', raza: 'Doméstico de pelo corto', sexo: 'MACHO', edad: 0, edadTexto: '4 meses', tamano: 'PEQUENO', esterilizacion: 'No aún por edad', vacunacion: 'Pendiente de completar', descripcion: 'Gatito curioso y juguetón que necesita un hogar seguro.', estado: 'DISPONIBLE', imagenUrl: catTwo },
  { id: 6, nombre: 'Nala', especie: 'GATO', raza: 'Doméstica de pelo corto', sexo: 'HEMBRA', edad: 0, edadTexto: '2 meses', tamano: 'PEQUENO', esterilizacion: 'No aún por edad', vacunacion: 'Pendiente de completar', descripcion: 'Gatita bebé que requiere cuidados y seguimiento veterinario.', estado: 'DISPONIBLE', imagenUrl: catThree },
  { id: 11, nombre: 'Oliver', especie: 'GATO', raza: 'Doméstico de pelo corto', sexo: 'MACHO', edad: 0, edadTexto: '3 meses', tamano: 'PEQUENO', esterilizacion: 'No aún por edad', vacunacion: 'Pendiente de completar', descripcion: 'Gatito juguetón y muy curioso.', estado: 'DISPONIBLE', imagenUrl: catFour },
  { id: 12, nombre: 'Mila', especie: 'GATO', raza: 'Doméstica atigrada', sexo: 'HEMBRA', edad: 5, edadTexto: '5 años', tamano: 'PEQUENO', esterilizacion: 'Pendiente de confirmar', vacunacion: 'Pendiente de confirmar', descripcion: 'Tranquila, elegante y afectuosa.', estado: 'DISPONIBLE', imagenUrl: catFive },
  { id: 13, nombre: 'Tom', especie: 'GATO', raza: 'Mestizo tipo siamés', sexo: 'MACHO', edad: 0, edadTexto: '3 meses', tamano: 'PEQUENO', esterilizacion: 'No aún por edad', vacunacion: 'Pendiente de completar', descripcion: 'Gatito amable que busca una familia paciente.', estado: 'DISPONIBLE', imagenUrl: catSix },
]

export default provisionalPets
