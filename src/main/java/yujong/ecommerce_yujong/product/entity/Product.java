package yujong.ecommerce_yujong.product.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.ord.entity.Ord;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name="Product")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable=false)
    private int price;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.PRD_SELLING;

    @PositiveOrZero
    @Max(50)
    @Column(nullable=false)
    private int stock;

    @PositiveOrZero
    @Max(50)
    @Column(nullable=false)
    private int leftStock;

    @Column(nullable=false)
    private int category;

    //확인하기!
    @Column
    private String mainImage;

//==================================================================================================================


    //< Board(1) - Product(1) 일대일 양방향 매핑. 주인 객체: Board >
    @OneToOne(mappedBy="product", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private Board board;



    //< Board - Product 연관관계 편의 메소드 > //교재 p190~
    //- set이 붙는 연관관계 편의 메소드는 기본적으로 Setter 세터와 형식이 같음!!
    public void setProduct(Board board){

        //- 아래보다, 김영한님 강의에 내가 필기한 것이 더 올바른 방법임.
        /*
        < 뤼튼 답변 >
        // 적절한 사용 예제: 무한루프 방지

        public void setProduct(Board board) {
            if (this.board != null) {
                this.board.setProduct(null);
            }

            this.board = board;

            if (board != null && board.getProduct() != this) {
                board.setProduct(this);
            }
        }

         */


        // Step 1)
        //- 'this.board != null'
        //  : 만약, 현재 특정된 상품 Product의 게시글 Board 정보가 비어져 있지 않은 상태(=깨끗하지 않은)이라면,
        //   이것은 즉 곧 이 현재 상품 Product가 과거에 이미 해당 게시글 Board에 포함된 상품 Product로서 기 작서된 게시글에
        //   포함되어 있었던지 등 어떤 경로를 통해서였든지
        //   이 현재 상품 Product가 이 쇼핑몰에서 기존에 자신과 연결되어 있는 게시글 Board 정보를 가지고 있는 상태라는 것을 의미하므로,
        //   (= 현재 특정된 상품 Product가 이미 어떤 다른 게시글 Board 객체와 연결되어 있는 상태인 경우 라는 의미임)
        //- 'this.board.setSeller(null)'
        //  : 현재 특정된 상품 Product의 기존 게시글 Board 정보 를 지워버린다.
        //    즉, 현재 특정된 상품 Product가 가지고 있었던 기존 게시글 Board 정보를
        //    현재 특정된 상품 Product에서 떼어내어 제거시키는 것임.
        //    즉, 현재 상품 Product와 기존 자신이 포함되어 있던 게시글 Board 객체 간의 연관관계 매핑을 끊어버리고 해제시키는 것임.
        //   (= 현재 특정된 상품 Product와 매핑되어 있는 게시글 Board 객체의 상품 Product 정보를 null로 해버리는 것임)
        if(this.board != null){
            this.board.setProduct(null);
        }

        //Step2)
        //외부 클래스 어딘가에서 이 메소드 setProduct를 새로운 인자값(=새로운 게시글 'Board board')으로 호출할 때,
        //그 때 주어지는 새로운 게시글('Board board')을 '현재 상품의 게시글('this.board')'로 새롭게 설정함.
        //즉, '현재 Product 엔티티 객체의 필드 board'를, 여기서 외부 클래스 어딘가에서 이 메소드 setProduct를 
        //새로운 인자값(=새로운 게시글 'Board board')'으로 호출할 때 주어지는 새로운 게시글('Board board') Board 객체로 설정하는 것임.
        //즉, 이 때의 Product 객체가 어떤 Board에 속하는지 지정하는 것임.
        this.board = board;

        //Step3)
        //- 'board != null'
        //  : 혹시라도 만약에, 외부에서 새롭게 들어온 게시글 Board 정보(Board board)가 비어 있는 상태가 아니면서(='&&'),
        //    (그냥 의례적으로 NullPointerException을 방지하기 위해 입력한 부분.
        //     당연히, 외부에서 새롭게 들어온 게시글 Board 정보가 비어 있지 않은 상태여야
        //     (물론, 당연히 비어있지 않기 때문에 이 비어 있지 않은 정보를 떼어내면서 현재 자신의 상품 Product로서의 정보로 대체해야
        //     하는 것임),
        //     바로 다음에 나오는 board.getProduct() != this 이 부분에 대한 검사 검증이 진행될 수 있음. 당연한 소리임.)
        //- 외부 클래스 어딘가에서 이 메소드 setProduct를 새로운 인자값(=새로운 게시글 'Board board')으로 호출할 때,
        //  step1 에서 그 외부에서 들어온 새로운 Board 객체를 현재 Product 엔티티 객체의 필드 board의 값으로 받아들였고,
        //  이제 그 새롭게 받아들인 게시글 board가, 기존에 이미 어떤 다른 상품 Product 객체와 연결되어 있는 상태인지를 확인하는 것임.
        //- 'board.getProduct() != this'
        //  : 만약, 새롭게 받아들인 게시글 board가 현재 Product 객체와 연결되어 있는 것이 아닌 기존에 어떤 다른 상품 Product 객체와
        //    연결되어 있는 상태라면,
        //- board.setProduct(this)
        //  : 자기 자신 board 객체로부터 자기 자신 board 객체와 기존에 연결되어 있는 어떤 다른 상품 Product 객체를 제거하고,
        //    자기 자신 board에 새롭게 현재 상품 Product 객체를 연결하는 것임

        // 아래 코드에 대한 뤼튼의 코멘트
        // : 아래 코드로 인해 Board 클래스 내부에서 product.setBoard(this); 호출될 가능성 있음.
        //   이 경우, 상호 호출로 인해 무한루프 발생 가능성 있음.

        if(board != null && board.getProduct() != this){
            board.setProduct(this);
        }


    }



    //==================================================================================================================



    @ManyToOne
    @JoinColumn(name="seller_id")
    //@JoinColumn(name="sellerId", referencedColumnName="sellerId")
    //'referencedColumnName' 관련해서 아래 두 링크 반드시 참조!
    //결론: @JoinColumn 사용할 떄, referencedColumnName은 보통 다 생략한다!
    //https://boomrabbit.tistory.com/217
    //https://resilient-923.tistory.com/416
    private Seller seller;


    //==================================================================================================================



    //< Order(N) : Product(1). N:1 양방향 연관관계. 주인객체는 Order 객체 >
    @OneToMany(mappedBy="product", cascade=CascadeType.ALL)
    private List<Ord> ordList = new ArrayList<>();


    //==================================================================================================================

    //*****중요*****
    //[ Enum 함수 ]

    //< 1. 작동 원리 >

    //*****중요*****
    //- Java에서 Enum 클래스는 내부적으로 클래스로 처리되므로 필드나 메소드를 가질 수 있습니다.
    //  Enum의 생성자 함수인  ProductStatus(String value, String code) 는
    //  enum 항목(PRD_SELLING, PRD_SOLDOUT)이 선언될 때 호출됩니다.
    //- 예를 들어, 아래에서의 'PRD_SELLING("1", "판매중")'에서 "1"은 code에 대입되고 "판매중"은 value에 대입됩니다.
    //  getCode()와 getValue() 메소드는 각각 code와 value 값을 반환합니다.

    //- 따라서, 예를 들어, 만약 아래에서 생성자를 아래처럼 추가적으로 변형시켜 작성했다면,
    //  ProductStatus(String status, String yujong, Strign value, String code){
    //      this.status = status;
    //      this.yujong = yujong;
    //      this.value = value;
    //      this.code = code;
    //  }
    //  ---> PRD_SELLING("1", "판매중") 이것의 형식도 바뀌어서
    //       PRD_SELLING("판매중 상태", "유종", "1", "판매중") 이런 형식과 같이 바뀌어야 되는 것이다!!
    //- 그리고, 그에 따라 필드도 private String status, private Strign yujong 이렇게 더 추가해야 하는 것임
    //- 그리고, 그 추가된 필드의 Getter도 추가해야 되는 것임!



    //< 2. 아래처럼 Product 엔티티 클래스 내부에 위치하는 enum 내부 클래스 ProductStatus 인 경우 DB 저장 형식 >

    /*
    - Product 테이블:
    productId(PK)  price	status(바로 이거!)	stock	leftStock	category	mainImage	sellerId(FK)
        1	        100	        1	              50	    45	       123	    image.jpg	    101
        2	        50	        2	              30	    30	       456	    image2.jpg	    102
        3	        80      	1	              20	    18	       789	    image3.jpg	    103

    - ProductStatus 테이블(내부 클래스로 정의되기 때문에 엔티티클래스가 아니기에 당연히 테이블 없음)
      : 이 테이블은 내부 클래스로 정의된 ProductStatus 열거형을 나타냅니다.
        이 테이블은 별도로 존재하지 않을 것이며, 'Product 테이블의 status 열에 값을 저장하는 방식을 사용'합니다.

    - Product 테이블의 컬럼 status에 ProductStatus 열거 enum 내부클래스의 값이 직접 저장됨.
      예를 들어, 아래 내부클래스에서 정의한 것처럼, '판매중'은 '1'로 저장되고, '매진'은 '2'로 저장됨.
      이러한 값은 Product 엔티티 클래스 내부에서 ProductStatus 열거 enum 내부클래스를 참조하여 사용되며,
      DB에 저장된 값과 매핑되는 것임.
      즉, 이 방식을 통해 enum 내부클래스의 값은 별도의 테이블을 사용하지 않고, 부모 엔티티 클래스의 컬럼에 직접 저장되므로,
      내부클래스로 정의된 enum 열거형을 처리할 수 있음.


     */

    /*
    [ EnumType.STRING 과 EnumType.ORDINAL ]

    1. EnumType.STRING
     - Enum의 실제 이름을 문자열 String으로 DB에 저장함.
       권고되는 방법.

             < Product 테이블 >
        +----+---------+-------------+
        | ID | NAME    |   STATUS    |
        +----+---------+-------------+
        | 1  | product1| PRD_SELLING |
        | 2  | product2| PRD_SOLDOUT |
        ----------------- -------------

     - Product 클래스의 외부 enum 클래스 ProductStatus를 참조하는 필드 ProductStatus (EnumType.STRING)
       PRD_SELLING -> PRD_SELLING
       PRD_SOLDOUT -> PRD_SOLDOUT


    2. EnumType.ORDINAL
     - Enum 값들이 선언된 순서대로 0부터 시작하는 정수로 데이터베이스에 저장됩니다.
       예를 들어, PRD_SELLING은 0, PRD_SOLDOUT은 1로 저장됩니다. 절대 사용하면 안됨!


           < Product 테이블 >
        +----+---------+-------+
        | ID |  NAME   | STATUS|
        +----+---------+-------+
        |  1 | product1|    0  |
        |  2 | product2|    1  |
        +----+---------+-------+

        ProductStatus (EnumType.ORDINAL)
        0 -> PRD_SELLING
        1 -> PRD_SOLDOUT

     */

    //*****중요***** JPA1-2_API 레퍼지터리 파일에서 domain 폴더의 Delivery.java 파일 확인하기!
    //이상적으로는,
    //1. 아래 내부 enum 클래스 ProductStatus를, 바깥으로 빼서 별도의 클래스로 만들고
    //   (단, 절대 엔티티 클래스가 아님. 그 클래스 위에는 아무것도 붙이지 않음),
    //2. 그 외부로 뺀 enum 클래스 ProductStatus를, 아래처럼
    //   여기 Product 엔티티 클래스의 내부에 필드로 선언하고 그 필드에 '@Enumerated(EnumType.STRING)'을 붙이는 것이 맞다!
    //   @Enumerated(EnumType.STRING)
    //   private ProductStatus productStatus;


    public enum ProductStatus{
        PRD_SELLING("1", "판매중"),
        PRD_SOLDOUT("2", "매진");

        private String value;
        private String code;


        //*****중요*****
        //- 여기서 만약 Pro
        ProductStatus(String value,String code){
            this.value = value;
            this.code = code;
        }

        public String getValue(){
            return value;
        }

        public String getCode(){
            return code;
        }



        //==================================================================================================================


        /*

        //[ 위에서 작성한 enum 내부 클래스 PrductStatus 를 외부 클래스 어딘가에서 호출할 때 등의 사용법 ]


        //< 0. 전제: ProductStatus 객체 생성 >

        // Product.ProductStatus productStatus = Product.ProductStatus.PRD_SELLING;
        // 근데 사실 이미 Product 클래스 내부에 필드 status 있어서, 여기서 임의로 만든 prodcutStatus는
        // 아래 예시들에서 사용 안했음.
        // *****중요*****
        // ---> Prodcut.ProductStatus.PRD_SELLING 의 결과값은 기본적으로 당연히 { "1", "판매중" } 이 나오게 되는 것임


        //< 1. 현재 판매 중인 상품의 상태 조회 >

        //Prodcut product = new Product();
        //product.setStatus(Product.ProductStatus.PRD_SELLING); //Product 엔티티의 status 필드(컬럼)에 '판매중' 값(행) 넣기.

        //System.out.println(product.getStatus().getValue()); //Product 엔티티의 status 필드(컬럼)의 Value 속성 출력하기.
        // ---> '판매중' 출력해줌.
        //내부 클래스나 enum을 참조할 때는 외부 클래스 이름(여기서는 Product 엔티티 클래스)을 함께 명시해야 한다!!


        //< 2. 현재 판매 중인 상품 정보 출력 >

        //Product product = new Product();
        //product.setStatus(Product.productStatus.PRD_SELLING);

        //public void printProductInfo(Product product){
        //      System.out.println("상태 " + product.getStatus().getValue());
        // }


        //< 3. 상품 상태 변경 >

        //Product produt = new Product();
        //product.setStatus(Product.productStatus.PRD_SELLING);

        //public void sellProduct(Product product){
        //      if(product.getLeftStock() == 0){
        //         product.setStatus(Product.ProductStatus.PRD_SOLDOUT);
        //         // Product 엔티티의 status 필드(컬럼)에 '매진' 값(행) 넣기.
        //      }
        //}


        < 4. >

        import team017.product.Entity.Product;
        import team017.product.Entity.Product.ProductStatus;

        public class Main {
            public static void main(String[] args) {


        // ProductStatus 열거형을 사용하여 제품 상태 설정
        ProductStatus sellingStatus = ProductStatus.PRD_SELLING;
        ProductStatus soldoutStatus = ProductStatus.PRD_SOLDOUT;


        // 열거형의 코드와 값을 가져오기
        String sellingCode = sellingStatus.getCode(); // "1"을 반환
        String soldoutCode = soldoutStatus.getCode(); // "2"를 반환
        String sellingValue = sellingStatus.getValue(); // "판매중"을 반환
        String soldoutValue = soldoutStatus.getValue(); // "매진"을 반환


        // 결과 출력
        System.out.println("판매 상태 코드: " + sellingCode);
        System.out.println("매진 상태 코드: " + soldoutCode);
        System.out.println("판매 상태 값: " + sellingValue);
        System.out.println("매진 상태 값: " + soldoutValue);
    }
}

         */

        /*

        [ 스프링 부트와 JPA에서의 Enum 활용]

        1. *****중요*****
        스프링 부트와 JPA에서 Enum은 주로 데이터베이스 엔티티의 필드 값으로 사용됩니다.
        이 경우, 해당 필드가 가질 수 있는 값들이 제한적이고 명확하게 정의되어야 할 때 유용합니다.

        예를 들어, 온라인 쇼핑몰에서 판매하는 상품의 상태를 나타내는 ProductStatus enum을 생각해봅시다.
        이 enum은 "판매중", "매진" 등 제한된 범위의 값만을 가질 것입니다.

        2.
        JPA(Entity Manager 또는 Repository)를 통해 데이터베이스로부터 엔티티 객체를 가져오거나 저장할 때,
        해당 엔티티 객체가 포함하고 있는 Enum 필드는 어떻게 처리될까요?

        기본적으로 JPA는 엔티티 객체 안에 포함된 모든 값을 데이터베이스 컬럼에 맞게 변환해서 저장하려고 시도합니다.
        그런데 문제는, Java의 Enum 타입과 SQL 데이터베이스간에 자동으로 맵핑되는 타입이 없다는 점입니다.

        그래서 JPA는 Enum 필드를 데이터베이스에 어떻게 저장할지를 결정하기 위해 두 가지 전략을 제공합니다:

        EnumType.ORDINAL: Enum 상수가 선언된 순서대로 0부터 시작하는 정수 값으로 데이터베이스에 저장됩니다.
        EnumType.STRING: Enum 상수의 이름 그대로 문자열 형태로 데이터베이스에 저장됩니다.

        위의 ProductStatus enum을 예로 들면, PRD_SELLING은 "판매중" 문자열 또는 0, PRD_SOLDOUT은 "매진" 문자열
        또는 1로 데이터베이스에 저장됩니다.


         */


    }


}
