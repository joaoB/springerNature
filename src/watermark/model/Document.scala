
package watermark.model

import scala.concurrent.Future

abstract class Document(content: String, title: String, author: String, var watermark: Option[Watermark] = None) {
  def setWatermark(w: Watermark) = this.watermark = Some(w)
}

case class Book(content: String, title: String, author: String, topic: Topic) extends Document(content, title, author)
case class Journal(content: String, title: String, author: String) extends Document(content, title, author) 