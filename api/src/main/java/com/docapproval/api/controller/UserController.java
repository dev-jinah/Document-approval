package com.docapproval.api.controller;

import com.docapproval.api.dto.login.LoginCreateDto;
import com.docapproval.api.entity.UserEntity;
import com.docapproval.api.response.common.ErrorModel;
import com.docapproval.api.response.user.UserModel;
import com.docapproval.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "User", description = "사용자(신청자/결재자)")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    @Operation(summary = "사용자 생성", description = "사용자 이름이 데이터에 없을경우 사용자 생성",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 생성 데이터",
                    content = @Content(schema = @Schema(implementation = LoginCreateDto.class))
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CREATED", content = @Content(schema = @Schema(implementation = UserModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity<Object> userCreate(
            @RequestBody @Validated LoginCreateDto loginCreateDto) {

        UserEntity user = userService.createUser(loginCreateDto);
        if (null == user) {
            return userService.getErrorResponse();
        }

        return new ResponseEntity<>(new UserModel(user), HttpStatus.CREATED);
    }
    // 사용자 리스트 조회
    @GetMapping("")
    @Operation(summary = "사용자 정보 전체 조회", description = "사용자 정보 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserModel.class)))),
    })
    public ResponseEntity listUsers(){

        List<UserModel> users = userService.getAllUsers();
        if (null == users) {
            return userService.getErrorResponse();
        }

        return ResponseEntity.ok(users);
    }

    // 사용자 정보 조회 by userId
    @GetMapping("{userId:^[0-9]+$}")
    @Operation(summary = "사용자 정보 조회 by 사용자 Id", description = "사용자 id로 사용자 정보 조회",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "사용자 id", description = "사용자 id", required = true)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = UserModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity getUser(
            @PathVariable(name = "userId") Long userId) {

        UserEntity user = userService.getUserById(userId);
        if (null == user) {
            return userService.getErrorResponse();
        }

        return ResponseEntity.ok(new UserModel(user));
    }

    // 사용자 정보 조회 by userName
    @GetMapping("{userName:^.*\\D+.*$}")
    @Operation(summary = "사용자 정보 조회 by 사용자 name", description = "사용자 name 으로 사용자 정보 조회",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "사용자 name", description = "사용자 name", required = true)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = UserModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity getUser(
            @PathVariable(name = "userName") String userName) {

        UserEntity user = userService.getUserByName(userName);
        if (null == user) {
            return userService.getErrorResponse();
        }

        return ResponseEntity.ok(new UserModel(user));
    }
}
