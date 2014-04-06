package future.talk.repository

import org.apache.lucene.store.FSDirectory
import java.io.File
import org.apache.lucene.index._
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import future.talk.FutureTalkSettings
import org.apache.lucene.util.Version
import websiteschema.mpsegment.lucene.MPSegmentAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.search.{Query, TermQuery, IndexSearcher}
import java.util.UUID
import scala.Some

object SearchEngine {

  private val version = Version.LUCENE_46
  private val analyzer = new MPSegmentAnalyzer
  private val dir = FSDirectory.open(new File(FutureTalkSettings.dataPath))
  private var retireIndexSearcher = false

  def index(document: Document): Unit = {
    indexWriter addDocument document
    indexWriter.commit
    retireIndexSearcher = true
  }

  def get(id: UUID): Option[Document] = {
    val query = new TermQuery(new Term("id", id.toString))
    val result = searcher.search(query, 1)
    if (result.totalHits > 0) {
      Some(searcher.doc(result.scoreDocs(0).doc))
    } else {
      None
    }
  }

  def search(query: Query): Option[List[Document]] = {
    val result = searcher.search(query, 1000)
    if (result.totalHits > 0) {
      Some(result.scoreDocs.map(_.doc)
        .map(docId => searcher.doc(docId)).toList)
    } else {
      None
    }
  }

  private var _searcher = new IndexSearcher(DirectoryReader.open(dir))
  private def searcher = {
    if(retireIndexSearcher) {
      _searcher.getIndexReader.close()
      _searcher = new IndexSearcher(DirectoryReader.open(dir))
      retireIndexSearcher = true
    }
    _searcher
  }

  private def createIndexWriter = {
    val indexWriterConfig = new IndexWriterConfig(version, analyzer)
    indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND)

    new IndexWriter(dir, indexWriterConfig)
  }

  private lazy val indexWriter = createIndexWriter

}
