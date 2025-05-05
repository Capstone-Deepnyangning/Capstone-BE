package com.deepnyangning.capstonebe.domain.access.specification;

import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import com.deepnyangning.capstonebe.domain.access.entity.FailLog;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;


public class FailLogSpecification {

    public static Specification<FailLog> withFilters(LocalDateTime startTime, LocalDateTime endTime, AuthMethod authMethod){
        return Specification
                .where(accessTimeBetween(startTime, endTime))
                .and(authMethodEquals(authMethod));
    }

    // 조회 기간 - accessTime이 특정 범위에 있는지 조회
    public static Specification<FailLog> accessTimeBetween(LocalDateTime startTime, LocalDateTime endTime){
        return (root, query, cb) -> {
          if(startTime == null || endTime == null) return null;
          return cb.between(root.get("accessTime"), startTime, endTime);
        };
    }

    // 인증 방식 - authMethod가 같은지 판단
    public static Specification<FailLog> authMethodEquals(AuthMethod authMethod){
        return (root, query, cb) -> {
          if(authMethod == null) return null;
          return cb.equal(root.get("authMethod"), authMethod);
        };
    }
}
