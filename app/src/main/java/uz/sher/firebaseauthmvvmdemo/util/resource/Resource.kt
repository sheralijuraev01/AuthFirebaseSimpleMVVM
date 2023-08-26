package uz.sher.firebaseauthmvvmdemo.util.resource

sealed class Resource<T> {
    class Loading <T>: Resource<T>()
    class Success<T : Any>(val data: T) : Resource<T>()
    class Failure<T : Any>(val t: String) : Resource<T>()
}