package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.BackupCopyMapper;
import com.rongke.model.BackupCopy;
import com.rongke.service.BackupCopyService;
import org.springframework.stereotype.Service;

/**
 * @BackupCopyServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class BackupCopyServiceImpl extends ServiceImpl<BackupCopyMapper, BackupCopy> implements BackupCopyService {
}
