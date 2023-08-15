package com.felo.masane3_test.utils

class Constants
{
    companion object{
        const val BASE_URL = "https://dash.masane3online.com/api/"

        const val ERROR_UNKNOWN = "Unknown error"

        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"

        const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."
        const val NETWORK_TIMEOUT = 100000L
        const val PAGINATION_PAGE_SIZE = 10
        const val DATABASE_VERSION = 12
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing
        const val UNABLE_TODO_OPERATION_WO_INTERNET = "No Internet"
    }
}