package com.example.postproject.controller;

import com.example.postproject.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 생성
    @PostMapping(value = "/post/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String commentContent,
                             HttpSession session) {
        // 로그인 되어 있는지 체크
        String nickname = (String) session.getAttribute("nickname");
        if (nickname == null) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        commentService.createComment(id, commentContent, nickname);
        return "redirect:/post/{id}";
    }

    // 댓글 삭제
    @PostMapping(value = "post/{id}/comment/{commentId}")
    public String deleteComment(@PathVariable long id,
                                @PathVariable long commentId,
                                HttpSession session) {
        // 로그인 되어 있는지, 댓글 작성자인지 체크
        String nickname = (String) session.getAttribute("nickname");
        if (nickname == null || !commentService.findByCommentId(commentId).get().getNickname().equals(nickname)) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        commentService.deleteComment(commentId);
        return "redirect:/post/{id}";
    }

    // 답글 생성


    // 답글 삭제
}
