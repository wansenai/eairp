package com.wansenai.bo

import com.fasterxml.jackson.annotation.JsonFormat
import com.wansenai.NoArg
import lombok.Data

@NoArg
data class FileDataBO(

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var id: Long? = null,

    var uid: String? = null,

    var fileName : String,

    var fileUrl : String,

    var fileType : String? = null,

    var fileSize : Long? = null,
) {
    companion object {
        @JvmStatic
        @JvmOverloads
        fun builder(
            fileName: String,
            fileUrl: String,
            id: Long? = null,
            uid: String? = null,
            fileType: String? = null,
            fileSize: Long? = null
        ): FileDataBO {
            return FileDataBO(id, uid, fileName, fileUrl, fileType, fileSize)
        }
    }

}
