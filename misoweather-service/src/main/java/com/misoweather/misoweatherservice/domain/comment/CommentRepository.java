package com.misoweather.misoweatherservice.domain.comment;

import com.misoweather.misoweatherservice.domain.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMember(Member member);
    List<Comment> findAllByOrderByIdDesc(Pageable page);
    List<Comment> findByIdLessThanOrderByIdDesc(Long id, Pageable page);
    Boolean existsByIdLessThan(Long id);
}