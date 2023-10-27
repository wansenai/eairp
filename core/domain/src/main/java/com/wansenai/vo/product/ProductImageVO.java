package com.wansenai.vo.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ProductImageVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productImageId;

    private String type;

    private String imageName;

    private String imageUrl;
}
