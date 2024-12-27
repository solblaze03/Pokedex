package com.example.pokedex.Clases

data class pokeFlavorText(
    val flavor_text_entries: List<infoPoke>
)


data class infoPoke(
    val flavor_text: String,
    val language : languageFlavor,

)


data class languageFlavor(
    val name: String,
    val language:String
)