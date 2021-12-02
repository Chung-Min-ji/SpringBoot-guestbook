package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
// Page<Entity> 의 엔티티 객체들을 DTO 객체로 변환해서 자료구조에 담아줌
// 화면 출력에 필요한 페이지 정보들을 구성.
public class PageResultDTO<DTO, EN> {

    // PageResultDTO는 List<DTO> 타입으로 dto 객체 보관함.
    // 따라서 Page<Etity>의 내용물 중에서 엔티티 객체를 DTO로 변환하는 기능 필요.
    // 일반적으로 abstract class를 이용해 처리하지만, 이 경우 매번 새로운 클래스가 필요하다는 단점이 있다.
    // 예제에서는 엔티티 객체의 DTO 변환을 서비스 인터페이스에 정의한 메서드(entityToDto())와, 별도의 Function 객체로 만들어 처리.
    private List<DTO> dtoList;  //DTO리스트
    private int totalPage; //총 페이지 번호
    private int page; //현재 페이지 번호
    private int size; // 목록 사이즈
    private int start, end; // 현재 화면에서 시작 페이지번호, 끝 페이지 번호
    private boolean prev, next; //이전, 다음
    private List<Integer> pageList; //페이지 번호 목록

    // Page<Entity> 타입 이용해서 생성할 수 있도록 생성자로 작성
    // Function<EN, DTO> 는 엔티티 객체를 DTO로 변환해주는 기능 (p162)
    public PageResultDTO(Page<EN>result, Function<EN, DTO> fn) {
        dtoList = result.stream()
                .map(fn)
                .collect(Collectors.toList());

        totalPage= result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber()+1; //0부터 시작하므로 1 추가
        this.size = pageable.getPageSize();

        //temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;
        prev = start > 1;
//        end = totalPage > tempEnd ? tempEnd : totalPage;
        end = Math.min(totalPage, tempEnd);
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }

}
