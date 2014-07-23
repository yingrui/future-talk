package future.talk.util

import java.io.{PrintWriter, File}

import scala.io.Source

object FileUtil {

  implicit class RichFile(file: File) {

    def write(content: String) = {
      val writer = new PrintWriter(file)
      writer.write(content)
      writer.close()
      file
    }

    def content = Source.fromFile(file).getLines().mkString("\n")
  }

  def createTempFile = {
    val file = java.io.File.createTempFile("future-talk-", ".tmp")
    file.deleteOnExit()
    file
  }

  def createTempFile(content: String): java.io.File = createTempFile.write(content)

}
