package com.example.proyectoapi.proyectoapi.pedroluis.data.model

data class MediaItem(
    val idDrink: String = "",
    val strAlcoholic: String = "",
    val strCategory: String = "",
    val strCreativeCommonsConfirmed: String = "",
    val strDrink: String = "",
    val strDrinkThumb: String = "",
    val strGlass: String = "",
    val strIBA: String? = null,
    val strImageAttribution: String? = null,
    val strImageSource: String = "",
    val strIngredient1: String? = null,
    val strIngredient2: String? = null,
    val strIngredient3: String? = null,
    val strIngredient4: String? = null,
    val strIngredient5: String? = null,
    val strIngredient6: String? = null,
    val strInstructions: String = "",
    val strInstructionsDE: String = "",
    val strInstructionsES: String = "",
    val strInstructionsFR: String = "",
    val strInstructionsIT: String = "",
    val strMeasure1: String? = null,
    val strMeasure2: String? = null,
    val strMeasure3: String? = null,
    val strMeasure4: String? = null,
    val strMeasure5: String? = null,
    val strMeasure6: String? = null,
    val strTags: String? = null,
    val strVideo: String? = null,
    val dateModified: String? = null
) {
    constructor() : this("", "", "", "", "", "", "", null, null, "", null, null, null, null, null, null, "", "", "", "", "", null, null, null, null, null, null, null, null)
}
