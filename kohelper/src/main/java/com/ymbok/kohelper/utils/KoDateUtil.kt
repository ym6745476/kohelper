package com.ymbok.kohelper.utils
import java.lang.Exception
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object KoDateUtil {

    /** 时间日期格式化到年月日时分秒.  */
    const val dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss"

    /** 时间日期格式化到年月日.  */
    const val dateFormatYMD = "yyyy-MM-dd"

    /** 时间日期格式化到年月.  */
    const val dateFormatYM = "yyyy-MM"

    /** 时间日期格式化到年月日时分.  */
    const val dateFormatYMDHM = "yyyy-MM-dd HH:mm"

    /** 时间日期格式化到月日时分.  */
    const val dateFormatMDHM = "MM-dd HH:mm"

    /** 时间日期格式化到月日.  */
    const val dateFormatMD = "MM/dd"

    /** 时分秒.  */
    const val dateFormatHMS = "HH:mm:ss"

    /** 时分.  */
    const val dateFormatHM = "HH:mm"

    /** 上午.  */
    const val AM = "AM"

    /** 下午.  */
    const val PM = "PM"

    /**
     * Date format pattern used to parse HTTP date headers in RFC 1123 format.
     * Fri, 26 Jun 2015 02:17:54 GMT
     */
    const val PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz"

    /**
     * Date format pattern used to parse HTTP date headers in RFC 1036 format.
     */
    const val PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz"

    /**
     * Date format pattern used to parse HTTP date headers in ANSI C
     * `asctime()` format.
     */
    const val PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy"

    /**
     * String类型的日期时间转化为Date类型.
     * @param strDate String形式的日期时间
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return Date Date类型日期时间
     */
    fun getDateByFormat(strDate: String, format: String): Date? {
        val simpleDateFormat = SimpleDateFormat(format)
        var date: Date? = null
        try {
            date = simpleDateFormat.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    /**
     * String类型的日期时间转化为Date类型.
     *
     * @param strDate String形式的日期时间
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return Date Date类型日期时间
     */
    fun getDateByFormat(strDate: String?, format: String?, locale: Locale?): Date? {
        val mSimpleDateFormat = SimpleDateFormat(format, locale)
        var date: Date? = null
        try {
            date = mSimpleDateFormat.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    /**
     * 获取偏移之后的Date.
     * @param date 日期时间
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset 偏移(值大于0,表示+,值小于0,表示－)
     * @return Date 偏移之后的日期时间
     */
    fun getDateByOffset(date: Date?, calendarField: Int, offset: Int): Date? {
        val c: Calendar = GregorianCalendar()
        try {
            c.time = date
            c.add(calendarField, offset)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return c.time
    }

    /**
     * 获取指定日期时间的字符串(可偏移).
     *
     * @param strDate String形式的日期时间
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset 偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    fun getStringByOffset(strDate: String?, format: String?, calendarField: Int, offset: Int): String? {
        var mDateTime: String? = null
        try {
            val c: Calendar = GregorianCalendar()
            val mSimpleDateFormat = SimpleDateFormat(format)
            c.time = mSimpleDateFormat.parse(strDate)
            c.add(calendarField, offset)
            mDateTime = mSimpleDateFormat.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return mDateTime
    }

    /**
     * Date类型转化为String类型(可偏移).
     *
     * @param date the date
     * @param format the format
     * @param calendarField the calendar field
     * @param offset the offset
     * @return String String类型日期时间
     */
    fun getStringByOffset(date: Date?, format: String?, calendarField: Int, offset: Int): String? {
        var strDate: String? = null
        try {
            val c: Calendar = GregorianCalendar()
            val mSimpleDateFormat = SimpleDateFormat(format)
            c.time = date
            c.add(calendarField, offset)
            strDate = mSimpleDateFormat.format(c.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return strDate
    }


    /**
     * Date类型转化为String类型.
     *
     * @param date the date
     * @param format the format
     * @return String String类型日期时间
     */
    fun getStringByFormat(date: Date?, format: String?): String? {
        val mSimpleDateFormat = SimpleDateFormat(format)
        var strDate: String? = null
        try {
            strDate = mSimpleDateFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return strDate
    }

    /**
     * 获取指定日期时间的字符串,用于导出想要的格式.
     * MMM dd,yyyy kk:mm:ss aa
     * @param strDate the date
     * @param format the format
     * @return String String类型日期时间
     */
    fun getStringByDateFormat(strDate: String?, format: String?): String? {
        var mDateTime: String? = null
        try {
            val c: Calendar = GregorianCalendar()
            val mSimpleDateFormat = SimpleDateFormat("MMM dd,yyyy kk:mm:ss aa", Locale.ENGLISH)
            c.time = mSimpleDateFormat.parse(strDate)
            val mSimpleDateFormat2 = SimpleDateFormat(format)
            mDateTime = mSimpleDateFormat2.format(c.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mDateTime
    }

    /**
     * 获取指定日期时间的字符串,用于导出想要的格式.
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param format 输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 转换后的String类型的日期时间
     */
    fun getStringByFormat(strDate: String?, format: String?): String? {
        var mDateTime: String? = null
        try {
            val c: Calendar = GregorianCalendar()
            val mSimpleDateFormat = SimpleDateFormat(dateFormatYMDHMS)
            c.time = mSimpleDateFormat.parse(strDate)
            val mSimpleDateFormat2 = SimpleDateFormat(format)
            mDateTime = mSimpleDateFormat2.format(c.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mDateTime
    }

    /**
     * 获取milliseconds表示的日期时间的字符串.
     *
     * @param milliseconds the milliseconds
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 日期时间字符串
     */
    fun getStringByFormat(milliseconds: Long, format: String?): String? {
        var thisDateTime: String? = null
        try {
            val mSimpleDateFormat = SimpleDateFormat(format)
            thisDateTime = mSimpleDateFormat.format(milliseconds)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return thisDateTime
    }

    /**
     * 获取表示当前日期时间的字符串.
     *
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型的当前日期时间
     */
    fun getCurrentDate(format: String?): String? {
        var curDateTime: String? = null
        try {
            val mSimpleDateFormat = SimpleDateFormat(format)
            val c: Calendar = GregorianCalendar()
            curDateTime = mSimpleDateFormat.format(c.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return curDateTime
    }

    /**
     * 获取表示当前日期时间的字符串.
     * @return String String类型的当前日期时间
     */
    fun getCurrentDate(): Date {
        val calendar: Calendar = GregorianCalendar()
        return calendar.time
    }

    /**
     * 获取表示当前日期时间的字符串(可偏移).
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset 偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    fun getCurrentDateByOffset(format: String?, calendarField: Int, offset: Int): String? {
        var mDateTime: String? = null
        try {
            val mSimpleDateFormat = SimpleDateFormat(format)
            val c: Calendar = GregorianCalendar()
            c.add(calendarField, offset)
            mDateTime = mSimpleDateFormat.format(c.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mDateTime
    }

    /**
     *
     * 计算两个日期所差的天数.
     *
     * @param milliseconds1 the milliseconds1
     * @param milliseconds2 the milliseconds2
     * @return int 所差的天数
     */
    fun getOffsetDay(milliseconds1: Long, milliseconds2: Long): Int {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = milliseconds1
        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = milliseconds2
        val y1 = calendar1[Calendar.YEAR]
        val y2 = calendar2[Calendar.YEAR]
        val d1 = calendar1[Calendar.DAY_OF_YEAR]
        val d2 = calendar2[Calendar.DAY_OF_YEAR]
        var maxDays = 0
        var day = 0
        //先判断是否同年
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR)
            day = d1 - d2 + maxDays
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR)
            day = d1 - d2 - maxDays
        } else {
            day = d1 - d2
        }
        return day
    }

    /**
     * 计算两个日期所差的小时数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的小时数
     */
    fun getOffectHour(date1: Long, date2: Long): Int {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = date1
        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = date2
        val h1 = calendar1[Calendar.HOUR_OF_DAY]
        val h2 = calendar2[Calendar.HOUR_OF_DAY]
        var h = 0
        val day = getOffsetDay(date1, date2)
        h = h1 - h2 + day * 24
        return h
    }

    /**
     * 计算两个日期所差的分钟数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的分钟数
     */
    fun getOffectMinutes(date1: Long, date2: Long): Int {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = date1
        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = date2
        val m1 = calendar1[Calendar.MINUTE]
        val m2 = calendar2[Calendar.MINUTE]
        val h = getOffectHour(date1, date2)
        var m = 0
        m = m1 - m2 + h * 60
        return m
    }

    /**
     * 获取本周一.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    fun getFirstDayOfWeek(format: String): String? {
        return getDayOfWeek(format, Calendar.MONDAY)
    }

    /**
     * 获取本周日.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    fun getLastDayOfWeek(format: String): String? {
        return getDayOfWeek(format, Calendar.SUNDAY)
    }

    /**
     * 获取本周的某一天.
     *
     * @param format the format
     * @param calendarField the calendar field
     * @return String String类型日期时间
     */
    private fun getDayOfWeek(format: String, calendarField: Int): String? {
        var strDate: String? = null
        try {
            val c: Calendar = GregorianCalendar()
            val mSimpleDateFormat = SimpleDateFormat(format)
            val week = c[Calendar.DAY_OF_WEEK]
            if (week == calendarField) {
                strDate = mSimpleDateFormat.format(c.time)
            } else {
                var offectDay = calendarField - week
                if (calendarField == Calendar.SUNDAY) {
                    offectDay = 7 - Math.abs(offectDay)
                }
                c.add(Calendar.DATE, offectDay)
                strDate = mSimpleDateFormat.format(c.time)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return strDate
    }

    /**
     * 获取本月第一天.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    fun getFirstDayOfMonth(format: String?): String? {
        var strDate: String? = null
        try {
            val c: Calendar = GregorianCalendar()
            val mSimpleDateFormat = SimpleDateFormat(format)
            //当前月的第一天
            c[GregorianCalendar.DAY_OF_MONTH] = 1
            strDate = mSimpleDateFormat.format(c.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return strDate
    }

    /**
     * 获取本月最后一天.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    fun getLastDayOfMonth(format: String?): String? {
        var strDate: String? = null
        try {
            val c: Calendar = GregorianCalendar()
            val mSimpleDateFormat = SimpleDateFormat(format)
            // 当前月的最后一天
            c[Calendar.DATE] = 1
            c.roll(Calendar.DATE, -1)
            strDate = mSimpleDateFormat.format(c.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return strDate
    }


    /**
     * 获取表示当前日期的0点时间毫秒数.
     *
     * @return the first time of day
     */
    fun getFirstTimeOfDay(): Long {
        var date: Date? = null
        try {
            val currentDate = getCurrentDate(dateFormatYMD)
            date = getDateByFormat("$currentDate 00:00:00", dateFormatYMDHMS)
            return date!!.time
        } catch (e: Exception) {
        }
        return -1
    }

    /**
     * 获取表示当前日期24点时间毫秒数.
     * @return the last time of day
     */
    fun getLastTimeOfDay(): Long {
        var date: Date? = null
        try {
            val currentDate = getCurrentDate(dateFormatYMD)
            date = getDateByFormat("$currentDate 24:00:00", dateFormatYMDHMS)
            return date!!.time
        } catch (e: Exception) {
        }
        return -1
    }

    /**
     * 判断是否是闰年()
     * (year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
     * @param year 年代（如2012）
     * @return boolean 是否为闰年
     */
    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && year % 400 != 0 || year % 400 == 0
    }

    /**
     * 根据时间返回格式化后的时间的描述.
     * 小于1小时显示多少分钟前  大于1小时显示今天＋实际日期，大于今天全部显示实际时间
     *
     * @param strDate the str date
     * @param outFormat the out format
     * @return the string
     */
    fun formatDateStr2Desc(strDate: String?, outFormat: String?): String? {
        val df: DateFormat = SimpleDateFormat(dateFormatYMDHMS)
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        try {
            c2.time = df.parse(strDate)
            c1.time = Date()
            val d = getOffsetDay(c1.timeInMillis, c2.timeInMillis)
            if (d == 0) {
                val h = getOffectHour(c1.timeInMillis, c2.timeInMillis)
                if (h > 0) {
                    return "今天" + getStringByFormat(strDate, dateFormatHM)
                    //return h + "小时前";
                } else if (h < 0) {
                    //return Math.abs(h) + "小时后";
                } else if (h == 0) {
                    val m = getOffectMinutes(c1.timeInMillis, c2.timeInMillis)
                    if (m > 0) {
                        return m.toString() + "分钟前"
                    } else if (m < 0) {
                        //return Math.abs(m) + "分钟后";
                    } else {
                        return "刚刚"
                    }
                }
            } else if (d > 0) {
                if (d == 1) {
                    //return "昨天"+getStringByFormat(strDate,outFormat);
                } else if (d == 2) {
                    //return "前天"+getStringByFormat(strDate,outFormat);
                }
            } else if (d < 0) {
                if (d == -1) {
                    //return "明天"+getStringByFormat(strDate,outFormat);
                } else if (d == -2) {
                    //return "后天"+getStringByFormat(strDate,outFormat);
                } else {
                    //return Math.abs(d) + "天后"+getStringByFormat(strDate,outFormat);
                }
            }
            val out = getStringByFormat(strDate, outFormat)
            if (!KoStringUtil.isEmpty(out)) {
                return out
            }
        } catch (e: Exception) {
        }
        return strDate
    }


    /**
     * 取指定日期为星期几.
     *
     * @param strDate 指定日期
     * @param inFormat 指定日期格式
     * @return String   星期几
     */
    fun getWeekNumber(strDate: String?, inFormat: String?): String? {
        var week = "星期日"
        val calendar: Calendar = GregorianCalendar()
        val df: DateFormat = SimpleDateFormat(inFormat)
        try {
            calendar.time = df.parse(strDate)
        } catch (e: Exception) {
            return "错误"
        }
        val intTemp = calendar[Calendar.DAY_OF_WEEK] - 1
        when (intTemp) {
            0 -> week = "星期日"
            1 -> week = "星期一"
            2 -> week = "星期二"
            3 -> week = "星期三"
            4 -> week = "星期四"
            5 -> week = "星期五"
            6 -> week = "星期六"
        }
        return week
    }

    /**
     * 根据给定的日期判断是否为上下午.
     *
     * @param strDate the str date
     * @param format the format
     * @return the time quantum
     */
    fun getTimeQuantum(strDate: String, format: String): String {
        val mDate = getDateByFormat(strDate, format)
        val hour = mDate!!.hours
        return if (hour >= 12) "PM" else "AM"
    }

    /**
     * 根据给定的毫秒数算得时间的描述.
     *
     * @param milliseconds the milliseconds
     * @return the time description
     */
    fun getTimeDescription(milliseconds: Long): String {
        return if (milliseconds > 1000) {
            //大于一分
            if (milliseconds / 1000 / 60 > 1) {
                val minute = milliseconds / 1000 / 60
                val second = milliseconds / 1000 % 60
                minute.toString() + "分" + second + "秒"
            } else {
                //显示秒
                (milliseconds / 1000).toString() + "秒"
            }
        } else {
            milliseconds.toString() + "毫秒"
        }
    }

    fun getDateValue(dateText: String?): IntArray? {
        //年月日时间选择器
        val calendar = Calendar.getInstance()
        var year = calendar[Calendar.YEAR]
        var month = calendar[Calendar.MONTH] + 1
        var day = calendar[Calendar.DATE]
        if (KoStringUtil.isNotEmpty(dateText)) {
            val dateNew = getDateByFormat(dateText!!, dateFormatYMD)
            if (dateNew != null) {
                year = 1900 + dateNew.year
                month = dateNew.month + 1
                day = dateNew.date
            }
        }
        val ymd = IntArray(3)
        ymd[0] = year
        ymd[1] = month
        ymd[2] = day
        return ymd
    }

    /**
     * 取得当月天数
     */
    fun getCurrentMonthLastDay(): Int {
        val a = Calendar.getInstance()
        a[Calendar.DATE] = 1 //把日期设置为当月第一天
        a.roll(Calendar.DATE, -1) //日期回滚一天，也就是最后一天
        return a[Calendar.DATE]
    }
}