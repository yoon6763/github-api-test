package githubtest

import githubtest.dto.commit.CommitItem
import githubtest.dto.repository.Repository
import githubtest.service.GitHubService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

var githubService: GitHubService = DiContainer.githubService

fun main() {
    val owner = "yoon6763"

    val currentDateTime = LocalDateTime.now()
    val sinceDate = currentDateTime.minusDays(7)

    val repositoriesWithCommits = githubService.getRepositoriesWithCommits(owner, sinceDate)

    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    println("${sinceDate.format(dateFormat)} ~ ${currentDateTime.format(dateFormat)} 사이의 커밋 내역")

    repositoriesWithCommits.forEach { (repository, commits) ->
        printCommits(repository, commits)
    }
}

private fun printCommits(repository: Repository, commits: List<CommitItem>) {
    if (commits.isEmpty()) {
        return
    }

    println("repository: ${repository.name}")

    commits.forEach {
        println("commit: ${it.commit.message}")
    }

    println("=====================================")
}