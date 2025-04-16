
package com.docapproval.api.controller;

import com.docapproval.api.dto.approvalRequest.ApprovalRequestCreateDto;
import com.docapproval.api.dto.login.LoginCreateDto;
import com.docapproval.api.response.common.ErrorModel;
import com.docapproval.api.response.user.LoginModel;
import com.docapproval.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Login", description = "로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/login", produces = "application/json")
public class LoginController {

    private final UserService userService;

    @PostMapping("")
    @Operation(summary = "로그인", description = "로그인 (사용자 이름이 데이터에 없을경우 사용자 생성)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "로그인 데이터",
                    content = @Content(schema = @Schema(implementation = LoginCreateDto.class))
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = LoginModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity<Object> login(
            @RequestBody @Validated LoginCreateDto loginCreateDto) {

        LoginModel loginModel = userService.getUserInfoOrCreate(loginCreateDto);
        if (null == loginModel) {
            return userService.getErrorResponse();
        }

        return new ResponseEntity<>(loginModel, HttpStatus.OK);
    }
}
