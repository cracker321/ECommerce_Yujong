package yujong.ecommerce_yujong.ord.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yujong.ecommerce_yujong.ord.dto.OrdPostDto;
import yujong.ecommerce_yujong.ord.dto.OrdResponseDto;
import yujong.ecommerce_yujong.ord.entity.Ord;
import yujong.ecommerce_yujong.ord.mapper.OrdMapper;
import yujong.ecommerce_yujong.ord.service.OrdService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
@Validated
public class OrdController {
    private final OrdService ordService;
    private final OrdMapper ordMapper;




    /* 주문 Ord 생성 Create */
    @PostMapping
    public ResponseEntity postOrd(@RequestBody @Valid OrdPostDto ordPostDto){

        OrdResponseDto response = ordService.createOrd(ordPostDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    /* 주문 Ord 삭제 Delete */
    @DeleteMapping("/{order_id}")
    public ResponseEntity deleteOrd(@PathVariable("order_id") @Positive Long ordId){

        ordService.deleteOrd(ordId);

        return new ResponseEntity<>("Success delete",HttpStatus.OK);

    }



    /* 주문 Ord 조회 Read */
    @GetMapping("/{order_id}")
    public ResponseEntity getOrd(@PathVariable("order_id") @Positive Long ordId){
        Ord ord= ordService.findVerifiedOrd(ordId);
        OrdResponseDto response = ordMapper.ordToOrdResponseDto(ord);

        log.info("redirect 가 전송됨");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



}