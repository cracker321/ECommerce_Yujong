package yujong.ecommerce_yujong.board.controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequestMapping("/boards")
@AllArgsConstructor //사용자 정의 생성자가 없는 경우에는 '기본 생성자'가 기본적으로 숨겨져 있지만,
                    //이렇게 @AllArgsConstructor를 사용하여 '모든 매개변수를 포함하는 생성자'를 만드는 경우,
                    //모든 매개변수를 포함하는 생성자도 어찌되었든 '사용자 정의 생성자'이고, 그에 따라
                    //아무 매개변수도 들어가 있지 않은 '기본 생성자'를 직접 'public BoardController(){}' 이렇게 생성하든가, 
                    //아니면, @NoArgsConstructor 를 어노테이션 붙여줘야 한다!
//@NoArgsConstructor: 매개변수가 없는 기본 생성자를 만들어줌.
//@RequiredArgsConstructor: //@RequiredArgsConsturctor 어노테이션을 사용하면,
                            //final로 선언된 필드나 @NonNull 어노테이션을 사용한 필드만을 필요로 하는 생성자를 만들어준다!

//cf) '회원 엔티티 Member', '배송 엔티티 Delivery'와 같은 엔티티 객체들은 컨트롤러, 서비스, 레펏 등 여기저기에 다 걸쳐가며
//    왔다갔다하며 사용되는 것이고, 따라서 이 엔티티 객체들은 의존성 주입의 대상이 아님!
//    의존성 주입의 대상은 대부분 Service, Repository 이다!!
public class BoardController {

    //게시글 등록
}
