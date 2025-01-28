package githubtest

import githubtest.client.GitHubApiClient
import githubtest.service.GitHubService

object DiContainer {

    private const val TOKEN = Constant.TOKEN

    val githubApiClient: GitHubApiClient by lazy { GitHubApiClient(TOKEN) }
    val githubService: GitHubService by lazy { GitHubService() }

}
