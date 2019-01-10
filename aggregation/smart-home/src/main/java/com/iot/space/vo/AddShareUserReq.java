package com.iot.space.vo;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lucky
 * @ClassName AddShareUserReq
 * @Description TODO
 * @date 2019/1/2 14:48
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class AddShareUserReq implements Serializable {

    @NotNull(message = "homeId.notnull")
    private Long homeId;

    @NotEmpty(message = "userName.not.null")
    private String userName;

    @NotEmpty(message = "toAccount.not.null")
    private String toAccount;
}
