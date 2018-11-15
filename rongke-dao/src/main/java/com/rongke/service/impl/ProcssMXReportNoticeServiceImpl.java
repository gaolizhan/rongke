package com.rongke.service.impl;

import com.rongke.dto.MxDTO;
import com.rongke.service.ProcssMXReportResultService;
import com.rongke.thread.ProcssMXBillNoticeResultThread;
import com.rongke.thread.ProcssMXReportResultThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProcssMXReportNoticeServiceImpl implements ProcssMXReportResultService {

    @Autowired
    private ProcssMXReportResultThread procssMXReportResultThread;

    @Override
    public void mxBillReportResultProcess(MxDTO dto) {
        procssMXReportResultThread.processCallbackResults(dto);
    }

}
