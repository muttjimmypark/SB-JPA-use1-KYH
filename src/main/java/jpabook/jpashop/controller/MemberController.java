package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.Service.MemberService;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        /**
         * 아규먼트의 @Valid로 폼의 @NotNull 필드 exception 터지게 validation 처리
         *
         * BindingResult라는걸 활용하면 오류를 어디 담아둔채 본 메서드를 마저 실행시킨다
         * error를 담은채로 저 폼을 리턴하면, 타임리프가 BindingResult를 같이
         *
         * createMemberForm의 name필드에서 처리해준덕분에 @NotNull의 에러메시지를 나타나게
         * POST로 MemberForm 정보들도 같이 넘어가니까, 다른필드에서 작성한게 유지도 됨
         *          */

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";

        /**
         * 화면~컨트롤러 간 폼데이터를 별도로 만들어서
         * 도메인데이터 코드가 지저분해지는것을 막고
         * 서로 다를 수 있는 제약조건을 별도로 관리할수 있다
         */
    }
}
