package githubtest

import okhttp3.OkHttpClient
import okhttp3.Request
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

fun main() {
    val owner = "yoon6763"
    val repo = "coding-test"
    val token = "github access token"

    fetchGitHubCommits(owner, repo, token)
}

fun fetchGitHubCommits(owner: String, repo: String, token: String) {
    val client = OkHttpClient()
    val url = "https://api.github.com/repos/$owner/$repo/commits"
    val objectMapper = jacksonObjectMapper()

    val request = Request.Builder().url(url).addHeader("Authorization", "Bearer $token").build()

    client.newCall(request).execute().use { response ->
        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            responseBody?.let {
                // JSON을 Map 리스트로 변환
                val commits: List<Map<String, Any>> = objectMapper.readValue(it)
                commits.forEach { commit ->
                    val commitDetails = commit["commit"] as Map<*, *>
                    val message = commitDetails["message"] as String
                    val author = (commitDetails["author"] as Map<*, *>)["name"] as String
                    println("Author: $author, Message: $message")
                }
            }
        } else {
            println("Error: ${response.code} - ${response.message}")
        }
    }
}
