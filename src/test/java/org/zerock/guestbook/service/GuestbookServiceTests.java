package org.zerock.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService service;

    @Test
    public void testRegister(){

        GuestbookDTO dto = GuestbookDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user0")
                .build();

        System.out.println(service.register(dto));
    }

    @Test
    public void testList(){
        //reqDTO : 파라미터 수집 -> Pageable 객체 리턴
        PageRequestDTO reqDTO = PageRequestDTO.builder().page(1).size(10).build();
        // resultDTO : reqDTO에서 얻어낸 Pageable 객체를 통해,
        //             페이징처리에 필요한 정보 저장 (service.getList())
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(reqDTO);

        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage()); //전체 페이지 개수

        System.out.println("----------------------------------");
        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }

        System.out.println("===================================");
        resultDTO.getPageList().forEach(i-> System.out.println(i)); //화면에 출력될 페이지 번호
    }
}
