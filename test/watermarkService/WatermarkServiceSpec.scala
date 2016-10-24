package watermarkService

import org.scalatest.FunSuite
import scala.reflect.api.Position
import watermark.service.WatermarkService
import watermark.model.Business
import watermark.model.Book
import watermark.service.WatermarkServiceGeneric
import org.scalatest.BeforeAndAfter
import watermark.service.Message
import watermark.model.Science
import watermark.model.Journal
import watermark.model.Document

class WatermarkServiceSpec extends FunSuite with BeforeAndAfter {

  var service: WatermarkServiceGeneric = _

  before {
    service = WatermarkService
  }

  test("Valid ticket is returned") {
    val d = new Book("content", "title", "author", Business)
    val ticket = service.watermark(d)
    assert(ticket.isValidInt)
  }

  test("Request watermark - should return process status") {
    val d = new Book("content", "title", "author", Business)
    val ticket = service.watermark(d)
    val message = service.getByTicket(ticket) match {
      case Left(msg) => msg
    }
    assert(message == Message.notComplete)
  }

  test("Request invalid ticket") {
    val invalidTicket = -1
    val message = service.getByTicket(invalidTicket) match {
      case Left(msg) => msg
    }
    assert(message == Message.invalidTicket)
  }

  def testCorrectWatermark(doc: Document, watermark: String) {
    val ticket = service.watermark(doc)
    Thread.sleep(20000)
    val message = service.getByTicket(ticket) match {
      case Left(msg)  => fail("watermark took more than 20000 to be generated")
      case Right(doc) => assert(doc.watermark.get.toString == watermark)
    }
  }

  test("Book watermark is correctly generated") {
    val watermark = """{content:”book”, title:”The Dark Code”, author:”Bruce Wayne”, topic:”Science”}"""
    val doc = new Book("book", "The Dark Code", "Bruce Wayne", Science)
    testCorrectWatermark(doc, watermark)
  }

  test("Journal watermark is correctly generated") {
    val watermark = """{content:”journal”, title:”Journal of human flight routes”, author:”Clark Kent”}"""
    val doc = new Journal("journal", "Journal of human flight routes", "Clark Kent")
    testCorrectWatermark(doc, watermark)
  }

}