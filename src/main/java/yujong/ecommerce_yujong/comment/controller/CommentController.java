package yujong.ecommerce_yujong.comment.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yujong.ecommerce_yujong.comment.dto.CommentPatchDto;
import yujong.ecommerce_yujong.comment.dto.CommentPostDto;
import yujong.ecommerce_yujong.comment.entity.Comment;
import yujong.ecommerce_yujong.comment.mapper.CommentMapper;
import yujong.ecommerce_yujong.comment.service.CommentService;
import yujong.ecommerce_yujong.global.response.MultiResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("comments")
@AllArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;




    /* 댓글 Comment 등록 Create */
    @PostMapping()
    public ResponseEntity postComment(@Valid @RequestBody CommentPostDto commentPostDto) {

        Comment comment = commentService.createComment(
                commentMapper.commentPostDtoToComment(commentPostDto), commentPostDto.getMemberId());

        return new ResponseEntity<>((commentMapper.commentToCommentResponseDto(comment)), HttpStatus.CREATED);
    }




    /* 댓글 Comment 조회 Read */
    @GetMapping("/{board_Id}")
    public ResponseEntity getComment(@PathVariable("board_Id") @Positive Long boardId,
                                     @Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {

        Page<Comment> commentPage = commentService.findCommentByBoard(boardId,page - 1, size);

        List<Comment> commentList = commentPage.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(commentMapper.commentToCommentResponseDtos(commentList),
                        commentPage), HttpStatus.OK);
    }





    /* 댓글 Comment 수정 Update */
    @PatchMapping("/{comment_Id}")
    public ResponseEntity patchComment(@PathVariable("comment_Id") @Positive Long commentId,
                                       @Valid @RequestBody CommentPatchDto commentPatchDto) {

        commentPatchDto.setCommentId(commentId);

        Comment comment = commentService.updateComment(
                commentMapper.commentPatchDtoToComment(commentPatchDto), commentPatchDto.getMemberId());

        return new ResponseEntity<>(commentMapper.commentToCommentResponseDto(comment), HttpStatus.OK);
    }





    /* 댓글 Comment 삭제 Delete */
    @DeleteMapping("/{comment_Id}")
    public ResponseEntity deleteComment(@PathVariable("comment_Id") @Positive Long commentId,
                                        @Positive @RequestParam Long memberId) {


        commentService.deleteComment(commentId, memberId);

        String message = "Success!";

        return ResponseEntity.ok(message);
    }

}