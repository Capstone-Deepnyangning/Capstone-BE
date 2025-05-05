package com.deepnyangning.capstonebe.domain.access.specification;

import com.deepnyangning.capstonebe.domain.access.entity.AccessLog;
import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AccessLogSpecification {

    public static Specification<AccessLog> withFilters(
            LocalDateTime startTime,
            LocalDateTime endTime,
            String identifier,
            String name,
            AuthMethod authMethod
    ){
        return Specification
                .where(accessTimeBetween(startTime, endTime))
                .and(hasIdentifierLike(identifier))
                .and(hasNameLike(name))
                .and(authMethodEquals(authMethod));
    }

    // 조회 기간 - accessTime이 특정 범위에 있는지 조회
    public static Specification<AccessLog> accessTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return (root, query, cb) -> {
            if(startTime == null || endTime == null) return null;
            return cb.between(root.get("accessTime"), startTime, endTime);
        };
    }

    // 사용자 - identifier 키워드 검색
    public static Specification<AccessLog> hasIdentifierLike(String identifier){
        return (root, query, cb) -> {
            if(identifier == null || identifier.trim().isEmpty()) return null;

            Join<AccessLog, User> userJoin = root.join("user", JoinType.INNER);
            String pattern = "%" + identifier.toLowerCase().replace(" ", "") + "%";
            Expression<String> identifierExpr = cb.lower(cb.function("replace", String.class, userJoin.get("identifier"), cb.literal(" "), cb.literal("")));
            return cb.like(identifierExpr, pattern);
        };
    }

    // 사용자 - name 키워드 검색
    public static Specification<AccessLog> hasNameLike(String name){
        return (root, query, cb) -> {
          if(name == null || name.trim().isEmpty()) return null;

          Join<AccessLog, User> userJoin = root.join("user");
          String pattern = "%" + name.toLowerCase().replace(" ", "") + "%";
          Expression<String> nameExpr = cb.lower(cb.function("replace", String.class, userJoin.get("name"), cb.literal(" "), cb.literal("")));
          return cb.like(nameExpr, pattern);
        };
    }

    // 인증 방식 - authMethod가 같은지 판단
    public static Specification<AccessLog> authMethodEquals(AuthMethod authMethod){
        return (root, query, cb) -> {
          if(authMethod == null) return null;
          return cb.equal(root.get("authMethod"), authMethod);
        };
    }
}
