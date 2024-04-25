package com.example.postproject.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "updateDate")
    private String updateDate;

    @Column(name = "views")
    private int views;

    // 코멘트 개수
    @Column(name = "eachComments")
    private int eachComments;

    // 코멘트 목록
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

    public void setContent(String content) {
        this.content = content.replace("\n", "<br/>");
    }

}
