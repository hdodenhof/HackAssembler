package de.hdodenhof.hackassembler

interface Instruction {
    fun binary(): String
}

class AInstruction(addressString: String) : Instruction {

    private val address: Int

    init {
       address = parseAddress(addressString)
    }

    override fun binary() = "0${address.toString(2).padStart(15, '0')}"

    companion object {
        private fun parseAddress(address: String): Int {
            return address.toIntOrNull() ?: throw InvalidInstructionException()
        }
    }
}

class CInstruction(destString: String, compString: String, jumpString: String) : Instruction {

    private val dest: Dest
    private val comp: Comp
    private val jump: Jump

    init {
        dest = parseDest(destString)
        comp = parseComp(compString)
        jump = parseJump(jumpString)
    }

    override fun binary() = "111${comp.code}${dest.code}${jump.code}"

    companion object {
        private fun parseDest(dest: String): Dest {
            if (dest.isBlank()) {
                return Dest.NULL
            }

            return when(dest) {
                "M" -> Dest.M
                "D" -> Dest.D
                "MD" -> Dest.MD
                "A" -> Dest.A
                "AM" -> Dest.AM
                "AD" -> Dest.AD
                "AMD" -> Dest.AMD
                else -> throw InvalidInstructionException()
            }
        }

        private fun parseComp(comp: String): Comp {
            return when(comp) {
                "0" -> Comp.ZERO
                "1" -> Comp.ONE
                "-1" -> Comp.MINUS_ONE
                "D" -> Comp.D
                "A" -> Comp.A
                "!D" -> Comp.NOT_D
                "!A" -> Comp.NOT_A
                "-D" -> Comp.MINUS_D
                "-A" -> Comp.MINUS_A
                "D+1" -> Comp.D_PLUS_ONE
                "A+1" -> Comp.A_PLUS_ONE
                "D-1" -> Comp.D_MINUS_ONE
                "A-1" -> Comp.A_MINUS_ONE
                "D+A" -> Comp.D_PLUS_A
                "D-A" -> Comp.D_MINUS_A
                "A-D" -> Comp.A_MINUS_D
                "D&A" -> Comp.D_AND_A
                "D|A" -> Comp.D_OR_A
                "M" -> Comp.M
                "!M" -> Comp.NOT_M
                "-M" -> Comp.MINUS_M
                "M+1" -> Comp.M_PLUS_ONE
                "M-1" -> Comp.M_MINUS_ONE
                "D+M" -> Comp.D_PLUS_M
                "D-M" -> Comp.D_MINUS_M
                "M-D" -> Comp.M_MINUS_D
                "D&M" -> Comp.D_AND_M
                "D|M" -> Comp.D_OR_M
                else -> throw InvalidInstructionException()
            }
        }

        private fun parseJump(jump: String): Jump {
            if (jump.isBlank()) {
                return Jump.NULL
            }

            return when(jump) {
                "JGT" -> Jump.JGT
                "JEQ" -> Jump.JEQ
                "JGE" -> Jump.JGE
                "JLT" -> Jump.JLT
                "JNE" -> Jump.JNE
                "JLE" -> Jump.JLE
                "JMP" -> Jump.JMP
                else -> throw InvalidInstructionException()
            }
        }
    }
}

enum class Dest(val code: String) {
    NULL("000"),
    M("001"),
    D("010"),
    MD("011"),
    A("100"),
    AM("101"),
    AD("110"),
    AMD("111")
}

enum class Comp(val code: String) {
    ZERO("0101010"),
    ONE("0111111"),
    MINUS_ONE("0111010"),
    D("0001100"),
    A("0110000"),
    NOT_D("0001101"),
    NOT_A("0110001"),
    MINUS_D("0001111"),
    MINUS_A("0110011"),
    D_PLUS_ONE("0011111"),
    A_PLUS_ONE("0110111"),
    D_MINUS_ONE("0001110"),
    A_MINUS_ONE("0110010"),
    D_PLUS_A("0000010"),
    D_MINUS_A("0010011"),
    A_MINUS_D("0000111"),
    D_AND_A("0000000"),
    D_OR_A("0010101"),
    M("1110000"),
    NOT_M("1110001"),
    MINUS_M("1110011"),
    M_PLUS_ONE("1110111"),
    M_MINUS_ONE("1110010"),
    D_PLUS_M("1000010"),
    D_MINUS_M("1010011"),
    M_MINUS_D("1000111"),
    D_AND_M("1000000"),
    D_OR_M("1010101")
}

enum class Jump(val code: String) {
    NULL("000"),
    JGT("001"),
    JEQ("010"),
    JGE("011"),
    JLT("100"),
    JNE("101"),
    JLE("110"),
    JMP("111")
}
