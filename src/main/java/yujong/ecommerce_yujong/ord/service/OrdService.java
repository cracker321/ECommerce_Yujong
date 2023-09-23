package yujong.ecommerce_yujong.ord.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.board.repository.BoardRepository;
import yujong.ecommerce_yujong.board.service.BoardService;
import yujong.ecommerce_yujong.global.exception.BusinessLogicException;
import yujong.ecommerce_yujong.global.exception.ExceptionCode;
import yujong.ecommerce_yujong.member.entity.Customer;
import yujong.ecommerce_yujong.member.service.CustomerService;
import yujong.ecommerce_yujong.ord.dto.OrdPostDto;
import yujong.ecommerce_yujong.ord.dto.OrdResponseDto;
import yujong.ecommerce_yujong.ord.entity.Ord;
import yujong.ecommerce_yujong.ord.mapper.OrdMapper;
import yujong.ecommerce_yujong.ord.repository.OrdRepository;
import yujong.ecommerce_yujong.product.entity.Product;
import yujong.ecommerce_yujong.product.repository.ProductRepository;
import yujong.ecommerce_yujong.product.service.ProductService;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrdService {
    private final OrdRepository ordRepository;
    private final OrdMapper ordMapper;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final CustomerService customerService;





    /* 주문 Ord 조회 Read */
    @Transactional
    public Ord findVerifiedOrd(Long ordId){
        Optional<Ord> optionalOrd = ordRepository.findById(ordId);
        Ord findOrd = optionalOrd.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
        return findOrd;
    }




    /* 주문 Ord 생성 Create */
    /* 주문은 Customer 만 할 수 있고, 판매자는 내역만 조회로 가져감 */
    public OrdResponseDto createOrd(OrdPostDto ordPostDto) {

        /* 고객 Customer 존재 여부 확인 */
        Customer findCustomer = customerService.findCustomer(ordPostDto.getCustomerId());

        /* 게시글 Board 존재 여부 확인 */
        Board findBoard = boardService.findVerifiedBoard(ordPostDto.getBoardId());

        /* 상품 존재 여부 확인 */
        Product findProduct = productService.findProduct(findBoard.getProduct().getProductId());

        /* 주문 시 재고가 없다면 */
        if(findProduct.getLeftStock() == 0){

            findProduct.setStatus(Product.ProductStatus.PRD_SOLDOUT);
            productRepository.save(findProduct);

            throw new BusinessLogicException(ExceptionCode.PRODUCT_SOLDOUT);
        }


        /* 재고가 있다면 */
        else{
            Ord ord = ordMapper.ordPostDtoToOrd(findCustomer,findProduct, ordPostDto);

            /* 재고 < 수량 인 경우 */
            if(findProduct.getLeftStock() < ord.getQuantity()){
                throw new BusinessLogicException(ExceptionCode.PRODUCT_NOT_ENOUGH);
            }

            /* 재고 > 수량 인 경우 */
            /* 주문 등록 시, 재고에서 수량만큼 빼기 */
            findProduct.setLeftStock(findProduct.getLeftStock() - ord.getQuantity());
            productRepository.save(findProduct);

            /* ord DB 저장 */
            ord.setProduct(productService.findProduct(findBoard.getBoardId()));
            ord.setCustomer(customerService.findVerifiedCustomer(findCustomer.getCustomerId()));
            ordRepository.save(ord);
            OrdResponseDto responseDto = ordMapper.ordToOrdResponseDto(ord);

            return responseDto;
        }
    }



    /* 주문 Ord 삭제 Delete */
    @Transactional
    public void deleteOrd(Long ordId){
        Ord foundOrd = findVerifiedOrd(ordId);
        ordRepository.delete(foundOrd);
    }

    
    
}