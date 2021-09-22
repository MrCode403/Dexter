package ma.dexter.util

import org.antlr.runtime.Token
import org.jf.smali.smaliFlexLexer
import org.jf.smali.smaliParser
import java.io.StringReader

/* const */ val METHOD_DIRECTIVE_REGEX = Regex(
    """^\.method (?:(?:[a-z\-]*) )*(.*)${'$'}"""
)

/* const */ val FIELD_DIRECTIVE_REGEX = Regex(
    """^\.field (?:(?:[a-z\-]*) )*(.*)${'$'}"""
)

/* const */ val FIELD_METHOD_CALL_REGEX = Regex(
    """^.*?((L.*?;)\s*->\s*(.*))${'$'}""", RegexOption.DOT_MATCHES_ALL
)

const val END_METHOD_DIRECTIVE = ".end method"

fun getClassDefPath(classDefType: String) =
    if (classDefType.length >= 2) {
        classDefType.substring(1, classDefType.length - 1)
    } else {
        classDefType
    }

/**
 * Utility method to tokenize smali.
 */
inline fun tokenizeSmali(
    smaliCode: String,
    callback: (token: Token, line: Int, column: Int) -> Unit
) {
    val lexer = smaliFlexLexer(StringReader(smaliCode), 31)
    lexer.setSuppressErrors(true)

    var token: Token

    while (true) {
        token = lexer.nextToken()

        if (token == null || token.type == smaliParser.EOF) break
        if (token.type == smaliParser.WHITE_SPACE) continue

        val line = token.line - 1
        val column = token.charPositionInLine

        callback(token, line, column)
    }
}
