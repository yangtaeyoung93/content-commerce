package com.commerce.content.domain;

import com.commerce.content.domain.tag.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_idx", updatable = false)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @CreatedDate // 엔티티가 생성될 때 생성 시간 지정
    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @CreatedDate // 엔티티가 수정될 때 생성 시간 지정
    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    public void setUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }

    @ManyToMany(mappedBy = "articles")
    private List<Tag> tags = new ArrayList<>();
}
