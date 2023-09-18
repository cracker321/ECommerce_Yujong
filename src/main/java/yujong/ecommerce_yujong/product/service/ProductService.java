package yujong.ecommerce_yujong.product.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.product.entity.Product;
import yujong.ecommerce_yujong.product.repository.ProductRepository;

@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class ProductService {


    private final ProductRepository productRepository;


    //[ 상품 등록 ]
    public Product createProduct(Seller seller, Product product){

        product.setStatus(Product.ProductStatus.PRD_SELLING);
        product.setSeller(seller);
        Product productSaved = productRepository.save(product);
        //*****중요*****
        //< 'JpaRepository의 내장 메소드 save' >
        //: DB에 저장시킨 상품 Product 저장시키고, 그 상품 Product 를 반환해주는 메소드이다!
        //  'save'위에 ctrl 눌러서 확인해보면 'Returns: the saved entities; will never be null.' 일가ㅗ 나옴!

        return productSaved;

    }



















}
