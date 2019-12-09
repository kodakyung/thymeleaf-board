package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.req.ArticleCreateReqDto;
import com.example.demo.dto.req.ArticleUpdateReqDto;
import com.example.demo.dto.res.ArticleResDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Api(value = "Article(게시글)")
@Slf4j
public class ArticleController {
    private final ArticleService articleService;
    private final UserRepository userRepository;

    @ApiOperation("게시글 생성")
    @PostMapping("/boards/{boardIdx}")
    public String createArticle(@PathVariable final Long boardIdx, ArticleCreateReqDto articleCreateReqDto, Model model) {// todo test user
        ArticleResDto article = articleService.createArticle(articleCreateReqDto, testUser(), boardIdx);
        model.addAttribute("article", article);
        return "redirect:articles/" + article.getArticleIdx(); // todo 생성한 '게시글 조회'로 이동
    }

    @ApiOperation("게시글 삭제")
    @DeleteMapping("/articles/{articleIdx}")
    public String deleteArticle(@PathVariable final Long articleIdx) {
        Long returnBoardIdx = articleService.deleteArticle(articleIdx, testUser());
        return "redirect:boards/" + returnBoardIdx; // todo "게시판의 게시글 목록 조회"로 돌아가기
    }

    @ApiOperation("게시글 수정")
    @PutMapping("/articles/{articleIdx}")
    public String editArticle(@PathVariable final Long articleIdx, ArticleUpdateReqDto articleUpdateReqDto) {
        ArticleResDto editArticle = articleService.editArticle(articleIdx, testUser(), articleUpdateReqDto);
        return "redirect:articles/" + editArticle.getArticleIdx(); // todo 수정한 '게시글 조회'로 이동
    }

    @ApiOperation("게시글 추천")
    @PutMapping("/articles/recommend/{articleIdx}")
    public Integer recommendArticle(@PathVariable final Long articleIdx) {
        // todo check user
        return articleService.addRecommendCnt(articleIdx, testUser());
//        return "redirect:articles/" + articleIdx;
    }

    private User testUser() { // todo security 구현 후 삭제
        return userRepository.findById(1L).get();
    }
}
