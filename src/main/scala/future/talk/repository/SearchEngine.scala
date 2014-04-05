package future.talk.repository

import org.apache.lucene.store.FSDirectory
import java.io.File
import org.apache.lucene.index._
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import future.talk.FutureTalkSettings
import org.apache.lucene.util.Version
import websiteschema.mpsegment.lucene.MPSegmentAnalyzer
import org.apache.lucene.document.{Field, TextField, Document}
import org.apache.lucene.search.{TermQuery, IndexSearcher}
import java.util.UUID
import org.apache.lucene.queryparser.classic.QueryParser
import scala.Some

object SearchEngine {

  val version = Version.LUCENE_46
  val analyzer = new MPSegmentAnalyzer
  val dir = FSDirectory.open(new File(FutureTalkSettings.dataPath))

  private def createIndexWriter = {
    val indexWriterConfig = new IndexWriterConfig(version, analyzer)
    indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND)

    new IndexWriter(dir, indexWriterConfig)
  }

  private lazy val indexWriter = createIndexWriter

  def index(document: Document): Unit = {
    indexWriter addDocument document
    indexWriter.commit
  }

  def get(id: UUID): Option[Document] = {
    val searcher = new IndexSearcher(DirectoryReader.open(dir))
    val query = new TermQuery(new Term("id", id.toString))
    val result = searcher.search(query, 1)
    if (result.totalHits > 0) {
      Some(searcher.doc(result.scoreDocs(0).doc))
    } else {
      None
    }
  }

}
