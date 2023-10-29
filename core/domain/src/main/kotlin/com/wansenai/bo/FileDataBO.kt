package com.wansenai.bo

import com.fasterxml.jackson.annotation.JsonFormat
import com.wansenai.NoArg
import lombok.Data

@Data
@NoArg
data class FileDataBO(

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var id: Long? = null,

    var uid: String? = null,

    var fileName : String,

    var fileUrl : String,

    var fileType : String,

    var fileSize : Long? = null,
)
