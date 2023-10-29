package com.wansenai.bo

import com.wansenai.NoArg
import lombok.Data

@Data
@NoArg
data class FileDataBO(

    var id: Long? = null,

    var fileName : String,

    var fileUrl : String,

    var fileType : String,

    var fileSize : Long? = null,
)
