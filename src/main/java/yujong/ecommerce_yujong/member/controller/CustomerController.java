package yujong.ecommerce_yujong.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yujong.ecommerce_yujong.member.dto.CustomerPatchDto;
import yujong.ecommerce_yujong.member.entity.Customer;
import yujong.ecommerce_yujong.member.entity.Member;
import yujong.ecommerce_yujong.member.mapper.MemberMapper;
import yujong.ecommerce_yujong.member.service.CustomerService;
import yujong.ecommerce_yujong.member.service.MemberService;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final MemberService memberService;
    private final MemberMapper memberMapper;




    /* 소비자 마이 페이지 조회 */
    @GetMapping("/{customer_id}")
    public ResponseEntity getCustomer(@PathVariable("customer_id") @Positive long customerId) {
        Member member = customerService.findCustomer(customerId).getMember();
        return ResponseEntity.ok(memberMapper.memberToCustomerDto(member));
    }



    /* 소비자 정보 수정 */
    @PutMapping("/{customer_id}")
    public ResponseEntity putCustomer(@PathVariable("customer_id") @Positive long customerId,
                                      @RequestBody CustomerPatchDto customerPatchDto) {
        Customer customer = customerService.updateCustomer(customerId, memberMapper.customerPatchDtoToCustomer(customerPatchDto));
        long memberId = customer.getMember().getMemberId();
        Member member = memberService.updateMember(memberId, memberMapper.customerPatchDtoToMember(customerPatchDto));
        return ResponseEntity.ok(memberMapper.memberToCustomerDto(member));
    }
}

