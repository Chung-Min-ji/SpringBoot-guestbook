package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Setter(onMethod_=@Autowired)
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1,300).forEach(i->{
            Guestbook guestbook = Guestbook.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer("user" + (i%10))
                    .build();
            guestbookRepository.save(guestbook);
//            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    public void updateTest(){

        Optional<Guestbook> result = guestbookRepository.findById(300L);
        // 존재하는 번호로 테스트

        if(result.isPresent()){
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Titled...");
            guestbook.changeContent("Changed Content...");

            guestbookRepository.save(guestbook);
        }
    }

    //-- 단일항목 검색 테스트
    @Test
    public void testQuery1(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        //--1. 동적인 처리 위해 Q도메인 클래스 얻어옴.
        // Q도메인 클래스를 이용하면, 엔티티 클래스에 선언된 title, content같은 필드들을 변수로 활용할 수 있다.
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        //--2. BooleanBuilder는 where문에 들어가는 조건들을 넣어주는 컨테이너
        BooleanBuilder builder = new BooleanBuilder();

        //--3. 원하는 조건은 필드 값과 같이 결합해서 생성.
        // BooleanBuilder 안에 들어가는 값은 com.querydsl.core.types.Predicate 타입이어야 함.
        // (Java 에 있는 Predicate 타입이 아님)
        BooleanExpression expression = qGuestbook.title.contains(keyword);

        //--4. 만들어진 조건은 where 문에 and, or 같은 키워드와 결합시킴.
        builder.and(expression);

        //-- 5. BooleanBuilder는 GuestbookRepository에 추가된 QuerydslPredicateExcutor 인터페이스의 findAll()을 사용할 수 있다.
        // findAll() : 페이징처리에 사용 (리턴타입을 Page<T> 타입으로 지정할 경우, 파라미터는 반드시 Pageable 타입이어야 함)
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }


    //-- 다중항목 검색 테스트
    @Test
    public void testQuery2(){

        Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());

        //--1. 동적인 처리 위해 Q도메인 클래스 얻어옴.
        // Q도메인 클래스를 이용하면, 엔티티 클래스에 선언된 title, content같은 필드들을 변수로 활용할 수 있다.
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        //--2. BooleanBuilder는 where문에 들어가는 조건들을 넣어주는 컨테이너
        BooleanBuilder builder = new BooleanBuilder();

        //--3. 원하는 조건은 필드 값과 같이 결합해서 생성.
        // BooleanBuilder 안에 들어가는 값은 com.querydsl.core.types.Predicate 타입이어야 함.
        // (Java 에 있는 Predicate 타입이 아님)
        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        //--4. 만들어진 조건은 where 문에 and, or 같은 키워드와 결합시킴.
        // (1) exTitle 과 exContent 조건을 or() 로 결합
        BooleanExpression exAll = exTitle.or(exContent);

        // (2) builder 에 추가
        builder.and(exAll);

        // (3) gno가 0보다 크다는 조건을 추가.
        // gt() : GreaterThan
        builder.and(qGuestbook.gno.gt(0L));

        //-- 5. BooleanBuilder는 GuestbookRepository에 추가된 QuerydslPredicateExcutor 인터페이스의 findAll()을 사용할 수 있다.
        // findAll() : 페이징처리에 사용 (리턴타입을 Page<T> 타입으로 지정할 경우, 파라미터는 반드시 Pageable 타입이어야 함)
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook->{
            System.out.println(guestbook);
        });
    }
}