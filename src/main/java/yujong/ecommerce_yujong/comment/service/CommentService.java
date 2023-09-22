package yujong.ecommerce_yujong.comment.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.board.service.BoardService;
import yujong.ecommerce_yujong.comment.entity.Comment;
import yujong.ecommerce_yujong.comment.repository.CommentRepository;
import yujong.ecommerce_yujong.global.exception.BusinessLogicException;
import yujong.ecommerce_yujong.global.exception.ExceptionCode;
import yujong.ecommerce_yujong.member.service.MemberService;

import java.util.Optional;


@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class CommentService {

    private final MemberService memberService;
    //회원가입만 했고, 구매하지 않은(Customer가 아닌) 회원(Member)도 코멘트를 남길 수 있어야 하기 때문에 MemberService를 의존성주입해욤.

    private final CommentRepository commentRepository;
    private final BoardService boardService;





//====================================================================================================================



    //[ 댓글 Comment 조회 Read ]


    public Comment findComment(Long commentId){

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment findComment = optionalComment.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));

        return findComment;

    }


//====================================================================================================================


    //[ 댓글 Comment 등록 Create ]

    //클라언트로부터 넘어온 댓글 Comment 작성 Create 요청
    public Comment createComment(Comment comment, long memberId){

        //댓글 Comment 를 작성하려고 하는 회원 Member 가 현재 DB에 존재하는 회원 Member 인지 여부 확인
        comment.setMember(memberService.findVerifiedMember(memberId));
        verifiedMember(comment); // 클라이언트의 댓글 Comment 작성 요청에 있는 회원 Member가 DB에 존재하는 회원인지 여부만 그냥 확인하는 것.
        comment.setBoard(boardService.findVerifiedBoard(comment.getBoard().getBoardId()));
        verifiedBoard(comment); // 클라이언트의 댓글 Comment 작성 요청에 있는 게시글 Board가 실제 DB에 존재하는 게시글인지 여부만 확인하는 것.

        return commentRepository.save(comment);

    }



    //[ 클라이언트로부터 넘어온 해당 댓글 Comment 를 작성하려고 하는 회원 Member가 DB에 존재하는 회원 Member 인지 여부만 그냥 확인하는 것 ]

    private void verifiedMember(Comment comment){

        commentRepository.findById(comment.getMember().getMemberId());
        //반환값: DB에 클라이언트로부터 넘어온 해당 댓글 Comment를 작성하려고 하는 회원 Member가 없으면 null,
        //       있으면 Optional 객체로 감싸진 해당 회원 Member 객체가 반환됨.


    }


    //[ 클라언트로부터 넘어온 해당 댓글 Comment가 달려 있는 게시글 Board가 DB에 존재하는 게시글 Board 인지 여부만 그냥 확인하는 것 ]

    private void verifiedBoard(Comment comment){

        commentRepository.findById(comment.getBoard().getBoardId());
        //반환값: DB에 클라이언트로부터 넘어온 해당 댓글 Comment가 달려 있는 게시글 Board가 없으면 null,
        //       있으면 Optional 객체로 감싸진 해당 회원 Member 객체가 반환됨
    }



//====================================================================================================================



    //[ 댓글 Comment 수정 Update ]

    public Comment updateComment(Long commentId, Comment comment){

        Comment foundComment = findComment(comment.getCommentId());

        foundComment.set

    }




    //[ 댓글 Comment 작상자 postUserId 와 수정자 editUserId 의 id가 같은지 여부를 확인한 하는 메소드 ]

    private void verifyWriter(Long postUserId, Long editUserId){

        if(!postUserId.equals(editUserId)){
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_MEMBER);
        }
    }




//====================================================================================================================




//====================================================================================================================



//====================================================================================================================




//====================================================================================================================




//====================================================================================================================




//====================================================================================================================




}
