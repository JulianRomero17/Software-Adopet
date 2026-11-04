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

const catalogImages = {
  'PerroA1.jpg': dogOne,
  'PerroA2.jpg': dogTwo,
  'PerroA3.jpg': dogThree,
  'PerroA4.jpg': dogFour,
  'PerroA5.jpg': dogFive,
  'PerroA6.jpg': dogSix,
  'PerroA7.jpg': dogSeven,
  'GatoA1.jpg': catOne,
  'GatoA2.jpg': catTwo,
  'GatoA3.jpg': catThree,
  'GatoA4.jpeg': catFour,
  'GatoA5.jpg': catFive,
  'GatoA6.jpg': catSix,
}

export function normalizePet(pet) {
  const imageName = pet.imagenUrl?.split(/[\\/]/).pop()
  return {
    ...pet,
    imagenUrl: catalogImages[imageName] || pet.imagenUrl,
  }
}
