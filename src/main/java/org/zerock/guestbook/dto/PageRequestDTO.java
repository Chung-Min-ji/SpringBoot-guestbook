package org.zerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
// 화면에서 전달되는 Page라는 파라미터와, size라는 파라미터 수집하는 역할.
// JPA에서 사용하는 Pageable 객체를 생성.
public class PageRequestDTO {

    private int page;
    private int size;

    // 페이지 번호 등은 기본값을 가지는 것이 좋기 때문에, 1과 10으로 설정.
    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){

        // JPA에서 페이지 번호 0부터 시작하므로, page-1 형태로 작성
        return PageRequest.of(page-1, size, sort);
    }
}
