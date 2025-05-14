package app.echoirx.domain.model

import androidx.annotation.StringRes
import app.echoirx.R

sealed class QualityConfig(
    @param:StringRes val label: Int,
    val quality: String,
    @param:StringRes val summary: Int
) {
    data object HiRes : QualityConfig(
        label = R.string.quality_label_hires,
        quality = "HI_RES_LOSSLESS",
        summary = R.string.quality_desc_hires
    )

    data object Lossless : QualityConfig(
        label = R.string.quality_label_lossless,
        quality = "LOSSLESS",
        summary = R.string.quality_desc_lossless
    )

    data object AAC320 : QualityConfig(
        label = R.string.quality_label_aac_320,
        quality = "HIGH",
        summary = R.string.quality_desc_aac_320
    )

    data object AAC96 : QualityConfig(
        label = R.string.quality_label_aac_96,
        quality = "LOW",
        summary = R.string.quality_desc_aac_96
    )

    data object DolbyAtmosAC3 : QualityConfig(
        label = R.string.quality_label_dolby_ac3,
        quality = "DOLBY_ATMOS_AC3",
        summary = R.string.quality_desc_dolby_ac3
    )

    data object DolbyAtmosAC4 : QualityConfig(
        label = R.string.quality_label_dolby_ac4,
        quality = "DOLBY_ATMOS_AC4",
        summary = R.string.quality_desc_dolby_ac4
    )
}