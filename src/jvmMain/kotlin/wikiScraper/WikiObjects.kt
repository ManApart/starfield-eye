package wikiScraper

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResp(val query: TokenRespQuery)

@Serializable
data class TokenRespQuery(val tokens: TokenRespTokens)

@Serializable
data class TokenRespTokens(val logintoken: String)

@Serializable
data class PageResult(val parse: PageParse)

@Serializable
data class PageParse(val title: String, @SerialName("pageid") val pageId: Int, val text: PageText)

@Serializable
data class PageText(@SerialName("*") val text: String)
