package org.mameli.extension

fun Map<*, *>.getByKeyAsList(key: String): List<*>? {
    return this[key] as? List<*>
}

fun Map<*, *>.getByKeyAsStringList(key: String): List<String>? {
    return this[key] as? List<String>
}

fun Map<*, *>.getByKeyAsMap(key: String?): Map<*, *>? {
    return this[key] as? Map<*, *>?
}

fun Map<*, *>.getByKeyAsMapList(key: String?): List<Map<*, *>>? {
    return this[key] as? List<Map<*, *>>?
}
