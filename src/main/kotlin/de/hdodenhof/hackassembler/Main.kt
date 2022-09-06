package de.hdodenhof.hackassembler

import java.io.File
import java.io.FileWriter

fun main(args: Array<String>) {
    HackAssembler().assemble(args)
}

class HackAssembler {

    fun assemble(args: Array<String>) {
        val inputFile = File(args[0])
        val inputFilePath = inputFile.absolutePath.substringBeforeLast(File.separatorChar)
        val inputFileName = inputFile.name.substringBeforeLast(".")
        val outputFile = File(inputFilePath, "$inputFileName.hack")

        val instructions = Parser(inputFile).parse()

        FileWriter(outputFile).buffered().use { writer ->
            instructions.forEach {
                writer.append(it.binary())
                writer.newLine()
            }
        }
    }
}
