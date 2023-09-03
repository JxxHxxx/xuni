package com.xuni.api.support;

import com.xuni.core.common.support.ServiceOnly;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 순수하게 ServiceOnly - Repository 빈들만 테스트 하려는 경우
 * 수동 등록, @Service 가 붙은 빈은 주입되지 않습니다.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DataJpaTest(includeFilters = @ComponentScan.Filter(classes = {ServiceOnly.class}))
public @interface ServiceOnlyTest {
}
