package de.hdodenhof.hackassembler

import java.io.File
import java.io.FileReader

class Parser(private val file: File) {

    private val aInstructionPattern = Regex("^@([\\dA-Za-z_.$:]+)(?:[ \\t]*)?(?://.*)?\$")
    private val cInstructionPattern = Regex("^(?:(\\S{1,3})=)?(\\S{1,3})(?:;(\\S{1,3}))?(?:[ \\t]*)?(?://.*)?\$")

    private val labelPattern = Regex("^\\(([\\dA-Za-z_.\$:]+)\\)(?:[ \\t]*)?(?://.*)?\$")

    private val symbolTable = SymbolTable()

    fun parse(): ArrayList<Instruction> {
        var lineNumber = 0
        iterateLines(file) { line ->
            if (line.startsWith("(")) {
                storeSymbol(line, lineNumber)
            } else {
                lineNumber++
            }
        }

        val instructions = arrayListOf<Instruction>()
        iterateLines(file) { line ->
            parseLine(line)?.let { instructions.add(it) }
        }

        return instructions
    }

    private fun iterateLines(file: File, lineAction: (String) -> Unit) {
        FileReader(file).buffered().useLines { lines ->
            lines.forEach {
                val trimmed = it.trim()
                when {
                    trimmed.isBlank() -> return@forEach
                    trimmed.startsWith("//") -> return@forEach
                    else -> lineAction.invoke(trimmed)
                }
            }
        }
    }

    private fun storeSymbol(line: String, lineNumber: Int) {
        val (label) = labelPattern.find(line)?.destructured ?: throw InvalidInstructionException()
        symbolTable[label] = lineNumber
    }

    private fun parseLine(line: String): Instruction? {
        return when {
            line.startsWith("(") -> null
            line.startsWith("@") -> parseAInstruction(line)
            else -> parseCInstruction(line)
        }
    }

    private fun parseAInstruction(line: String): AInstruction {
        val (address) = aInstructionPattern.find(line)?.destructured
            ?: throw InvalidInstructionException()

        return if (address.toIntOrNull() == null) {
            symbolTable[address]?.let {
                AInstruction(it)
            } ?: run {
                AInstruction(symbolTable.add(address))
            }
        } else {
            AInstruction(address)
        }
    }

    private fun parseCInstruction(line: String): CInstruction {
        val (dest, comp, jump) = cInstructionPattern.find(line)?.destructured
            ?: throw InvalidInstructionException()

        return CInstruction(dest, comp, jump)
    }

}
