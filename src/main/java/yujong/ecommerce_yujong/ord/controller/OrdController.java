package yujong.ecommerce_yujong.ord.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yujong.ecommerce_yujong.ord.dto.OrdPostDto;
import yujong.ecommerce_yujong.ord.mapper.OrdMapper;
import yujong.ecommerce_yujong.ord.service.OrdService;

import javax.validation.Valid;


@RequestMapping("/orders")
@Validated
@RequiredArgsConstructor
@RestController
public class OrdController {


    private final OrdService ordService;
    private final OrdMapper ordMapper;



//==================================================================================================================

    //[ 주문 Ord 생성 Create ]

    @PostMapping
    public ResponseEntity postOrd(@RequestBody @Valid OrdPostDto ordPostDto){





    }





//==================================================================================================================




//==================================================================================================================





//==================================================================================================================




//==================================================================================================================














}
