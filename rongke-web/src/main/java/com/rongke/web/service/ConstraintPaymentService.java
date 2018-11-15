package com.rongke.web.service;

import java.math.BigDecimal;

public interface ConstraintPaymentService {
    String payment(Long loanId, String payment) throws Exception;
}
