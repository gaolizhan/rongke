package com.rongke.service.impl;

import com.rongke.dto.MxDTO;
import com.rongke.service.ProcessCallbackResultsService;
import com.rongke.thread.MxTaskCallbackThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProcessCallbackResultsServiceImpl implements ProcessCallbackResultsService {
    @Autowired
    private MxTaskCallbackThread mxTaskCallbackThread;

    @Override
    public void MxTaskReportProcessor(MxDTO dto) {
        mxTaskCallbackThread.processCallbackResults(dto);
    }
}
