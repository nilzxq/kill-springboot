package com.debug.kill.server.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author nilzxq
 * @Date 2020-06-14 11:02
 */
@Data
@ToString
public class KillDto implements Serializable {

    @NotNull
    private Integer killId;

    private Integer userId;


}
