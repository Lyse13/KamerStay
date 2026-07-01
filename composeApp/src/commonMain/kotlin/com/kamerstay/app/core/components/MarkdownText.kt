package com.kamerstay.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Blocs Markdown ────────────────────────────────────────────

private sealed class MdBlock {
    data class Header(val level: Int, val content: String) : MdBlock()
    data class Bullet(val content: String) : MdBlock()
    data class Numbered(val number: Int, val content: String) : MdBlock()
    data class Paragraph(val content: String) : MdBlock()
    object Blank : MdBlock()
}

// ── Parser de blocs ───────────────────────────────────────────

private fun parseBlocks(text: String): List<MdBlock> {
    val blocks = mutableListOf<MdBlock>()
    var numberedCounter = 0
    val numberedRegex = Regex("^(\\d+)\\.\\s+(.*)")

    for (line in text.lines()) {
        when {
            line.isBlank() -> {
                numberedCounter = 0
                if (blocks.lastOrNull() !is MdBlock.Blank) blocks.add(MdBlock.Blank)
            }
            line.startsWith("### ") -> {
                numberedCounter = 0
                blocks.add(MdBlock.Header(3, line.removePrefix("### ")))
            }
            line.startsWith("## ") -> {
                numberedCounter = 0
                blocks.add(MdBlock.Header(2, line.removePrefix("## ")))
            }
            line.startsWith("# ") -> {
                numberedCounter = 0
                blocks.add(MdBlock.Header(1, line.removePrefix("# ")))
            }
            line.startsWith("- ") || line.startsWith("* ") || line.startsWith("• ") -> {
                numberedCounter = 0
                val content = line.drop(2).trimStart()
                blocks.add(MdBlock.Bullet(content))
            }
            numberedRegex.matches(line) -> {
                numberedCounter++
                val content = numberedRegex.find(line)!!.groupValues[2]
                blocks.add(MdBlock.Numbered(numberedCounter, content))
            }
            else -> {
                numberedCounter = 0
                val prev = blocks.lastOrNull()
                // Fusionne les lignes consécutives en un seul paragraphe
                if (prev is MdBlock.Paragraph) {
                    blocks[blocks.lastIndex] = MdBlock.Paragraph(prev.content + "\n" + line)
                } else {
                    blocks.add(MdBlock.Paragraph(line))
                }
            }
        }
    }
    return blocks.dropLastWhile { it is MdBlock.Blank }
}

// ── Parser inline (bold, italic, code) ───────────────────────

private fun parseInline(
    text: String,
    baseColor: Color,
    fontSize: TextUnit
): AnnotatedString = buildAnnotatedString {
    var i = 0
    while (i < text.length) {
        when {
            // **bold**
            text.startsWith("**", i) -> {
                val end = text.indexOf("**", i + 2)
                if (end != -1) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text.substring(i + 2, end))
                    }
                    i = end + 2
                } else { append(text[i]); i++ }
            }
            // *italic* (ne pas confondre avec **)
            text[i] == '*' && i + 1 < text.length && text[i + 1] != '*' -> {
                val end = text.indexOf('*', i + 1)
                if (end != -1 && text.getOrNull(end + 1) != '*') {
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(text.substring(i + 1, end))
                    }
                    i = end + 1
                } else { append(text[i]); i++ }
            }
            // _italic_
            text[i] == '_' -> {
                val end = text.indexOf('_', i + 1)
                if (end != -1) {
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(text.substring(i + 1, end))
                    }
                    i = end + 1
                } else { append(text[i]); i++ }
            }
            // `inline code`
            text[i] == '`' -> {
                val end = text.indexOf('`', i + 1)
                if (end != -1) {
                    withStyle(SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = Color(0x22000000),
                        fontSize   = (fontSize.value - 1).sp
                    )) { append(text.substring(i + 1, end)) }
                    i = end + 1
                } else { append(text[i]); i++ }
            }
            else -> { append(text[i]); i++ }
        }
    }
}

// ── Composable public ─────────────────────────────────────────

/**
 * Rend du Markdown léger dans une bulle de chat Compose.
 * Supporte : **bold**, *italic*, `code`, # headers, - listes, 1. listes numérotées.
 */
@Composable
fun MarkdownText(
    text: String,
    baseColor: Color,
    fontSize: TextUnit = 14.sp,
    lineHeight: TextUnit = 20.sp,
    modifier: Modifier = Modifier
) {
    val blocks = remember(text) { parseBlocks(text) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        blocks.forEach { block ->
            when (block) {
                is MdBlock.Header -> {
                    val (size, weight, topPad) = when (block.level) {
                        1    -> Triple(18.sp, FontWeight.ExtraBold, 6.dp)
                        2    -> Triple(16.sp, FontWeight.Bold,      4.dp)
                        else -> Triple(15.sp, FontWeight.SemiBold,  2.dp)
                    }
                    Spacer(Modifier.height(topPad))
                    Text(
                        text       = parseInline(block.content, baseColor, size),
                        fontSize   = size,
                        fontWeight = weight,
                        color      = baseColor,
                        lineHeight = (size.value * 1.4f).sp
                    )
                }

                is MdBlock.Bullet -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Text(
                            text       = "•",
                            color      = baseColor,
                            fontSize   = fontSize,
                            lineHeight = lineHeight,
                            modifier   = Modifier.paddingFromBaseline(top = 0.dp)
                        )
                        Text(
                            text       = parseInline(block.content, baseColor, fontSize),
                            fontSize   = fontSize,
                            color      = baseColor,
                            lineHeight = lineHeight,
                            modifier   = Modifier.weight(1f)
                        )
                    }
                }

                is MdBlock.Numbered -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Text(
                            text       = "${block.number}.",
                            color      = baseColor,
                            fontSize   = fontSize,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = lineHeight
                        )
                        Text(
                            text       = parseInline(block.content, baseColor, fontSize),
                            fontSize   = fontSize,
                            color      = baseColor,
                            lineHeight = lineHeight,
                            modifier   = Modifier.weight(1f)
                        )
                    }
                }

                is MdBlock.Paragraph -> {
                    Text(
                        text       = parseInline(block.content, baseColor, fontSize),
                        fontSize   = fontSize,
                        color      = baseColor,
                        lineHeight = lineHeight
                    )
                }

                MdBlock.Blank -> Spacer(Modifier.height(4.dp))
            }
        }
    }
}
