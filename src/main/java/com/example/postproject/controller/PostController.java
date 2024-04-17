package com.example.postproject.controller;

import com.example.postproject.domain.Post;
import com.example.postproject.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 홈페이지
    @GetMapping(value = {"", "/"})
    public String home(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "home";
    }

    // 게시글 작성
    @GetMapping(value = "/post/write")
    public String writePage() {
        return "write-page";
    }

    @PostMapping(value = "/post/write")
    public String writePost(Post post, HttpSession session) {
        // 로그인 상태 확인
        String nickname = (String) session.getAttribute("nickname");
        if (nickname == null) {
            throw new IllegalStateException("권한이 없습니다.");
        }
        postService.createPost(post, nickname);
        return "redirect:/";
    }

    // 게시글 보기
    @GetMapping(value = "/post/{id}")
    public String showPost(@PathVariable long id, Model model) {
        Post post = postService.getPostById(id).orElse(null);
        model.addAttribute("post", post);
        return "content-page";
    }

    // 게시글 수정
    @GetMapping(value = "/post/{id}/edit")
    public String updatePage(@PathVariable long id, Model model) {
        Post post = postService.getPostById(id).orElse(null);
        model.addAttribute("post", post);
        return "edit-page";
    }

    @PostMapping(value = "/post/{id}/edit")
    public ResponseEntity<String> updatePost(@PathVariable long id, String title, String content, HttpSession session) {
        // 로그인 상태 확인
        String nickname = (String) session.getAttribute("nickname");
        if (nickname == null) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        postService.editPost(id, title, content);
        return ResponseEntity.ok().body("edit-page");
    }

    // 게시글 삭제
    @PostMapping(value = "/post/{id}/delete")
    public ResponseEntity<String> deletePost(@PathVariable long id, HttpSession session) {
        // 로그인 상태 확인
        String nickname = (String) session.getAttribute("nickname");
        if (nickname == null) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        return postService.deletePost(id);
    }


    // 비밀번호 확인
//     @PostMapping(value = "/post/{id}/validate-password")
//     public ResponseEntity<String> validatePasswordAndRedirectToEditPage(@PathVariable long id, String password, RedirectAttributes redirectAttributes) {
//        if (postService.isPasswordCorrect(id, password)) {
//            redirectAttributes.addAttribute("id", id);
//            return ResponseEntity.ok().body("redirect:/post/{id}/edit");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("redirect:/post/{id}"); // 비밀번호가 틀렸을 때 리다이렉트할 경로 지정
//        }
//    }
}
