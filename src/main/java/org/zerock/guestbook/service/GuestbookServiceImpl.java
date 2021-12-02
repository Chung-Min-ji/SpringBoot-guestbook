package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // final, @NonNull 필드만 parameter로 갖는 constructor 생성
                         // 단일 생성자의 경우, @Autowired 없이도 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repo; // 반드시 final로 선언

    @Override
    public Long register(GuestbookDTO dto) {

        log.debug("register({}) invoked...", dto);

        //-- 1. DTO => Entity 변환
        Guestbook entity = dtoToEntity(dto);

        log.info("entity: {}", entity);

        //-- 2. save()로 insert
        repo.save(entity);

        return entity.getGno();
    }

    // 목록 조회 With 페이징
    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO reqDTO){
        Pageable pageable = reqDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(reqDTO); //검색조건 처리

        Page<Guestbook> result = repo.findAll(booleanBuilder, pageable); //Querydsl 사용

        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    // 특정 게시물 조회
    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result= repo.findById(gno);

        return result.isPresent()? entityToDto(result.get()): null;
    }

    // 게시물 삭제
    @Override
    public void remove(Long gno) {
        repo.deleteById(gno);
    }

    // 게시물 수정
    @Override
    public void modify(GuestbookDTO dto) {
        //업데이트하는 항목은 제목, 내용
        Optional<Guestbook> result = repo.findById(dto.getGno());

        if(result.isPresent()){
            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repo.save(entity);
        }
    }

    // 검색처리
    // reqDTO를 파라미터로 받아서 검색조건(TYPE)이 있는 경우에는 conditionBuilder 변수를 생성해서 각 검색 조건을 OR로 연결.
    // 반면 검색조건 없다면 gno>0 으로만 생성
    private BooleanBuilder getSearch(PageRequestDTO reqDTO){
        //Querydsl 처리
        String type= reqDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = reqDTO.getKeyword();
        BooleanExpression expression = qGuestbook.gno.gt(0L); // gno>0 조건만 생성
        booleanBuilder.and(expression);

        if(type==null || type.trim().length()==0){ // 검색 조건이 없는 경우
            return booleanBuilder;
        }

        // 검색 조건 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}
