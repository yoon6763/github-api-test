package githubtest.service

import githubtest.DiContainer
import githubtest.client.GitHubApiClient
import githubtest.dto.commit.CommitItem
import githubtest.dto.repository.Repository
import java.time.LocalDateTime

class GitHubService {

    private var gitHubApiClient: GitHubApiClient = DiContainer.githubApiClient

    fun getRepositoriesWithCommits(owner: String, sinceDate: LocalDateTime): Map<Repository, List<CommitItem>> {
        return getRepositories(owner).associateWith { repository ->
            getCommits(owner, repository.name, sinceDate)
        }
    }

    // 리포지토리 목록 가져오기
    private fun getRepositories(owner: String): List<Repository> {
        val repositories = mutableListOf<Repository>()
        var page = 1
        val perPage = 100

        while (true) {
            val response = gitHubApiClient.getRepositories(owner, page, perPage)
            repositories.addAll(response)

            if (response.size < perPage) {
                break
            }

            page++
        }

        return repositories
    }

    // 커밋 목록 가져오기
    private fun getCommits(owner: String, repo: String, sinceDate: LocalDateTime): List<CommitItem> {
        return gitHubApiClient.getCommits(owner, repo, sinceDate)
    }
}