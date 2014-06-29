package future.talk.repository

import org.apache.lucene.store.FSDirectory
import java.io.File
import org.apache.lucene.index._
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import future.talk.FutureTalkSettings
import org.apache.lucene.util.Version
import websiteschema.mpsegment.lucene.MPSegmentAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.search.{TopDocs, Query, TermQuery, IndexSearcher}
import java.util.UUID

object SearchEngine {

  private val version = Version.LUCENE_46
  private val dir = FSDirectory.open(new File(FutureTalkSettings.dataPath))
  private val indexWriter = createIndexWriter

  def index(document: Document): Unit = {
    indexWriter addDocument document
    indexWriter.commit
  }

  def get(id: UUID): Option[Document] = {
    val query = new TermQuery(new Term("id", id.toString))
    getSearchResult[Document](query) {
      (result, searcher) => searcher.doc(result.scoreDocs(0).doc)
    }
  }

  def search(query: Query): Option[List[Document]] = {
    getSearchResult[List[Document]](query) {
      (result, searcher) => result.scoreDocs.map(_.doc)
        .map(docId => searcher.doc(docId)).toList
    }
  }

  def getSearchResult[T](query: Query)(toResult: (TopDocs, IndexSearcher) => T): Option[T] = {
    val searcher = getSearcher
    try {
      val result = searcher.search(query, 1000)
      if (result.totalHits > 0) {
        Some(toResult(result, searcher))
      } else {
        None
      }
    } finally {
      searcher.getIndexReader.close()
    }
  }

  private def getSearcher = new IndexSearcher(DirectoryReader.open(dir))

  private def createIndexWriter = {
    val indexWriterConfig = new IndexWriterConfig(version, new MPSegmentAnalyzer)
    indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND)

    new IndexWriter(dir, indexWriterConfig)
  }

}
