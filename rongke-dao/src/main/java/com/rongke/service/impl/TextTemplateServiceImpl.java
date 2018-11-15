package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.TextTemplateMapper;
import com.rongke.model.TextTemplate;
import com.rongke.service.TextTemplateService;
import org.springframework.stereotype.Service;

/**
 * @TextTemplateServiceImpl
 * @文本模板ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class TextTemplateServiceImpl extends ServiceImpl<TextTemplateMapper, TextTemplate> implements TextTemplateService {
}
