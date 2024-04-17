package com.example.postproject.service;

import com.example.postproject.domain.Member;
import com.example.postproject.domain.Post;
import com.example.postproject.repository.MemberRepository;
import com.example.postproject.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public PostService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    // 글 작성
    @Transactional
    public void createPost(Post post, String nickname) {
        post.setViews(0);

        // 글쓴이, 작성 날짜 설정
        post.setUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        post.setNickname(nickname);

        // 게시글 저장
        Post savedPost = postRepository.save(post);

        // Member의 postList에 Post 추가
        Optional<Member> optionalMember = Optional.ofNullable(memberRepository.findByNickname(nickname));
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.getPostList().add(savedPost);
            memberRepository.save(member);
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    // 글 수정
    public void editPost(long id, String title, String texts) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            post.setContent(texts);
            if (!title.isEmpty()) {
                post.setTitle(title);
            }

            // 수정 날짜 설정
//            LocalDateTime now = LocalDateTime.now();
//            String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            post.setUpdateDate(formattedDate);

            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다. id: " + id);
        }
    }

    // 글 삭제
    public ResponseEntity<String> deletePost(long id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isPresent()) {
            postRepository.delete(optionalPost.get());
            return ResponseEntity.ok("Post deleted successfully");
        } else {
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }

    }

    // 글 전체 찾기
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByIdDesc();
    }

    // 특정 글 찾기
    public Optional<Post> getPostById(long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setViews(post.getViews() + 1);
            postRepository.save(post);
            return optionalPost;
        } else {
            throw new IllegalArgumentException("해당 게시물이 존재하지 않습니다.");
        }
    }

    // 비밀번호 확인 로직
//    public boolean isPasswordCorrect(Long id, String enteredPassword) {
//        Optional<Post> optionalPost = postRepository.findById(id);
//        if (optionalPost.isPresent()) {
//            Post post = optionalPost.get();
//            return post.getPassword().equals(enteredPassword);
//        }
//        return false;
//    }
}
