package com.monkey.common_module.pageable;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

public class CustomPageableResolver extends PageableHandlerMethodArgumentResolver {

    private static final List<Integer> ALLOWED_SIZES = List.of(10, 30, 50);
    private static final int DEFAULT_SIZE = 10;

    @Override
    public Pageable resolveArgument(
            MethodParameter methodParameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory) {

        Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();

        int finalSize = ALLOWED_SIZES.contains(size) ? size : DEFAULT_SIZE;

        return PageRequest.of(page, finalSize, pageable.getSort());
    }
}
