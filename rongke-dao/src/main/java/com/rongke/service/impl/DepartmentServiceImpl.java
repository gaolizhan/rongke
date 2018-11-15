package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.DepartmentMapper;
import com.rongke.model.Department;
import com.rongke.service.DepartmentService;
import org.springframework.stereotype.Service;

/**
 * @DepartmentServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
}
