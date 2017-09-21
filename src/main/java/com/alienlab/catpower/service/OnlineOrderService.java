package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.OnlineOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Administrator on 2017/9/20.
 */
public interface OnlineOrderService {
    /**
     * 得到所有的课程
     * @param pageable
     * @return
     */
    Page<OnlineOrder> findAll(Pageable pageable);
}
