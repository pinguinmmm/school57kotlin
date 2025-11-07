package ru.tbank.education.school.homework
import java.io.*
import java.nio.file.*
import java.nio.charset.StandardCharsets
import java.io.IOException
interface FileAnalyzer {

    /**
     * Считает количество строк и слов в указанном входном файле и записывает результат в выходной файл.
     *
     * Словом считается последовательность символов, разделённая пробелами,
     * табуляциями или знаками перевода строки. Пустые части после разделения не считаются словами.
     *
     * @param inputFilePath путь к входному текстовому файлу
     * @param outputFilePath путь к выходному файлу, в который будет записан результат
     * @return true если операция успешна, иначе false
     */
    fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean
}
class IOFileAnalyzer : FileAnalyzer {
    override fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean {
        return try {
            val inputFile = File(inputFilePath)

            if (!inputFile.exists() || !inputFile.isFile) {
                println("Ошибка: входной файл не найден или это не файл.")
                return false
            }

            var lineCount = 0
            var wordCount = 0

            BufferedReader(FileReader(inputFile)).use { reader ->
                reader.lineSequence().forEach { line ->
                    lineCount++
                    wordCount += line.split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
                }
            }

            BufferedWriter(FileWriter(outputFilePath)).use { writer ->
                writer.write("Общее количество строк: $lineCount\n")
                writer.write("Общее количество слов: $wordCount\n")
            }

            true
        } catch (e: FileNotFoundException) {
            println("Ошибка: файл не найден (${e.message})")
            false
        } catch (e: SecurityException) {
            println("Ошибка доступа к файлу (${e.message})")
            false
        } catch (e: IOException) {
            println("Ошибка ввода/вывода (${e.message})")
            false
        } catch (e: Exception) {
            println("Неизвестная ошибка: ${e.message}")
            false
        }
    }
}
class NIOFileAnalyzer : FileAnalyzer {
    override fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean {
        return try {
            val input = Paths.get(inputFilePath)

            if (!Files.exists(input) || !Files.isRegularFile(input)) {
                println("Ошибка: входной файл не найден или это не файл.")
                return false
            }

            val lines = Files.readAllLines(input, StandardCharsets.UTF_8)
            val lineCount = lines.size
            val wordCount = lines.sumOf { line ->
                line.split("\\s+".toRegex()).count { it.isNotEmpty() }
            }

            val output = Paths.get(outputFilePath)
            val result = listOf(
                "Общее количество строк: $lineCount",
                "Общее количество слов: $wordCount"
            )

            Files.write(output, result, StandardCharsets.UTF_8)

            true
        } catch (e: NoSuchFileException) {
            println("Ошибка: файл не найден (${e.message})")
            false
        } catch (e: AccessDeniedException) {
            println("Ошибка доступа к файлу (${e.message})")
            false
        } catch (e: IOException) {
            println("Ошибка ввода/вывода (${e.message})")
            false
        } catch (e: Exception) {
            println("Неизвестная ошибка: ${e.message}")
            false
        }
    }
}
fun main() {
    val ioAnalyzer = IOFileAnalyzer()
    val nioAnalyzer = NIOFileAnalyzer()

    val inputFilePath = "input.txt"
    val outputFilePathIO = "result_io.txt"
    val outputFilePathNIO = "result_nio.txt"

    println("=== Анализ через Java IO ===")
    ioAnalyzer.countLinesAndWordsInFile(inputFilePath, outputFilePathIO)

    println("=== Анализ через Java NIO ===")
    nioAnalyzer.countLinesAndWordsInFile(inputFilePath, outputFilePathNIO)
}
