package dev.sandroisu.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * {
 * "status": "ok",
 * "totalResults": 191,
 * -"articles": [
 * -{
 * -"source": {
 * "id": "techcrunch",
 * "name": "TechCrunch"
 * },
 * "author": "Aria Alamalhodaei",
 * "title": "Former top SpaceX exec Tom Ochinero sets up new VC firm, filings reveal",
 * "description": "Former senior SpaceX executive Tom Ochinero is teaming up with SpaceX alum-turned-VC,
 * Achal Upadhyaya, and one of Sequoia’s top finance leaders, Spencer...",
 * "url": "https://techcrunch.com/2024/04/16/former-top-spacex-exec-tom-ochinero-sets-up-new-vc-firm-filings-reveal/",
 * "urlToImage": "https://s.yimg.com/ny/api/res/1.2/iL6qMKdP8imCsssGt6Lsjw--/YXBwaWQ9aGlnaGxhbmRlcjt3PTEyMDA7aD04MDA-
 * /https://media.zenfs.com/en/techcrunch_350/6956d8ca15194f754ca85de0d76a879f",
 * "publishedAt": "2024-04-16T22:01:02Z",
 * "content": "Former senior SpaceX executive Tom Ochinero is teaming up with SpaceX alum-turned-VC,
 * Achal Upadhyaya, and one of Sequoias top finance leaders,
 * Spencer Hemphill, on a new venture called Interlagos Ca… [+2253 chars]"
 * }
 * ]
 * }
 */
@Serializable
data class ResponseDTO<E>(
    @SerialName("status")
    val status: String,
    @SerialName("totalResults")
    val totalResults: Int,
    @SerialName("articles")
    val articles: List<E>,
)
