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
            post.getComments().add(comment);

            // Post의 eachComments 값을 1 증가
            post.setEachComments(post.getEachComments() + 1);
            postRepository.save(post);
            commentRepository.save(comment);
        } else {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        }
    }

    // 댓글 삭제
    @Transactional
    public ResponseEntity<String> deleteComment(long commentId) {
        // 해당 댓글 찾기
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            Post post = comment.getPost();

            comment.getReplies().forEach(reply -> {
                post.setEachComments(post.getEachComments() - 1);
                post.getComments().remove(reply);
                commentRepository.delete(reply);
            });

            // 해당 게시글에서 댓글 삭제, 댓글 개수 감소
            post.setEachComments(post.getEachComments() - 1);
            post.getComments().remove(comment);

            // 게시글 저장, 댓글 삭제
            postRepository.save(post);
            commentRepository.delete(comment);

            return ResponseEntity.ok("성공적으로 삭제되었습니다.");
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // 답글 생성
    @Transactional
    public void createReply(Long postId, Long commentId, String replyContent, String nickname) {
        // 새로운 답글 객체 생성 후, 내용/닉네임/작성 날짜 설정
        Comment reply = new Comment();
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        if (!optionalComment.isPresent()) throw new IllegalArgumentException("해당 댓글이 존재하지 않습니다.");

        reply.setContent(replyContent);
        reply.setNickname(nickname);
        reply.setUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        reply.setPost(optionalPost.get());
        reply.setParentComment(optionalComment.get());

        Post post = optionalPost.get();
        post.setEachComments(post.getEachComments() + 1);
        postRepository.save(post);
        commentRepository.save(reply);
    }

    // 답글 삭제
    @Transactional
    public ResponseEntity<String> deleteReply(Long replyId) {
        Optional<Comment> optionalReply = commentRepository.findById(replyId);

        if (optionalReply.isPresent()) {
            // 해당 게시글 및 부모 댓글에서 해당 답글 삭제, 게시글 전체 댓글 수 1 감소
            Post post = optionalReply.get().getPost();
            Comment parentComment = optionalReply.get().getParentComment();

            post.setEachComments(post.getEachComments() - 1);
            post.getComments().remove(optionalReply.get());
            parentComment.getReplies().remove(optionalReply.get());

            postRepository.save(post);
            commentRepository.delete(optionalReply.get());

            return ResponseEntity.ok("성공적으로 삭제되었습니다.");
        }

        return new ResponseEntity<>("해당 답글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }

    // 댓글 및 답글 존재 및 닉네임 일치 체크
    public boolean checkNickname(long commentId, String nickname) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.isPresent() && optionalComment.get().getNickname().equals(nickname);
    }

    // 댓글 및 답글 존재 체크
    public boolean checkComment(long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.isPresent();
    }

}
