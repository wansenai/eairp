import com.wansenai.NoArg

@NoArg
data class CustomerDTO (
    val customerName: String? = null,

    val contact: String? = null,

    val phoneNumber: String? = null,

    val page: Long? = null,

    val pageSize: Long? = null,

    val startDate: String? = null,

    val endDate: String? = null,
)
