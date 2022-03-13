package com.zs.convert;

import com.zs.entity.User;
import com.zs.vo.UserVO;
import org.mapstruct.Mapper;

/**
 * @Author: zs
 * @Date: Created in 2022/3/12
 * @Description:
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

    UserVO convertToVO(User user);

}
