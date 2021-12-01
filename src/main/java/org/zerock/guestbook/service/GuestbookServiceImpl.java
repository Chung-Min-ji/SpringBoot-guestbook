package org.zerock.guestbook.service;

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
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // final, @NonNull 필드만 parameter로 갖는 constructor 생성
                         // 단일 생성자의 경우, @Autowired 없이도 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repo; // 반드시 final로 선언

    @Override
    public Long register(GuestbookDTO dto) {

        log.info("register({}) invoked...", dto);

        Guestbook entity = dtoToEntity(dto);

        log.info("entity: {}", entity);

        // save()로 insert
        repo.save(entity);

        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO reqDTO){
        Pageable pageable = reqDTO.getPageable(Sort.by("gno").descending());

        Page<Guestbook> result = repo.findAll(pageable);

        Function<Guestbook, GuestbookDTO> fn = (entity-> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }
}
