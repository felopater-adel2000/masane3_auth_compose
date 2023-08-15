package com.felo.masane3_test.utils

class MainUtils
{
    companion object{
        fun isNetworkError(msg: String): Boolean {
            when {
                msg.contains(Constants.UNABLE_TO_RESOLVE_HOST) -> return true
                else -> return false
            }
        }
    }
}