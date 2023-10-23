package com.wansensoft.dto.product;

import lombok.Data;

@Data
public class ProductImageDTO {

    private Long productImageId;

    private String uid;

    private String type;

    private String status;

    private String imageName;

    private String imageUrl;

    private Integer imageSize;
}
