package org.zerock.guestbook.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor // 생성자 자동주입 위한 어노테이션. final, @NonNull 필드를 parameter로 가지는 생성자 생성.
public class GuestbookController {

    private final GuestbookService service; // final***

    @GetMapping({"/"})
    public String list(){

        log.debug("list() invoked....");

        return "/guestbook/list";
    }
    
    @GetMapping("/list")
    public void list(PageRequestDTO reqDTO, Model model){
        log.debug("list({}, mode) invoked...", reqDTO);

        PageResultDTO result = service.getList(reqDTO);

        model.addAttribute("result", result);
    }
}
