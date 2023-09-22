package yujong.ecommerce_yujong.ord.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.ord.dto.OrdPostDto;
import yujong.ecommerce_yujong.ord.dto.OrdResponseDto;
import yujong.ecommerce_yujong.ord.entity.Ord;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class OrdService {






//==================================================================================================================



    //[ 주문 Ord 생성 Create ]
    //- 고객 Customer 만 주문할 수 있고, 판매자 Seller 는 주문 내역만 조회로 가져감

    public OrdResponseDto createOrd(OrdPostDto ordPostDto){




    }



    //[ DB에 현재 존재하는 고객 Cusotmer 인지 여부 확인하고, 존재하는 고객 Customer 라면, 그 고객 Cusotmer 객체를 반환해줌]

    public Page<Ord> findCustomerOrd(Long customerId, int page, int size)



//==================================================================================================================





//==================================================================================================================




//==================================================================================================================






//==================================================================================================================




}
