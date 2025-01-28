package githubtest

import okhttp3.OkHttpClient
import okhttp3.Request
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import githubtest.dto.commit.CommitResponse
import githubtest.dto.repository.Repository

const val token = "github personal access token"

fun main() {
    val owner = "yoon6763"

    val githubRepositories = getGithubRepositoryList(owner)

    for (repository in githubRepositories) {
        println("repository: ${repository.name}")
    }
}

fun getGithubRepositoryList(owner: String): List<Repository> {
    val client = OkHttpClient()
    val objectMapper = jacksonObjectMapper()

    val repositories = ArrayList<Repository>()
    var page = 1
    val perPage = 100 // 최대 100개까지 가져올 수 있음

    while (true) {
        val url = "https://api.github.com/users/$owner/repos?per_page=$perPage&page=$page"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string()

        if (!response.isSuccessful || body.isNullOrEmpty()) break

        val pageRepositories: List<Repository> = objectMapper.readValue(body)
        if (pageRepositories.isEmpty()) break // 더 이상 가져올 데이터가 없을 경우 종료

        repositories.addAll(pageRepositories)
        page++
    }

    return repositories
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
