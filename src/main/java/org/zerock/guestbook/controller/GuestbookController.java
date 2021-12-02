package org.zerock.guestbook.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
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
    public void list(@ModelAttribute("reqDTO") PageRequestDTO reqDTO, Model model){
        log.debug("list({}, mode) invoked...", reqDTO);

        PageResultDTO result = service.getList(reqDTO);

        model.addAttribute("result", result);
    }

    // 게시물 등록화면
    @GetMapping("/register")
    public void register(){
        log.debug("register() GET invoked...");
    }

    // 게시물 등록 처리
    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes rttrs){
        log.debug("registerPOST({}, rttrs) invoked...", rttrs);

        // 새로 추가된 엔티티 번호
        Long gno = service.register(dto);

        rttrs.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }

    // 게시물 조회
    @GetMapping({"/read", "/modify"})
    // @ModelAttribute 없어도 되지만 명시적으로 처리
    public void read(long gno, @ModelAttribute("reqDTO") PageRequestDTO reqDTO, Model model){
        log.debug("read({}, {}, model) invoked...", gno, reqDTO);

        GuestbookDTO dto = service.read(gno);

        model.addAttribute("dto", dto);
    }

    // 수정
    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("reqDTO") PageRequestDTO reqDTO, RedirectAttributes rttrs){
        log.info("modify({},{},rtts) invoked.... dto, reqDTO");

        service.modify(dto);

        rttrs.addAttribute("page", reqDTO.getPage());
        rttrs.addAttribute("type", reqDTO.getType());
        rttrs.addAttribute("keyword", reqDTO.getKeyword());
        rttrs.addAttribute("gno", dto.getGno());

        return "redirect:/guestbook/read";
    }

    // 삭제
    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes rttrs){
        log.debug("remove({}, rttrs) invoked...", gno);

        service.remove(gno);

        rttrs.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }
}
