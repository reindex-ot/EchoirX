package app.echoirx.presentation.components.models

data class ChipAction(
    val label: String,
    val icon: Any,
    val contentDescription: String,
    val onClick: () -> Unit
)