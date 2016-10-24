package watermark.service

import scala.collection.mutable.HashMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

import watermark.model._
import watermark.model.Document

abstract class WatermarkServiceGeneric(
    var service: WatermarkerGeneratorGeneric,
    var tickets: HashMap[Int, Document]) {

  def watermark(document: Document): Int
  def getByTicket(ticket: Int): Either[String, Document]

}



object WatermarkService extends WatermarkService
class WatermarkService
    extends WatermarkServiceGeneric(new WatermarkerGenerator, new HashMap[Int, Document]) {

  override def watermark(document: Document): Int = {
    val code = document.hashCode
    if (!tickets.contains(code)) {
      tickets.+=((code, document))
      service.generate(document).onComplete {
        case Success(watermark) => document.setWatermark(watermark)
        case _                  => tickets.-=(code) //async failed
      }
    }
    code
  }

  override def getByTicket(ticket: Int): Either[String, Document] =
    if (!tickets.contains(ticket)) {
      Left(Message.invalidTicket)
    } else {
      tickets(ticket) match {
        case elem if elem.watermark.isDefined => Right(elem)
        case elem                             => Left(Message.notComplete)
      }
    }
}