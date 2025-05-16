package app.echoirx.data.remote.mapper

import app.echoirx.data.remote.dto.SearchResultDto
import app.echoirx.domain.model.SearchResult

object SearchResultMapper {
    fun SearchResultDto.toDomain(): SearchResult =
        SearchResult(
            id = id,
            title = title,
            duration = duration,
            explicit = explicit,
            cover = cover,
            artists = artists,
            modes = modes,
            formats = formats,
            trackNumber = trackNumber
        )
}