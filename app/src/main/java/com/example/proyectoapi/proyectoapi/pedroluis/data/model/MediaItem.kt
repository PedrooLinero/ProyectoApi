package com.example.proyectoapi.proyectoapi.pedroluis.data.model

import bebidas

data class MediaItem(
    val idDrink: String,
    val strAlcoholic: String,
    val strCategory: String,
    val strCreativeCommonsConfirmed: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val strGlass: String,
    val strIBA: String?, // Permitir valores nulos
    val strImageAttribution: String?, // Permitir valores nulos
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
    val strVideo: String?,
    val dateModified: String?
) {
    fun toBebidas(): bebidas{
        return bebidas(
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
            strIngredient1 = strIngredient1,
            strIngredient2 = strIngredient2,
            strIngredient3 = strIngredient3,
            strIngredient4 = strIngredient4,
            strIngredient5 = strIngredient5,
            strIngredient6 = strIngredient6,
            strInstructions = strInstructions,
            strInstructionsDE = strInstructionsDE,
            strInstructionsES = strInstructionsES,
            strInstructionsFR = strInstructionsFR,
            strInstructionsIT = strInstructionsIT,
            strMeasure1 = strMeasure1,
            strMeasure2 = strMeasure2,
            strMeasure3 = strMeasure3,
            strMeasure4 = strMeasure4,
            strMeasure5 = strMeasure5,
            strMeasure6 = strMeasure6,
            strTags = strTags,
            strVideo = strVideo,
            dateModified = dateModified
        )
    }
}


