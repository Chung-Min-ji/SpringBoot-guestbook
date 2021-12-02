package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

public interface GuestbookService {

    // 게시물 등록
    Long register(GuestbookDTO dto);

    // 목록 조회 with 페이징
    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO reqDTO);

    // 게시물 조회
    GuestbookDTO read(Long gno);

    // 게시물 삭제
    void remove(Long gno);

    // 게시물 수정
    void modify(GuestbookDTO dto);

    // 인터페이스의 실제 구현내용을 가지는 default 메소드로 정의.
    // DTO => Entity 변환
    default Guestbook dtoToEntity(GuestbookDTO dto){
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    // Entity => DTO 변환
    default GuestbookDTO entityToDto(Guestbook entity){
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }

    //
}
