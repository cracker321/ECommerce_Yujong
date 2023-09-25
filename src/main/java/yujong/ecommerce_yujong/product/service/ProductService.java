package yujong.ecommerce_yujong.product.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.board.dto.BoardPatchDto;
import yujong.ecommerce_yujong.board.dto.BoardPostDto;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.global.exception.BusinessLogicException;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.product.entity.Product;
import yujong.ecommerce_yujong.product.mapper.ProductMapper;
import yujong.ecommerce_yujong.product.repository.ProductRepository;

import java.util.Optional;

import static yujong.ecommerce_yujong.global.exception.ExceptionCode.PRODUCT_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class ProductService { //완료!!


    private final ProductRepository productRepository;
    private final ProductMapper productMapper;





    /* 상품 Product 조회 Read */

    public Product findProduct(long productId){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessLogicException(PRODUCT_NOT_FOUND));

        return product;
    }



    /* DB에 상품 Product 존재 여부 확인 */
    public Product findVerifiedProduct(Product product){

        Optional<Product> optionalProduct = productRepository.findById(product.getProductId());

        Product findProduct = optionalProduct.orElseThrow(() -> new BusinessLogicException(PRODUCT_NOT_FOUND));

        return findProduct;
    }





    /* 상품 Product 등록 Create */
    public Product createProduct(Seller seller, BoardPostDto boardPostDto){


        Product product = productMapper.boardPostDtoToProduct(boardPostDto);


        product.setStatus(Product.ProductStatus.PRD_SELLING);
        product.setSeller(seller);

        Product productSaved = productRepository.save(product);


        return productSaved;

    }




    /* 상품 Product 수정 Update */
    public Product updateProduct(Board findBoard, BoardPatchDto boardPatchDto){

        Product findProduct = findVerifiedProduct(findBoard.getProduct());


        Optional.ofNullable(boardPatchDto.getPrice())
                .ifPresent(price -> findProduct.setPrice(price));

        Optional.ofNullable(boardPatchDto.getStatus())
                .ifPresent(status -> findProduct.setStatus(status));


        return productRepository.save(findProduct);


    }

}
