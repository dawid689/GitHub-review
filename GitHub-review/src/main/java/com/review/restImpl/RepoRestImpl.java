package com.review.restImpl;

import com.review.rest.RepoRest;
import com.review.resultModel.Result;
import com.review.resultModel.ResultError;
import com.review.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.review.constants.ErrorMessages.INCORRECT_REQUEST;


@RestController
public class RepoRestImpl implements RepoRest {

    @Autowired
    RepoService repoService;

    @Override
    public ResponseEntity<Result> listUserRepos(String username, String acceptHeader) {
        try {
            return repoService.listUserRepos(username, acceptHeader);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ResultError(INCORRECT_REQUEST, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }
}
