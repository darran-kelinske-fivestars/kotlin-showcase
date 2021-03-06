package component.repository

/**
 * Serialization support for util classes.
 * @author Eric Pabst (epabst@gmail.com)
 * Date: 6/18/17
 * Time: 1:41 AM
 */
external interface LongJS {
    val high_: Int
    val low_: Int
}

private val HIGH_AMOUNT: Long = (Int.MAX_VALUE.toLong() + 1) * 2

fun LongJS?.toNormal(): Long? {
    if (this == null) {
        return null
    }
    else if (high_ == undefined) {
        val number: Number = asDynamic()
        return number.toLong()
    }
    else {
        return high_.toLong() * HIGH_AMOUNT + (if (low_ >= 0) low_.toLong() else low_.toLong() + HIGH_AMOUNT)
    }
}

fun <E : WithID<E>> String.toID(): ID<E>? = ID(this)

external interface IDJS {
    val id: LongJS?
    val _id: String?
}

fun <E : WithID<E>> IDJS.toNormal(): ID<E> = (id.toNormal()?.toString() ?: _id) ?.let { ID<E>(it) } ?: error("required ID not present")
