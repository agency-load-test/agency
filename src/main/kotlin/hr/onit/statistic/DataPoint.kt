package hr.onit.statistic

import java.time.LocalDateTime

data class DataPoint(val time : LocalDateTime, val description :  String, val value : Double )
