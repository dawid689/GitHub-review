package com.review.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.review.model.Branch;
import com.review.model.Repo;
import com.review.resultModel.Result;
import com.review.resultModel.ResultError;
import com.review.resultModel.ResultList;
import com.review.service.RepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import static com.review.constants.ErrorMessages.*;

@Configuration
@Slf4j
@Service
public class RepoServiceImpl implements RepoService {

    @Override
    public ResponseEntity<Result> listUserRepos(String username, String acceptHeader) {

        if (acceptHeader.equals("application/xml")) {
            try {
                return new ResponseEntity<>(new ResultError(UNSUPPORTED_DATA_TYPE,
                        HttpStatus.NOT_ACCEPTABLE.value()),
                        HttpStatus.NOT_ACCEPTABLE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (acceptHeader.equals("application/json")) {
            try {
                String reposWithoutOwnerUrl = "https://api.github.com/users/{username}/repos";
                String reposUrl = reposWithoutOwnerUrl.replace("{username}", username);

                ObjectMapper objectMapper = new ObjectMapper();

                URL urlTypeRepos = new URL(reposUrl);
                HttpURLConnection reposConnection = (HttpURLConnection) urlTypeRepos.openConnection();
                reposConnection.setRequestMethod("GET");
                int responseCodeRepos = reposConnection.getResponseCode();

                if (responseCodeRepos == HttpURLConnection.HTTP_OK) {
                    JsonNode jsonNodeR = objectMapper.readTree(reposConnection.getInputStream());
                    List<Repo> repoInfoList = new ArrayList<>();

                    for (JsonNode repoNode : jsonNodeR) {
                        String repoName = repoNode.get("name").asText();
                        String ownerLogin = repoNode.get("owner").get("login").asText();
                        boolean isFork = repoNode.get("fork").asBoolean();
                        if (!isFork) {
                            repoInfoList.add(new Repo(repoName, ownerLogin, null));
                        }
                    }

                    String branchesWithoutOwnerAndRepoNameUrl = "https://api.github.com/repos/{username}/{repo}/branches";
                    String branchesWithOwner = branchesWithoutOwnerAndRepoNameUrl.replace("{username}", username);

                    for (Repo repo : repoInfoList) {
                        String repoName = repo.getName();
                        String branchesUrlWithOwnerAndRepoName = branchesWithOwner.replace("{repo}", repoName);
                        List<Branch> branchInfoList = new ArrayList<>();

                        URL urlTypeBranches = new URL(branchesUrlWithOwnerAndRepoName);
                        HttpURLConnection branchesConnection = (HttpURLConnection) urlTypeBranches.openConnection();
                        branchesConnection.setRequestMethod("GET");
                        int responseCodeBranches = branchesConnection.getResponseCode();

                        if (responseCodeBranches == HttpURLConnection.HTTP_OK) {
                            JsonNode jsonNodeB = objectMapper.readTree(branchesConnection.getInputStream());
                            for (JsonNode branchNode : jsonNodeB) {
                                String branchName = branchNode.get("name").asText();
                                String commitSHA = branchNode.get("commit").get("sha").asText();
                                Branch b = new Branch(branchName, commitSHA);
                                branchInfoList.add(b);
                            }
                        }
                        repo.setBranches(branchInfoList);
                        branchesConnection.disconnect();
                    }
                    reposConnection.disconnect();

                    return new ResponseEntity<>(new ResultList(repoInfoList), HttpStatus.OK);

                } else {
                    return new ResponseEntity<>(new ResultError(USER_DOES_NOT_EXIST, HttpStatus.NOT_FOUND.value()),
                            HttpStatus.NOT_FOUND);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Message: " + ex);
            }
        }

        return new ResponseEntity<>(new ResultError(INCORRECT_REQUEST, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }
}
