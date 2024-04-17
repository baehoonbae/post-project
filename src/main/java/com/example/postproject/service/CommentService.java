package com.example.postproject.service;

import com.example.postproject.domain.Comment;
import com.example.postproject.domain.Post;
import com.example.postproject.repository.CommentRepository;
import com.example.postproject.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    // 댓글 생성
    @Transactional
    public void createComment(long postId, String commentContent, String nickname) {
        // 새로운 댓글 객체 생성 후, 내용/닉네임/작성 날짜 설정
        Comment comment = new Comment();

        comment.setContent(commentContent);
        comment.setNickname(nickname);
        comment.setUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 해당 게시글에 댓글을 추가
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            comment.setPost(post);
            commentRepository.save(comment);

            // Post의 eachComments 값을 1 증가
            post.setEachComments(post.getEachComments() + 1);
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("Post does not exist. PostId: " + postId);
        }
    }

    // 댓글 삭제
    @Transactional
    public ResponseEntity<String> deleteComment(long commentId) {
        // 해당 댓글 찾기
        Optional<Comment> optionalComment = findByCommentId(commentId);

        if (optionalComment.isPresent()) {
            // 해당 게시글에서 댓글 삭제, 댓글 개수 1 감소
            Post post = optionalComment.get().getPost();
            int eachComments = post.getEachComments();
            post.setEachComments(eachComments - 1);
            post.getComments().remove(optionalComment.get());

            // 게시글 저장, 댓글 삭제
            postRepository.save(post);
            commentRepository.delete(optionalComment.get());

            return ResponseEntity.ok("Post deleted successfully");
        } else {
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }

    }

    // 특정 댓글 찾기
    public Optional<Comment> findByCommentId(long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            return optionalComment;
        } else {
            throw new IllegalArgumentException("해당 댓글이 존재하지 않습니다.");
        }
    }

}
