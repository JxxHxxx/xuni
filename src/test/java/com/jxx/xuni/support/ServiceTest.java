package com.jxx.xuni.support;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.context.annotation.ComponentScan.*;

/**
 * @Repository - @Service 레이어를 테스트 합니다.
 * 외부 라이브러리, MVC 의존이 필요한 클래스에 @Service 를 붙이면 @ServiceTest 에서 오류가 날 수 있습니다.
 * 외부 라이브러리, MVC 의존이 필요한 클래스에는 @Component 를 붙이는 것을 권장합니다.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DataJpaTest(includeFilters = @Filter(classes = {Service.class}))
public @interface ServiceTest {
}
