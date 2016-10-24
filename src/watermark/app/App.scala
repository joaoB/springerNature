package watermark.app

import watermark.model._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import watermark.service.WatermarkService

object App {

  def main(args: Array[String]): Unit = {

    //simple usage
    val book = new Book("book", "The Dark Code", "Bruce Wayne", Science)
    val ticket = WatermarkService.watermark(book)
    println("Asked WatermarkService to watermark book: " + book.title)
    println("Received ticket: " + ticket)
    println("Polling the status using ticket: " + ticket)
    WatermarkService.getByTicket(ticket) match {
      case Left(msg)  => println(msg)
      case Right(doc) => println("Doc received: " + doc.watermark.toString)
    }
    println("Watermark is not completed yet...")

    Thread.sleep(20000)
    println("Polling the status using ticket: " + ticket)
    WatermarkService.getByTicket(ticket) match {
      case Left(msg)  => println(msg)
      case Right(doc) => println("Doc received: " + doc.watermark.toString)
    }
  }
}

