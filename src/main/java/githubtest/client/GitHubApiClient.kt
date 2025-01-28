package githubtest.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import githubtest.dto.commit.CommitResponse
import githubtest.dto.repository.Repository
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GitHubApiClient(private val token: String) {

    companion object {
        const val BASE_URL = "https://api.github.com"
    }

    private val client = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    // 모든 리포지토리 가져오기
    fun getRepositories(owner: String): List<Repository> {
        val repositories = mutableListOf<Repository>()
        var page = 1
        val perPage = 100

        while (true) {
            val endpoint = "/users/$owner/repos?per_page=$perPage&page=$page"
            val request = createRequest(endpoint)

            val response = client.newCall(request).execute()
            val body = response.body?.string()

            if (!response.isSuccessful || body.isNullOrEmpty()) break

            val pageRepositories: List<Repository> = objectMapper.readValue(body)
            if (pageRepositories.isEmpty()) break

            repositories.addAll(pageRepositories)
            page++
        }

        return repositories
    }

    // 특정 리포지토리의 커밋 가져오기
    fun getCommits(
        owner: String,
        repo: String,
        sinceDate: LocalDateTime = LocalDateTime.now().minusDays(7),
    ): CommitResponse {
        val parsedSinceDate = sinceDate.format(DateTimeFormatter.ISO_DATE_TIME)
        val endpoint = "/repos/$owner/$repo/commits?since=$parsedSinceDate"
        val request = createRequest(endpoint)

        val response = client.newCall(request).execute()
        val body = response.body?.string()

        return objectMapper.readValue(body!!)
    }

    // 공통 Request 생성
    private fun createRequest(endpoint: String): Request {
        return Request.Builder()
            .url(BASE_URL + endpoint)
            .addHeader("Authorization", "Bearer $token")
            .build()
    }
}