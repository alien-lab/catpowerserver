package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.OnlineOrder;
import com.alienlab.catpower.service.OnlineOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/9/20.
 */
@Service
@Transactional
public class OnlineOrderServiceImpl implements OnlineOrderService {
    @Autowired


    @Override
    @Transactional(readOnly = true)
    public Page<OnlineOrder> findAll(Pageable pageable) {
        log.debug("Request to get all OnlineOrders");
        Page<OnlineOrder> result = onlineOrderRepository.findAll(pageable);
        return result;
    }
}
