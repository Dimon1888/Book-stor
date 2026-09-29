package com.example.project.mapper;

import com.example.project.config.MapperConfig;
import com.example.project.dto.order.OrderItemResponseDto;
import com.example.project.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
