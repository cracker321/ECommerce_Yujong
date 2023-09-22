package yujong.ecommerce_yujong.참고사항;

public class 페이징Paging {



            /*

    [ 댓글 Comment 페이징 Paging ]

    < 순서1) CommentController 클래스의 메소드 getComment 에서 페이징 처리를 시작함 >

    //댓글 Comment 조회 Read

    @GetMapping("/{board-Id}")
    public ResponseEntity getComment(@PathVariable("board-Id") @Positive Long boardId,
                                     @Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {

        Page<Comment> commentPage = commentService.findCommentByBoard(boardId,page - 1, size);

        List<Comment> commentList = commentPage.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(commentMapper.commentToCommentResponseDtos(commentList),
                        commentPage), HttpStatus.OK);
    }


    - 'Page<Comment> commentPage = commentService.findCommentByBoard(boardId,page - 1, size);'
      : 클라이언트가 전달하여 매개변수 인자로 들어온 boardId를 통해, 클라이언트가 찾고자 하는 게시글 Board 를 찾고,
        그 게시글 Board에 달려 있는 댓글들을 '페이지 단위, 즉 Page 객체에 담아서(Page 객체 단위로)' 가져옴.
        즉, 댓글들을 페이지 단위로 조회하고, 그 결괏값(달려있는 댓글들)을 'Page<Comment> 객체 형식'으로 가져옴.
        어찌됐든, '반환되는 타입은 Page 타입 객체'인 상태임.

    - 'List<Comment> commentList = commentPage.getContent();
      : 위에서 가져온 'Page<Comment> 타입 객체'에서 '실제 댓글 Comment 객체'만을 추출하여
        List 형태로 commentList 변수에 담는 것임.
        '인터페이스 Page<T> 의 내장 메소드 getContent()'는 'Page 타입 객체에 담긴 댓글들 Comment들'에서
        실제 데이터인 '댓글들 Comment들 객체'를 추출하여 List 형태로 변환까지 자동으로 시켜주고,
        그 List<T>(여기서는 'List<Comment>') 를 반환해주는 메소드임.









    < 순서2) CommentService 클래스의 메소드 findCommentByBoard 에서 댓글 페이징 처리 >

    # CommentService의 메소드 findCommentByBoard
    // 게시글 하나당 달려 있는 댓글 Comment 을 페이징 Paging 으로 조회 Read

    public Page<Comment> findCommentByBoard(Long boardId, int page, int size) {
        return commentRepository.findByBoard_BoardId(boardId, PageRequest.of(page, size, Sort.by("commentId").descending()));
    }



    -'PageRequest': JPA 내장 클래스로, 인터페이스 Pageable의 구현체임. 페이지네이션의 요청 정보를 담고 있음.
    - 'of(page, size)': 내장 클래스 PageRequest 의 내부 정적 메소드로, 주어진 페이지 번호(page)와
                        그 페이지 내부에 포함시킬 데이터(댓글 또는 게시글 등등)를 몇 개까지 포함시킬지(size) 등의 조건에 맞는
                        PageRequest 객체를 생성함. 페이지 번호는 0부터 시작함.
                        e.g) .of(1, 10)
                             : 두 번째 페이지에 10개의 데이터(댓글 또는 게시글)를 담아 반환해라!
    - 'Sort.by("commentId").descending(): commentId 필드의 값을 기준으로 내림차순 정렬하겠다는 의미.
                                          오름차순 원하면 ascending() 입력.

    - 'return commentRepository.findByBoard_BoardId(boardId, PageRequest.of(page, size, Sort.by("commentId").descending()));'
      : 클라이언트가 전달하여 매개변수 인자로 들어온 boardId를 통해, 클라이언트가 찾고자 하는 게시글 Board 를 찾고,
        그 게시글 Board에 달려 있는 댓글들을 원하는 페이지 번호(page)와 그 안에 포함시키길 원하는 댓글의 수(size)에 맞게
        필드 commentId의 값 기준으로 내림차순 정렬된 결과를 반환하는 것임.



    PageRequest 객체
    |
    |-- page (어떤 페이지인지 지정)
    |-- size (한 페이지에 어떤 크기의 데이터가 들어가는지 지정)
    |-- sort (어떻게 데이터를 정렬할 것인지 지정)
        |
        |-- direction (오름차순/내림차순)
        |-- properties (정렬 기준이 되는 필드들)









    < 순서3) CommentRepository 에서 댓글 페이징 처리 >


    # 레펏 CommentRepository 의 메소드(아래는 사용자 정의 쿼리메소드) findByBoard_BoardId

    public Page<Comment> findByBoard_BoardId(Long boardId, Pageable pageable);



    *****중요*****
    - 사용자 정의 쿼리 메소드임. SQL문으로 바꾸면 아래와 같음.
      SELECT * FROM comment WHERE board_id = ? ORDER BY comment_id DESC LIMIT ?, ?

      예를 들어,
      SELECT * FROM comment WHERE board_id = 10 ORDER BY comment_id DESC LIMIT 0, 10 은
      '게시글 아이디 boardId 가 10인 게시글 Board 에 달린 댓글들 중 첫 페이지 데이터(즉, 상위 20개 댓글)를
      컬럼(필드) commentId 기준으로 내림차순해서 가져와라' 라는 의미임.

      'LIMIT'의 첫 번째 '?': 첫 번째 값(Offset). 몇 번째 페이지의 내부 데이터(댓글 등)를 가져올지 지정하는 것.
                            기본값은 0부터 시작하기에, 첫 번째 페이지부터 그 페이지 내부 데이터 가져오길 원하면,
                            0으로 지정하면 됨.
                            e.g) PageRequest.of(0, 10)
                                 : 첫 번째 페이지의 그 페이지 내부 데이터(댓글 등)를 10개씩 가져오게 됨.
      'LIMIT'의 두 번째 '?': 두 번째 값. 페이지 하나당 보여줄 그 페이지 내부 데이터(댓글 등)의 개수.


    - 'Page<Comment>':
    - 'findByBoard_BoardId'
      : 쿼리메소드 이름이자, 이 자체로 DB에 날릴 SQL쿼리를 담고 있음.
      * 'findBy': SQL에서 SELECT를 의미함. 쿼리메소드에서 SELECT 쿼리를 시작하는 역할을 하는 키워드임.
      * 'Board': Board 테이블을 SELECT 한다는 의미임. WHERE 조건 절의 일부로 해석되는 것임.
      * 'BoardId': Board 테이블의 BoardId 컬럼의 데이터(행, 라인)를 찾는다는 것임.
    - '매개변수 Long boardId'
      : 위 쿼리메소드 WHERE 절의 '조건 값'으로 사용되는 것임.
        즉, 게시글 아이디 Board ID 가 10인 게시글(즉, 게시글 번호가 10인 게시글)을 조건값으로 줄테니 그 10번 게시글을 선택하라는 의미임.
    - '매개변수 Pageable pageable'
      : JPA 내장 인터페이스 Pageable. 페이지네이션 및 정렬에 관한 정보를 담고 있음.
        이 내부에 내장 클래스 PageRequest 를 가지고 있음.







    < 순서4) PageInfo 클래스 생성 >

    - JPA 내장 클래스 아님!! 개발자가 직접 만든 클래스임!

    @Data
    @AllArgsConstructor
    public class PageInfo {

        private int page; //현재 페이지 번호
        private int size; //페이지당 댓글 수
        private long totalElements; //전체 댓글 수
        private int totalPages; //전체 페이지 수
    }


    - 페이징 처리를 수행한 후의 결과 데이터와 페이지 정보를 클라이언트에게 전달하기 위한 객체임.
      페이지네이션 정보를 관리하는 클래스임.
      JPA의 내장 인터페이스 Page 객체로부터 추출되며, 클라이언트가 요청한 페이지의 정보와 전체 데이터에 대한 정보를 포함하고 있음.
    - PageInfo 클래스에 대한 객체 생성은 MultiResponseDto의 생성자에서 이뤄지고 있음.
      그리고 이 과정에서 JPA의 내장 인터페이스인 Page 객체를 사용하고 있음.
      Page 객체는 페이징 처리된 결과를 담고 있는 객체임.
      Page 객체에는 아래 정보들이 담겨져 있음.

            현재 페이지 번호(page.getNumber())
            한 페이지당 데이터 개수(page.getSize())
            전체 데이터 수(page.getTotalElements())
            전체 페이지 수(page.getTotalPages())









    < 순서5) MultiResponoseDto<T> 클래스 생성 >

    @Data
    @AllArgsConstructor
    public class MultiResponseDto<T> {

        private List<T> data; //응답 데이터 목로을 담는 필드
        private PageInfo pageInfo; //페이지 정보를 담는 필드


        //기본값으로는 페이지 번호가 0부터 시작하기 때문에, 그것을 1부터 시작하는 것으로 변경
        public MultiResponseDto(List<T> data, Page page) {
            this.data = data;
            this.pageInfo = new PageInfo(page.getNumber()+1,
                    page.getSize(), page.getTotalElements(), page.getTotalPages());
        }
    }


    - 여러 개의 다른 종류의 데이터타입 객체 및 그에 대한 페이지 정보도 함께 클라이언트에게 반환하기 위해 사용됨.
      즉, MultiResponseDto 객체는 최종적으로 클라이언트에게 넘겨주는 객체임!!

    - 'private List<T> data': 클라이언트에게 실제 반환해주려는 데이터 목록 List<T>. 모든 데이터타입을 다 수용할 수 있음.
    - 'private PageInfo pageInfo': 이 MultiResponseDto<T> 객체 내에 PageInfo 객체 자체를 담음(포함시킴).
                                   PageInfo 객체에는 1) 현재 페이지 번호, 2) 한 페이지당 데이터 개수,
                                   3) 전체 데이터 개수, 4) 전체 페이지 개수 등의 페이징 관련 정보가 포함되어 있음.

    - 'page.getNumber()+1': 페이지 번호가 0부터 시작하는 것(기본값)을 1부터 시작하도록 조정한 것임.



     */






}
