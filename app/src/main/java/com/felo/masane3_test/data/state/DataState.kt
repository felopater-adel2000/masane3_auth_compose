package com.felo.masane3_test.data.state


data class DataState<T>(
    var error: Event<StateError>? = null,
    var loading: Loading = Loading(false),
    var data: Data<T>? = null,
) {

    companion object {
        fun <T> error(response: Response): DataState<T> {
            return DataState(error = Event(StateError(response)))
        }

        fun <T> loading(isLoading: Boolean, cashedData: T? = null): DataState<T> {
            return DataState(
                loading = Loading(isLoading),
                data = Data(Event.dataEvent(cashedData), null)
            )
        }

        fun <T> success(data: T? = null, response: Response? = null): DataState<T> {
            return DataState(
                data = Data(
                    Event.dataEvent(data), Event.responseEvent(response)
                )
            )
        }
    }


}