package com.review.service;

import com.review.resultModel.Result;
import org.springframework.http.ResponseEntity;


public interface RepoService {

    ResponseEntity<Result> listUserRepos(String username, String acceptHeader);

}