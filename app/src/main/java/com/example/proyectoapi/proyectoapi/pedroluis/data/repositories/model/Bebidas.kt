import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import kotlinx.serialization.Serializable

@Serializable
data class bebidas(
    val dateModified: String?,
    val idDrink: String,
    val strAlcoholic: String,
    val strCategory: String,
    val strCreativeCommonsConfirmed: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val strGlass: String,
    val strIBA: String?,
    val strImageAttribution: String?, // <-- Cambia esto a String?
    val strImageSource: String,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strInstructions: String,
    val strInstructionsDE: String,
    val strInstructionsES: String,
    val strInstructionsFR: String,
    val strInstructionsIT: String,
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strTags: String?,
    val strVideo: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is bebidas) return false

        return idDrink == other.idDrink &&
                dateModified == other.dateModified &&
                strAlcoholic == other.strAlcoholic &&
                strCategory == other.strCategory &&
                strCreativeCommonsConfirmed == other.strCreativeCommonsConfirmed &&
                strDrink == other.strDrink &&
                strDrinkThumb == other.strDrinkThumb &&
                strGlass == other.strGlass &&
                strIBA == other.strIBA &&
                strImageAttribution == other.strImageAttribution &&
                strImageSource == other.strImageSource &&
                strIngredient1 == other.strIngredient1 &&
                strIngredient2 == other.strIngredient2 &&
                strIngredient3 == other.strIngredient3 &&
                strIngredient4 == other.strIngredient4 &&
                strIngredient5 == other.strIngredient5 &&
                strIngredient6 == other.strIngredient6 &&
                strInstructions == other.strInstructions &&
                strInstructionsDE == other.strInstructionsDE &&
                strInstructionsES == other.strInstructionsES &&
                strInstructionsFR == other.strInstructionsFR &&
                strInstructionsIT == other.strInstructionsIT &&
                strMeasure1 == other.strMeasure1 &&
                strMeasure2 == other.strMeasure2 &&
                strMeasure3 == other.strMeasure3 &&
                strMeasure4 == other.strMeasure4 &&
                strMeasure5 == other.strMeasure5 &&
                strMeasure6 == other.strMeasure6 &&
                strTags == other.strTags &&
                strVideo == other.strVideo
    }

    override fun hashCode(): Int {
        return idDrink.hashCode()
    }

}



fun bebidas.toMediaItem() = MediaItem(
    idDrink = idDrink,
    strAlcoholic = strAlcoholic,
    strCategory = strCategory,
    strCreativeCommonsConfirmed = strCreativeCommonsConfirmed,
    strDrink = strDrink,
    strDrinkThumb = strDrinkThumb,
    strGlass = strGlass,
    strIBA = strIBA ?: "No disponible",
    strImageAttribution = strImageAttribution ?: "Sin atribución", // <--- Evita null
    strImageSource = strImageSource ?: "Sin fuente",
    strIngredient1 = strIngredient1 ?: "Ingrediente desconocido",
    strIngredient2 = strIngredient2 ?: "Ingrediente desconocido",
    strIngredient3 = strIngredient3 ?: "Ingrediente desconocido",
    strIngredient4 = strIngredient4 ?: "Ingrediente desconocido",
    strIngredient5 = strIngredient5 ?: "Ingrediente desconocido",
    strIngredient6 = strIngredient6 ?: "Ingrediente desconocido",
    strInstructions = strInstructions,
    strInstructionsDE = strInstructionsDE ?: "Sin instrucciones",
    strInstructionsES = strInstructionsES ?: "Sin instrucciones",
    strInstructionsFR = strInstructionsFR ?: "Sin instrucciones",
    strInstructionsIT = strInstructionsIT ?: "Sin instrucciones",
    strMeasure1 = strMeasure1 ?: "Medida desconocida",
    strMeasure2 = strMeasure2 ?: "Medida desconocida",
    strMeasure3 = strMeasure3 ?: "Medida desconocida",
    strMeasure4 = strMeasure4 ?: "Medida desconocida",
    strMeasure5 = strMeasure5 ?: "Medida desconocida",
    strMeasure6 = strMeasure6 ?: "Medida desconocida",
    strTags = strTags ?: "Sin etiquetas",
    strVideo = strVideo ?: "Sin video",
    dateModified = dateModified ?: "Fecha desconocida"
)
