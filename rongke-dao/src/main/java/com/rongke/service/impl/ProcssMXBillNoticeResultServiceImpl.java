package com.rongke.service.impl;

import com.rongke.dto.MxDTO;
import com.rongke.service.ProcssMXBillNoticeResultService;
import com.rongke.thread.ProcssMXBillNoticeResultThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProcssMXBillNoticeResultServiceImpl implements ProcssMXBillNoticeResultService {
    @Autowired
    private ProcssMXBillNoticeResultThread procssMXBillNoticeResultThread;
    @Override
    public void mxBillNoticeResultProcess(MxDTO dto) {
        procssMXBillNoticeResultThread.processCallbackResults(dto);
    }
}
