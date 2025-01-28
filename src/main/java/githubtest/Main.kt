package githubtest

import okhttp3.OkHttpClient
import okhttp3.Request
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import githubtest.client.GitHubApiClient
import githubtest.dto.commit.CommitResponse
import githubtest.dto.repository.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
    val owner = "yoon6763"

    val githubApiClient = GitHubApiClient(Constant.TOKEN)
    val githubRepositories = githubApiClient.getRepositories(owner)

    val currentDateTime = LocalDateTime.now()
    val sinceDate = currentDateTime.minusDays(7)

    // yyyy-MM-dd HH:mm 형식으로 변환
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    println("${sinceDate.format(dateFormat)} ~ ${currentDateTime.format(dateFormat)} 사이의 커밋 내역")

    githubRepositories.forEach { repository ->
        val commitListOfRepository = githubApiClient.getCommits(owner, repository.name, sinceDate)
        printCommits(repository, commitListOfRepository)
    }
}

private fun printCommits(repository: Repository, commitListOfRepository: CommitResponse) {
    if (commitListOfRepository.isEmpty()) {
        return
    }

    println("repository: ${repository.name}")

    commitListOfRepository.forEach { commit ->
        println("commit: ${commit.commit.message}")
    }

    println("=====================================")
}