package yujong.ecommerce_yujong.comment.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final CommentRepository commentRepository;
    private final BoardService boardService;





    /* 댓글 Comment 조회 Read */
    public Comment findComment(Long commentId){

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment findComment = optionalComment
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));

        return findComment;

    }




    /* 댓글 Comment 등록 Create */
    public Comment createComment(Comment comment, long memberId){

        //댓글 Comment 를 작성하려고 하는 회원 Member 가 현재 DB에 존재하는 회원 Member 인지 여부 확인
        comment.setMember(memberService.findVerifiedMember(memberId));
        verifiedMember(comment);
        comment.setBoard(boardService.findVerifiedBoard(comment.getBoard().getBoardId()));
        verifiedBoard(comment);

        return commentRepository.save(comment);

    }


    /* 문의댓글 Comment를 남긴 사람이 DB에 존재하는 회원 Member인지 여부 확인 */
    private void verifiedMember(Comment comment){

        commentRepository.findById(comment.getMember().getMemberId());

    }




    /* 문의댓글 Comment가 달려 있는 게시글 Board가 DB에 존재하는 게시글 Board 인지 여부 확인 */

    private void verifiedBoard(Comment comment){

        commentRepository.findById(comment.getBoard().getBoardId());
    }



//====================================================================================================================



    /* 댓글 Comment 수정 Update */
    public Comment updateComment(Comment comment, Long memberId){

        Comment foundComment = findComment(comment.getCommentId());

        verifyWriter(foundComment.getMember().getMemberId(), memberId);


        Optional.ofNullable(comment.getContext()).ifPresent(context -> foundComment.setContext(context));


        return commentRepository.save(comment);


    }


    /* 문의댓글 Comment의 원 작성자 originalCommentId 와 수정 희망자 editCommenterId 가 동일한지 여부를 확인 */
    private void verifyWriter(Long originalCommenterId, Long editCommenterId){

        if(!originalCommenterId.equals(editCommenterId)){
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_MEMBER);
        }
    }





    /* 댓글 Comment 삭제 Delete */
    public void deleteComment(Long memberId, Long commentId){

        Comment foundComment = findComment(commentId);

        Long originalCommenterId = foundComment.getMember().getMemberId();

        verifyWriter(originalCommenterId, memberId);

        commentRepository.delete(foundComment);


    }




    /* 전체 문의댓글 Comment 페이징 Paging */

    public Page<Comment> findComments(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }



    /* 게시글 하나당 달려 있는 문의댓글 Comment 을 페이징 Paging 으로 조회 Read */
    public Page<Comment> findCommentByBoard(Long boardId, int page, int size) {
        return commentRepository.findByBoard_BoardId(boardId, PageRequest.of(page, size, Sort.by("commentId").descending()));
    }

}
