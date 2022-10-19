package com.capital.composesample.include

import java.util.*

object Function {

    /**
     * 檢查ID是否有效
     */
    fun isValidID(strID: String?): Boolean {
        var str = strID
        if (str == null || str == "") {
            return false
        }
        val pidCharArray = charArrayOf(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        )

        // 原身分證英文字應轉換為10~33，這裡直接作個位數*9+10
        val pidIDInt = intArrayOf(
            1, 10, 19, 28, 37, 46, 55, 64, 39, 73, 82, 2, 11, 20, 48, 29, 38, 47, 56, 65, 74, 83, 21, 3, 12, 30
        )

        // 轉換大寫
        str = str.uppercase(Locale.ENGLISH)

        // 字串轉成char陣列
        val strArr = str.toCharArray()
        var verifyNum = 0

        // 檢查身分證字號
        if (str.matches(Regex("[A-Z][1-2|8-9][0-9]{8}"))) {
            // 第一碼
            verifyNum += pidIDInt[Arrays.binarySearch(pidCharArray, strArr[0])]

            // 第二~九碼
            var i = 1
            var j = 8
            while (i < 9) {
                verifyNum += Character.digit(strArr[i], 10) * j
                i++
                j--
            }

            // 檢查碼
            verifyNum = (10 - verifyNum % 10) % 10
            return verifyNum == Character.digit(strArr[9], 10)
        }
        return false
    }
}