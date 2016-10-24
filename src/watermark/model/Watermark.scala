package watermark.model

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

abstract class Watermark(document: Document)

class BookWatermark(bookDocument: Book) extends Watermark(bookDocument) {
  override def toString: String = {
    val content = bookDocument.content
    val title = bookDocument.title
    val author = bookDocument.author
    val topic = bookDocument.topic.name
    s"{content:”$content”, title:”$title”, author:”$author”, topic:”$topic”}"
  }
}

class JournalWatermark(journalDocument: Journal) extends Watermark(journalDocument) {
  override def toString: String = {
    val content = journalDocument.content
    val title = journalDocument.title
    val author = journalDocument.author
    s"{content:”$content”, title:”$title”, author:”$author”}"
  }
}

abstract class WatermarkerGeneratorGeneric {
  def generate(doc: Document): Future[Watermark]  //simulate async
}

class WatermarkerGenerator extends WatermarkerGeneratorGeneric {
  override def generate(doc: Document): Future[Watermark] = Future {
    doc match {
      case w: Book => {
        Thread.sleep(10000) 
        new BookWatermark(w)
      }
      case w: Journal => {
        Thread.sleep(5000)
        new JournalWatermark(w)
      }
    }
  }
}
