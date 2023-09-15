package com.review.rest;

import com.review.resultModel.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;


@RequestMapping(path = "/github")
public interface RepoRest {

    @GetMapping(path = "/repos")
    ResponseEntity<Result> listUserRepos(@RequestParam(name = "username") String username,
                                         @RequestHeader(name = "Accept") String acceptHeader);

}
