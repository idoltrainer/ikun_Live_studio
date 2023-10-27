package com.idoltrainer.webvideo.model.dto.user;

import com.idoltrainer.webvideo.model.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserSearchDTO extends PageQuery {

    /**
     * 查找内容
     */
    private String searchText;
}
