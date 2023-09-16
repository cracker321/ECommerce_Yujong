package yujong.ecommerce_yujong.참고사항;

public class RequestBody_ResponseBody {


// https://cheershennah.tistory.com/179
// https://wildeveloperetrain.tistory.com/144
//
//[ @RequestBody ] 요청 본문. 클라이언트 --> 서버

//클라이언트에서 서버로 필요한 데이터를 요청하기 위해 JSON 데이터를 요청 '본문(body)'에 담아서 서버로 보내면,
//서버에서는 @RequestBody 어노테이션을 사용하여 HTTP 요청 본문에 담긴 값들을 자바객체로 변환시켜, 객체에 저장한다.
//즉, 'HttpRequeset'의 본문인 requestBody의 내용'을 '자바 객체'로 '매핑'하는 역할
//'클라이언트의 요청'이 '@RequestBody가 붙어있는 메소드'로 올 경우, 'DispatcherServlet'에서 먼저 '해당 HttpRequest의 미디어타입을
//확인'하고, 해당 미디어타입에 맞는 'MessageConverter'를 통해, '요청 본문인 requestBody'를 통채로 변환해서, 해당 메소드로 전달해준다.
//cf) 'GET 메소드'의 요청 경우에는, '클라이언트의 요청 데이터'가 'HttpRequest'의 requestBody'로 전달되는 것이 아니라!,
//    'URL 또는 URI의 파라미터'로 전달되기 때문에, '@PathVariable' 또는 'RequestParam' 등의 어노테이션을 통해서
//    '클라이언트의 요청'을 전달받아야 한다.


//[ @ResponseBody ] 응답 본문. 서버 --> 클라이언트
//서버에서 클라이언트로 응답 데이터를 전송하기 위해 @ResponseBody 어노테이션을 사용하여 자바 객체를
//HTTP 응답 본문의 객체로 변환하여 클라이언트로 전송한다.
//즉, '자바 객체'를 'HttpResponse의 본문인 responseBody의 내용'으로 '매핑'하는 역할
//마찬가지로, '리턴 타입'에 맞는 'MessageConverter'를 통해 '리턴하는 객체'를 '해당 타입'으로 변환시켜서 '클라이언트'에게 전달해줌
//@RestController 를 붙인 컨트롤러의 경우, 따로 @ResponseBody를 명시하지 않아도,
//자동으로 'HttpResponse의 본문 responseBody'에 '자바 객체'가 '매핑'되어 전달된다.
}
