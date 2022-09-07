package de.hdodenhof.hackassembler

import java.util.concurrent.atomic.AtomicInteger

class SymbolTable {

    private val custom = mutableMapOf<String, Int>()
    private var nextAddress = AtomicInteger(16)

    fun contains(key: String): Boolean {
        return BUILTIN.containsKey(key) || custom.containsKey(key)
    }

    operator fun get(key: String): Int? {
        return BUILTIN[key] ?: custom[key]
    }

    operator fun set(key: String, value: Int) {
        if (BUILTIN.containsKey(key)) {
            throw IllegalArgumentException()
        }

        custom[key] = value
    }

    fun add(key: String): Int {
        nextAddress.getAndIncrement().let {
            custom[key] = it
            return it
        }
    }

    companion object {
        val BUILTIN = mapOf(
            "R0" to 0,
            "R1" to 1,
            "R2" to 2,
            "R3" to 3,
            "R4" to 4,
            "R5" to 5,
            "R6" to 6,
            "R7" to 7,
            "R8" to 8,
            "R9" to 9,
            "R10" to 10,
            "R11" to 11,
            "R12" to 12,
            "R13" to 13,
            "R14" to 14,
            "R15" to 15,
            "SCREEN" to 16384,
            "KBD" to 24576,
            "SP" to 0,
            "LCL" to 1,
            "ARG" to 2,
            "THIS" to 3,
            "THAT" to 4
        )
    }
}
