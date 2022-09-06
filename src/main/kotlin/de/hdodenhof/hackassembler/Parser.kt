package de.hdodenhof.hackassembler

import java.io.File
import java.io.FileReader

class Parser(private val file: File) {

    private val aInstructionPattern = Regex("^@(\\d+)(?:[ \\t]*)?(?://.*)?\$")
    private val cInstructionPattern = Regex("^(?:(\\S{1,3})=)?(\\S{1,3})(?:;(\\S{1,3}))?(?:[ \\t]*)?(?://.*)?\$")

    fun parse(): ArrayList<Instruction> {
        val instructions = arrayListOf<Instruction>()
        FileReader(file).buffered().useLines { lines ->
            lines.forEach {
                val trimmed = it.trim()
                when {
                    trimmed.isBlank() -> return@forEach
                    trimmed.startsWith("//") -> return@forEach
                    else -> {
                        instructions.add(parseLine(it))
                    }
                }
            }
        }
        return instructions
    }

    private fun parseLine(line: String): Instruction {
        return if (line.startsWith("@")) {
            parseAInstruction(line)
        } else {
            parseCInstruction(line)
        }
    }

    private fun parseAInstruction(line: String): AInstruction {
        val (address) = aInstructionPattern.find(line)?.destructured
            ?: throw InvalidInstructionException()

        return AInstruction(address)
    }

    private fun parseCInstruction(line: String): CInstruction {
        val (dest, comp, jump) = cInstructionPattern.find(line)?.destructured
            ?: throw InvalidInstructionException()

        return CInstruction(dest, comp, jump)
    }

}
