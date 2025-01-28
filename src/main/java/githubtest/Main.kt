package githubtest

import okhttp3.OkHttpClient
import okhttp3.Request
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import githubtest.dto.commit.CommitResponse

const val token = "github_personal_access_token"

fun main() {
    val owner = "yoon6763"
    val repo = "objects-practice"

    fetchGitHubCommits(owner, repo, token)
}

fun fetchGitHubCommits(owner: String, repo: String, token: String) {
    val client = OkHttpClient()
    val objectMapper = jacksonObjectMapper()

    val url = "https://api.github.com/repos/$owner/$repo/commits"
    val request = Request.Builder().url(url).addHeader("Authorization", "Bearer $token").build()

    val response = client.newCall(request).execute()
    val body = response.body?.string()

    val commitResponse = objectMapper.readValue<CommitResponse>(body!!)
    for (commit in commitResponse) {
        println("commit: ${commit.commit.message}")
        println("author: ${commit.commit.author.name}")
        println("date: ${commit.commit.author.date}")
        println("url: ${commit.html_url}")
        println()
    }
}
