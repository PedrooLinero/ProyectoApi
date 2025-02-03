package com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.model

import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import kotlinx.serialization.Serializable

@Serializable
data class bebidas(
    val dateModified: String?, // Ahora es nullable
    val idDrink: String,
    val strAlcoholic: String,
    val strCategory: String,
    val strCreativeCommonsConfirmed: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val strGlass: String,
    val strIBA: String,
    val strImageAttribution: String,
    val strImageSource: String,
    val strIngredient1: String?, // También nullable
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
)


fun bebidas.toMediaItem() = MediaItem(
    idDrink = idDrink,
    strAlcoholic = strAlcoholic,
    strCategory = strCategory,
    strCreativeCommonsConfirmed = strCreativeCommonsConfirmed,
    strDrink = strDrink,
    strDrinkThumb = strDrinkThumb,
    strGlass = strGlass,
    strIBA = strIBA,
    strImageAttribution = strImageAttribution,
    strImageSource = strImageSource,
    strIngredient1 = strIngredient1 ?: "Ingrediente desconocido", // Valor predeterminado para strIngredient1
    strIngredient2 = strIngredient2 ?: "Ingrediente desconocido",  // Valor predeterminado para strIngredient2
    strIngredient3 = strIngredient3 ?: "Ingrediente desconocido", // Y así sucesivamente...
    strIngredient4 = strIngredient4 ?: "Ingrediente desconocido",
    strIngredient5 = strIngredient5 ?: "Ingrediente desconocido",
    strIngredient6 = strIngredient6 ?: "Ingrediente desconocido",
    strInstructions = strInstructions,
    strInstructionsDE = strInstructionsDE,
    strInstructionsES = strInstructionsES,
    strInstructionsFR = strInstructionsFR,
    strInstructionsIT = strInstructionsIT,
    strMeasure1 = strMeasure1 ?: "Medida desconocida",
    strMeasure2 = strMeasure2 ?: "Medida desconocida",
    strMeasure3 = strMeasure3 ?: "Medida desconocida",
    strMeasure4 = strMeasure4 ?: "Medida desconocida",
    strMeasure5 = strMeasure5 ?: "Medida desconocida",
    strMeasure6 = strMeasure6 ?: "Medida desconocida",
    strTags = strTags ?: "Sin etiquetas",
    strVideo = strVideo ?: "Sin video",
    dateModified = dateModified ?: "Fecha desconocida" // Valor predeterminado para dateModified
)



