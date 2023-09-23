package yujong.ecommerce_yujong.board.controller;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yujong.ecommerce_yujong.board.dto.BoardPatchDto;
import yujong.ecommerce_yujong.board.dto.BoardPostDto;
import yujong.ecommerce_yujong.board.dto.BoardResponseDto;
import yujong.ecommerce_yujong.board.dto.BoardTotalResponseDto;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.board.mapper.BoardMapper;
import yujong.ecommerce_yujong.board.service.BoardService;
import yujong.ecommerce_yujong.global.response.MultiResponseDto;

import javax.validation.constraints.Positive;
import java.util.List;

@Data
@RequestMapping("/boards")
@RequiredArgsConstructor
@RestController
public class BoardController {


    private final BoardService boardService;
    private final BoardMapper boardMapper;





    /* 게시글 Board 등록 Create */
    @PostMapping()
    public ResponseEntity postBoard(@RequestBody BoardPostDto boardPostDto){

        BoardResponseDto response = boardService.createBoard(boardPostDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }





    /* 게시글 Board 수정 Update */
    @PatchMapping("/{board_id}")
    public ResponseEntity patchBoard(@PathVariable("board_id") long boardId,
                                     @RequestBody BoardPatchDto boardPatchDto){

        BoardResponseDto response = boardService.updateBoard(boardPatchDto, boardId);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    /* 게시글 Board 삭제 Delete */
    @DeleteMapping("/{board_id}")
    public ResponseEntity deleteBoard(@PathVariable("board_id") long boardId){

        boardService.deleteBoard(boardId);

        return new ResponseEntity<>("Removal Success", HttpStatus.OK);
    }






    /* 단일 게시글 Board 조회 Read */
    @GetMapping("/{board_id}")
    public ResponseEntity getBoard(@PathVariable("board_id") long boardId,
                                   BoardResponseDto baordResponseDto){

        Board findBoard = boardService.findVerifiedBoard(boardId);

        BoardResponseDto boardResponseDto = boardMapper.boardToBoardResponseDto(findBoard);

        return new ResponseEntity<>(boardResponseDto, HttpStatus.OK);

    }





    /* 전체 게시글 Board 조회 Read */
    @GetMapping()
    public ResponseEntity GetBoards(@Positive @RequestParam int page,
                                    @Positive @RequestParam int size) {

        Page<Board> boardsPage = boardService.findBoards(page - 1, size);
        List<Board> boardList = boardsPage.getContent();
        List<BoardTotalResponseDto> response = boardService.getBoards(boardList);

        return new ResponseEntity<>(
                new MultiResponseDto<>(response, boardsPage), HttpStatus.OK);
    }

}
